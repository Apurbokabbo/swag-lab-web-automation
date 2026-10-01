package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;
import org.testng.Assert;

import java.text.Collator;
import java.util.*;
import java.util.function.Supplier;

import static utilities.BaseDriver.getDriver;


public class HomePage extends BasePage{

	LoginPage loginPageObj = new LoginPage();

    public String ALL_RIGHT_TEXT = "© 2025 Sauce Labs. All Rights Reserved. Terms of Service | Privacy Policy";
    public String a = "";


    //MENU BUTTONS
	public By HOME_MENU__CROSS_BUTTON = By.xpath("//button[@id='react-burger-cross-btn']");
	public By HOME_MENU_ALL_ITEMS_BUTTON = By.xpath("//a[@id='inventory_sidebar_link']");
	public By HOME_MENU_ABOUT_BUTTON = By.xpath("//a[@id='about_sidebar_link']");
	public By HOME_MENU_LOGOUT_BUTTON = By.xpath("//a[@id='logout_sidebar_link']");
    public By HOME_MENU_RESET_APP_STATE = By.xpath("//a[@id='reset_sidebar_link']");

    //SORTING
	public By SORT_BUTTON = By.xpath("//select[@class='product_sort_container']");
	public By SORT_OPTION_AZ = By.xpath("//option[@value='az']");
	public By SORT_OPTION_ZA = By.xpath("//option[@value='za']");
	public By SORT_OPTION_LOW_PRICE_HIGH = By.xpath("//option[@value='lohi']");
	public By SORT_OPTION_HIGH_PRICE_LOW = By.xpath("//option[@value='hilo']");


    // Products
	public By FIRST_PRODUCT_PRICE_LOCATION = By.xpath("//div[3]//div[2]//div[2]//div[1]"); //FIRST DIV VALUE CHANGE FOR OTHER PRODUCTS
	public By FIRST_PRODUCT_ADD_TO_CART_BUTTON = By.xpath("//button[@id='add-to-cart-sauce-labs-bolt-t-shirt']");
	public By FIRST_PRODUCT_TITTLE_LOCATION = By.xpath("//div[normalize-space()='Sauce Labs Fleece Jacket']");
	public By FIRST_PRODUCT_DESCRIPTION_LOCATOR = By.xpath("//div[@class=\"inventory_item_desc\" and @data-test=\"inventory-item-desc\"]");
//	public By b = By.xpath("");

	//Page Title
	public By PAGE_TTITLE_LOCATOR = By.xpath("//span[@class='title']");


    //Footer
	public By ALL_RIGHT_TEXT_LOCATOR = By.xpath("//div[@class='footer_copy']");
	public By FOOTER_TWITTER_BUTTON = By.xpath("//a[normalize-space()='Twitter']");
	public By FOOTER_FACEBOOK_BUTTON = By.xpath("//a[normalize-space()='Facebook']");
	public By FOOTER_LINKEDIN_BUTTON = By.xpath("//a[normalize-space()='LinkedIn']");


	public void appearHomePage (String userName , String password) throws InterruptedException {
		loginPageObj.loginWithValidCredentials(userName, password);
		waitForVisibilityOfElement(SWAG_LAB_PAGE_TITTLE_LOCATOR, 10);
		passwordSaveAlertDisappear();
		assertionHard(SWAG_LAB_PAGE_TITTLE_LOCATOR,SWAG_LAB_PAGE_TITTLE_TEXT);

	}

	/**
	 * Applies a filter on Swag Labs product page and verifies it.
	 *
	 * @param filterLocator  The By locator for the filter dropdown
	 * @param filterOption   The visible text of the option to select (e.g. "Price (low to high)", "Name (A to Z)")
	 */
	public void applyAndVerifyFilter(By filterLocator, String filterOption) {
		WebElement filterDropdown = wait.until(ExpectedConditions.elementToBeClickable(filterLocator));

		// Try as a real <select> first
		try {
			Select select = new Select(filterDropdown);
			select.selectByVisibleText(filterOption);
		} catch (UnexpectedTagNameException e) {
			// fallback for custom dropdowns (click to open and click option)
			clickCustomDropdownAndSelect(filterDropdown, filterOption);
		}

		// optional debug: capture selected option text (if <select>)
		try {
			Select select = new Select(getDriver().findElement(filterLocator));
			String selected = select.getFirstSelectedOption().getText().trim();
			if (!selected.equals(filterOption)) {
				// sometimes visible text differs slightly (trim/extra spaces). Try contains fallback
				if (!selected.contains(filterOption) && !filterOption.contains(selected)) {
					// continue but log - will be caught by verification below
					System.out.println("Selected option (visible) = '" + selected + "' ; expected '" + filterOption + "'");
				}
			}
		} catch (Exception ignored) { /* not a select — ignore */ }

		// Decide verification based on filterOption text (case-insensitive contains)
		String opt = filterOption.toLowerCase(Locale.ROOT);
		if (opt.contains("name") && opt.contains("a to z")) {
			waitUntilSorted(() -> getVisibleNames(), true);
		} else if (opt.contains("name") && opt.contains("z to a")) {
			waitUntilSorted(() -> getVisibleNames(), false);
		} else if (opt.contains("price") && opt.contains("low")) {
			waitUntilSorted(() -> getVisiblePrices(), true);
		} else if (opt.contains("price") && opt.contains("high")) {
			waitUntilSorted(() -> getVisiblePrices(), false);
		} else {
			throw new IllegalArgumentException("Unsupported filter: " + filterOption);
		}
	}

	// Generic waiter that polls until the supplier returns a list that is sorted (or timeout)
	private <T extends Comparable<? super T>> void waitUntilSorted(Supplier<List<T>> supplier, boolean ascending) {
		try {
			boolean ok = wait.until(driver -> {
				List<T> actual = supplier.get();
				if (actual == null || actual.isEmpty()) return false;
				List<T> expected = new ArrayList<>(actual);
				expected.sort(ascending ? Comparator.naturalOrder() : Comparator.reverseOrder());
				return actual.equals(expected);
			});
			Assert.assertTrue(ok, "List did not become sorted within timeout");
		} catch (TimeoutException te) {
			// gather final snapshot for a helpful message
			List<?> finalList = supplier.get();
			List<?> sorted = new ArrayList<>(finalList);
			if (ascending) Collections.sort((List) sorted);
			else Collections.sort((List) sorted, Collections.reverseOrder());
			Assert.fail("After waiting, list not sorted. Actual: " + finalList + " Expected: " + sorted);
		}
	}

	// Return visible product names in page order
	private List<String> getVisibleNames() {
		List<WebElement> nameElements = getDriver().findElements(By.className("inventory_item_name"));
		List<String> names = new ArrayList<>();
		Collator collator = Collator.getInstance(Locale.ENGLISH);
		collator.setStrength(Collator.PRIMARY);
		for (WebElement el : nameElements) {
			try {
				names.add(el.getText().trim());
			} catch (StaleElementReferenceException sere) {
				// If stale, try to re-find element later (return null so wait will retry)
				return Collections.emptyList();
			}
		}
		// Normalize for case-insensitive comparison
		List<String> normalized = new ArrayList<>();
		for (String n : names) normalized.add(n.toLowerCase(Locale.ROOT));
		return normalized;
	}

	// Return visible product prices as Doubles in page order
	private List<Double> getVisiblePrices() {
		List<WebElement> priceElements = getDriver().findElements(By.className("inventory_item_price"));
		List<Double> prices = new ArrayList<>();
		for (WebElement el : priceElements) {
			try {
				String raw = el.getText();
				// remove currency symbols and any non-digit except dot
				String cleaned = raw.replaceAll("[^0-9.]", "");
				if (cleaned.isEmpty()) return Collections.emptyList(); // wait and retry
				prices.add(Double.parseDouble(cleaned));
			} catch (NumberFormatException | StaleElementReferenceException e) {
				return Collections.emptyList(); // let wait retry
			}
		}
		return prices;
	}

	// Fallback for custom dropdown widgets
	private void clickCustomDropdownAndSelect(WebElement dropdownElement, String visibleText) {
		dropdownElement.click();
		// try to find option by text under the dropdown - adjust locator if your DOM differs
		By optionLocator = By.xpath(String.format("//div[contains(@class,'product_sort_container') or //select]//option[text()='%s' or contains(text(),'%s')]", visibleText, visibleText));
		try {
			WebElement option = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[text()='" + visibleText + "']")));
			option.click();
		} catch (TimeoutException e) {
			// final fallback: try clicking an element that contains the text anywhere
			List<WebElement> candidates = getDriver().findElements(By.xpath("//*[contains(text(), '" + visibleText + "')]"));
			if (!candidates.isEmpty()) {
				candidates.get(0).click();
			} else {
				throw new NoSuchElementException("Cannot find dropdown option: " + visibleText);
			}
		}
	}

}
