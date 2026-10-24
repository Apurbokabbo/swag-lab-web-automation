package testcases;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.HomePage;
import pages.LoginPage;
import pages.MenuPage;
import utilities.BaseDriver;

import java.util.Arrays;
import java.util.List;

import static utilities.URLs.BASE_URL;
import static utilities.URLs.CART_URL;
import static utilities.URLs.HOME_URL;

@Epic("Menu Test Cases")
@Feature("Side Menu")
public class MenuPageTestCase extends BaseDriver {

    HomePage homePageObj = new HomePage();
    CartPage cartPageObj = new CartPage();
    MenuPage menuObj = new MenuPage();


    // ===================== SETUP + SMALL HELPERS =====================

    // Runs after BaseDriver.setBrowser, so a fresh browser is ready.
    @BeforeMethod(alwaysRun = true)
    public void loginToHomePage() throws InterruptedException {
        homePageObj.appearHomePage(LoginPage.STANDARD_USER_NAME, LoginPage.PASSWORD);
    }

    private List<String> threeItems() {
        return Arrays.asList(homePageObj.first_item_name, homePageObj.second_item_name, homePageObj.third_item_name);
    }

    private void addItems(List<String> names) {
        for (String name : names) {
            homePageObj.addToCart(name);
        }
    }



    // ===================== OPEN / CLOSE =====================

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = {"smoke", "regression"}, priority = 0, description = "Menu opens and shows all five items")
    public void menuOpensWithAllItems() {
        menuObj.openMenu();
        Assert.assertTrue(menuObj.isMenuOpen(), "Menu should be open");
        Assert.assertEquals(menuObj.getMenuItemNames(), menuObj.EXPECTED_MENU_ITEMS, "Menu items mismatch");
        Assert.assertTrue(menuObj.isElementVisible(menuObj.MENU_CROSS_BUTTON, menuObj.small_wait), "Close (X) button not visible");
        menuObj.logInfo("Menu items verified: " + menuObj.getMenuItemNames());
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = {"regression"}, priority = 1, description = "Close (X) button closes the menu")
    public void crossButtonClosesMenu() throws InterruptedException {
        menuObj.openMenu();
        menuObj.closeMenu();
        Assert.assertFalse(menuObj.isMenuOpen(), "Menu should be closed");
        Assert.assertEquals(menuObj.getUrl(), HOME_URL, "Closing the menu must not change the page");
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(dataProvider = "pagesWithMenu",dataProviderClass = MenuPage.class, groups = {"regression"}, priority = 2, description = "Menu is available with all items on every page")
    public void menuIsAvailableOnEveryPage(String path) {
        menuObj.openPage(path);
        menuObj.openMenu();
        Assert.assertEquals(menuObj.getMenuItemNames(), menuObj.EXPECTED_MENU_ITEMS, "Menu items mismatch on " + path);
    }

    // ===================== ALL ITEMS / ABOUT =====================

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = {"regression"}, priority = 3, description = "All Items from the cart page opens the products page")
    public void allItemsOpensProductsPage() {
        addItems(Arrays.asList(homePageObj.first_item_name));
        cartPageObj.openCart();
        Assert.assertEquals(menuObj.getUrl(), CART_URL, "Should be on cart page");

        menuObj.openMenu();
        menuObj.clickMenuItem(menuObj.MENU_ALL_ITEMS);
        menuObj.waitForUrlContains("inventory.html");
        Assert.assertEquals(menuObj.getUrl(), HOME_URL, "All Items did not open the products page");
        Assert.assertEquals(homePageObj.getCartBadgeCount(), 1, "Cart should be kept");
    }

    @Severity(SeverityLevel.MINOR)
    @Test(groups = {"regression"}, priority = 4, description = "About opens the Sauce Labs website")
    public void aboutOpensSauceLabsWebsite() {
        menuObj.openMenu();
        menuObj.clickMenuItem(menuObj.MENU_ABOUT);
        menuObj.waitForUrlContains("saucelabs.com");
        Assert.assertTrue(menuObj.getUrl().contains("saucelabs.com"), "About did not open saucelabs.com, URL: " + menuObj.getUrl());
    }

    // ===================== LOGOUT =====================

    @Severity(SeverityLevel.BLOCKER)
    @Test(groups = {"smoke", "regression"}, priority = 5, description = "Logout returns to the login page")
    public void logoutReturnsToLoginPage() {
        menuObj.logout();
        Assert.assertEquals(menuObj.currentUrlWithoutSlash(), BASE_URL.replaceAll("/+$", ""), "Not on the login page URL");
        menuObj.logInfo("Logout successful");
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = {"regression"}, priority = 6, description = "After logout the products page cannot be opened directly")
    public void protectedPageBlockedAfterLogout() {
        menuObj.logout();
        menuObj.openPage("/inventory.html");
        Assert.assertTrue(menuObj.isElementVisible(menuObj.LOGIN_PAGE_BUTTON, menuObj.small_wait), "Should be redirected to the login page");
        Assert.assertTrue(menuObj.getText(menuObj.LOGIN_PAGE_ERROR_MESSAGE).contains(menuObj.PROTECTED_PAGE_ERROR_PART),
                "Expected 'logged in' error message");
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = {"regression"}, priority = 7, description = "Browser Back after logout does not return to the products page")
    public void browserBackAfterLogoutStaysLoggedOut() {
        menuObj.logout();
        menuObj.goBack();
        Assert.assertTrue(menuObj.isElementVisible(menuObj.LOGIN_PAGE_BUTTON, menuObj.small_wait), "Back must not show the products page");
        Assert.assertNotEquals(menuObj.getUrl(), HOME_URL, "Products page is open after logout");
    }

    // ===================== RESET APP STATE =====================

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = {"smoke", "regression"}, priority = 8, description = "Reset App State clears the cart badge")
    public void resetAppStateClearsCartBadge() {
        addItems(threeItems());
        Assert.assertEquals(homePageObj.getCartBadgeCount(), 3, "Badge should show 3 before reset");

        menuObj.openMenu();
        menuObj.clickMenuItem(menuObj.MENU_RESET_APP_STATE);
        menuObj.waitUntilCartBadgeGone();
        Assert.assertEquals(homePageObj.getCartBadgeCount(), 0, "Badge should be cleared after reset");
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = {"regression"}, priority = 9, description = "After Reset App State and a refresh, Add to cart buttons are restored")
    public void resetAppStateRestoresAddToCartButtons() {
        addItems(threeItems());
        menuObj.openMenu();
        menuObj.clickMenuItem(menuObj.MENU_RESET_APP_STATE);
        menuObj.waitUntilCartBadgeGone();

        menuObj.navigaterRefresh();
        for (String name : threeItems()) {
            Assert.assertTrue(menuObj.isElementVisible(homePageObj.addToCartButton(name), menuObj.small_wait),
                    "Add to cart button not restored for " + name);
        }
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = {"regression"}, priority = 10, description = "Reset App State from the cart page empties the cart")
    public void resetAppStateEmptiesCart() {
        addItems(threeItems());
        cartPageObj.openCart();
        Assert.assertEquals(cartPageObj.getCartItemNames().size(), 3, "Cart should hold 3 items before reset");

        menuObj.openMenu();
        menuObj.clickMenuItem(menuObj.MENU_RESET_APP_STATE);
        menuObj.waitUntilCartBadgeGone();
        menuObj.navigaterRefresh();

        Assert.assertTrue(cartPageObj.isElementVisible(cartPageObj.CART_PAGE_TITLE_LOCATOR, cartPageObj.small_wait));
        Assert.assertTrue(cartPageObj.getCartItemNames().isEmpty(), "Cart should be empty after reset");
    }
}