package etestHandlers;

import com.etest.PayloadUtil;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class CreateETest extends EtestBase{
    String newCreateTitle;

    @Test
    @Parameters({"url", "env"})
    public void createTestWithNoTitle(String url, String env) {
        Map<String, Object> payload = (Map<String, Object>) PayloadUtil.readPayloadFromFile(env).get("createQuiz");
        payload.put("title", null);
        Map<String, Object> response = PayloadUtil.readPayloadFromResponseAsObject(MethodHandlers.createQuiz(url, payload).asString());
        Assert.assertEquals(response.get("code"), -1);
        Assert.assertEquals(response.get("description"), "Missing Title");
    }

    @Test
    @Parameters({"url", "env"})
    public void createTestWithExistingTitle(String url, String env) {
        Map<String, Object> payload = (Map<String, Object>) PayloadUtil.readPayloadFromFile(env).get("updateQuiz");
        payload.remove("id");
        Map<String, Object> response = PayloadUtil.readPayloadFromResponseAsObject(MethodHandlers.createQuiz(url, payload).asString());
        Assert.assertEquals(response.get("code"), -1);
        Assert.assertEquals(response.get("description"), "Test Already Exists, Change Title or Create New Test");
    }

    @Test
    @Parameters({"url", "env"})
    public void createTestWithNewTitle(String url, String env) {
        Map<String, Object> payload = PayloadUtil.readPayloadFromFile(env);
        newCreateTitle = PayloadUtil.generateRandomTitle(6, env);
        Map<String, Object> newPayload = (Map<String, Object>) payload.get("createQuiz");
        newPayload.put("title", newCreateTitle);
        Map<String, Object> response = PayloadUtil.readPayloadFromResponseAsObject(MethodHandlers.createQuiz(url, newPayload).asString());
        Assert.assertEquals(response.get("code"), 0);
        Assert.assertEquals(response.get("description"), "Created Successfully");
        payload.put("createQuiz", newPayload);
        PayloadUtil.saveNewJsonData(payload, env);
    }

    @Test
    @Parameters({"env", "url"})
    public void confirmTestCreatedById(String env, String url) {
        Map<String, Object> payload = (Map<String, Object>) PayloadUtil.readPayloadFromFile(env).get("createQuiz");
        String id = MethodHandlers.getIDofTestTitle(env, url);
        Map<String, Object> response = PayloadUtil
                .readPayloadFromResponseAsObject(MethodHandlers.getQuiz(url, id).asString());
        payload.put("id", Integer.parseInt(id));
        Assert.assertEqualsDeep(response, payload);
    }
}
