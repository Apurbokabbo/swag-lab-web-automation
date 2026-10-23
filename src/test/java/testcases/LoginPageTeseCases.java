package testcases;


import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.Test;
import pages.LoginPage;
import utilities.BaseDriver;

import static utilities.URLs.BASE_URL;

@Feature("Authentication")
@Epic("Login Page Test Cases")

public class LoginPageTeseCases extends BaseDriver{
	
	
	LoginPage loginObj = new LoginPage() ;

	@Severity(SeverityLevel.NORMAL)
	@Test(groups = {"smoke", "regression"}, priority = 0)
	public void loginPageAppear()  {
		loginObj.isElementVisible(loginObj.LOGIN_PAGE_TITTLE_LOCATOR, loginObj.small_wait);
		loginObj.isElementVisible(loginObj.LOGIN_PAGE_LOGO_LOCATOR,loginObj.small_wait);
		loginObj.assertionURL(BASE_URL);
		loginObj.assertPlaceholderText(loginObj.LOGIN_PAGE_USER_NAME_INPUT_FIELD, loginObj.LOGIN_PAGE_USER_NAME_INPUT_FIELD_PLACEHOLDER_TEXT);
		loginObj.assertPlaceholderText(loginObj.LOGIN_PAGE_PASSWORD_INPUT_FIELD, loginObj.LOGIN_PAGE_PASSWORD_INPUT_FIELD_PLACEHOLDER_TEXT);
		loginObj.isElementVisible(loginObj.LOGIN_BUTTON,loginObj.small_wait);
		loginObj.logInfo("Login page elements are visible");
	}

	@Severity(SeverityLevel.CRITICAL)
	@Test(groups = {"smoke", "regression"}, priority = 1)
	public void loginWithValidCredentials() {
		loginObj.loginWithValidCredentials(loginObj.login_user_name, loginObj.login_password);
	}


	@Severity(SeverityLevel.NORMAL)
	@Test(groups = {"regression"}, priority = 2)
	public void emptyFieldWarningMessageAssertion()  {
		loginObj.loginWithInvalidData("","", "Epic sadface: Username is required");
		loginObj.loginWithInvalidData(loginObj.login_user_name,"", "Epic sadface: Password is required");

	}



}
