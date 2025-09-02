package testcases;


import org.testng.annotations.Test;
import pages.LoginPage;
import utilities.BaseDriver;

public class LoginPageTeseCases extends BaseDriver{
	
	
	LoginPage loginObj = new LoginPage() ;
	
	
	@Test
	public void loginWithValidCredentials() throws InterruptedException{
		
		loginObj.loginWithValidCredentials("standard_user", loginObj.PASSWORD);
		
	}
	
	
	@Test
	public void placeHolderVerify() throws InterruptedException {
		
		loginObj.assertionSoft(loginObj.LOGIN_PAGE_USER_NAME_INPUT_FIELD, loginObj.LOGIN_PAGE_USER_NAME_INPUT_FIELD_PLACEHOLDER_TEXT);
		loginObj.assertionSoft(loginObj.LOGIN_PAGE_PASSWORD_INPUT_FIELD, loginObj.LOGIN_PAGE_PASSWORD_INPUT_FIELD_PLACEHOLDER_TEXT);
	
		
		
	}

}
