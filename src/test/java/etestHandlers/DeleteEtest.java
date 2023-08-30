package etestHandlers;

import com.etest.PayloadUtil;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class DeleteEtest extends EtestBase{

    static String id;

    @Test
    @Parameters("url")
    public void deleteTestNoId(String url) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", null);
        Map<String, Object> response = PayloadUtil.readPayloadFromResponseAsObject(MethodHandlers.deleteQuiz(url, payload).asString());
        Assert.assertEquals(response.get("code"), -1);
        Assert.assertEquals(response.get("description"), "Record not Found");
    }

    @Test
    @Parameters("url")
    public void deleteTestInvalidId(String url) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", "123d");
        Map<String, Object> response = PayloadUtil.readPayloadFromResponseAsObject(MethodHandlers.deleteQuiz(url, payload).asString());
        Assert.assertEquals(response.get("code"), -1);
        Assert.assertEquals(response.get("description"), "Record not Found");
    }

    @Test
    @Parameters({"env", "url"})
    public void deleteTest(String env, String url) {
        id = MethodHandlers.getIDofTestTitle(env, url);
        Map<String, Object> newPayload = new HashMap<>();
        newPayload.put("id", id);
        Map<String, Object> response = PayloadUtil.readPayloadFromResponseAsObject(MethodHandlers.deleteQuiz(url, newPayload).asString());
        Assert.assertEquals(response.get("code"), 0);
        Assert.assertEquals(response.get("description"), "Deleted Successfully");
    }

    @Test
    @Parameters({"env", "url"})
    public void confirmDeletedTest(String env, String url) {
        Map<String, Object> response =  PayloadUtil.readPayloadFromResponseAsObject(MethodHandlers.getQuiz(url,id).asString());
        Assert.assertEquals(response.get("code"), -1);
        Assert.assertEquals(response.get("description"), "Record not Found!");
    }
}
