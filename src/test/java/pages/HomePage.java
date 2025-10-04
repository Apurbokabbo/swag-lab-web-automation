package pages;

import org.openqa.selenium.By;


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
		Thread.sleep(6000);
		assertionHard(SWAG_LAB_PAGE_TITTLE_LOCATOR,SWAG_LAB_PAGE_TITTLE_TEXT);

	}

}
