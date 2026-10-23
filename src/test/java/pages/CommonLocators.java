package pages;

import org.openqa.selenium.By;

public class CommonLocators {


    //wait times configuration
    public  int long_wait = 45;
    public  int medium_wait = 20;
    public  int small_wait = 10;



    public String SWAG_LAB_PAGE_TITTLE_TEXT="Swag Labs";



    //TOP NAV BAR ELEMENTS
    public By TOP_NAV_BAR_CART_BUTTON = By.xpath("//a[@class='shopping_cart_link']");
    public By HOME_MENU_BUTTON = By.xpath("//button[@id='react-burger-menu-btn']");
    public By SWAG_LAB_PAGE_TITTLE_LOCATOR = By.xpath("//div[contains(text(),'Swag Labs')]");


    //Product
    public String cart_page_tittle_text = "Your Cart";
    public String cart_page_continue_shopping_button_text = "Continue Shopping";
    public String cart_page_checkout_button_text = "Checkout";
    public String cart_page_remove_button_text = "Remove";

    public String first_item_name = "Sauce Labs Backpack";
    public String second_item_name = "Sauce Labs Bike Light";
    public String third_item_name = "Sauce Labs Bolt T-Shirt";
    public String fourth_item_name = "Sauce Labs Fleece Jacket";
    public String fifth_item_name = "Sauce Labs Onesie";
    public String sixth_item_name = "Test.allTheThings() T-Shirt (Red)";

    public String first_item_price = "$29.99";
    public String second_item_price = "$9.99";
    public String third_item_price = "$15.99";
    public String fourth_item_price = "$49.99";
    public String fifth_item_price = "$7.99";
    public String sixth_item_price = "$15.99";
}
