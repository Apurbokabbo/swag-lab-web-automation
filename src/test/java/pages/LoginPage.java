package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


public class LoginPage extends BasePage{
	
	

	public String LOGIN_PAGE_USER_NAME_INPUT_FIELD_PLACEHOLDER_TEXT = "Username";
	public String LOGIN_PAGE_PASSWORD_INPUT_FIELD_PLACEHOLDER_TEXT = "Password";
	public String PASSWORD = "secret_sauce";
	public String ERROR_FOR_BLOCKED_USER_TEXT = "Epic sadface: Sorry, this user has been locked out.";
	public String ERROR_FOR_CREDENTIALS_WRONG_TEXT = "Epic sadface: Username and password do not match any user in this service";
	public String a = "";

	
//	public By b = By.id(""); 
	public By LOGIN_PAGE_TITTLE_LOCATOR = By.xpath("//div[contains(text(),'Swag Labs')]"); 
	public By HOME_PAGE_TITTLE_LOCATOR = By.xpath("//div[@id='']"); 
	public By LOGIN_PAGE_USER_NAME_INPUT_FIELD = By.xpath("//input[@id='user-name']"); 
	public By LOGIN_PAGE_PASSWORD_INPUT_FIELD = By.xpath("//input[@id='password']"); 
	public By LOGIN_BUTTON = By.xpath("//input[@id='login-button']"); 
	public By PASSWORD_FOR_ALL_USERS_LOCATOR = By.xpath("//body/div[@id='root']/div[1]/div[2]/div[2]/div[1]/div[2]"); 
	public By ACCEPTED_USER_NAMES = By.xpath("//div[@id='login_credentials']"); 
	public By ERROR_MESSAGE_LOCATOR = By.xpath("//body/div[@id='root']/div[1]/div[2]/div[1]/div[1]/div[1]/form[1]/div[3]/h3[1]"); 
		
	public By c = By.xpath(""); 
	
	
	public void loginWithValidCredentials(String userName , String password) throws InterruptedException {
		
		Thread.sleep(500);
		findElement(LOGIN_PAGE_USER_NAME_INPUT_FIELD).sendKeys(userName);
		findElement(LOGIN_PAGE_PASSWORD_INPUT_FIELD).sendKeys(password);
		findElement(LOGIN_BUTTON).click();
		waitForVisibilityOfElement(LOGIN_PAGE_TITTLE_LOCATOR, 3);
		assertionHard(SWAG_LAB_PAGE_TITTLE_LOCATOR,SWAG_LAB_PAGE_TITTLE_TEXT);
		
	}
	
	
	public void loginMethodWithOutValidData(String userName , String password , String errorMessage ) throws InterruptedException {
		Thread.sleep(500);
		findElement(LOGIN_PAGE_USER_NAME_INPUT_FIELD).sendKeys(userName);
		findElement(LOGIN_PAGE_PASSWORD_INPUT_FIELD).sendKeys(password);
		findElement(LOGIN_BUTTON).click();
		waitForVisibilityOfElement(ERROR_MESSAGE_LOCATOR, 2);
		assertionHard(ERROR_MESSAGE_LOCATOR, errorMessage);
		
	}
	
	
	

}
