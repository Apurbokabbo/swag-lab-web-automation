
# Swag Lab Web Automation

## Overview
This is an automated testing framework for the Swag Lab web application, built using Selenium with Java. The framework implements the Page Object Model (POM) design pattern and uses TestNG for test execution.

## Technology Stack
- Java 21
- Maven 3.5.2
- TestNG 7.9.0
- Selenium Java 4.32.0
- Selenium DevTool v137 4.33.0
- Web Driver 6.1.0
- LogBack Classic 1.5.18
- Maven Surefire Plugin 3.5.2
- Allure Reports 2.29.0
- AspectJ 1.9.24

## Prerequisites
- Java Development Kit (JDK) 21
- Maven


## Project Structure
```
swag_lab_web_automation/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── org/example/     # Main source directory (currently empty or placeholder)
│
│   ├── test/
│   │   └── java/
│   │       ├── pages/           # Page Object classes
│   │       │   ├── BasePage.java
│   │       │   ├── BlockUserListPage.java
│   │       │   ├── CustomerReportPage.java
│   │       │   ├── DashboardPage.java
│   │       │   ├── DataBundleLogPage.java
│   │       │   ├── ForgotPasswordPage.java
│   │       │   ├── LoginPage.java
│   │       │   ├── SearchUserPage.java
│   │       │   └── StoreManagementPage.java
│   │       │
│   │       ├── testcases/       # Test case classes
│   │       │   ├── BlockUserListTestCase.java
│   │       │   ├── CustomerReportTestCase.java
│   │       │   ├── DashboardPageTestCases.java
│   │       │   ├── DataBundleLogTestCase.java
│   │       │   ├── ForgotPasswordPageTestCase.java
│   │       │   ├── LoginPageTestCase.java
│   │       │   ├── SearchUserTestCases.java
│   │       │   └── StoreManagementTestCase.java
│   │       │
│   │       └── utilities/       # Helper classes
│   │           ├── BaseDriver.java
│   │           ├── TestListener.java
│   │           └── URLs.java
│   │
│   └── resources/               # Resource/config files
│       ├── dashboardChangePassword.txt
│       └── logback.xml
│
├── RegressionTestSuite.xml      # TestNG regression suite
├── SmokeTestSuite.xml           # TestNG smoke suite
├── pom.xml                      # Maven configuration
└── .gitignore                   # Git ignore rules


```

## Test Categories

### Smoke Tests
- Critical path testing
- Basic functionality verification
- Essential user journeys

### Regression Tests
- Comprehensive testing
- Feature interaction testing
- Edge cases and validation

## Features Tested

1. **Login**
    - Valid/Invalid credentials
    - OTP verification
    - Password validation
    - Error messages

2. **Forgot Password**
    - Email validation
    - Password reset functionality
    - Success and error messages
    - Password strength validation

3. **Dashboard**
    - Notification with navigation 
    - Password change functionality
    - Signout functionality
    - Dashboard text assertions

4. **Customer Report**
    - Customer filter functionality
    - Customer ticket generation
    - Transaction id navigation
    - Pagination functionality

5. **Search User**
    - Search by email
    - Search by phone number
    - Search by document number
    - Search by kyc name 
    - Search by account no
    - Searched data navigation
    - Proper data validation

6. **Block User List**
    - Block user list pagination
    - Checkbox functionality
    - Filter functionality
    - Pagination functionality
    - Unblock user functionality

7. **Data Bundle Log**
    - Data bundle log pagination
    - Filter functionality
    - Bunle wise status & message validation
   
8. **Store Management**
    - Store management pagination
    - Store filter functionality
    - Read & update operations functionality
    - Navigation to store details
    - Status change functionality




## Setup Instructions

### 1. Environment Setup
- Install JDK 21
- Install Maven 3.5.0+
### 2. Project Setup
```
```bash
# Clone repository
git clone <repository-url>
cd fastpay-support-panel-automation

# Install dependencies
mvn clean install
```

## Running Tests

### All Tests

```bash
mvn clean test
```

### Smoke Tests Only

```bash
Linux : mvn clean test Dsurefire.suiteXmlFiles=SmokeTestSuite.xml -Dgroups=smoke
Windows (Headless) : mvn clean test "-Dsurefire.suiteXmlFiles=SmokeTestSuite.xml" "-Dgroups=smoke" -Dbrowser=chrome -Dmode=headless
Windows (Headed) : mvn clean test "-Dsurefire.suiteXmlFiles=SmokeTestSuite.xml" "-Dgroups=smoke" -Dbrowser=chrome -Dmode=headed

```

### Regression Tests Only

```bash
Linux : mvn clean test Dsurefire.suiteXmlFiles=RegressionTestSuite.xml -Dgroups=regression
Windows (Headless) : mvn clean test "-Dsurefire.suiteXmlFiles=RegressionTestSuite.xml" "-Dgroups=regression" -Dbrowser=chrome -Dmode=headless
Windows (Headed) : mvn clean test "-Dsurefire.suiteXmlFiles=RegressionTestSuite.xml" "-Dgroups=regression" -Dbrowser=chrome -Dmode=headed

```

## Test Reports

### Allure Reports

```bash
# Generate report
allure generate --clean -o allure-report

# Open report
allure open allure-report
```

#### Sample Report View

![Allure Report Sample](assets/testSummary.png)
![Allure Report Sample](assets/featureWIseSummary.png)
![Allure Report Sample](assets/graphicalAnalysis.png)

## Framework Features
- Page Object Model implementation
- Fluent wait mechanisms
- Screenshot capture on failure
- Reusable components
- Custom utilities
- Parallel execution support
- Group-based test execution
- Allure reporting integration

## Best Practices
- Modular test structure
- Proper exception handling
- Clear naming conventions
- Consistent coding standards
- Regular cleanup mechanisms
- Efficient element locators
- Proper timeout management
- Detailed test documentation

## Troubleshooting

1. **Changes in any locators may result as test failure**
2. **Automation may fail due slow network.**


## Contributing

```bash
1. Fork the repository
2. Create feature branch
3. Commit changes
4. Push to branch
5. Create pull request
```

## Version History
- `v0.0.1-SNAPSHOT`: Initial framework setup

## License
© 2025 **A M Kabbo Hossain** - [SQA Engineer]. All rights reserved.
