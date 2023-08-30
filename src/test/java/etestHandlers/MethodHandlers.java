package etestHandlers;

import com.etest.PayloadUtil;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.SpecificationQuerier;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class MethodHandlers {

    public static RequestSpecification requestSpecification() {
        return given().when();
    }
    public static RequestSpecification requestSpecification(String id) {
        return given().queryParam("id", id);
    }
    public static RequestSpecification requestSpecification(Map<String, Object> payload) {
        return given().contentType(ContentType.JSON).when().body(payload);
    }
    public static Response getQuizTitle(String url) {
        RequestSpecification reqBody = requestSpecification();
        Response response = reqBody.get(url).then().assertThat().statusCode(200).extract().response();
        Reporter.testInfo("Response Body");
        Reporter.responsePayloadInfo(response);
        return response;
    }
    public static Response getQuiz(String url, String id) {
        RequestSpecification reqBody = requestSpecification(id);
        Response response = reqBody.get(url + "/ques").then().extract().response();
        Reporter.testInfo("Response Body");
        Reporter.responsePayloadInfo(response);
        return response;
    }
    public static Response createQuiz(String url, Map<String, Object> payload) {
        RequestSpecification reqBody = requestSpecification(payload);
        QueryableRequestSpecification requestObj = SpecificationQuerier.query(reqBody);
        Reporter.testInfo("Request Body");
        Reporter.requestPayloadInfo(requestObj);
        Response response = reqBody.post(url + "/create").then().assertThat().statusCode(200).extract().response();
        Reporter.testInfo("Response Body");
        Reporter.responsePayloadInfo(response);
        return response;
    }
    public static Response updateQuiz(String url, Map<String, Object> payload) {
        RequestSpecification reqBody = requestSpecification(payload);
        QueryableRequestSpecification requestObj = SpecificationQuerier.query(reqBody);
        Reporter.testInfo("Request Body");
        Reporter.requestPayloadInfo(requestObj);
        Response response = reqBody.put(url + "/ques").then().extract().response();
        Reporter.testInfo("Response Body");
        Reporter.responsePayloadInfo(response);
        return response;
    }
    public static Response deleteQuiz(String url, Map<String, Object> payload) {
        RequestSpecification reqBody = requestSpecification(payload);
        QueryableRequestSpecification requestObj = SpecificationQuerier.query(reqBody);
        Reporter.testInfo("Request Body");
        Reporter.requestPayloadInfo(requestObj);
        Response response = reqBody.delete(url + "/ques").then().extract().response();
        Reporter.testInfo("Response Body");
        Reporter.responsePayloadInfo(response);
        return response;
    }

    public static String getIDofTestTitle(String env, String url) {
        Map<String, Object> payload = (Map<String, Object>) PayloadUtil.readPayloadFromFile(env).get("createQuiz");
        String title = (String) payload.get("title");
        List<Map<String, Object>> responseArray =  PayloadUtil.readPayloadFromResponseAsList(getQuizTitle(url).asString());
        responseArray =  responseArray.stream()
                .filter(item -> title.equalsIgnoreCase((String) item.get("title"))).collect(Collectors.toList());
        return String.valueOf(responseArray.get(0).get("id"));
    }
}
