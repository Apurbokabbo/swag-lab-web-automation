package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

import static utilities.BaseDriver.getDriver;

public class HomePage extends BasePage {

	LoginPage loginPageObj = new LoginPage();

	public String ALL_RIGHT_TEXT = "© 2025 Sauce Labs. All Rights Reserved. Terms of Service | Privacy Policy";

	// ===================== MENU BUTTONS =====================
	public By HOME_MENU__CROSS_BUTTON = By.xpath("//button[@id='react-burger-cross-btn']");
	public By HOME_MENU_ALL_ITEMS_BUTTON = By.xpath("//a[@id='inventory_sidebar_link']");
	public By HOME_MENU_ABOUT_BUTTON = By.xpath("//a[@id='about_sidebar_link']");
	public By HOME_MENU_LOGOUT_BUTTON = By.xpath("//a[@id='logout_sidebar_link']");
	public By HOME_MENU_RESET_APP_STATE = By.xpath("//a[@id='reset_sidebar_link']");

	// ===================== SORTING =====================
	public By SORT_BUTTON = By.xpath("//select[@class='product_sort_container']");
	public By SORT_OPTION_AZ = By.xpath("//option[@value='az']");
	public By SORT_OPTION_ZA = By.xpath("//option[@value='za']");
	public By SORT_OPTION_LOW_PRICE_HIGH = By.xpath("//option[@value='lohi']");
	public By SORT_OPTION_HIGH_PRICE_LOW = By.xpath("//option[@value='hilo']");

	// ===================== PRODUCTS =====================
	// First div value changes for other products
	public By FIRST_PRODUCT_PRICE_LOCATION = By.xpath("//div[3]//div[2]//div[2]//div[1]");
	public By FIRST_PRODUCT_ADD_TO_CART_BUTTON = By.xpath("//button[@id='add-to-cart-sauce-labs-bolt-t-shirt']");
	public By FIRST_PRODUCT_TITTLE_LOCATION = By.xpath("//div[normalize-space()='Sauce Labs Fleece Jacket']");
	public By FIRST_PRODUCT_DESCRIPTION_LOCATOR =
			By.xpath("//div[@class='inventory_item_desc' and @data-test='inventory-item-desc']");

	// ===================== PAGE TITLE =====================
	public By PAGE_TTITLE_LOCATOR = By.xpath("//span[@class='title']");

	// ===================== FOOTER =====================
	public By ALL_RIGHT_TEXT_LOCATOR = By.xpath("//div[@class='footer_copy']");
	public By FOOTER_TWITTER_BUTTON = By.xpath("//a[normalize-space()='Twitter']");
	public By FOOTER_FACEBOOK_BUTTON = By.xpath("//a[normalize-space()='Facebook']");
	public By FOOTER_LINKEDIN_BUTTON = By.xpath("//a[normalize-space()='LinkedIn']");

	// ===================== INTERNAL LOCATORS =====================
	private static final By PRODUCT_NAME = By.className("inventory_item_name");
	private static final By PRODUCT_PRICE = By.className("inventory_item_price");

	// Created on demand so the driver already exists when it is used
	private WebDriverWait explicitWait() {
		return new WebDriverWait(getDriver(), Duration.ofSeconds(10));
	}

	// ===================== ACTIONS =====================
	public void appearHomePage(String userName, String password) throws InterruptedException {
		loginPageObj.loginWithValidCredentials(userName, password);
		waitForVisibilityOfElement(SWAG_LAB_PAGE_TITTLE_LOCATOR, 10);
		passwordSaveAlertDisappear();
		assertionHard(SWAG_LAB_PAGE_TITTLE_LOCATOR, SWAG_LAB_PAGE_TITTLE_TEXT);
	}

	/**
	 * Applies a filter on the Swag Labs product page and verifies the resulting order.
	 *
	 * @param filterLocator the By locator for the filter dropdown (e.g. SORT_BUTTON)
	 * @param filterOption  visible text of the option, e.g. "Name (A to Z)", "Name (Z to A)",
	 *                      "Price (low to high)", "Price (high to low)"
	 */
	public void applyAndVerifyFilter(By filterLocator, String filterOption) {
		WebElement filterDropdown =
				explicitWait().until(ExpectedConditions.elementToBeClickable(filterLocator));

		// Real <select> first, custom dropdown as fallback
		try {
			new Select(filterDropdown).selectByVisibleText(filterOption);
		} catch (UnexpectedTagNameException e) {
			clickCustomDropdownAndSelect(filterDropdown, filterOption);
		}

		// Match the full phrase: "price (high to low)" also contains "low",
		// so checking only for "low" would wrongly treat it as ascending.
		String opt = filterOption.toLowerCase(Locale.ROOT);
		if (opt.contains("a to z")) {
			waitUntilSorted(this::getVisibleNames, true);
		} else if (opt.contains("z to a")) {
			waitUntilSorted(this::getVisibleNames, false);
		} else if (opt.contains("low to high")) {
			waitUntilSorted(this::getVisiblePrices, true);
		} else if (opt.contains("high to low")) {
			waitUntilSorted(this::getVisiblePrices, false);
		} else {
			throw new IllegalArgumentException("Unsupported filter: " + filterOption);
		}
	}

	// ===================== HELPERS =====================

	/** Polls until the supplied list is sorted in the requested order, or fails with a clear message. */
	private <T extends Comparable<? super T>> void waitUntilSorted(Supplier<List<T>> supplier, boolean ascending) {
		Comparator<T> order = ascending ? Comparator.<T>naturalOrder() : Comparator.<T>reverseOrder();
		try {
			explicitWait().ignoring(StaleElementReferenceException.class).until(d -> {
				List<T> actual = supplier.get();
				if (actual == null || actual.isEmpty()) return false;
				List<T> expected = new ArrayList<>(actual);
				expected.sort(order);
				return actual.equals(expected);
			});
		} catch (TimeoutException te) {
			List<T> actual = supplier.get();
			List<T> expected = new ArrayList<>(actual);
			expected.sort(order);
			Assert.fail("List not sorted after waiting. Actual: " + actual + " Expected: " + expected);
		}
	}

	/** Visible product names in page order (lower-cased for case-insensitive comparison). */
	private List<String> getVisibleNames() {
		List<String> names = new ArrayList<>();
		for (WebElement el : getDriver().findElements(PRODUCT_NAME)) {
			names.add(el.getText().trim().toLowerCase(Locale.ROOT));
		}
		return names;
	}

	/** Visible product prices in page order. Returns an empty list if any price is not ready yet. */
	private List<Double> getVisiblePrices() {
		List<Double> prices = new ArrayList<>();
		for (WebElement el : getDriver().findElements(PRODUCT_PRICE)) {
			String cleaned = el.getText().replaceAll("[^0-9.]", "");
			if (cleaned.isEmpty()) return Collections.emptyList();
			prices.add(Double.parseDouble(cleaned));
		}
		return prices;
	}

	/** Fallback for custom (non-<select>) dropdown widgets. */
	private void clickCustomDropdownAndSelect(WebElement dropdownElement, String visibleText) {
		dropdownElement.click();
		try {
			explicitWait().until(ExpectedConditions.elementToBeClickable(
					By.xpath("//*[normalize-space(text())='" + visibleText + "']"))).click();
		} catch (TimeoutException e) {
			throw new NoSuchElementException("Cannot find dropdown option: " + visibleText);
		}
	}
}