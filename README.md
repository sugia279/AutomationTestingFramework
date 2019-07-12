# Automation Testing Framework using JSON Data Driven
A framework helps the automation engineer build the complete test automation easily by integrating the various automation libraries, tools and supporting the way to execute Test Suite/ Test case using JSON for Data Driven.
## The automation libraries, tools are used in this framework.
- [TestNG](https://testng.org/doc/index.html)                - A Testing framework
- [Selenium WebDriver](https://www.seleniumhq.org/)    - A library you can use to test Web Application.
- [Rest Assured](http://rest-assured.io/)          - A library you can use to test HTTP-based REST services.
- [Extent Report 4.0](http://extentreports.com/)     - A framework for creating a test report

**It can:**
- Support WebUI Testing, APIs Testing.
- Integrating Extent Report 4.0 to generate a well-formed test report as HTML.
- Apply Page Object pattern.
- Write tests with multi data sets (data-driven) from JSON file.
- Run tests in parallel — TestNG feature.
- Run tests remotely (Selenium Grid or a cloud testing provider).
- It can be executed from Jenkins or any other CI tool.

## Execution Diagram
- Below diagram illustrates how the project works when running an execution file:
![Execution Diagram](https://github.com/sugia279/AutomationTestingFramework/blob/master/TestExecutionDiagram.png)

- The TestNG runs the **Test Suite** file which is formed by [xml file](https://github.com/sugia279/AutomationTestingFramework/blob/master/src/test/executionfile/saucedemo_webuitesting/smoketest.xml).

## Using JSON for Data Driven
A test suite is defined in a JSON file that contains a collection of test cases, and a collection of test step with specified parameters in repectively.
1. Testsuite/ testcase format.  
```
{
  "suiteName": "Verify [Add To Cart] feature",
  "suiteDescription": "",
  "testCases": [
    {
      "testId": "IN_AC1",
      "testName": "Verify shopping cart counter (select 1 item)",
      "testDescription": "",
      "testObjectives": "Verify 1 item is added to cart",
      "note": "",
      "testSteps": [
        {
          "name": "Login with standard_user",
          "class": "LoginAction",
          "method": "login",
          "parameters": {
            "user": "standard_user",
            "password": "secret_sauce"
          }
        },
        {
          "name": "Add item [Sauce Labs Backpack] to cart",
          "class": "InventoryAction",
          "method": "addToCartTest",
          "parameters": {
            "Item Names": ["Sauce Labs Backpack"],
            "Counter": 1
          }
        }
      ]
    }
   ]
}
```
2. How to use?
- Specify the JSON file path in Data Provider function. And then, in test method, we can use **curTestCase** var to get param value from the specified test step (cast the value to the specified type).
> DataProvider method
```
    @DataProvider
    protected Object[] testDataSet(){
        return fetchDataToDataSet("functional/inventory/AddToCartTest.json");
    }
```

Refer to [this for example](https://github.com/sugia279/AutomationTestingFramework/blob/master/src/test/java/saucedemo_webuitesting/suites/functional/inventory/AddToCartTest.json)

## Apply Page Object Pattern
Based on the series of Test Automation Design Pattern from site "https://www.automatetheplanet.com/advanced-page-object-pattern/", this framework supports 2 base classes are BaseUI<M>, BaseUIMap. So a UI Control as page/modal dialog or any UI control should be a set of 3 classes that extent to above 3 classes.
 - A class extents BaseUI<M>: where defining the highlevel action methods in page/modal dialog/ control.
 - A class extents BaseUIMap: where finding the elements in page, control or modal dialog for mapping, and this class is used by Map() functions in BaseUI class and BaseUIValidator class.

> LoginPage
```
public class LoginPage extends BaseWebUI<LoginPageMap,LoginPageValidator> {
    public LoginPage(WebAction action) {
        super(new LoginPageMap(), new LoginPageValidator(), action);
    }
    public LoginPage login(String user, String password) {
        webAction.type(Map().getTxtUser(), "User Name", user);
        webAction.type(Map().getTxtPassword(), "Password", password);
        webAction.click(Map().getBtnLogIn(), "Login");
        return this;
    }
}
```
> LoginPageMap
```
public class LoginPageMap extends BaseWebUIMap {
    @FindBy(id = "user-name")
    private WebElement txtUser;
    @FindBy(id = "password")
    private WebElement txtPassword;
    @FindBy(css = ".btn_action")
    private WebElement btnLogIn;
    public WebElement getTxtUser()    {
        return txtUser;
    }

    public WebElement getTxtPassword()    {
        return txtPassword;
    }

    public WebElement getBtnLogIn()    {
        return btnLogIn;
    }
}
```

## Implement test methods for each test suite/ test cases
1. BaseTest class is supported for something such as:
  - Contain RunTest method which is used to run all testcases are specified by testDataSet DataProvider method.
  - Integrating to the Extent Report: 
    - Initialize Test Report in @BeforeSuite method.
    - Create and add test suites/ test cases information in @BeforeMethod method.
    - Log test status for each test step, test case (Pass, Fail, Skip).  
  - Support option of starting browser at @BeforeMethod, and options of stop browser at @AfterMethod or @AfterClass
  - Loading the specified config file in @BeforeSuite.
  - Refer to [this file](https://github.com/sugia279/AutomationTestingFramework/blob/master/src/test/java/core/test_execution/BaseTest.java) to get more.
  
2. WebAction class is wrapper of Selenium Webdriver.
  - Can access to Selenium Web Driver to start/ stop browser.
  - Support some web actions in visually.
  - This object should be passed through all UI control (see ## Apply Page Object Pattern section ).
3. APIAction class is wrapper of Rest Assured library.
  - Support the methods for API Testing via Rest Assured.

## Using Test Report
  - The Extent Test Report is built by singleton class, so we can call TestReportManager.getIntance() to set Test Steps information in Test Method easily.
Eg:  
> TestReportManager.getInstance().setStepInfo("Login to SauceDemo Page with user name ='" + user + "', password ='" + password + "'");
![Test Report](https://github.com/sugia279/AutomationTestingFramework/blob/master/TestReportReadme.PNG)

## Some words.
  - Thanks for all of the support you guys (my colleagues) given me to complete this framework regardless of how big or small. Especially you [Nguyen Trong Tuyen](https://github.com/trongtuyen96) who have contributed many good ideas for this framework.
  - Besides, thanks Anton Angelov for your sharing knowlege about the professional automation testing framework via https://www.automatetheplanet.com/
- Finally, all contributions are welcome.

