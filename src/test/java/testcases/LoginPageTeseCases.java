package testcases;


import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.Test;
import pages.LoginPage;
import utilities.BaseDriver;

import static pages.BasePage.softAssert;

public class LoginPageTeseCases extends BaseDriver{
	
	
	LoginPage loginObj = new LoginPage() ;

	@Severity(SeverityLevel.CRITICAL)
	@Test(groups = {"smoke", "regression"}, priority = 1)
	public void loginWithValidCredentials() throws InterruptedException{
		
		loginObj.loginWithValidCredentials("standard_user", loginObj.PASSWORD);
		
	}

	@Severity(SeverityLevel.NORMAL)
	@Test(groups = {"smoke", "regression"}, priority = 0)
	public void placeHolderVerify() throws InterruptedException {
		loginObj.assertPlaceholderText(loginObj.LOGIN_PAGE_USER_NAME_INPUT_FIELD, loginObj.LOGIN_PAGE_USER_NAME_INPUT_FIELD_PLACEHOLDER_TEXT);
		loginObj.assertPlaceholderText(loginObj.LOGIN_PAGE_PASSWORD_INPUT_FIELD, loginObj.LOGIN_PAGE_PASSWORD_INPUT_FIELD_PLACEHOLDER_TEXT);
	}

	@Severity(SeverityLevel.NORMAL)
	@Test(groups = {"regression"}, priority = 2)
	public void emptyFieldWarningMessageAssertion() throws InterruptedException {
		loginObj.loginMethodWithOutValidData("","", "Epic sadface: Username is required");
		loginObj.loginMethodWithOutValidData("standard_user","", "Epic sadface: Password is required");

	}



}
