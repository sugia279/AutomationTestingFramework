package core.base_action;

import core.extent_report.ReportLogLevel;
import core.extent_report.TestReportManager;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.io.output.WriterOutputStream;

import java.io.PrintStream;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType;

public class RestAction {
    private StringWriter requestWriter;
    protected PrintStream requestCapture;
    private StringWriter responseWriter;
    protected PrintStream responseCapture;
    private ContentType contentType;
    private NumberReturnType numberReturnType;

    public RestAction() {
        contentType = ContentType.JSON;
        numberReturnType = NumberReturnType.BIG_DECIMAL;
    }

    public void initLogWriter() {
        requestWriter = new StringWriter();
        requestCapture = new PrintStream(new WriterOutputStream(getRequestWriter(), "UTF-8"), true);

        responseWriter = new StringWriter();
        responseCapture = new PrintStream(new WriterOutputStream(getResponseWriter(), "UTF-8"), true);
    }

    //endregion
    public Response runGetMethod(String url) {
        TestReportManager.getInstance().setSubStepInfo("Request Method: GET<br> <pre>Url: <a href='" + url + "'>" + url + "</a></pre>", ReportLogLevel.LOG_LVL_4);
        Response response = executeMethod("get", url, specifyRequest());
        return response;
    }

    public Response runPutMethod(String url, String bodyRequest) {
        TestReportManager.getInstance().setSubStepInfo("Request Method: PUT<br> <pre>Url: <a href='" + url + "'>" + url + "</a></pre><br>Body:<br><pre>" + bodyRequest + "</pre>", ReportLogLevel.LOG_LVL_4);
        Response response = executeMethod("put", url, specifyRequest().body(bodyRequest));
        return response;
    }

    public Response runPutMethod(String url, LinkedHashMap<String, Object>  bodyRequest) {
        TestReportManager.getInstance().setSubStepInfo("Request Method: PUT<br> <pre>Url: <a href='" + url + "'>" + url + "</a></pre><br>Body:<br><pre>" + bodyRequest + "</pre>", ReportLogLevel.LOG_LVL_4);
        Response response = executeMethod("put", url, specifyRequest().body(bodyRequest));
        return response;
    }

    public Response runDeleteMethod(String url) {
        TestReportManager.getInstance().setSubStepInfo("Request Method: DELETE<br> <pre>Url: <a href='" + url + "'>" + url + "</a></pre>", ReportLogLevel.LOG_LVL_4);
        Response response = executeMethod("delete", url, specifyRequest());
        return response;
    }

    public Response runPostMethod(String url, String bodyRequest) {
        TestReportManager.getInstance().setSubStepInfo("Request Method: POST<br> <pre>Url: <a href='" + url + "'>" + url + "</a></pre><br>Body:<br><pre>" + bodyRequest + "</pre>", ReportLogLevel.LOG_LVL_4);
        Response response = executeMethod("post", url, specifyRequest().body(bodyRequest));
        return response;
    }

    public Response runPostMethod(String url, LinkedHashMap<String, Object>  bodyRequest) {
        TestReportManager.getInstance().setSubStepInfo("Request Method: POST<br> <pre>Url: <a href='" + url + "'>" + url + "</a></pre><br>Body:<br><pre>" + bodyRequest + "</pre>", ReportLogLevel.LOG_LVL_4);
        Response response = executeMethod("post", url, specifyRequest().body(bodyRequest));
        return response;
    }

    public Response executeMethod(String method, String url, RequestSpecification reqSpec) {
        Response response = null;
        switch (method.toLowerCase()) {
            case "get":
                response = reqSpec.when().get(url);
                break;
            case "post":
                response = reqSpec.when().post(url);
                break;
            case "put":
                response = reqSpec.when().put(url);
                break;
            case "delete":
                response = reqSpec.when().delete(url);
                break;
            case "patch":
                response = reqSpec.when().patch(url);
                break;
        }
        return response;
    }

    public RequestSpecification specifyRequest() {
        initLogWriter();
        return given()
                .contentType(contentType)
                .filter(new RequestLoggingFilter(requestCapture))
                .filter(new ResponseLoggingFilter(responseCapture))
                .log().ifValidationFails()
                .config(RestAssured.config().jsonConfig(jsonConfig().numberReturnType(numberReturnType)));
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public NumberReturnType getNumberReturnType() {
        return numberReturnType;
    }

    public void setNumberReturnType(NumberReturnType numberReturnType) {
        this.numberReturnType = numberReturnType;
    }

    public StringWriter getRequestWriter() {
        return requestWriter;
    }

    public StringWriter getResponseWriter() {
        return responseWriter;
    }

    //endregion

}
