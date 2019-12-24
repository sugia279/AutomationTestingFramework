package core.base_action;

import core.extent_report.ReportLogLevel;
import core.extent_report.TestReportManager;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

public class WebAction {
    protected final String ACTION_LOG_LVL = ReportLogLevel.LOG_LVL_4;
    protected final String HIGHER_ACTION_LOG_LVL = ReportLogLevel.LOG_LVL_3;
    private WebDriver browser;
    private WebDriverWait browserWait;
    private List<WebDriver> listBrowsers;
    private BrowserType browserType;
    private JavascriptExecutor executor;
    private int timeoutDefault;
    private int timeDelay = 0;
    private List<String> attributeAsElementName;

    public WebAction() {
        listBrowsers = new ArrayList<>();
        browserType = BrowserType.CHROME;
        timeoutDefault = 20;
    }

    //region set/get method
    public WebDriver getBrowser() {
        return browser;
    }

    public void setBrowser(WebDriver browser){
        this.browser = browser;
        this.browserWait = new WebDriverWait(browser, getTimeoutDefault());
        this.executor = (JavascriptExecutor) browser;
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

    public int getTimeDelay() {
        return timeDelay;
    }

    public void setTimeDelay(int timeDelay) {
        this.timeDelay = timeDelay;
    }

    public void setAttributeAsElementName(String... attributeAsElementName) {
        this.attributeAsElementName = Arrays.asList(attributeAsElementName);
    }

    public String getElementName(WebElement ele) {
        String name = ele.getText();
        if(attributeAsElementName != null) {
            for (String att : attributeAsElementName) {
                if (name.isEmpty() && isAttributePresent(ele, att)) {
                    name = ele.getAttribute(att);
                    break;
                }
            }
        }

        return name;
    }

    public String getText(WebElement ele){
        waitForElement(ele);
        scrollIntoView(ele);
        return ele.getText();
    }

    public String getAttribute(WebElement ele, String attributeName){
        waitForElement(ele);
        return ele.getAttribute(attributeName);
    }
    //endregion

    //region browser action
    public void startBrowser() {
        WebDriver newBrowser;
        switch (getBrowserType()) {
            case CHROME:
                ChromeDriverManager.getInstance(DriverManagerType.CHROME).setup();
                newBrowser = new ChromeDriver();
                break;
            case FIREFOX:
                FirefoxDriverManager.getInstance(DriverManagerType.FIREFOX).setup();
                newBrowser = new FirefoxDriver();
                break;
            case IE:
                InternetExplorerDriverManager.getInstance(DriverManagerType.IEXPLORER).arch32().setup();
                newBrowser = new InternetExplorerDriver();
                break;
            default:
                ChromeDriverManager.getInstance(DriverManagerType.CHROME).setup();
                newBrowser = new ChromeDriver();
                break;
        }
        //browser.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        getListBrowsers().add(newBrowser);
        setBrowser(newBrowser);
    }

    public void stopBrowser() {
        if (browser != null && !browser.toString().contains("(null)")) {
            browser.quit();
            browser = null;
            browserWait = null;
        }
    }

    public void stopAllBrowsers(){
        for(WebDriver brows : getListBrowsers()){
            if (brows != null && !brows.toString().contains("(null)")) {
                brows.quit();
                brows = null;
                browserWait = null;
            }
        }
    }

    public void getToUrl(String url, boolean isMaximize) {
        getBrowser().get(url);
        if (isMaximize == true) {
            getBrowser().manage().window().maximize();
        }
        TestReportManager.getInstance().getTestReport().testLog(String.format("%sOpen and navigate to '[%s]'", ACTION_LOG_LVL, url));
    }

    public void openLinkInNewTab(String url) {
        executor.executeScript("window.open('');");
        ArrayList<String> tabs = new ArrayList<>(getBrowser().getWindowHandles());
        getBrowser().switchTo().window(tabs.get(tabs.size() - 1));
        //Launch URL in the new tab
        getBrowser().get(url);
    }

    public void openLinkInNewWindow(String url) {
        startBrowser();
        browser.get(url);
    }

    public void switchBrowser(int index){
        setBrowser(getListBrowsers().get(index));
    }

    public List<WebDriver> getListBrowsers() {
        return listBrowsers;
    }
    //endregion

    //region find and wait element action
    public WebElement findElement(By by) {
        return findElement(by, getTimeoutDefault());
    }

    public WebElement findElement(WebElement parent, By by) {
        waitForElement(parent.findElement(by));
        return parent.findElement(by);
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

    public List<WebElement> findElements(WebElement parent, By by) {
        return findElements(parent, by, timeoutDefault);
    }


    public List<WebElement> findElements(WebElement parent, By by, int timeout) {
        waitForElement(parent, timeout);
        return parent.findElements(by);
    }

    public Object waitUntil(ExpectedCondition condition) {
        return getBrowserWait().until(condition);
    }

    public Object waitUntil(ExpectedCondition condition, int timeOutInSec) {
        return new WebDriverWait(getBrowser(), timeOutInSec).until(condition);
    }

    public void waitUntilWithoutThrowException(ExpectedCondition condition) {
        try {
            getBrowserWait().until(condition);
        }catch(Exception ex){}
    }

    public boolean waitForElement(WebElement ele) {
        return waitForElement(ele, timeoutDefault);
    }

    public boolean waitForElement(WebElement ele, int timeout) {
        boolean res = false;
        if(ele!=null && !ele.isDisplayed()){
            scrollIntoView(ele);
        }
        res = waitUntil(ExpectedConditions.visibilityOf(ele),timeout) != null;
        return res;
    }

    public void waitForElementVisible(By locator) {
        try {
            waitUntil(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException timeout) {
            TestReportManager.getInstance().getTestReport().testLog(ACTION_LOG_LVL + "Element does not visible. Locator: [" + locator + "]");
        }
    }

    public boolean waitForTextPresentOnElement(WebElement ele, String text) {
        return waitForTextPresentOnElement(ele, text, timeoutDefault);
    }

    public boolean waitForTextPresentOnElement(WebElement ele, String text, int timeout) {
        WebDriverWait wait = new WebDriverWait(getBrowser(), timeout);
        ExpectedCondition<Boolean> textToBePresentInElementValue = arg0 -> {
            try {
                String actText = ele.getText();
                return actText.contains(text);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        };
        return wait.until(textToBePresentInElementValue);
    }

    public void waitForElementPresent(By locator) {
        ExpectedCondition<Boolean> elementPresent = arg0 -> {
            try {
                return getBrowser().findElement(locator) != null;
            } catch (Exception e) {
                return false;
            }
        };
        waitUntil(elementPresent);
    }

    public void waitForElementNotPresent(By locator) {
        ExpectedCondition<Boolean> elementNotPresent = arg0 -> {
            try {
                return getBrowser().findElement(locator) == null;
            } catch (Exception e) {
                return true;
            }
        };
        waitUntil(elementNotPresent);
    }

    public void waitForExpectedCondition(Callable<Boolean> function){
        waitForExpectedCondition(function, getTimeoutDefault());
    }

    public void waitForExpectedCondition(Callable<Boolean> function, int timeOut){
        ExpectedCondition<Boolean> funcCondition = arg0 -> {
            try {
                return function.call();
            } catch (Exception e) {
                return false;
            }
        };
        waitUntil(funcCondition, timeOut);
    }

    public void waitForPageLoadComplete(){
        waitForExpectedCondition(() -> executor.executeScript("return document.readyState").equals("complete"));
    }
    //endregion

    //region click action
    public void click(WebElement ele) {
        click(ele, null);
    }

    public void retryClick(WebElement ele, String elementName) {
        retryClick(ele, null, timeoutDefault, elementName);
    }

    public void retryClick(WebElement ele, int timeout, String elementName) {
        retryClick(ele, null, timeout, elementName);
    }

    public void retryClick(WebElement ele, ExpectedCondition expectedCondition, String elementName) {
        retryClick(ele, expectedCondition, timeoutDefault, elementName);
    }

    public void retryClick(WebElement ele, ExpectedCondition expectedCondition, int timeout, String eleName) {
        delayTime();
        TestReportManager.getInstance().getTestReport().testLog(ACTION_LOG_LVL + "Click [" + eleName + "]");
        ExpectedCondition<Boolean> elementIsClickable = arg0 -> {
            try {
                scrollIntoView(ele);
                ele.click();
                if (expectedCondition != null) {
                    waitUntil(expectedCondition, 2);
                }
                return true;
            } catch (Exception e) {
                return false;
            }
        };
        waitUntil(elementIsClickable, timeout);
    }

    public void jsClick(WebElement ele) {
        delayTime();
        waitForElement(ele);
        executor.executeScript("arguments[0].click();", ele);
        TestReportManager.getInstance().getTestReport().testLog(ACTION_LOG_LVL + "Click [" + ele.getText() + "]");
    }

    public void click(WebElement ele, String elementName) {
        delayTime();
        waitForElement(ele);
        if (elementName == null)
            elementName = getElementName(ele);

        TestReportManager.getInstance().getTestReport().testLog(ACTION_LOG_LVL + "Click [" + elementName + "]");
        ele.click();
    }
    //endregion

    //region type action
    public void jsTypeByCss(String locatorString, String value, String elementName, WebElement foundElement){
        delayTime();
        waitForElement(foundElement);
        executor.executeScript("$(\"" + locatorString + "\")[0].value = \"" + value + "\"");
        TestReportManager.getInstance().getTestReport().testLog(String.format("%sType [%s] into [%s]", ACTION_LOG_LVL, value, elementName));
    }

    public void jsTypeByXPath(String locatorString, String value, String elementName, WebElement foundElement){
        delayTime();
        waitForElement(foundElement);
        executor.executeScript("$x(\"" + locatorString + "\")[0].value = \"" + value + "\"");
        TestReportManager.getInstance().getTestReport().testLog(String.format("%sType [%s] into [%s]", ACTION_LOG_LVL, value, elementName));
    }

    public void type(WebElement ele, String txt) {
        type(ele, txt, null, true);
    }

    public void type(WebElement ele, String txt, String elementName) {
        type(ele, txt, elementName, true);
    }

    public void type(WebElement ele, String txt, boolean reTypeToMatchText) {
        type(ele, txt, null, reTypeToMatchText);
    }

    public void type(WebElement ele, String txt, String elementName, boolean reTypeToMatchText) {
        delayTime();
        waitForElement(ele);
        if (reTypeToMatchText) {
            ExpectedCondition<Boolean> textToBePresentInElementValue = arg0 -> {
                try {
                    ele.clear();
                    ele.sendKeys(txt);
                    return ele.getAttribute("value").equals(txt);
                } catch (Exception e) {
                    return false;
                }
            };
            waitUntil(textToBePresentInElementValue,timeoutDefault);
        } else {
            ele.clear();
            ele.sendKeys(txt);
        }

        if (elementName == null) {
            elementName = getElementName(ele);
        }
        TestReportManager.getInstance().getTestReport().testLog(String.format("%sType [%s] into [%s]", ACTION_LOG_LVL, txt, elementName));
    }

    public void appendText(WebElement ele, String txt) {
        delayTime();
        waitForElement(ele);
        ele.sendKeys(txt);
        TestReportManager.getInstance().getTestReport().testLog(ACTION_LOG_LVL + "Append '" + txt + "' into [" + ele.getText() + "]");
    }

    public void setValue(WebElement ele, String txt) {
        delayTime();
        waitForElement(ele);
        executor.executeScript("arguments[0].value = arguments[1];", ele, txt);
        TestReportManager.getInstance().getTestReport().testLog(ACTION_LOG_LVL + "Type '" + txt + "' into [" + ele.getText() + "]");
    }

    public void setTimeValue(WebElement ele, String txt, String elementName) {
        delayTime();
        waitForElement(ele);
        ele.clear();
        ele.sendKeys(txt);
        TestReportManager.getInstance().getTestReport().testLog(ACTION_LOG_LVL + "Type '" + txt + "' into [" + elementName + "] time input");
    }
    //endregion

    public void scrollIntoView(WebElement ele) {
        executor.executeScript("arguments[0].scrollIntoView();", ele);
    }

    private boolean isAttributePresent(WebElement element, String attribute) {
        Boolean result = false;
        try {
            String value = element.getAttribute(attribute);
            if (value != null) {
                result = true;
            }
        } catch (Exception e) {
        }

        return result;
    }

    public boolean isElementVisible(By locatorKey, int timeout) {
        try {
            return findElement(locatorKey, timeout).isDisplayed();
        } catch (NoSuchElementException | TimeoutException e) {
            return false;
        }
    }

    public boolean isElementPresent(By locatorKey, int timeout) {
        try {
            return findElements(locatorKey, timeout).size() > 0;
        }catch(NoSuchElementException | TimeoutException e){
            return false;
        }
    }

    public boolean isElementPresent(WebElement parent,By locatorKey, int timeout) {
        try {
            return findElements(parent, locatorKey, timeout).size() > 0;
        }catch(NoSuchElementException | TimeoutException e){
            return false;
        }
    }

    public void delayTime() {
        try {
            Thread.sleep(getTimeDelay());
        } catch (InterruptedException e) {
            TestReportManager.getInstance().getTestReport().testFail(ACTION_LOG_LVL + "InterruptedException: " + e.getMessage(), "");
        }
    }

    public void delayTime(int timeDelay) {
        setTimeDelay(timeDelay);
        delayTime();
        setTimeDelay(0);
    }
}
