package pages;


import org.openqa.selenium.By;

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
    public String ="a";



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
    public By a = By.xpath("");





}
