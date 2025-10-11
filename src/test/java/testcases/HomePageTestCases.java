package testcases;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import utilities.BaseDriver;



import static pages.BasePage.softAssert;
import static utilities.URLs.HOME_URL;

public class HomePageTestCases extends BaseDriver {

    LoginPage loginPageObj = new LoginPage();
    HomePage homePageObj = new HomePage();

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = {"smoke", "regression"}, priority = 0)
    public void homePageAppearanceTestAndAssertion() throws InterruptedException {
        loginPageObj.loginWithValidCredentials(loginPageObj.login_user_name, loginPageObj.login_password);
        homePageObj.isElementVisible(homePageObj.SWAG_LAB_PAGE_TITTLE_LOCATOR ,10);
        homePageObj.assertionURL(HOME_URL);
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = {"smoke", "regression"}, priority = 1)
    public void test() throws InterruptedException {
        homePageObj.appearHomePage(loginPageObj.login_user_name, loginPageObj.login_password);
        homePageObj.applyAndVerifyFilter(homePageObj.SORT_BUTTON, "Price (low to high)");


    }

}
