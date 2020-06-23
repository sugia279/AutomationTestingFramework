package core.keywords;

import core.base_action.Action;
import core.extent_report.TestReportManager;
import core.utilities.restassuredmatcher.CaseInsensitiveStringMatcher;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.json.simple.JSONArray;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class RestAPIAction extends Action {
    private String outputString = "";
    private RequestSpecification reqSpec;
    private Response response;
    public RestAPIAction(Action action){
        super(action);
    }

    public void GET(LinkedHashMap<String,Object> request, LinkedHashMap<String, Object> response){
        restAPIActionHandler("get", request, response);
    }

    public void POST(LinkedHashMap<String,Object> request, LinkedHashMap<String, Object> response){
        restAPIActionHandler("post", request, response);
    }

    public void PUT(LinkedHashMap<String,Object> request, LinkedHashMap<String, Object> response){
        restAPIActionHandler("put", request, response);
    }

    public void DELETE(LinkedHashMap<String,Object> request, LinkedHashMap<String, Object> response){
        restAPIActionHandler("delete", request, response);
    }
    public void PATCH(LinkedHashMap<String,Object> request, LinkedHashMap<String, Object> response){
        restAPIActionHandler("patch", request, response);
    }

    private void restAPIActionHandler(String method,LinkedHashMap<String,Object> requestJson, LinkedHashMap<String, Object> responseJson){
        ArrayList<String> passTexts = new ArrayList<>();
        ArrayList<AssertionError> errors = new ArrayList<>();

        sendRequest(method, requestJson, passTexts, errors);
        //outputString = "<button type=\"button\" class=\"collapsible\">Request & Response</button><div class=\"content\">";
        outputString = outputString + "Request: <br><pre>" + getRestAction().getRequestWriter().toString() + "</pre><br>";

        //validate response
        validateResponse(responseJson, response, passTexts, errors);
        outputString = outputString + "Response: <br><pre>" + getRestAction().getResponseWriter().toString() + "</pre><br>";
        //outputString = outputString + "</div>";
        TestReportManager.getInstance().getTestReport().testLog(outputString);

        boolean resultStep = errors.size() <= 0;
        softAssert.assertTrue(resultStep, formatPassTexts(passTexts) + formatErrors(errors));
    }

    private void sendRequest(String method, LinkedHashMap<String, Object> request, ArrayList<String> passText, ArrayList<AssertionError> errors){
        try {
            getRestAction().initLogWriter();

            String requestUrl = (String) request.get("url");
            reqSpec = getRestAction().specifyRequest();

            LinkedHashMap<String, Object> headerItems = (LinkedHashMap<String, Object>)request.get("headers");
            if (headerItems != null && headerItems.keySet().size() > 0) {
                reqSpec = reqSpec.headers(headerItems);
            }

            String contentType = (String) request.get("contentType");
            if (contentType != null) {
                reqSpec = reqSpec.contentType(contentType);
            }

            LinkedHashMap<String, Object> formParams = (LinkedHashMap<String, Object>)request.get("formParams");
            if (formParams != null && formParams.keySet().size() > 0) {
                reqSpec = reqSpec.formParams(formParams);
            }

            LinkedHashMap<String, Object> queryParams = (LinkedHashMap<String, Object>)request.get("queryParams");
            if (queryParams != null && queryParams.keySet().size() > 0) {
                reqSpec = reqSpec.queryParams(queryParams);
            }

            LinkedHashMap<String, Object>  requestBody = (LinkedHashMap<String, Object>)request.get("body");
            if (requestBody != null) {
                reqSpec = reqSpec.body(requestBody);
            }

            response = getRestAction().executeMethod(method, requestUrl, reqSpec);
            passText.add("The request is sent successfully.");
        }
        catch(AssertionError e){
            errors.add(e);
        }
    }

    public void validateResponse(LinkedHashMap<String, Object> responseJson){
        ArrayList<String> passTexts = new ArrayList<>();
        ArrayList<AssertionError> errors = new ArrayList<>();

        validateResponse(responseJson, response, passTexts, errors);
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
                        LinkedHashMap<String, Object> fieldPaths = (LinkedHashMap<String, Object>) entry.getValue();
                        for (String fieldPath : fieldPaths.keySet()) {
                            LinkedHashMap<String, Object> jsonMatchers = (LinkedHashMap<String, Object>) fieldPaths.get(fieldPath);
                            for (String matcher : jsonMatchers.keySet()) {
                                Matcher<?> definedMatcher = buildMatcherPattern(matcher, jsonMatchers.get(matcher));
                                validRes.body(fieldPath, definedMatcher);
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

    public void storeResponseValue(ArrayList<LinkedHashMap<String, Object>> variables){
        //store runtime wars
        ArrayList<LinkedHashMap<String, Object>> storedParams = variables;
        if(storedParams != null) {
            storedParams.forEach(ele -> {
                        try {
                            String varName = (String) ele.get("varName");
                            String valueName = (String) ele.get("key");
                            Object value = response.getBody().path(valueName);
                            String cast = (String) ele.get("cast");
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
