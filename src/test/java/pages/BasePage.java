package pages;

import io.github.cdimascio.dotenv.Dotenv;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import io.qameta.allure.Allure;
import utilities.TestListener;

import static org.testng.Assert.assertEquals;
import static utilities.BaseDriver.getDriver;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BasePage {

	Dotenv dotenv = Dotenv.load();
	public String login_user_name = dotenv.get("LOGIN_USER");
	public String login_password = dotenv.get("LOGIN_PASSWORD");




	private static final String LOWERCASE_CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
	private static final String UPPERCASE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String NUMERIC_CHARACTERS = "0123456789";
	private static final String SPECIAL_CHARACTERS = "$%&_+-.@#";

	public String SWAG_LAB_PAGE_TITTLE_TEXT="Swag Labs";



	//TOP NAV BAR ELEMENTS
	public By TOP_NAV_BAR_CART_BUTTON = By.xpath("//a[@class='shopping_cart_link']");
	public By HOME_MENU_BUTTON = By.xpath("//button[@id='react-burger-menu-btn']");
	public By SWAG_LAB_PAGE_TITTLE_LOCATOR = By.xpath("//div[contains(text(),'Swag Labs')]");


	//Product
	public String cart_page_tittle_text = "Your Cart";
	public String cart_page_continue_shopping_button_text = "Continue Shopping";
	public String cart_page_checkout_button_text = "Checkout";
	public String cart_page_remove_button_text = "Remove";

	public String first_item_name = "Sauce Labs Backpack";
	public String second_item_name = "Sauce Labs Bike Light";
	public String third_item_name = "Sauce Labs Bolt T-Shirt";
	public String fourth_item_name = "Sauce Labs Fleece Jacket";
	public String fifth_item_name = "Sauce Labs Onesie";
	public String sixth_item_name = "Test.allTheThings() T-Shirt (Red)";

	public String first_item_price = "$29.99";
	public String second_item_price = "$9.99";
	public String third_item_price = "$15.99";
	public String fourth_item_price = "$49.99";
	public String fifth_item_price = "$7.99";
	public String sixth_item_price = "$15.99";




	public static SoftAssert softAssert = new SoftAssert();
	private static final Logger logger = LoggerFactory.getLogger(TestListener.class);

	public  WebElement findElement(By locator)
	{
		return getDriver().findElement(locator);
	}

	public void clickOn(By locator)
	{
		findElement(locator).click();
	}

	public void writeText(By locator , String text) throws InterruptedException
	{
		findElement(locator).click();
		hardTextClear(locator);
		findElement(locator).sendKeys(text);
		Thread.sleep(500);
	}

	public void hardTextClear(By locator){
		findElement(locator).sendKeys(Keys.chord(Keys.CONTROL, "a")); // Select all existing text
		findElement(locator).sendKeys(Keys.DELETE); // Delete existing text
	}

	public String getText(By locator)
	{
		return findElement(locator).getText();
	}

	public String getUrl()
	{
		return getDriver().getCurrentUrl();
	}

	public void hover(By locator)
	{
		Actions action =  new Actions(getDriver());
		action.moveToElement(findElement(locator)).perform();
	}

	public void assertionHard(By locator, String expected)
	{
		assertEquals(getText(locator), expected);
	}

	public void assertionURL(String expected)
	{
		softAssert.assertEquals(getUrl(), expected);
	}

	public void assertionSoft(By locator, String expected)
	{
		softAssert.assertEquals(getText(locator), expected, "Text did not match!");
	}

	public void passwordSaveAlertDisappear(){

//		try {
//			WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(3));
//			// Try multiple possible button texts
//			List<String> buttonTexts = Arrays.asList("OK", "Not now", "No thanks", "Never");
//
//			for (String text : buttonTexts) {
//				try {
//					WebElement button = wait.until(ExpectedConditions.elementToBeClickable(
//							By.xpath(String.format("//button[contains(text(), '%s')]", text))
//					));
//					button.click();
//					return;
//				} catch (TimeoutException ignored) {}
//			}
//		} catch (Exception e) {
//			// Popup didn't appear, continue
//		}
		try {
			// Method 1: Press Escape key
			Actions actions = new Actions(getDriver());
			actions.sendKeys(Keys.ESCAPE).perform();
			Thread.sleep(500);
			System.out.println("Pressed Escape to dismiss popup");

		} catch (Exception e1) {
			try {
				// Method 2: Click outside the popup
				JavascriptExecutor js = (JavascriptExecutor) getDriver();
				js.executeScript("document.body.click();");
				Thread.sleep(500);
				System.out.println("Clicked outside popup");

			} catch (Exception e2) {
				try {
					// Method 3: Try to find and click button
					WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(3));
					WebElement button = wait.until(ExpectedConditions.elementToBeClickable(
							By.cssSelector("button[aria-label*='OK'], button[aria-label*='Close'], button.mdc-button")
					));
					button.click();
					System.out.println("Clicked dismiss button");

				} catch (Exception e3) {
					System.out.println("Could not dismiss popup, continuing...");
				}
			}
		}
	}

	public void alertAccept(By locator)
	{
		clickOn(locator);
		getDriver().switchTo().alert().accept();
	}

	public void alertCancel(By locator)
	{
		clickOn(locator);
		getDriver().switchTo().alert().dismiss();
	}

	public void alertInput(By locator, String text)
	{
		clickOn(locator);
		getDriver().switchTo().alert().sendKeys(text);

	}

	public void dropDownSelectByVisibleText(By locator, String text)
	{
		Select select = new Select(findElement(locator));
		select.selectByVisibleText(text);
	}

	public void dropDownSelectByValue(By locator, String text)
	{
		Select select = new Select(findElement(locator));
		select.selectByValue(text);
	}

	public void dropDownSelectByIndex(By locator, int index)
	{
		Select select = new Select(findElement(locator));
		select.selectByIndex(index);

	}

	public void dropDownDeselectByVisibleText(By locator, String text)
	{
		Select select = new Select(findElement(locator));
		select.deselectByVisibleText(text);
	}

	public void dropDownDeselectByValue(By locator, String text)
	{
		Select select = new Select(findElement(locator));
		select.deselectByValue(text);
	}

	public void dropDownDeselectByIndex(By locator, int index)
	{
		Select select = new Select(findElement(locator));
		select.deselectByIndex(index);

	}

	public void newTab()
	{
		getDriver().switchTo().newWindow(WindowType.TAB);
	}

	public void newWindow()
	{
		getDriver().switchTo().newWindow(WindowType.WINDOW);
	}

	public List<String> windowHandling()
	{
		Set<String> windowHandles = getDriver().getWindowHandles();
		ArrayList<String> windowHandlesList = new ArrayList<String>(windowHandles);
		int size = windowHandlesList.size();
		System.out.println("Window/Tab numbers: "+size);
		return windowHandlesList;
	}

	public void windowSwitch(int tabnumber)
	{
		try
		{
			getDriver().switchTo().window(windowHandling().get(tabnumber));
		}
		catch (Exception e)
		{
			System.out.println("Window tab number not exist!!!" +"Tab number is given :"+ tabnumber);
		}

	}

	public void navigaterForward()
	{
		getDriver().navigate().forward();
	}

	public void navigaterBack()
	{
		getDriver().navigate().back();
	}

	public void navigaterRefresh()
	{

		getDriver().navigate().refresh();
	}

	public void navigaterTo(String url)
	{

		getDriver().navigate().to(url);
	}



	public void findTotalElement(String tagName)
	{
		List<WebElement> elements = getDriver().findElements(By.tagName(tagName));
		System.out.println(elements.size());
	}

	public void keyboardTab()
	{
		Actions action =  new Actions(getDriver());
		action.sendKeys(Keys.TAB);
		action.build().perform();
	}

	public void tabOnEnterOnKeyboard()
	{
		Actions action =  new Actions(getDriver());
		action.sendKeys(Keys.ENTER);
		action.build().perform();
	}

	public void keyboardSelectAll(By locator)
	{
		Actions action =  new Actions(getDriver());
		findElement(locator).click();
		action.keyDown(Keys.CONTROL);
		action.sendKeys("a");
		action.keyUp(Keys.CONTROL);
		action.build().perform();

	}

	public void keyboardBackspace()
	{
		Actions action =  new Actions(getDriver());
		action.keyDown(Keys.BACK_SPACE);
		action.build().perform();
	}

	public void keyboardCopy(By locator)
	{
		Actions action =  new Actions(getDriver());
		keyboardSelectAll(locator);
		action.keyDown(Keys.CONTROL);
		action.sendKeys("c");
		action.keyUp(Keys.CONTROL);
		action.build().perform();

	}

	public void copyToClipboard(String text) {
		StringSelection stringSelection = new StringSelection(text);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);
	}

	public void keyboardPaste()
	{
		Actions action =  new Actions(getDriver());
		action.keyDown(Keys.CONTROL);
		action.sendKeys("v");
		action.keyUp(Keys.CONTROL);
		action.build().perform();
	}

	public void scrollUntilElementFound(By targetLocator, By... optionalContainerLocator) {
		JavascriptExecutor js = (JavascriptExecutor) getDriver();

		WebElement container = null;
		boolean isContainerScroll = optionalContainerLocator.length > 0;

		if (isContainerScroll) {
			container = findElement(optionalContainerLocator[0]);
		}

		int maxScrolls = 30;
		int scrollStep = 200;
		boolean elementFound = false;

		for (int i = 0; i < maxScrolls; i++) {
			try {
				WebElement target = findElement(targetLocator);
				if (target.isDisplayed()) {
					elementFound = true;
					break;
				}
			} catch (NoSuchElementException e) {
				// element not yet in DOM or not visible
			}

			if (isContainerScroll) {
				js.executeScript("arguments[0].scrollTop += arguments[1];", container, scrollStep);
			} else {
				js.executeScript("window.scrollBy(0, arguments[0]);", scrollStep);
			}

			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		if (!elementFound) {
			throw new RuntimeException("Element not found after scrolling: " + targetLocator.toString());
		}
	}




	public void scrollHorizontallyLeft(By locator) {
		JavascriptExecutor js = (JavascriptExecutor) getDriver();
		js.executeScript("arguments[0].scrollLeft = arguments[0].scrollWidth;", getDriver().findElement(locator));

	}

	public void scrollHorizontallyUntilVisible(By containerLocator, By targetLocator) {
		JavascriptExecutor js = (JavascriptExecutor) getDriver();
		WebElement container = getDriver().findElement(containerLocator);
		WebElement target = getDriver().findElement(targetLocator);

		int maxScroll = container.getSize().width * 10; // Avoid infinite loops
		int scrolled = 0;
		int step = 50;

		while (!isElementInView(container, target) && scrolled < maxScroll) {
			js.executeScript("arguments[0].scrollLeft += arguments[1];", container, step);
			scrolled += step;
		}
	}

	private boolean isElementInView(WebElement container, WebElement target) {
		JavascriptExecutor js = (JavascriptExecutor) getDriver();
		return (Boolean) js.executeScript(
				"const container = arguments[0], elem = arguments[1];" +
						"const containerRect = container.getBoundingClientRect();" +
						"const elemRect = elem.getBoundingClientRect();" +
						"return elemRect.left >= containerRect.left && elemRect.right <= containerRect.right;"
				, container, target);
	}

	public void scrollToElement(By locator) {
		WebElement element = getDriver().findElement(locator);
		((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
	}

	public void takeScreenShotAllureAttach(String screenshotname) {
		Allure.addAttachment(screenshotname, new ByteArrayInputStream(((TakesScreenshot)getDriver()).getScreenshotAs(OutputType.BYTES)));
	}

	public void waitForVisibilityOfElement(By locator, int timeoutInSeconds) {
		WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeoutInSeconds));
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	public void fluentWaitClickOnElement(By locator, int holdInSeconds) {
		FluentWait<WebDriver> wait = new FluentWait<>(getDriver())
				.withTimeout(Duration.ofSeconds(holdInSeconds))
				.pollingEvery(Duration.ofMillis(500))
				.ignoring(NoSuchElementException.class);

		WebElement element = wait.until(driver -> {
			WebElement el = driver.findElement(locator);
			return (el != null && el.isDisplayed() && el.isEnabled()) ? el : null;
		});

		element.click();
	}

	public static WebElement fluentWaitForElement(By locator, int timeoutInSeconds, int pollingTimeInMillis) {
		Wait<WebDriver> wait = new FluentWait<>(getDriver())
				.withTimeout(Duration.ofSeconds(timeoutInSeconds))
				.pollingEvery(Duration.ofMillis(pollingTimeInMillis))
				.ignoring(NoSuchElementException.class); // Ignore NoSuchElementException

		return wait.until(driver -> {
			WebElement element = driver.findElement(locator);
			return (element.isDisplayed()) ? element : null;
		});
	}

	public boolean isElementVisible(By locator, int timeoutInSeconds) {
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeoutInSeconds));
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			return true;
		} catch (TimeoutException e) {
			return false;
		}
	}



	public void toasterMessageAssertion(By toaster_locator , String expectMessage){
		isElementVisible(toaster_locator,7);
		assertionSoft(toaster_locator,expectMessage);
	}

	public  String generateRandomEmail() {
		String[] domains = { "gmail.com", "yahoo.com", "outlook.com", "example.com", "domain.com" };
		Random random = new Random();

		// Generate a random username
		StringBuilder username = new StringBuilder();
		for (int i = 0; i < 8; i++) {
			char randomChar = (char) (random.nextInt(26) + 'a');
			username.append(randomChar);
		}

		// Select a random domain
		String domain = domains[random.nextInt(domains.length)];

		// Combine username and domain to form the email address
		return username.toString() + "@" + domain;
	}

	public  String generatePassword(int length) {
		SecureRandom random = new SecureRandom();
		StringBuilder password = new StringBuilder();

		// Ensure at least one character from each character set
		password.append(LOWERCASE_CHARACTERS.charAt(random.nextInt(LOWERCASE_CHARACTERS.length())));
		password.append(UPPERCASE_CHARACTERS.charAt(random.nextInt(UPPERCASE_CHARACTERS.length())));
		password.append(NUMERIC_CHARACTERS.charAt(random.nextInt(NUMERIC_CHARACTERS.length())));
		password.append(SPECIAL_CHARACTERS.charAt(random.nextInt(SPECIAL_CHARACTERS.length())));

		// Generate the remaining characters
		for (int i = 4; i < length; i++) {
			String allCharacters = LOWERCASE_CHARACTERS + UPPERCASE_CHARACTERS + NUMERIC_CHARACTERS + SPECIAL_CHARACTERS;
			password.append(allCharacters.charAt(random.nextInt(allCharacters.length())));
		}

		// Shuffle the generated characters for better randomness
		char[] passwordArray = password.toString().toCharArray();
		for (int i = passwordArray.length - 1; i > 0; i--) {
			int index = random.nextInt(i + 1);
			char temp = passwordArray[index];
			passwordArray[index] = passwordArray[i];
			passwordArray[i] = temp;
		}
		String pass = new String(passwordArray);
		return pass;
	}


	public void logInfo(String info){
		String border = "-------------------------------------------------";
		logger.info(border);
		logger.info(info);
		logger.info(border);
	}

	public  void assertPlaceholderText(By locator, String expectedText) {

		try{
			String actualPlaceholder = getDriver().findElement(locator).getAttribute("placeholder");
			Assert.assertEquals(actualPlaceholder, expectedText, "Placeholder text does not match!");
		} catch (Exception e) {
			assertionHard(locator,expectedText);
		}
	}


	public void calendarInput(By calendarInputField, String inputText) throws InterruptedException {
		copyToClipboard(inputText);
		fluentWaitClickOnElement(calendarInputField,5);
		Thread.sleep(1000);
		keyboardSelectAll(calendarInputField);
		keyboardPaste();
		Thread.sleep(1000);
	}

	public void eyeButtonTypeVerifier(By inputFieldLocator, By eyeButtonLocator) {
		// Wait for password field to be visible
		isElementVisible(inputFieldLocator, 10);
		WebElement passwordField = findElement(inputFieldLocator);

		passwordField.sendKeys("Password100@");

		// Click the eye icon to show password
		fluentWaitClickOnElement(eyeButtonLocator, 5);

		// Verify password is now visible (type = "text")
		passwordField = findElement(inputFieldLocator);
		String afterFirstClick = passwordField.getAttribute("type");
		Assert.assertEquals(afterFirstClick, "text", "Password field should be visible (type='text') after first click.");

		// Click the eye icon again to hide password
		fluentWaitClickOnElement(eyeButtonLocator, 5);

		// Verify password is now hidden (type = "password")
//		passwordField = findElement(inputFieldLocator);
		String afterSecondClick = findElement(inputFieldLocator).getAttribute("type");
		Assert.assertEquals(afterSecondClick, "password", "Password field should be hidden (type='password') after second click.");
	}

	public void saveToFile(String value , File FILE_PATH) {

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
			writer.write(String.valueOf(value));

		}
		catch (IOException e) {

			System.err.println("Error saving balance to file: " + e.getMessage());

		}
	}

	public String readFile(File filePath) throws IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			return reader.readLine(); // Read and return only the first line
		}
	}



	public boolean isRadioChecked(By radioLocator, String value) {
		WebElement radioInput = findElement(radioLocator);
		WebElement parent = radioInput.findElement(
				By.xpath("./ancestor::span[contains(@class,'MuiRadio-root')]")
		);
		return parent.getAttribute("class").contains("Mui-checked");
	}

	public void forceWriteText(By locator, String text) throws InterruptedException {
		WebElement element = findElement(locator);
		element.click();
		element.sendKeys(Keys.CONTROL + "a");
		element.sendKeys(Keys.DELETE);
		element.sendKeys(text);
		Thread.sleep(500);
	}

	public void assertContains(By locator, String partialText){
		softAssert.assertTrue(findElement(locator).getText().contains(partialText), "Text does not contain!");
	}

	public boolean isFileDownloaded(String dirPath, String fileName, int timeoutInSeconds) {
		File dir = new File(dirPath);
		int elapsed = 0;
		while (elapsed < timeoutInSeconds) {
			File[] files = dir.listFiles();
			if (files != null) {
				for (File file : files) {
					if (file.getName().equals(fileName)) {
						return true;
					}
				}
			}
			try {
				Thread.sleep(1000);
				elapsed++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public void cleanDownloadFolder(String downloadPath) {
		File folder = new File(downloadPath);
		File[] files = folder.listFiles();
		if (files != null) {
			for (File file : files) {
				file.delete();
			}
		}
	}




}
