package jsonplaceholder_webapitesting.keywords;

import core.base_action.BaseAction;
import core.base_action.RestAction;
import core.extent_report.TestReportManager;
import core.testdata_manager.TestStep;
import core.utilities.HashMapHandler;
import core.utilities.JsonHandler;
import core.utilities.StringHandler;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class WebAPIAction extends BaseAction {
    private String outputString = "";
    public WebAPIAction(BaseAction action){
        super(action);
    }

    public void GET(TestStep step){

    }

    public void POST(TestStep step){
        JSONObject requestSection = (JSONObject) step.getTestParams().get("request");
        String requestUrl = (String) StringHandler.replaceValueByMapData((String)requestSection.get("url"), "@var->", "@", testVars);
        String requestBody = requestSection.get("body").toString();
        Response res = new RestAction().runPostMethod(requestUrl, requestBody);

        JSONObject responseSection = (JSONObject) step.getTestParams().get("response");
        Long statusCode = (Long)responseSection.get("statusCode");
        if (statusCode != null) {
            getSoftAssert().assertEquals(res.statusCode(), statusCode.intValue(), "Ensure POST method is executed successful with status Code is " + statusCode);
        }
    }

    private void webAPIActionHandler(TestStep step){
        JSONObject requestSection = (JSONObject) step.getTestParams().get("request");
        String requestUrl = (String) StringHandler.replaceValueByMapData((String)requestSection.get("url"), "@var->", "@", testVars);
        LinkedHashMap<String, Object> queryParams = HashMapHandler.ConvertJsonObjectToHashMap((JSONObject)requestSection.get("queryParams"));
        String requestBody = requestSection.get("body").toString();
        LinkedHashMap<String, Object> responseSection = HashMapHandler.ConvertJsonObjectToHashMap((JSONObject) step.getTestParams().get("response"));

        RequestSpecification reqSpec = getRestAction().specifyRequest().queryParams(queryParams).body(requestBody);
        Response res = getRestAction().executeMethod(step.getMethod(), requestUrl, reqSpec);

    }
}
