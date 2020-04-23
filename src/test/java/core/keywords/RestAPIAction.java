package core.keywords;

import core.base_action.BaseAction;
import core.extent_report.TestReportManager;
import core.testdata_manager.TestStep;
import core.utilities.HashMapHandler;
import core.utilities.restassuredmatcher.CaseInsensitiveStringMatcher;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class RestAPIAction extends BaseAction {
    private String outputString = "";
    private RequestSpecification reqSpec;
    private Response response;
    public RestAPIAction(BaseAction action){
        super(action);
    }

    public void GET(TestStep step){
        restAPIActionHandler(step);
    }

    public void POST(TestStep step){
        restAPIActionHandler(step);
    }

    public void PUT(TestStep step){
        restAPIActionHandler(step);
    }

    public void DELETE(TestStep step){
        restAPIActionHandler(step);
    }
    public void PATCH(TestStep step){
        restAPIActionHandler(step);
    }

    private void restAPIActionHandler(TestStep step){
        ArrayList<String> passTexts = new ArrayList<>();
        ArrayList<AssertionError> errors = new ArrayList<>();

        JSONObject requestJObj = (JSONObject) step.getTestParams().get("request");

        sendRequest(step.getMethod(), requestJObj, passTexts, errors);
        //outputString = "<button type=\"button\" class=\"collapsible\">Request & Response</button><div class=\"content\">";
        outputString = outputString + "Request: <br><pre>" + getRestAction().getRequestWriter().toString() + "</pre><br>";

        JSONObject responseJsonObj = (JSONObject) step.getTestParams().get("response");
        LinkedHashMap<String, Object> responseSection = HashMapHandler.ConvertJsonObjectToHashMap(responseJsonObj);

        //validate response
        validateResponse(responseSection, response, passTexts, errors);
        outputString = outputString + "Response: <br><pre>" + getRestAction().getResponseWriter().toString() + "</pre><br>";
        //outputString = outputString + "</div>";
        TestReportManager.getInstance().getTestReport().testLog(outputString);

        boolean resultStep = errors.size() <= 0;
        softAssert.assertTrue(resultStep, formatPassTexts(passTexts) + formatErrors(errors));
    }

    private void sendRequest(String method, JSONObject requestJObj, ArrayList<String> passText, ArrayList<AssertionError> errors){
        try {
            getRestAction().initLogWriter();

            String requestUrl = (String) requestJObj.get("url");
            reqSpec = getRestAction().specifyRequest();

            LinkedHashMap<String, Object> headerItems = HashMapHandler.ConvertJsonObjectToHashMap((JSONObject) requestJObj.get("headers"));
            if (headerItems.keySet().size() > 0) {
                reqSpec = reqSpec.headers(headerItems);
            }

            String contentType = (String) requestJObj.get("contentType");
            if (contentType != null) {
                reqSpec = reqSpec.contentType(contentType);
            }

            LinkedHashMap<String, Object> formParams = HashMapHandler.ConvertJsonObjectToHashMap((JSONObject) requestJObj.get("formParams"));
            if (formParams.keySet().size() > 0) {
                reqSpec = reqSpec.formParams(formParams);
            }

            LinkedHashMap<String, Object> queryParams = HashMapHandler.ConvertJsonObjectToHashMap((JSONObject) requestJObj.get("queryParams"));
            if (queryParams.keySet().size() > 0) {
                reqSpec = reqSpec.queryParams(queryParams);
            }

            JSONObject requestBody = (JSONObject) requestJObj.get("body");
            if (requestBody != null) {
                reqSpec = reqSpec.body(requestBody.toJSONString());
            }

            response = getRestAction().executeMethod(method, requestUrl, reqSpec);
            passText.add("The request is sent successfully.");
        }
        catch(AssertionError e){
            errors.add(e);
        }
    }

    public void validateResponse(TestStep step){
        ArrayList<String> passTexts = new ArrayList<>();
        ArrayList<AssertionError> errors = new ArrayList<>();

        validateResponse(step.getTestParams(), response, passTexts, errors);
        boolean resultStep = errors.size() <= 0;
        softAssert.assertTrue(resultStep, formatPassTexts(passTexts) + formatErrors(errors));
    }

    public ValidatableResponse validateResponse(LinkedHashMap<String, Object> responseJObj, Response response, ArrayList<String> passText, ArrayList<AssertionError> errors) {
        ValidatableResponse validRes = response.then();
        for (Map.Entry<String, Object> entry : responseJObj.entrySet()) {
            try {
                switch (entry.getKey()) {
                    case "statusCode":
                        int status = (entry.getValue() instanceof Integer) ? (int) entry.getValue() : ((Long) entry.getValue()).intValue();
                        validRes.statusCode(status);
                        passText.add("Ensure the status code is " + status);
                        break;
                    case "schemaPath":
                        validRes.body(JsonSchemaValidator.matchesJsonSchemaInClasspath("data\\api\\" + entry.getValue()));
                        passText.add("Ensure the response data schema match to the defined schema at" + entry.getValue());
                        break;
                    case "body":
                        JSONObject fieldPaths = (JSONObject) entry.getValue();
                        for (Object fieldPath : fieldPaths.keySet()) {
                            JSONObject jsonMatchers = (JSONObject) fieldPaths.get(fieldPath);
                            for (Object matcher : jsonMatchers.keySet()) {
                                Matcher<?> definedMatcher = buildMatcherPattern((String) matcher, jsonMatchers.get(matcher));
                                validRes.body((String) fieldPath, definedMatcher);
                                passText.add("Ensure the field [" + fieldPath + "] - " + definedMatcher.toString() + ".");
                            }
                        }
                        break;
                }
            } catch (AssertionError e) {
                errors.add(e);
            }
        }
        return validRes;
    }

    public void storeResponseValue(TestStep step){
        //store runtime wars
        JSONArray storedParams = (JSONArray) step.getTestParams().get("variables");
        if(storedParams != null) {
            storedParams.forEach(ele -> {
                        try {
                            String varName = (String) ((JSONObject) ele).get("varName");
                            String valueName = (String) ((JSONObject) ele).get("key");
                            Object value = response.getBody().path(valueName);
                            String cast = (String) ((JSONObject) ele).get("cast");
                            if (cast != null) {
                                switch (cast) {
                                    case "int":
                                        if (value instanceof String)
                                            value = Integer.valueOf((String) value);
                                        break;
                                    case "String":
                                        break;
                                }
                            }
                            testVars.put(varName, value);
                            getSoftAssert().assertTrue(true, "Store the value of field '" + valueName + "' : '" + value + "' into the run time variable list.");
                        } catch (AssertionError e) {
                            getSoftAssert().assertTrue(false, e.getMessage());
                        }
                    }

            );
        }
    }

    private Matcher<?> buildMatcherPattern(String matcherPattern, Object valueMatcher) {
        Matcher<?> definedMatcher = null;
        // handle for serial number of matcher pattern
        matcherPattern = matcherPattern.indexOf("[") == 0 && matcherPattern.indexOf(']') > 0
                ?   matcherPattern.substring(matcherPattern.indexOf(']') + 1).trim()
                :   matcherPattern;
        String[] funcParts = matcherPattern.split("\\.");
        if (valueMatcher instanceof Long) {
            int val = ((Long) valueMatcher).intValue();
            if (((Long) valueMatcher) == val) {
                valueMatcher = val;
            }
        } else if (valueMatcher instanceof String) {
            valueMatcher = valueMatcher.toString();
        }


        for (int i = funcParts.length - 1; i >= 0; i--) {

            if (i == funcParts.length - 1) {
                definedMatcher = CreateMatcher(funcParts[i], valueMatcher);
            } else {
                definedMatcher = CreateMatcher(funcParts[i], definedMatcher);
            }
            if (definedMatcher == null) {
                break;
            }
        }
        return definedMatcher;
    }

    private Matcher<?> CreateMatcher(String funcName, Object value) {
        switch (funcName) {
            case "containsIgnoringCase":
                return CaseInsensitiveStringMatcher.containsIgnoringCase(value.toString());
            case "equalTo":
                if (value instanceof Integer) {
                    return Matchers.equalTo((Integer) value);
                } else {
                    if (value instanceof Long) {
                        return Matchers.equalTo(((Long) value).intValue());
                    } else if (value instanceof Double) {
                        return Matchers.equalTo(new BigDecimal(value.toString()));
                    } else if (value instanceof BigDecimal) {
                        return Matchers.equalTo(new BigDecimal(value.toString()));
                    }
                }
                return Matchers.equalTo(value);
            case "equalToIgnoringCase":
                return Matchers.equalToIgnoringCase((String) value);
            case "everyItem":
                return Matchers.everyItem((Matcher<?>) value);
            case "greaterThan":
                if (value instanceof String) {
                    return Matchers.greaterThan((String) value);
                } else {
                    if (value instanceof Integer) {
                        return Matchers.greaterThan((Integer) value);
                    } else if (value instanceof Long) {
                        return Matchers.greaterThan(((Long) value).intValue());
                    } else if (value instanceof Double) {
                        return Matchers.greaterThan(new BigDecimal(value.toString()));
                    } else if (value instanceof BigDecimal) {
                        return Matchers.greaterThan((BigDecimal) value);
                    }
                }
                return Matchers.greaterThan((Long) value);
            case "greaterThanOrEqualTo":
                if (value instanceof String) {
                    return Matchers.greaterThanOrEqualTo((String) value);
                } else {
                    if (value instanceof Integer) {
                        return Matchers.greaterThanOrEqualTo((Integer) value);
                    } else if (value instanceof Long) {
                        return Matchers.greaterThanOrEqualTo(((Long) value).intValue());
                    } else if (value instanceof Double) {
                        return Matchers.greaterThanOrEqualTo(new BigDecimal(value.toString()));
                    } else if (value instanceof BigDecimal) {
                        return Matchers.greaterThanOrEqualTo((BigDecimal) value);
                    }
                }
                return Matchers.greaterThanOrEqualTo((Long) value);
            case "greaterThanOrEqualToIgnoringCase":
                return CaseInsensitiveStringMatcher.greaterThanOrEqualToIgnoringCase((String) value);
            case "hasKey":
                return Matchers.hasKey(value);
            case "hasItem":
                return Matchers.hasItem(value);
            case "hasItems":
                return Matchers.hasItems(((JSONArray) value).toArray());
            case "is":
                if (value instanceof String) {
                    return Matchers.is((String) value);
                } else {
                    if (value instanceof Integer) {
                        return Matchers.is((Integer) value);
                    } else if (value instanceof Long) {
                        return Matchers.is(((Long) value).intValue());
                    } else if (value instanceof Double) {
                        return Matchers.is(new BigDecimal(value.toString()));
                    } else if (value instanceof BigDecimal) {
                        return Matchers.is((BigDecimal) value);
                    }
                }
                return Matchers.is(value);
            case "lessThanOrEqualTo":
                if (value instanceof String) {
                    return Matchers.lessThanOrEqualTo((String) value);
                } else {
                    if (value instanceof Integer) {
                        return Matchers.lessThanOrEqualTo((Integer) value);
                    } else if (value instanceof Long) {
                        return Matchers.lessThanOrEqualTo(((Long) value).intValue());
                    } else if (value instanceof Double) {
                        return Matchers.lessThanOrEqualTo(new BigDecimal(value.toString()));
                    } else if (value instanceof BigDecimal) {
                        return Matchers.lessThanOrEqualTo((BigDecimal) value);
                    }
                }
                return Matchers.lessThanOrEqualTo((Long) value);
            case "lessThanOrEqualToIgnoringCase":
                return CaseInsensitiveStringMatcher.lessThanOrEqualToIgnoringCase((String) value);
            case "not":
                return Matchers.not((Matcher<?>) value);
        }
        return null;
    }

    private String formatErrors(List<AssertionError> errors) {
        StringBuilder temp = new StringBuilder();
        for (int i = 1; i <= errors.size(); i++) {
            temp.append(String.format("<font color=\"red\"><b>Error %s:</b></font> <br><pre>%s</pre>", errors.size() != 1 ? i : "", errors.get(i - 1)));
        }
        return temp.toString();
    }

    private String formatPassTexts(List<String> passTexts) {
        StringBuilder temp = new StringBuilder();
        temp.append("<font color=\"green\"><b>Validation:</b></font><br><pre>");
        for (int i = 1; i <= passTexts.size(); i++) {
            temp.append(String.format("<font color=\"green\">%s</font>%s<br>", passTexts.size() != 1 ? i + ". " : "", passTexts.get(i - 1)));
        }
        temp.append("</pre>");
        return temp.toString();
    }
}
