package pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.DataProvider;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static utilities.BaseDriver.getDriver;

public class CartPage extends BasePage{

    public String CART_PAGE_TITTLE="Your Cart";
    public String QUANTITY_TITTLE_TEXT="QTY";
    public String CART_DESCRIPTION_TITTLE_TEXT="Description";
    public String CHECKOUT_INFORMATION_TITTLE_TEXT="Checkout: Your Information";
    public String CART_CHECKOUT_INFO_FIRST_NAME_INPUT_FIELD_PLACEHOLDER="First Name";
    public String CART_CHECKOUT_INFO_LAST_NAME_INPUT_FIELD_PLACEHOLDER="Last Name";
    public String CART_CHECKOUT_INFO_ZIP_POSTCODE_INPUT_FIELD_PLACEHOLDER="Zip/Postal Code";
    public String CHECK_OUT_OVERVIEW_PAGE_TITTLE_TEXT="Checkout: Overview";
    public String CART_COMPLETE_PAGE_TITTLE_TEXT="Thank you for your order!";
    public String CART_COMPLETE_PAGE_SUB_TITTLE_TEXT="Your order has been dispatched, and will arrive just as fast as the pony can get there!";
    public String CART_OVER_VIEW_PAYMENT_INFORMATION_LABEL_TEXT="Payment Information:";
    public String CART_OVER_VIEW_PAYMENT_INFORMATION_DATA_TEXT="SauceCard #";
    public String CART_OVER_VIEW_SHIPPING_INFO_LABEL_TEXT="Shipping Information:";
    public String CART_OVER_VIEW_PRICE_TOTAL_LABEL_TEXT="Price Total";




    public By QUANTITY_TITTLE_LOCATOR = By.xpath("//div[@class='cart_quantity_label']");
    public By CART_DESCRIPTION_TITTLE_LOCATOR = By.xpath("//div[@class='cart_desc_label']");
    public By CART_REMOVE_BUTTON = By.xpath("//button[@id='remove-sauce-labs-bike-light']");
    public By CART_CONTINUE_SHOPPING_BUTTON = By.xpath("//button[@id='continue-shopping']");
    public By CART_CHECKOUT_BUTTON = By.xpath("//button[@id='checkout']");
    public By CART_CANCEL_BUTTON = By.xpath("//button[@id='cancel']");
    public By CART_CHECKOUT_INFO_FIRST_NAME_INPUT_FIELD = By.xpath("//input[@id='first-name']");
    public By CART_CHECKOUT_INFO_LAST_NAME_INPUT_FIELD = By.xpath("//input[@id='last-name']");
    public By CART_CHECKOUT_INFO_ZIP_POSTCODE_INPUT_FIELD = By.xpath("//input[@id='postal-code']");
    public By CART_FINISH_BUTTON = By.xpath("//button[@id='finish']");
    public By CART_HOME_BUTTON = By.xpath("//button[@id='back-to-products']");
    public By CART_COMPLETE_PAGE_SUB_TITTLE_LOCATOR = By.xpath("//div[@class='complete-text']");
    public By CART_COMPLETE_PAGE_TITTLE_LOCATOR = By.xpath("//h2[normalize-space()='Thank you for your order!']");
    public By CART_OVER_VIEW_PAYMENT_INFORMATION_LABEL_LOCATOR = By.xpath("//div[normalize-space()='Payment Information:']");
    public By CART_OVER_VIEW_PAYMENT_INFORMATION_DATA_LOCATOR = By.xpath("//div[starts-with(normalize-space(), 'SauceCard #')]");
    public By CART_OVER_VIEW_SHIPPING_INFO_LABEL_LOCATOR = By.xpath("//div[normalize-space()='Shipping Information:']");
    public By CART_OVER_VIEW_SHIPPING_INFO_DATA_LOCATOR = By.xpath("");
    public By CART_OVER_VIEW_PRICE_TOTAL_LABEL_LOCATOR = By.xpath("//div[normalize-space()='Price Total']");
    public By CART_OVER_VIEW_PRICE_TOTAL_ITEAM_SUB_AMOUNT_LOCATOR = By.xpath("//div[@class='summary_subtotal_label']");
    public By CART_OVER_VIEW_PRICE_TOTAL_ITEAM_TAX_LOCATOR = By.xpath("//div[@class='summary_tax_label']");
    public By CART_OVER_VIEW_PRICE_TOTAL_ITEAM_TOTAL_AMOUNTLOCATOR = By.xpath("//div[@class='summary_total_label']");


    // ===================== CART / CHECKOUT HELPERS =====================
    public By CART_PAGE_TITLE_LOCATOR = By.cssSelector("span.title");
    public By CART_ITEM_NAMES = By.cssSelector(".cart_item .inventory_item_name");
    public By CHECKOUT_CONTINUE_BUTTON = By.id("continue");
    public By CHECKOUT_ERROR_MESSAGE = By.cssSelector("[data-test='error']");
    public By OVERVIEW_SUBTOTAL_LABEL = By.cssSelector("[data-test='subtotal-label']");
    public By OVERVIEW_TAX_LABEL = By.cssSelector("[data-test='tax-label']");
    public By OVERVIEW_TOTAL_LABEL = By.cssSelector("[data-test='total-label']");
    public By COMPLETE_HEADER = By.cssSelector("[data-test='complete-header']");
    public By COMPLETE_TEXT = By.cssSelector("[data-test='complete-text']");



    @DataProvider(name = "checkoutInfoValidation")
    public static Object[][] checkoutInfoValidation() {
        return new Object[][]{
                // first name, last name, zip,     expected error
                {"",     "Doe", "12345", "Error: First Name is required"},
                {"John", "",    "12345", "Error: Last Name is required"},
                {"John", "Doe", "",      "Error: Postal Code is required"},
                {"",     "",    "",      "Error: First Name is required"}
        };
    }

    /** Waits until the browser URL contains the given part (e.g. "cart.html"). */
    public void waitForUrlContains(String urlPart) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(10)).until(ExpectedConditions.urlContains(urlPart));
    }

    /** Clicks an element, then waits until the URL shows the next page. */
    public void clickAndWaitForUrl(By locator, String urlPart) {
        clickOn(locator);
        waitForUrlContains(urlPart);
    }

    public void openCart() {
        clickAndWaitForUrl(TOP_NAV_BAR_CART_BUTTON, "cart.html");
    }

    /** Item names in the cart (or overview) list, in page order. */
    public List<String> getCartItemNames() {
        List<String> names = new ArrayList<>();
        for (WebElement el : getDriver().findElements(CART_ITEM_NAMES)) {
            names.add(el.getText().trim());
        }
        return names;
    }

    private By cartItemPart(String productName, String dataTest) {
        return By.xpath("//div[@data-test='inventory-item-name' and normalize-space()='" + productName
                + "']/ancestor::div[@data-test='inventory-item']//*[@data-test='" + dataTest + "']");
    }

    public String getCartItemPrice(String productName) {
        return findElement(cartItemPart(productName, "inventory-item-price")).getText().trim();
    }

    public String getCartItemQuantity(String productName) {
        return findElement(cartItemPart(productName, "item-quantity")).getText().trim();
    }

    /** Fills the checkout information form; empty values are skipped. Does not click Continue. */
    public void fillCheckoutInfo(String firstName, String lastName, String zip) {
        if (!firstName.isEmpty()) findElement(CART_CHECKOUT_INFO_FIRST_NAME_INPUT_FIELD).sendKeys(firstName);
        if (!lastName.isEmpty()) findElement(CART_CHECKOUT_INFO_LAST_NAME_INPUT_FIELD).sendKeys(lastName);
        if (!zip.isEmpty()) findElement(CART_CHECKOUT_INFO_ZIP_POSTCODE_INPUT_FIELD).sendKeys(zip);
    }

    /** Number in a label like "Item total: $39.98" or "Tax: $3.20". */
    public double getAmount(By labelLocator) {
        return Double.parseDouble(getText(labelLocator).replaceAll("[^0-9.]", ""));
    }

    /** Number from a price text like "$29.99". */
    public double toAmount(String priceText) {
        return Double.parseDouble(priceText.replaceAll("[^0-9.]", ""));
    }
}