package testcases;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.Test;
import pages.LoginPage;
import utilities.BaseDriver;

import static utilities.URLs.BASE_URL;

@Epic("Login Page Test Cases")
@Feature("Authentication")
public class LoginPageTeseCases extends BaseDriver {

	LoginPage loginObj = new LoginPage();



	@Severity(SeverityLevel.NORMAL)
	@Test(groups = {"smoke", "regression"},description = "Login page appear & data assertion", priority = 0)
	public void loginPageAppear() {
		loginObj.isElementVisible(loginObj.LOGIN_PAGE_TITTLE_LOCATOR, loginObj.small_wait);
		loginObj.isElementVisible(loginObj.LOGIN_PAGE_LOGO_LOCATOR, loginObj.small_wait);
		loginObj.assertionURL(BASE_URL);
		loginObj.assertPlaceholderText(loginObj.LOGIN_PAGE_USER_NAME_INPUT_FIELD, loginObj.LOGIN_PAGE_USER_NAME_INPUT_FIELD_PLACEHOLDER_TEXT);
		loginObj.assertPlaceholderText(loginObj.LOGIN_PAGE_PASSWORD_INPUT_FIELD, loginObj.LOGIN_PAGE_PASSWORD_INPUT_FIELD_PLACEHOLDER_TEXT);
		loginObj.isElementVisible(loginObj.LOGIN_BUTTON, loginObj.small_wait);
		loginObj.logInfo("Login page elements are visible");
	}

	@Severity(SeverityLevel.CRITICAL)
	@Test(groups = {"smoke", "regression"},description = "Login with valid credentials", priority = 1)
	public void loginWithValidCredentials() {
		loginObj.loginWithValidCredentials(LoginPage.STANDARD_USER_NAME, LoginPage.PASSWORD);
	}

	@Severity(SeverityLevel.NORMAL)
	@Test(groups = {"regression"}, description = "Assert warning messages for empty login fields",priority = 2)
	public void emptyFieldWarningMessageAssertion() {
		loginObj.loginWithInvalidData("", "", "Epic sadface: Username is required");
		loginObj.loginWithInvalidData(LoginPage.STANDARD_USER_NAME, "", "Epic sadface: Password is required");
	}


	@Severity(SeverityLevel.NORMAL)
	@Test(dataProvider = "invalidLoginData", dataProviderClass = LoginPage.class, groups = {"regression"},description = "Invalid login combinations show the correct error message" ,priority = 3 )
	public void loginWithInvalidDataProvider(String userName, String password, String expectedMessage) {
		loginObj.logInfo("Invalid login test - username: '" + userName + "', password: '" + password + "', expected: " + expectedMessage);
		loginObj.loginWithInvalidData(userName, password, expectedMessage);
		loginObj.logInfo("Error message verified for: '" + userName + "'");
	}


	@Severity(SeverityLevel.CRITICAL)
	@Test(dataProvider = "lockedOutUser", dataProviderClass = LoginPage.class ,groups = {"smoke", "regression"}, description = "Locked out user cannot log in",priority = 4 )
	public void lockedOutUserCannotLogin(String userName, String password, String expectedMessage) {
		loginObj.loginWithInvalidData(userName, password, expectedMessage);
		loginObj.logInfo("Error message verified for: '" + userName + "'");
	}
}