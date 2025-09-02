package utilities;

import java.time.Duration;
import java.util.HashMap;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import io.github.bonigarcia.wdm.WebDriverManager;

import static utilities.URLs.BASE_URL;


public class BaseDriver {

	private static String browserName = System.getProperty("browser", "chrome");
	private static String mode = System.getProperty("mode", "headed"); // added

	private static final ThreadLocal<WebDriver> lOCAL_DRIVER = new ThreadLocal<WebDriver>();

	public static void setDriver(WebDriver driver) {
		BaseDriver.lOCAL_DRIVER.set(driver);
		driver.get(BASE_URL);


		new WebDriverWait(driver, Duration.ofSeconds(10)).until(
				webDriver -> ((JavascriptExecutor) webDriver)
						.executeScript("return document.readyState").equals("complete")
		);
	}


	public static WebDriver getDriver() {
		return lOCAL_DRIVER.get();
	}

	public static WebDriver getBrowser(String browserName) {

		boolean isHeadless = mode.equalsIgnoreCase("headless");

		switch (browserName.toLowerCase()) {
			case "chrome":
				HashMap<String, Object> chromePrefs = new HashMap<>();
				chromePrefs.put("profile.default_content_settings.popups", 0);
				//chromePrefs.put("download.default_directory", "D:\\fastpay-support-panel-automation\\src\\test\\resources\\downloads"); // Set custom folder
				WebDriverManager.chromiumdriver().setup();
				ChromeOptions chromeOptions = new ChromeOptions();
				chromeOptions.setExperimentalOption("prefs", chromePrefs);
				if (isHeadless) {
					chromeOptions.addArguments("--headless=new");
					chromeOptions.addArguments("--window-size=1920,1080");
					chromeOptions.addArguments("--disable-gpu");
					chromeOptions.addArguments("--no-sandbox");
					chromeOptions.addArguments("--disable-setuid-sandbox");    // 👈 added
					chromeOptions.addArguments("--remote-allow-origins=*");
					chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
					chromeOptions.addArguments(
							"user-agent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) " +
									"Chromium/114.0.0.0 Safari/537.36"
					);

					// Use unique temp profile for each run
					// String uniqueProfile = "/tmp/chrome-profile-" + UUID.randomUUID();
					// chromeOptions.addArguments("--user-data-dir=" + uniqueProfile);

					// // Chromium binary path
					// chromeOptions.setBinary("/usr/bin/chromium");


					// WebDriverManager will handle the driver
					WebDriverManager.chromedriver().setup();

				}

				return new ChromeDriver(chromeOptions);

			case "firefox":
				WebDriverManager.firefoxdriver().setup();
				FirefoxOptions firefoxOptions = new FirefoxOptions();
				if (isHeadless) {
					firefoxOptions.addArguments("-headless");
					firefoxOptions.addArguments("--width=1920");
					firefoxOptions.addArguments("--height=1080");
				}
				return new FirefoxDriver(firefoxOptions);

			case "edge":
				WebDriverManager.edgedriver().setup();
				EdgeOptions edgeOptions = new EdgeOptions();
				if (isHeadless) {
					edgeOptions.addArguments("headless");
					edgeOptions.addArguments("window-size=1920,1080");
				}
				return new EdgeDriver(edgeOptions);

			default:
				throw new RuntimeException("Browser not Found!!! Using given name: " + browserName);
		}
	}

	@BeforeMethod(alwaysRun = true)
	public synchronized void setBrowser(ITestContext context) throws Exception {
		WebDriver driver = getBrowser(browserName);
		if (!mode.equalsIgnoreCase("headless")) {
			driver.manage().window().maximize();
		} else {
			driver.manage().window().setSize(new Dimension(1920, 1080));
		}

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

		setDriver(driver);
	}

	@AfterMethod(alwaysRun = true)
	public synchronized void closeBrowser() {
		getDriver().quit();
	}




}
