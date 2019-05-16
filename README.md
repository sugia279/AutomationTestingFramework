# Automation Testing Framework using JSON Data driven
A framework helps the automation engineer build the complete test automation easily by integrating the various automation libraries, tools and supporting the way to execute Test Suite/ Test case using JSON for Data Driven.
## The automation libraries, tools are using in this framework.
- [TestNG](https://testng.org/doc/index.html)                - A Testing framework
- [Selenium WebDriver](https://www.seleniumhq.org/)    - A Web Application Testing framework.
- [Rest Assured](http://rest-assured.io/)          - A library you can use to test HTTP-based REST services.
- [Extent Report 4.0](http://extentreports.com/)     - A framework for creating a test report

## Execution Diagram
- Below diagram illustrates how the project works when running an execution file:
![Execution Diagram](https://github.com/sugia279/AutomationTestingFramework/blob/master/ExecutionDiagram.png)

- The TestNG runs the execution file which is formed by [xml file](https://github.com/sugia279/AutomationTestingFramework/tree/master/src/test/executionfile/saucedemo_webuitesting).

## Using JSON for Data Driven
A test suite is defined in a JSON file that contains a collection of test cases, and a collection of test step with specified parameters in repectively.
1. Testsuite/ testcase Format.  
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
          "name": "Logon",
          "parameters": {
            "user": "standard_user",
            "password": "secret_sauce"
          }
        },
        {
          "name": "Add item to cart",
          "parameters": {
            "Item Names": ["Sauce Labs Backpack"],
            "Counter": 1
          }
        }
      ]
    }
  }
```
2. How to use?
- Specify the JSON file path in Data Provider function. And then, in test method, we can use **curTestCase** var to get param value from the specified test step (cast the value to the specified type).
```
  public Object getParamValueFromTestStep(int stepOrder, String paramName){
       return get_testSteps().get(stepOrder).getTestParams().get(paramName);
  }

  public Object[] getParamArrayValueFromTestStep(int stepOrder, String paramName){
      return ((JSONArray) get_testSteps().get(stepOrder).getTestParams().get(paramName)).toArray();
  }
```
Refer to [this for example](https://github.com/sugia279/AutomationTestingFramework/blob/master/src/test/java/saucedemo_webuitesting/suites/ui/functional/logon/LogOnTest.java)

## Apply Page Object Pattern
...
## Implement test methods for each test suite/ test cases?
...
## Using Test Report
...
## References
