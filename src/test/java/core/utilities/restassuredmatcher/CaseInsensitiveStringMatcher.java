package core.utilities.restassuredmatcher;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class CaseInsensitiveStringMatcher extends TypeSafeMatcher<String> {

    private final String expectedString;
    private final String methodName;

    private CaseInsensitiveStringMatcher(final String exString, final String method){
        methodName = method;
        this.expectedString = exString;
    }

    @Override
    protected boolean matchesSafely(final String actualString) {
        switch (methodName){
            case "containsIgnoringCase":
                return actualString.toLowerCase().contains(this.expectedString.toLowerCase());
            case "greaterThanOrEqualToIgnoringCase":
                return actualString.toLowerCase().compareToIgnoreCase(this.expectedString) >= 0;
            case "lessThanOrEqualToIgnoringCase":
                return actualString.toLowerCase().compareToIgnoreCase(this.expectedString) <= 0;
        }
        return false;
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("containing substring \"" + this.expectedString + "\"");
    }

    @Factory
    public static Matcher<String> containsIgnoringCase(final String exString){
        return new CaseInsensitiveStringMatcher(exString,"containsIgnoringCase");
    }

    @Factory
    public static Matcher<String> greaterThanOrEqualToIgnoringCase(final String exString){
        return new CaseInsensitiveStringMatcher(exString,"greaterThanOrEqualToIgnoringCase");
    }

    @Factory
    public static Matcher<String> lessThanOrEqualToIgnoringCase(final String exString){
        return new CaseInsensitiveStringMatcher(exString,"lessThanOrEqualToIgnoringCase");
    }

}
