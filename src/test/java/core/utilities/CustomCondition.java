package core.utilities;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nullable;

public class CustomCondition {

    public static ExpectedCondition<Boolean> elementPresent(final WebElement ele) {
        return new ExpectedCondition<Boolean>() {
            @Nullable
            @Override
            public Boolean apply(@Nullable WebDriver driver) {
                try {
                    return ele.isEnabled();
                } catch (NoSuchElementException ex) {
                    return false;
                }
            }
        };
    }

    public static ExpectedCondition<Boolean> textToBePresentInElementValue(final WebElement ele, String txt) {
        return new ExpectedCondition<Boolean>() {
            @Nullable
            @Override
            public Boolean apply(@Nullable WebDriver driver) {
                try {
                    String elementValue = ele.getAttribute("value");
                    if(!elementValue.equals(txt)) {
                        throw new Exception(String.format("Text [%s] was not matched to element value [%s]", txt, elementValue));
                    }
                     return true;
                } catch (Exception ex) {
                    ele.clear();
                    ele.sendKeys(txt);
                    return false;
                }
            }
        };
    }
}
