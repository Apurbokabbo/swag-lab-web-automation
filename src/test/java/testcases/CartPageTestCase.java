package testcases;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CommonLocators;
import pages.HomePage;
import pages.LoginPage;
import utilities.BaseDriver;
import java.util.Arrays;
import java.util.List;
import static utilities.URLs.CART_URL;
import static utilities.URLs.CHECKOUT_URL;
import static utilities.URLs.HOME_URL;

@Epic("Cart Page Test Cases")
@Feature("Cart And Checkout")
public class CartPageTestCase extends BaseDriver {

    HomePage homePageObj = new HomePage();
    CartPage cartPageObj = new CartPage();



    // ===================== SETUP + SMALL HELPERS =====================

    // Runs after BaseDriver.setBrowser, so a fresh browser is ready.
    @BeforeMethod(alwaysRun = true)
    public void loginToHomePage() throws InterruptedException {
        homePageObj.appearHomePage(LoginPage.STANDARD_USER_NAME, LoginPage.PASSWORD);
    }

    public List<String> threeItems() {
        return Arrays.asList(homePageObj.first_item_name, homePageObj.second_item_name, homePageObj.third_item_name);
    }

    public double threeItemsSubtotal() {
        return cartPageObj.toAmount(homePageObj.first_item_price)
                + cartPageObj.toAmount(homePageObj.second_item_price)
                + cartPageObj.toAmount(homePageObj.third_item_price);
    }

    /** Adds the given products from the home page and opens the cart. */
    public void addItemsAndOpenCart(List<String> names) {
        for (String name : names) {
            homePageObj.addToCart(name);
        }
        cartPageObj.openCart();
    }

    /** Cart -> Checkout -> information filled -> Continue -> overview page. */
    public void goToCheckoutOverview(List<String> names) {
        addItemsAndOpenCart(names);
        cartPageObj.clickAndWaitForUrl(cartPageObj.CART_CHECKOUT_BUTTON, "checkout-step-one.html");
        cartPageObj.fillCheckoutInfo("John", "Doe", "12345");
        cartPageObj.clickAndWaitForUrl(cartPageObj.CHECKOUT_CONTINUE_BUTTON, "checkout-step-two.html");
    }

    // ===================== CART PAGE APPEARANCE =====================

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = {"smoke", "regression"}, priority = 0, description = "Cart page appears with all main elements")
    public void cartPageAppearanceTestAndAssertion() {
        cartPageObj.openCart();
        Assert.assertEquals(cartPageObj.getUrl(), CART_URL, "Cart page URL mismatch");
        cartPageObj.assertionHard(cartPageObj.CART_PAGE_TITLE_LOCATOR, cartPageObj.CART_PAGE_TITTLE);
        cartPageObj.assertionHard(cartPageObj.QUANTITY_TITTLE_LOCATOR, cartPageObj.QUANTITY_TITTLE_TEXT);
        cartPageObj.assertionHard(cartPageObj.CART_DESCRIPTION_TITTLE_LOCATOR, cartPageObj.CART_DESCRIPTION_TITTLE_TEXT);
        Assert.assertTrue(cartPageObj.isElementVisible(cartPageObj.CART_CONTINUE_SHOPPING_BUTTON, cartPageObj.small_wait), "Continue Shopping button not visible");
        Assert.assertTrue(cartPageObj.isElementVisible(cartPageObj.CART_CHECKOUT_BUTTON, cartPageObj.small_wait), "Checkout button not visible");
        cartPageObj.logInfo("Cart page elements verified");
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = {"regression"}, priority = 1, description = "Cart is empty when nothing was added")
    public void emptyCartHasNoItems() {
        cartPageObj.openCart();
        Assert.assertTrue(cartPageObj.getCartItemNames().isEmpty(), "Cart should be empty");
        Assert.assertEquals(homePageObj.getCartBadgeCount(), 0, "Badge should not show a number");
    }

    // ===================== ITEMS IN CART =====================

    @Severity(SeverityLevel.CRITICAL)
    @Test(dataProvider = "products",dataProviderClass = CommonLocators.class, groups = {"smoke", "regression"}, priority = 2, description = "Added product shows in cart with correct name, price and quantity")
    public void addedItemAppearsInCart(String productName, String expectedPrice) {
        addItemsAndOpenCart(Arrays.asList(productName));
        Assert.assertEquals(cartPageObj.getCartItemNames(), Arrays.asList(productName), "Wrong item in cart");
        Assert.assertEquals(cartPageObj.getCartItemPrice(productName), expectedPrice, "Wrong price for " + productName);
        Assert.assertEquals(cartPageObj.getCartItemQuantity(productName), "1", "Quantity should be 1");
        cartPageObj.logInfo("Cart item verified: " + productName + " = " + expectedPrice);
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = {"regression"}, priority = 3, description = "Several added products all show in cart and badge matches")
    public void multipleItemsAppearInCart() {
        List<String> items = threeItems();
        addItemsAndOpenCart(items);
        Assert.assertEquals(cartPageObj.getCartItemNames(), items, "Cart items / order mismatch");
        Assert.assertEquals(homePageObj.getCartBadgeCount(), items.size(), "Badge count mismatch");
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = {"regression"}, priority = 4, description = "Cart items are kept after page refresh")
    public void cartItemsPersistAfterRefresh() {
        List<String> items = threeItems();
        addItemsAndOpenCart(items);
        cartPageObj.navigaterRefresh();
        Assert.assertTrue(cartPageObj.isElementVisible(cartPageObj.CART_PAGE_TITLE_LOCATOR, cartPageObj.small_wait));
        Assert.assertEquals(cartPageObj.getCartItemNames(), items, "Cart items lost after refresh");
    }

    // ===================== REMOVE =====================

    @Severity(SeverityLevel.CRITICAL)
    @Test(dataProvider = "products",dataProviderClass = CommonLocators.class,groups = {"smoke", "regression"}, priority = 5, description = "Removing an item from the cart empties it and clears the badge")
    public void removeItemFromCart(String productName, String price) {
        addItemsAndOpenCart(Arrays.asList(productName));
        cartPageObj.clickOn(homePageObj.removeButton(productName));
        Assert.assertTrue(cartPageObj.getCartItemNames().isEmpty(), productName + " still in cart");
        Assert.assertEquals(homePageObj.getCartBadgeCount(), 0, "Badge should be cleared");
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = {"regression"}, priority = 6, description = "Removing one of several items keeps the others")
    public void removeOneOfManyItems() {
        List<String> items = threeItems();
        addItemsAndOpenCart(items);
        cartPageObj.clickOn(homePageObj.removeButton(homePageObj.first_item_name));
        Assert.assertEquals(cartPageObj.getCartItemNames(), items.subList(1, items.size()), "Wrong items left");
        Assert.assertEquals(homePageObj.getCartBadgeCount(), items.size() - 1, "Badge count mismatch");
    }

    // ===================== NAVIGATION =====================

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = {"smoke", "regression"}, priority = 7, description = "Continue Shopping returns to the products page and keeps the cart")
    public void continueShoppingReturnsToHomePage() {
        addItemsAndOpenCart(Arrays.asList(homePageObj.first_item_name));
        cartPageObj.clickAndWaitForUrl(cartPageObj.CART_CONTINUE_SHOPPING_BUTTON, "inventory.html");
        Assert.assertEquals(cartPageObj.getUrl(), HOME_URL, "Did not return to products page");
        Assert.assertEquals(homePageObj.getCartBadgeCount(), 1, "Cart should still hold the item");
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = {"smoke", "regression"}, priority = 8, description = "Checkout button opens the information page")
    public void checkoutButtonOpensInformationPage() {
        addItemsAndOpenCart(Arrays.asList(homePageObj.first_item_name));
        cartPageObj.clickAndWaitForUrl(cartPageObj.CART_CHECKOUT_BUTTON, "checkout-step-one.html");
        Assert.assertEquals(cartPageObj.getUrl(), CHECKOUT_URL, "Checkout URL mismatch");
        cartPageObj.assertionHard(cartPageObj.CART_PAGE_TITLE_LOCATOR, cartPageObj.CHECKOUT_INFORMATION_TITTLE_TEXT);
        cartPageObj.assertPlaceholderText(cartPageObj.CART_CHECKOUT_INFO_FIRST_NAME_INPUT_FIELD, cartPageObj.CART_CHECKOUT_INFO_FIRST_NAME_INPUT_FIELD_PLACEHOLDER);
        cartPageObj.assertPlaceholderText(cartPageObj.CART_CHECKOUT_INFO_LAST_NAME_INPUT_FIELD, cartPageObj.CART_CHECKOUT_INFO_LAST_NAME_INPUT_FIELD_PLACEHOLDER);
        cartPageObj.assertPlaceholderText(cartPageObj.CART_CHECKOUT_INFO_ZIP_POSTCODE_INPUT_FIELD, cartPageObj.CART_CHECKOUT_INFO_ZIP_POSTCODE_INPUT_FIELD_PLACEHOLDER);
    }

    // ===================== CHECKOUT INFORMATION =====================

    @Severity(SeverityLevel.NORMAL)
    @Test(dataProvider = "checkoutInfoValidation",dataProviderClass = CartPage.class, groups = {"regression"}, priority = 9, description = "Missing checkout information shows the right error")
    public void checkoutInformationValidation(String first, String last, String zip, String expectedError) {
        addItemsAndOpenCart(Arrays.asList(homePageObj.first_item_name));
        cartPageObj.clickAndWaitForUrl(cartPageObj.CART_CHECKOUT_BUTTON, "checkout-step-one.html");
        cartPageObj.fillCheckoutInfo(first, last, zip);
        cartPageObj.clickOn(cartPageObj.CHECKOUT_CONTINUE_BUTTON);
        Assert.assertTrue(cartPageObj.isElementVisible(cartPageObj.CHECKOUT_ERROR_MESSAGE, cartPageObj.small_wait), "Error message not shown");
        Assert.assertEquals(cartPageObj.getText(cartPageObj.CHECKOUT_ERROR_MESSAGE), expectedError);
        Assert.assertEquals(cartPageObj.getUrl(), CHECKOUT_URL, "Should stay on the information page");
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = {"regression"}, priority = 10, description = "Cancel on the information page returns to the cart")
    public void cancelOnInformationPageReturnsToCart() {
        addItemsAndOpenCart(Arrays.asList(homePageObj.first_item_name));
        cartPageObj.clickAndWaitForUrl(cartPageObj.CART_CHECKOUT_BUTTON, "checkout-step-one.html");
        cartPageObj.clickAndWaitForUrl(cartPageObj.CART_CANCEL_BUTTON, "cart.html");
        Assert.assertEquals(cartPageObj.getUrl(), CART_URL, "Cancel did not return to cart");
        Assert.assertEquals(cartPageObj.getCartItemNames(), Arrays.asList(homePageObj.first_item_name), "Cart items should be kept");
    }

    // ===================== CHECKOUT OVERVIEW =====================

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = {"smoke", "regression"}, priority = 11, description = "Overview shows the right items, subtotal, tax and total")
    public void checkoutOverviewItemsAndTotals() {
        List<String> items = threeItems();
        goToCheckoutOverview(items);
        cartPageObj.assertionHard(cartPageObj.CART_PAGE_TITLE_LOCATOR, cartPageObj.CHECK_OUT_OVERVIEW_PAGE_TITTLE_TEXT);
        Assert.assertEquals(cartPageObj.getCartItemNames(), items, "Overview items mismatch");

        double subtotal = cartPageObj.getAmount(cartPageObj.OVERVIEW_SUBTOTAL_LABEL);
        double tax = cartPageObj.getAmount(cartPageObj.OVERVIEW_TAX_LABEL);
        double total = cartPageObj.getAmount(cartPageObj.OVERVIEW_TOTAL_LABEL);

        Assert.assertEquals(subtotal, threeItemsSubtotal(), 0.001, "Item total is not the sum of the prices");
        Assert.assertEquals(tax, subtotal * 0.08, 0.011, "Tax is not 8% of the item total");
        Assert.assertEquals(total, subtotal + tax, 0.011, "Total is not item total + tax");
        cartPageObj.logInfo("Overview totals verified: " + subtotal + " + " + tax + " = " + total);
    }

    @Severity(SeverityLevel.MINOR)
    @Test(groups = {"regression"}, priority = 12, description = "Overview shows payment and shipping information labels")
    public void checkoutOverviewInformationLabels() {
        goToCheckoutOverview(Arrays.asList(homePageObj.first_item_name));
        cartPageObj.assertionHard(cartPageObj.CART_OVER_VIEW_PAYMENT_INFORMATION_LABEL_LOCATOR, cartPageObj.CART_OVER_VIEW_PAYMENT_INFORMATION_LABEL_TEXT);
        Assert.assertTrue(cartPageObj.getText(cartPageObj.CART_OVER_VIEW_PAYMENT_INFORMATION_DATA_LOCATOR).startsWith(cartPageObj.CART_OVER_VIEW_PAYMENT_INFORMATION_DATA_TEXT));
        cartPageObj.assertionHard(cartPageObj.CART_OVER_VIEW_SHIPPING_INFO_LABEL_LOCATOR, cartPageObj.CART_OVER_VIEW_SHIPPING_INFO_LABEL_TEXT);
        cartPageObj.assertionHard(cartPageObj.CART_OVER_VIEW_PRICE_TOTAL_LABEL_LOCATOR, cartPageObj.CART_OVER_VIEW_PRICE_TOTAL_LABEL_TEXT);
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = {"regression"}, priority = 13, description = "Cancel on the overview page returns to the products page")
    public void cancelOnOverviewPageReturnsToHome() {
        goToCheckoutOverview(Arrays.asList(homePageObj.first_item_name));
        cartPageObj.clickAndWaitForUrl(cartPageObj.CART_CANCEL_BUTTON, "inventory.html");
        Assert.assertEquals(cartPageObj.getUrl(), HOME_URL, "Cancel did not return to products page");
    }

    // ===================== COMPLETE ORDER =====================

    @Severity(SeverityLevel.BLOCKER)
    @Test(groups = {"smoke", "regression"}, priority = 14, description = "Full purchase: order completes, message shows, cart is cleared")
    public void completeOrderEndToEnd() {
        goToCheckoutOverview(threeItems());
        cartPageObj.clickAndWaitForUrl(cartPageObj.CART_FINISH_BUTTON, "checkout-complete.html");
        cartPageObj.assertionHard(cartPageObj.CART_PAGE_TITLE_LOCATOR, "Checkout: Complete!");
        cartPageObj.assertionHard(cartPageObj.COMPLETE_HEADER, cartPageObj.CART_COMPLETE_PAGE_TITTLE_TEXT);
        cartPageObj.assertionHard(cartPageObj.COMPLETE_TEXT, cartPageObj.CART_COMPLETE_PAGE_SUB_TITTLE_TEXT);

        cartPageObj.clickAndWaitForUrl(cartPageObj.CART_HOME_BUTTON, "inventory.html");
        Assert.assertEquals(cartPageObj.getUrl(), HOME_URL, "Back Home did not open the products page");
        Assert.assertEquals(homePageObj.getCartBadgeCount(), 0, "Cart should be empty after the order");
        cartPageObj.logInfo("Order completed successfully");
    }
}