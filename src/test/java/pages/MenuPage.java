package pages;

import org.openqa.selenium.By;

public class MenuPage extends BasePage{

    // ===================== MENU BUTTONS =====================
    public By HOME_MENU_CROSS_BUTTON = By.xpath("//button[@id='react-burger-cross-btn']");
    public By HOME_MENU_ALL_ITEMS_BUTTON = By.xpath("//a[@id='inventory_sidebar_link']");
    public By HOME_MENU_ABOUT_BUTTON = By.xpath("//a[@id='about_sidebar_link']");
    public By HOME_MENU_LOGOUT_BUTTON = By.xpath("//a[@id='logout_sidebar_link']");
    public By HOME_MENU_RESET_APP_STATE = By.xpath("//a[@id='reset_sidebar_link']");
}
