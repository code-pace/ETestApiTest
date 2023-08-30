package etestHandlers;

import com.etest.PayloadUtil;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class UpdateETest extends EtestBase{
    @Test
    @Parameters({"env", "url"})
    public void updateTestNoTitle(String env, String url) {
        Map<String, Object> payload = (Map<String, Object>) PayloadUtil.readPayloadFromFile(env).get("updateQuiz");
        payload.put("title", null);
        Map<String, Object> response = PayloadUtil
                .readPayloadFromResponseAsObject(MethodHandlers.updateQuiz(url, payload).asString());
        Assert.assertEquals(response.get("code"), -1);
        Assert.assertEquals(response.get("description"), "Missing Title");
    }

    @Test
    @Parameters({"env", "url"})
    public void updateTestNonExistingId(String env, String url) {
        Map<String, Object> payload = (Map<String, Object>) PayloadUtil.readPayloadFromFile(env).get("updateQuiz");
        payload.put("id", "238sd2");
        Map<String, Object> response = PayloadUtil
                .readPayloadFromResponseAsObject(MethodHandlers.updateQuiz(url, payload).asString());
        Assert.assertEquals(response.get("code"), -1);
        Assert.assertEquals(response.get("description"), "Record not Found");
    }

    @Test
    @Parameters({"env", "url"})
    public void updateTestNoId(String env, String url) {
        Map<String, Object> payload = (Map<String, Object>) PayloadUtil.readPayloadFromFile(env).get("updateQuiz");
        payload.put("id", null);
        Map<String, Object> response = PayloadUtil
                .readPayloadFromResponseAsObject(MethodHandlers.updateQuiz(url, payload).asString());
        Assert.assertEquals(response.get("code"), -1);
        Assert.assertEquals(response.get("description"), "Record not Found");
    }

    @Test
    @Parameters({"env", "url"})
    public void updateTest(String env, String url) {
        Map<String, Object> payload = PayloadUtil.readPayloadFromFile(env);
        String newCreateTitle = PayloadUtil.generateRandomTitle(5, env);
        Map<String, Object> newPayload = (Map<String, Object>) payload.get("updateQuiz");
        newPayload.put("title", newCreateTitle);
        Map<String, Object> response = PayloadUtil
                .readPayloadFromResponseAsObject(MethodHandlers.updateQuiz(url, newPayload).asString());
        Assert.assertEquals(response.get("code"), 0);
        Assert.assertEquals(response.get("description"), "Updated Successfully");
        payload.put("updateQuiz", newPayload);
        PayloadUtil.saveNewJsonData(payload, env);
    }

    @Test
    @Parameters({"env", "url"})
    public void confirmTestUpdatedById(String env, String url) {
        Map<String, Object> payload = PayloadUtil.readPayloadFromFile(env);
        Map<String, Object> newPayload = (Map<String, Object>) payload.get("updateQuiz");
        String id = String.valueOf(newPayload.get("id"));
        Map<String, Object> response = PayloadUtil
                .readPayloadFromResponseAsObject(MethodHandlers.getQuiz(url, id).asString());
        Assert.assertEqualsDeep(response, newPayload);
    }
}
