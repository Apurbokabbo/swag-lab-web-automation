package utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
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
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import pages.BasePage;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static utilities.URLs.BASE_URL;

public class BaseDriver {

    private static final String mode = ConfigReader.get("MODE", "headless");
    private static final String browserName = ConfigReader.get("BROWSER", "chromium");
    private static final String chromiumPath = ConfigReader.get("CHROMIUM_PATH", "/usr/bin/chromium-browser");

    private static final ThreadLocal<WebDriver> LOCAL_DRIVER = new ThreadLocal<>();

    // ---------------------------------------------------------------
    // Driver Management
    // ---------------------------------------------------------------
    public static void setDriver(WebDriver driver) {
        LOCAL_DRIVER.set(driver);
        driver.get(BASE_URL);

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(webDriver -> ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState")
                        .equals("complete"));
    }

    public static WebDriver getDriver() {
        WebDriver driver = LOCAL_DRIVER.get();
        if (driver == null) {
            driver = getBrowser(browserName);
            LOCAL_DRIVER.set(driver);
        }
        return driver;
    }

    // ---------------------------------------------------------------
    // Browser Factory
    // ---------------------------------------------------------------
    public static WebDriver getBrowser(String browserName) {

        boolean isHeadless = mode.equalsIgnoreCase("headless");

        switch (browserName.toLowerCase()) {

            case "chrome": {
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();

                // Disable password manager & set permissions
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("credentials_enable_service", false);
                prefs.put("profile.password_manager_enabled", false);
                prefs.put("profile.default_content_settings.popups", 0);
                prefs.put("profile.default_content_setting_values.notifications", 1);
                prefs.put("profile.default_content_setting_values.geolocation", 1);
                prefs.put("profile.managed_default_content_settings.geolocation", 1);

                chromeOptions.setExperimentalOption("prefs", prefs);
                chromeOptions.setExperimentalOption("excludeSwitches",
                        Arrays.asList("enable-automation"));

                // Prevent password popup and automation banner
                chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
                chromeOptions.addArguments("--incognito");
                chromeOptions.addArguments("--disable-features=PasswordManager");
                chromeOptions.addArguments("--enable-geolocation");

                if (isHeadless) {
                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--window-size=1920,1080");
                    chromeOptions.addArguments("--disable-gpu");
                    chromeOptions.addArguments("--no-sandbox");
                }

                return new ChromeDriver(chromeOptions);
            }

            case "chromium": {
                WebDriverManager.chromiumdriver().setup();
                ChromeOptions chromiumOptions = new ChromeOptions();

                Map<String, Object> chromiumPrefs = new HashMap<>();
                chromiumPrefs.put("profile.default_content_settings.popups", 0);

                // Override with -DCHROMIUM_PATH=... or CHROMIUM_PATH in .env
                chromiumOptions.setBinary(chromiumPath);
                chromiumOptions.setExperimentalOption("prefs", chromiumPrefs);

                if (isHeadless) {
                    chromiumOptions.addArguments("--headless=new");
                    chromiumOptions.addArguments("--window-size=1920,1080");
                    chromiumOptions.addArguments("--disable-gpu");
                    chromiumOptions.addArguments("--no-sandbox");
                    chromiumOptions.addArguments("--disable-setuid-sandbox");
                    chromiumOptions.addArguments("--remote-allow-origins=*");
                    chromiumOptions.addArguments("--disable-blink-features=AutomationControlled");
                    chromiumOptions.addArguments("user-agent=Mozilla/5.0 (X11; Linux x86_64) "
                            + "AppleWebKit/537.36 (KHTML, like Gecko) "
                            + "Chromium/114.0.0.0 Safari/537.36");
                }

                return new ChromeDriver(chromiumOptions);
            }

            case "firefox": {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();

                if (isHeadless) {
                    firefoxOptions.addArguments("-headless");
                    firefoxOptions.addArguments("--width=1920");
                    firefoxOptions.addArguments("--height=1080");
                }

                return new FirefoxDriver(firefoxOptions);
            }

            case "edge": {
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();

                if (isHeadless) {
                    edgeOptions.addArguments("--headless=new");
                    edgeOptions.addArguments("--window-size=1920,1080");
                }

                return new EdgeDriver(edgeOptions);
            }

            default:
                throw new RuntimeException("Browser not Found!!! Using given name: " + browserName);
        }
    }

    // ---------------------------------------------------------------
    // TestNG Lifecycle Hooks
    // ---------------------------------------------------------------
    @BeforeMethod(alwaysRun = true)
    public synchronized void setBrowser(Method method) throws Exception {
        WebDriver driver = getBrowser(browserName);

        if (!mode.equalsIgnoreCase("headless")) {
            driver.manage().window().maximize();
        } else {
            driver.manage().window().setSize(new Dimension(1920, 1080));
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        setDriver(driver);

        // Starts only in headed mode; silently skipped when headless
        VideoRecorder.startRecording(method.getName());
    }

    @AfterMethod(alwaysRun = true)
    public synchronized void closeBrowser(ITestResult result) {
        boolean failed = result.getStatus() == ITestResult.FAILURE;

        try {
            if (failed) {
                BasePage basePage = new BasePage();
                basePage.takeScreenShotAllureAttach(result.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Keep and attach the video only for failed tests
            VideoRecorder.stopRecording(failed);

            WebDriver driver = LOCAL_DRIVER.get();
            if (driver != null) {
                driver.quit();
                LOCAL_DRIVER.remove(); // prevent leaks in parallel execution
            }
        }
    }
}