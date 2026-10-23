package testcases;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import utilities.BaseDriver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static utilities.URLs.CART_URL;
import static utilities.URLs.HOME_URL;

@Epic("Home Page Test Cases")
@Feature("Home Page Functionality")
public class HomePageTestCases extends BaseDriver {

    HomePage homePageObj = new HomePage();


    // ===================== SETUP =====================

    // Runs after BaseDriver.setBrowser (superclass hooks run first), so a fresh browser is ready.
    @BeforeMethod(alwaysRun = true)
    public void loginToHomePage() throws InterruptedException {
        homePageObj.appearHomePage(LoginPage.STANDARD_USER_NAME, LoginPage.PASSWORD);
    }

    // ===================== PAGE APPEARANCE =====================

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = {"smoke", "regression"}, priority = 0, description = "Home page appears with all main elements")
    public void homePageAppearanceTestAndAssertion() {
        Assert.assertEquals(homePageObj.getUrl(), HOME_URL, "Home page URL mismatch");
        // isElementVisible only returns true/false, so it must be asserted
        Assert.assertTrue(homePageObj.isElementVisible(homePageObj.SWAG_LAB_PAGE_TITTLE_LOCATOR, homePageObj.small_wait), "Swag Labs title not visible");
        Assert.assertTrue(homePageObj.isElementVisible(homePageObj.HOME_MENU_BUTTON, homePageObj.small_wait), "Menu button not visible");
        Assert.assertTrue(homePageObj.isElementVisible(homePageObj.TOP_NAV_BAR_CART_BUTTON, homePageObj.small_wait), "Cart button not visible");
        Assert.assertTrue(homePageObj.isElementVisible(homePageObj.SORT_BUTTON, homePageObj.small_wait), "Sort dropdown not visible");
        homePageObj.assertionHard(homePageObj.PAGE_TTITLE_LOCATOR, "Products");
        Assert.assertEquals(homePageObj.getProductCount(), 6, "Expected 6 products");
        homePageObj.logInfo("Home page elements verified");
    }

    // ===================== PRODUCTS =====================

    @Severity(SeverityLevel.NORMAL)
    @Test(dataProvider = "products",dataProviderClass = HomePage.class, groups = {"regression"}, priority = 1, description = "Each product is listed with the correct price")
    public void productNameAndPriceVerification(String productName, String expectedPrice) {
        Assert.assertTrue(homePageObj.getProductNames().contains(productName), productName + " not listed");
        Assert.assertEquals(homePageObj.getPriceOf(productName), expectedPrice, "Wrong price for " + productName);
        homePageObj.logInfo("Verified " + productName + " = " + expectedPrice);
    }

    @Severity(SeverityLevel.MINOR)
    @Test(groups = {"regression"}, priority = 2, description = "Products are sorted Name (A to Z) by default")
    public void defaultSortIsNameAToZ() {
        List<String> names = homePageObj.getProductNames();
        List<String> sorted = new ArrayList<>(names);
        Collections.sort(sorted);
        Assert.assertEquals(names, sorted, "Default order is not A to Z");
    }

    // ===================== SORTING =====================

    @Severity(SeverityLevel.CRITICAL)
    @Test(dataProvider = "sortOptions",dataProviderClass = HomePage.class,groups = {"smoke", "regression"}, priority = 3, description = "Every sort option orders the list correctly")
    public void sortingOptionsTest(String sortOption) {
        homePageObj.applyAndVerifyFilter(homePageObj.SORT_BUTTON, sortOption);
        homePageObj.logInfo("Sorted and verified: " + sortOption);
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = {"regression"}, priority = 4, description = "Changing the sort option changes the product order")
    public void sortingChangesTheOrder() {
        List<String> before = homePageObj.getProductNames();
        homePageObj.applyAndVerifyFilter(homePageObj.SORT_BUTTON, "Name (Z to A)");
        List<String> after = homePageObj.getProductNames();
        Assert.assertNotEquals(after, before, "Order did not change after sorting");
    }

    // ===================== ADD / REMOVE CART =====================

    @Severity(SeverityLevel.CRITICAL)
    @Test(dataProvider = "products",dataProviderClass = HomePage.class,groups = {"smoke", "regression"}, priority = 5, description = "Add and remove a product updates button and cart badge")
    public void addAndRemoveProductFromCart(String productName, String price) {
        homePageObj.addToCart(productName);
        Assert.assertEquals(homePageObj.getCartBadgeCount(), 1, "Badge should show 1 after adding");
        Assert.assertTrue(homePageObj.isElementVisible(homePageObj.removeButton(productName), homePageObj.small_wait), "Remove button missing");

        homePageObj.removeFromCart(productName);
        Assert.assertEquals(homePageObj.getCartBadgeCount(), 0, "Badge should be gone after removing");
        Assert.assertTrue(homePageObj.isElementVisible(homePageObj.addToCartButton(productName), homePageObj.small_wait), "Add to cart button missing");
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = {"regression"}, priority = 6, description = "Adding all products shows 6 on the cart badge")
    public void addAllProductsToCart() {
        List<String> names = homePageObj.getProductNames();
        for (String name : names) {
            homePageObj.addToCart(name);
        }
        Assert.assertEquals(homePageObj.getCartBadgeCount(), names.size());
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = {"regression"}, priority = 7, description = "Cart badge is kept after page refresh")
    public void cartBadgePersistsAfterRefresh() {
        homePageObj.addToCart(homePageObj.first_item_name);
        homePageObj.navigaterRefresh();
        Assert.assertTrue(homePageObj.isElementVisible(homePageObj.PAGE_TTITLE_LOCATOR, homePageObj.small_wait));
        Assert.assertEquals(homePageObj.getCartBadgeCount(), 1, "Cart badge lost after refresh");
    }

    // ===================== NAVIGATION =====================

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = {"smoke", "regression"}, priority = 8, description = "Cart icon opens the cart page")
    public void cartIconOpensCartPage() {
        homePageObj.clickOn(homePageObj.TOP_NAV_BAR_CART_BUTTON);
        Assert.assertEquals(homePageObj.getUrl(), CART_URL, "Cart page URL mismatch");
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = {"regression"}, priority = 9, description = "Product details page opens and back button returns to products")
    public void productDetailsPageOpensAndGoesBack() {
        homePageObj.openProductDetails(homePageObj.first_item_name);
        Assert.assertTrue(homePageObj.getUrl().contains("inventory-item.html"), "Not on product details page");
        homePageObj.assertionHard(homePageObj.PRODUCT_DETAIL_NAME, homePageObj.first_item_name);

        homePageObj.clickOn(homePageObj.BACK_TO_PRODUCTS_BUTTON);
        Assert.assertEquals(homePageObj.getUrl(), HOME_URL, "Back to products did not return to home page");
    }

    // ===================== FOOTER =====================

    @Severity(SeverityLevel.MINOR)
    @Test(groups = {"regression"}, priority = 10, description = "Footer text and social links are present")
    public void footerAppearanceAndLinks() {
        homePageObj.scrollToElement(homePageObj.ALL_RIGHT_TEXT_LOCATOR);
        Assert.assertTrue(homePageObj.getText(homePageObj.ALL_RIGHT_TEXT_LOCATOR).contains("Sauce Labs. All Rights Reserved."), "Footer copyright text mismatch");
        homePageObj.isElementVisible(homePageObj.FOOTER_TWITTER_BUTTON, homePageObj.small_wait);
        homePageObj.isElementVisible(homePageObj.FOOTER_FACEBOOK_BUTTON, homePageObj.small_wait);
        homePageObj.isElementVisible(homePageObj.FOOTER_LINKEDIN_BUTTON, homePageObj.small_wait);

//        Assert.assertTrue(homePageObj.getLinkHref(homePageObj.FOOTER_TWITTER_BUTTON).contains("saucelabs"), "Twitter link wrong");
//        Assert.assertTrue(homePageObj.getLinkHref(homePageObj.FOOTER_FACEBOOK_BUTTON).contains("facebook.com/saucelabs"), "Facebook link wrong");
//        Assert.assertTrue(homePageObj.getLinkHref(homePageObj.FOOTER_LINKEDIN_BUTTON).contains("linkedin.com/company/sauce-labs"), "LinkedIn link wrong");
    }
}