package core.base_action;


import java.util.Arrays;
import java.util.List;

public enum BrowserType{
    CHROME("chrome", "google chrome","googlechrome"),
    IE("internet explorer", "ie", "internetexplorer"),
    FIREFOX("ff", "fire fox", "firefox"),
    EDGE("edge","microsoft edge","microsoftedge");

    private final List<String> values;

    BrowserType(String ...values) {
        this.values = Arrays.asList(values);
    }

    public List<String> getValues() {
        return values;
    }

    public static BrowserType find(String name) {
        for (BrowserType browser : BrowserType.values()) {
            if (browser.getValues().contains(name.toLowerCase().trim())) {
                return browser;
            }
        }
        return CHROME;
    }
}