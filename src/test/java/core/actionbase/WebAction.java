package core.actionbase;

import core.extentreport.TestReportManager;
import core.extentreport.ReportLogLevel;
import core.utilities.CustomCondition;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.DriverManagerType;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import io.github.bonigarcia.wdm.InternetExplorerDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class WebAction {
    protected final String ACTION_LOG_LVL = ReportLogLevel.LOG_LVL_4;
    protected final String HIGHER_ACTION_LOG_LVL = ReportLogLevel.LOG_LVL_3;

    private WebDriver browser;
    private WebDriverWait browserWait;
    private BrowserType browserType;
    private JavascriptExecutor jsExecutor;
    private int timeoutDefault = 20;

    public WebAction() {
        browserType = BrowserType.CHROME;
    }

    //region set/get method
    public WebDriver getBrowser() {
        return browser;
    }

    public WebDriverWait getBrowserWait() {
        return browserWait;
    }

    public BrowserType getBrowserType() {
        return browserType;
    }

    public void setBrowserType(BrowserType browserType) {
        this.browserType = browserType;
    }

    public int getTimeoutDefault() {
        return timeoutDefault;
    }

    public void setTimeoutDefault(int timeoutDefault) {
        this.timeoutDefault = timeoutDefault;
    }
    //endregion

    //region Actions
    public void startBrowser()
    {
        switch (getBrowserType()) {
            case CHROME:
                ChromeDriverManager.getInstance(DriverManagerType.CHROME).setup();
                browser = new ChromeDriver();
                break;
            case FIREFOX:
                FirefoxDriverManager.getInstance(DriverManagerType.FIREFOX).setup();
                browser = new FirefoxDriver();
                break;
            default:
                InternetExplorerDriverManager.getInstance(DriverManagerType.IEXPLORER).setup();;
                browser = new InternetExplorerDriver();
                break;
        }
        browserWait = new WebDriverWait(browser, getTimeoutDefault());
        jsExecutor = (JavascriptExecutor) browser;
    }

    public void stopBrowser()
    {
        if(browser != null && !browser.toString().contains("(null)"))
        {
            browser.quit();
            browser = null;
            browserWait =null;
        }
    }

    public void getToUrl(String url, boolean isMaximize)
    {
        getBrowser().get(url);
        if(isMaximize == true){
            getBrowser().manage().window().maximize();
        }
        TestReportManager.getInstance().getTestReport().testLog(String.format("%sOpen and navigate to '[%s]'", ACTION_LOG_LVL, url));
    }

    public WebElement findElement(By by) {
        return findElement(by, getTimeoutDefault());
    }

    public WebElement findElement(By by, int timeout) {
        waitUntil(ExpectedConditions.presenceOfElementLocated(by), timeout);
        return getBrowser().findElement(by);
    }

    public List<WebElement> findElements(By by) {
        return findElements(by, getTimeoutDefault());
    }

    public List<WebElement> findElements(By by, int timeout) {
        waitUntil(ExpectedConditions.presenceOfElementLocated(by), timeout);
        return getBrowser().findElements(by);
    }

    public void type(WebElement ele, String eleName, String txt) {
        waitForElement(ele);
        ele.sendKeys(txt);
        waitUntil(CustomCondition.textToBePresentInElementValue(ele, txt));
        TestReportManager.getInstance().getTestReport().testLog(String.format("%sType [%s] into [%s]", ACTION_LOG_LVL, txt, eleName));
    }

    public void appendText(WebElement ele, String txt) {
        waitForElement(ele);
        ele.sendKeys(txt);
        TestReportManager.getInstance().getTestReport().testLog(ACTION_LOG_LVL + "Append '" + txt + "' into [" + ele.getText() + "]");
    }

    public void setValue(WebElement ele, String txt) {
        waitForElement(ele);
        getJsExecutor().executeScript("arguments[0].value = arguments[1];", ele, txt);
        TestReportManager.getInstance().getTestReport().testLog(ACTION_LOG_LVL + "Type '" + txt + "' into [" + ele.getText() + "]");
    }

    public void click(WebElement ele) {
        click(ele, null, true);
    }

    public void click(WebElement ele, String elementName) {
        click(ele, elementName, true);
    }

    public void click(WebElement ele, String elementName, boolean waitForEnabled) {
        if(waitForEnabled) {
            waitForElement(ele);
        }
        TestReportManager.getInstance().getTestReport().testLog(ACTION_LOG_LVL + "Click [" + elementName + "]");
        ele.click();
    }
//
//    public String getElementLabel(WebElement ele){
//        String txt = ele.getText();
//        if(txt.isEmpty() && isAttributePresent(ele, "aria-label")) {
//            return ele.getAttribute("aria-label");
//        }else if(txt.isEmpty() && isAttributePresent(ele, "ng-click")){
//            return ele.getAttribute("ng-click");
//        }else if(txt.isEmpty() && isAttributePresent(ele, "ng-model")){
//            return ele.getAttribute("ng-model");
//        }
//        return txt;
//    }


    public boolean isElementPresent(By locatorKey) {
        try {
            browser.findElement(locatorKey);
            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public boolean isElementVisible(String cssLocator){
        return browser.findElement(By.cssSelector(cssLocator)).isDisplayed();
    }

    private boolean isAttributePresent(WebElement element, String attribute) {
        Boolean result = false;
        try {
            String value = element.getAttribute(attribute);
            if (value != null){
                result = true;
            }
        } catch (Exception e) {}

        return result;
    }

    public void jsClick(WebElement ele) {
        //waitForElement(ele);
        getJsExecutor().executeScript("arguments[0].click();", ele);
        TestReportManager.getInstance().getTestReport().testLog(ACTION_LOG_LVL + "Click [" + ele.getText() + "]");
    }

    public void scrollIntoView(WebElement ele) {
        getJsExecutor().executeScript("arguments[0].scrollIntoView();", ele);
    }

    public void waitUntil(ExpectedCondition condition) {
        getBrowserWait().until(condition);
    }

    public void waitUntil(ExpectedCondition condition, int timeOutInSec) {
        new WebDriverWait(getBrowser(), timeOutInSec).until(condition);
    }

    public void waitForElement(WebElement ele) {
        waitUntil(CustomCondition.elementPresent(ele));
        scrollIntoView(ele);
        waitUntil(ExpectedConditions.visibilityOf(ele));
    }

    public void waitForPageLoadingComplete(){
        browserWait.until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
    }

    public JavascriptExecutor getJsExecutor() {
        return jsExecutor;
    }
}
