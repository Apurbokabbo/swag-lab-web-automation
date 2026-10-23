package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import utilities.ConfigReader;


public class LoginPage extends BasePage{

	public String login_user_name = ConfigReader.get("LOGIN_USER","");
	public String login_password = ConfigReader.get("LOGIN_PASSWORD","");

	public String LOGIN_PAGE_USER_NAME_INPUT_FIELD_PLACEHOLDER_TEXT = "Username";
	public String LOGIN_PAGE_PASSWORD_INPUT_FIELD_PLACEHOLDER_TEXT = "Password";
	public String PASSWORD = "secret_sauce";
	public String ERROR_FOR_BLOCKED_USER_TEXT = "Epic sadface: Sorry, this user has been locked out.";
	public String ERROR_FOR_CREDENTIALS_WRONG_TEXT = "Epic sadface: Username and password do not match any user in this service";



	public By LOGIN_PAGE_TITTLE_LOCATOR = By.xpath("//div[contains(text(),'Swag Labs')]"); 
	public By HOME_PAGE_TITTLE_LOCATOR = By.xpath("//div[@id='']"); 
	public By LOGIN_PAGE_USER_NAME_INPUT_FIELD = By.xpath("//input[@id='user-name']"); 
	public By LOGIN_PAGE_PASSWORD_INPUT_FIELD = By.xpath("//input[@id='password']"); 
	public By LOGIN_BUTTON = By.xpath("//input[@id='login-button']"); 
	public By PASSWORD_FOR_ALL_USERS_LOCATOR = By.xpath("//body/div[@id='root']/div[1]/div[2]/div[2]/div[1]/div[2]"); 
	public By ACCEPTED_USER_NAMES = By.xpath("//div[@id='login_credentials']"); 
	public By ERROR_MESSAGE_LOCATOR = By.xpath("//body/div[@id='root']/div[1]/div[2]/div[1]/div[1]/div[1]/form[1]/div[3]/h3[1]");


	@DataProvider(name = "invalidLoginData")
	public static Object[][] invalidLoginData() {
		String mismatch = "Epic sadface: Username and password do not match any user in this service";
		return new Object[][]{
				// username,             password,                       expected error message
				{"",                     "",                             "Epic sadface: Username is required"},
				{"standard_user",        "",                             "Epic sadface: Password is required"},
				{"",                     "secret_sauce",                 "Epic sadface: Username is required"},
				{"wrong_user",           "secret_sauce",                 mismatch},
				{"standard_user",        "wrong_password",               mismatch},
				{"wrong_user",           "wrong_password",               mismatch},
				{"STANDARD_USER",        "secret_sauce",                 mismatch},   // username is case-sensitive
				{"standard_user",        "SECRET_SAUCE",                 mismatch},   // password is case-sensitive
				{" standard_user ",      "secret_sauce",                 mismatch},   // leading/trailing spaces
				{"' OR '1'='1",          "' OR '1'='1",                  mismatch},   // SQL-injection style input
				{"<script>alert(1)</script>", "secret_sauce",            mismatch}    // script-style input
		};
	}

	@DataProvider(name = "lockedOutUser")
	public static Object[][] lockedOutUser() {
		return new Object[][]{
				{"locked_out_user", "secret_sauce", "Epic sadface: Sorry, this user has been locked out."}
		};
	}

	
	
	public void loginWithValidCredentials(String userName , String password)  {
		logInfo("Login With :" + userName);
		fluentWaitClickOnElement(LOGIN_PAGE_USER_NAME_INPUT_FIELD,medium_wait);
		findElement(LOGIN_PAGE_USER_NAME_INPUT_FIELD).sendKeys(userName);
		findElement(LOGIN_PAGE_PASSWORD_INPUT_FIELD).sendKeys(password);
		fluentWaitClickOnElement(LOGIN_BUTTON,small_wait);
		logInfo("With Credentials Submitted For Login ");
		waitForVisibilityOfElement(LOGIN_PAGE_TITTLE_LOCATOR, small_wait);
		assertionHard(SWAG_LAB_PAGE_TITTLE_LOCATOR,SWAG_LAB_PAGE_TITTLE_TEXT);
		logInfo("Login With Valid Credentials And Home Page Appear");
		
	}
	
	
	public void loginWithInvalidData(String userName , String password , String errorMessage ){
		logInfo("Login With :" + userName);
		fluentWaitClickOnElement(LOGIN_PAGE_USER_NAME_INPUT_FIELD,medium_wait);
		findElement(LOGIN_PAGE_USER_NAME_INPUT_FIELD).sendKeys(userName);
		findElement(LOGIN_PAGE_PASSWORD_INPUT_FIELD).sendKeys(password);
		fluentWaitClickOnElement(LOGIN_BUTTON,small_wait);
		logInfo("With Credentials Submitted For Login ");
		waitForVisibilityOfElement(ERROR_MESSAGE_LOCATOR, small_wait);
		logInfo("Error Message Appear For Invalid Credentials");
		assertionHard(ERROR_MESSAGE_LOCATOR, errorMessage);
		logInfo("Invalid Credentials Error Message Verified");
		
	}
	
	
	

}
