# Swag Labs Web Automation

UI test automation for [Swag Labs (saucedemo.com)](https://www.saucedemo.com) using **Java, Selenium, TestNG and Maven**, built with the Page Object Model. Reports use Allure, and failed tests get a screenshot and a video.

## Tech Stack

| Area | Tool |
|---|---|
| Language / build | Java 21, Maven |
| Browser automation | Selenium 4 |
| Test runner | TestNG 7.9 (parallel by method, data providers) |
| Reporting | Allure 2.29 (AspectJ weaver) |
| Logging | SLF4J + Logback (`src/test/resources/logback.xml`) |
| Config | dotenv-java (`.env`) |
| Failure evidence | Allure screenshot + screen video (Monte recorder) |

## What Is Covered

| Area | Test class | Examples |
|---|---|---|
| Login | `LoginPageTeseCases` | page elements, valid login, empty fields, wrong / mixed-case / injection input (data provider), locked-out user |
| Home (Products) | `HomePageTestCases` | product names and prices, default sort, all 4 sort options, add / remove to cart, cart badge, product details, footer links |
| Cart and Checkout | `CartPageTestCases` | cart items, remove, continue shopping, information validation, overview totals and tax, full order |
| Menu | `MenuPageTestCases` | planned (All Items, About, Logout, Reset App State) |

Tests are tagged with groups:

- `smoke`: critical paths, quick run
- `regression`: everything, including smoke

## Project Structure

```
.
├── src/test/java
│   ├── pages/            Page objects (locators + actions)
│   │   ├── BasePage          shared waits, clicks, assertions
│   │   ├── CommonLocators    elements shared by many pages (top bar, logo)
│   │   ├── LoginPage, HomePage, CartPage, MenuPage
│   ├── testcases/        TestNG test classes (one per page)
│   └── utilities/
│       ├── BaseDriver        browser start/stop, screenshot + video on failure
│       ├── ConfigReader      reads settings (see Configuration)
│       ├── TestListener      logs test start, pass, fail, skip
│       ├── VideoRecorder     screen recording (headed mode only)
│       └── URLs              page URLs
├── src/test/resources/logback.xml
├── SmokeTestSuite.xml        TestNG suite: group "smoke"
├── RegressionTestSuite.xml   TestNG suite: group "regression"
├── .env.example              copy to .env
└── pom.xml
```

## Setup

1. Install **JDK 21**, **Maven 3.6+** and **Google Chrome** (or Firefox / Edge).
2. Clone the project and open it in IntelliJ IDEA.
3. Create your local config:
   ```bash
   cp .env.example .env        # Windows: copy .env.example .env
   ```
4. Install dependencies:
   ```bash
   mvn clean install -DskipTests
   ```

## Configuration

Settings are read in this order: `-D` command-line option, then OS environment variable, then `.env` file, then default.
The names are **case-sensitive**: use `-DBROWSER`, not `-Dbrowser`.

| Key | Values | Default | Notes |
|---|---|---|---|
| `BASE_URL` | URL | none | `https://www.saucedemo.com` |
| `BROWSER` | `chrome`, `chromium`, `firefox`, `edge` | `chromium` | Use `chrome` on Windows and macOS |
| `MODE` | `headless`, `headed` | `headless` | Video recording works only in `headed` |
| `CHROMIUM_PATH` | file path | `/usr/bin/chromium-browser` | Only for `BROWSER=chromium` on Linux |
| `LOGIN_USER`, `LOGIN_PASSWORD` | text | none | Standard user |
| `LOCKED_OUT_USER`, `PROBLEM_USER`, `PERFORMANCE_GLITCH_USER`, `ERROR_USER`, `VISUAL_USER` | text | none | Other demo users (same password) |

The demo users and password are listed on the Swag Labs login page. `.env` is git-ignored, so never commit it; commit only `.env.example`.

## Running Tests

```bash
# Regression suite (default)
mvn clean test

# Smoke suite
mvn clean test -DsuiteFile=SmokeTestSuite.xml

# Choose browser and mode
mvn clean test -DBROWSER=chrome -DMODE=headed

# One class or one method
mvn clean test -Dtest=CartPageTestCases
mvn clean test -Dtest=HomePageTestCases#sortingOptionsTest
```

In IntelliJ you can also right-click a test class, method or suite XML and choose Run.

## Reports

Results go to `allure-results/` (git-ignored). Delete it before a run so old results don't mix into the report.

```bash
# Windows: rmdir /s /q allure-results     Linux/macOS: rm -rf allure-results
mvn clean test
allure serve allure-results          # build and open the report
```

On failure, the report contains a screenshot, and in headed mode the screen video is saved to `target/videos`. Logs print to the console.

## Adding a New Test

1. Add locators and actions to the page object in `pages/`. Prefer `data-test` attributes or ids, and avoid absolute XPaths.
2. Add the test to a class in `testcases/`. Every class extends `BaseDriver`, which starts a fresh browser for each test and closes it afterwards.
3. Tag it with `groups = {"regression"}`, and add `"smoke"` for critical paths.
4. Use TestNG `Assert` for checks. Use `@DataProvider` when the same steps run with different data.
5. Nothing else to edit: the suites run the whole `testcases` package by group.

## Conventions

- One page object per page; tests contain no locators.
- Tests are independent and never rely on run order.
- Waits are explicit (`WebDriverWait`); avoid `Thread.sleep`.
- Credentials and URLs come from `.env`, never hardcoded in tests.

## Troubleshooting

| Problem | Fix |
|---|---|
| Browser does not start on Windows | Set `BROWSER=chrome` (the default is `chromium`) |
| `-DBROWSER` or `-DMODE` seems ignored | Check the case: the keys are uppercase |
| `Unable to find CDP implementation` warning | Harmless; Selenium DevTools is not used by these tests |
| Old results in the Allure report | Delete `allure-results/` before running |
| Everything runs headless and no video appears | Use `-DMODE=headed` |