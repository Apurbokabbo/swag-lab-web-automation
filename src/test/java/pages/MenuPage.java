package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static utilities.BaseDriver.getDriver;
import static utilities.URLs.BASE_URL;

public class MenuPage extends BasePage {

    // ===================== LOCATORS =====================
    public By MENU_WRAP = By.className("bm-menu-wrap");
    public By MENU_CROSS_BUTTON = By.xpath("(//button[@type='button'])[2]");
    public By MENU_ITEMS = By.cssSelector(".bm-item-list a");
    public By MENU_ALL_ITEMS = By.id("inventory_sidebar_link");
    public By MENU_ABOUT = By.id("about_sidebar_link");
    public By MENU_LOGOUT = By.id("logout_sidebar_link");
    public By MENU_RESET_APP_STATE = By.id("reset_sidebar_link");

    public By CART_BADGE = By.className("shopping_cart_badge");
    public By LOGIN_PAGE_BUTTON = By.id("login-button");
    public By LOGIN_PAGE_ERROR_MESSAGE = By.cssSelector("[data-test='error']");


    // ===================== DATA PROVIDERS =====================

    @DataProvider(name = "pagesWithMenu")
    public static Object[][] pagesWithMenu() {
        return new Object[][]{
                {"/inventory.html"},
                {"/inventory-item.html?id=4"},
                {"/cart.html"},
                {"/checkout-step-one.html"}
        };
    }


    // ===================== EXPECTED DATA =====================
    public List<String> EXPECTED_MENU_ITEMS = Arrays.asList("All Items","Dynamic Catalog" ,"About", "Logout", "Reset App State");
    public String PROTECTED_PAGE_ERROR_PART = "You can only access '/inventory.html' when you are logged in";

    // ===================== ACTIONS =====================
    private WebDriverWait explicitWait() {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(10));
    }

    /** Opens the burger menu and waits until it is really open. */
    public void openMenu() {
        fluentWaitClickOnElement(HOME_MENU_BUTTON,small_wait);
        explicitWait().until(ExpectedConditions.attributeToBe(MENU_WRAP, "aria-hidden", "false"));
    }

    /** Closes the menu with the X button and waits until it is really closed. */
    public void closeMenu()throws InterruptedException {
        Thread.sleep(500);
        fluentWaitClickOnElement(MENU_CROSS_BUTTON,small_wait);
        explicitWait().until(ExpectedConditions.attributeToBe(MENU_WRAP, "aria-hidden", "true"));
    }

    public boolean isMenuOpen() {
        return "false".equals(findElement(MENU_WRAP).getAttribute("aria-hidden"));
    }

    /** Menu item labels in order (textContent also works while the menu animates). */
    public List<String> getMenuItemNames() {
        List<String> names = new ArrayList<>();
        for (WebElement el : getDriver().findElements(MENU_ITEMS)) {
            names.add(el.getAttribute("textContent").trim());
        }
        return names;
    }

    public void clickMenuItem(By menuItem) {
        fluentWaitClickOnElement(menuItem,small_wait);
    }

    /** Goes straight to a page of the site, e.g. "/cart.html". */
    public void openPage(String path) {
        getDriver().get(BASE_URL + path);
    }

    public void goBack() {
        getDriver().navigate().back();
    }

    public void waitForUrlContains(String urlPart) {
        explicitWait().until(ExpectedConditions.urlContains(urlPart));
    }

    public void waitUntilCartBadgeGone() {
        explicitWait().until(ExpectedConditions.invisibilityOfElementLocated(CART_BADGE));
    }

    /** URL without a trailing slash, so "https://site.com/" equals "https://site.com". */
    public String currentUrlWithoutSlash() {
        return getUrl().replaceAll("/+$", "");
    }

    public void logout() {
        openMenu();
        clickMenuItem(MENU_LOGOUT);
        Assert.assertTrue(isElementVisible(LOGIN_PAGE_BUTTON, small_wait), "Login page not shown after logout");
    }

}