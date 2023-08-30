package etestHandlers;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.restassured.response.Response;
import io.restassured.specification.QueryableRequestSpecification;
import org.testng.ITestResult;

import java.io.File;
import java.lang.reflect.Method;

public class Reporter {

    public static ExtentSparkReporter extentSparkReporter;
    public static ExtentReports extent;
    private static ThreadLocal<ExtentTest> nodeTest = new ThreadLocal<>();

    private static ThreadLocal<ExtentTest> parentExtentTest = new ThreadLocal<>();

    public void setReporterToExtent(String env) {
        extentSparkReporter = new ExtentSparkReporter(new File(System.getProperty("user.dir"), "extentReports.html"));
        extentSparkReporter.config().setDocumentTitle("Etest Backend Services");
        extentSparkReporter.config().setReportName("Etest Backend Services Test");
        extentSparkReporter.config().setTheme(Theme.DARK);
        extent = new ExtentReports();
        extent.attachReporter(extentSparkReporter);
        extent.setSystemInfo("Test Environment", env);
        extent.setSystemInfo("Operating System", System.getProperty("os.name"));
    }

    public void createTestReports() {
        ExtentTest test = extent.createTest(getClass().getName());
        parentExtentTest.set(test);
    }

    public void createTestReportForMethods(Method method) {
        ExtentTest child = parentExtentTest.get().createNode(method.getName());
        nodeTest.set(child);
    }

    public static void testInfo(String info) {
        nodeTest.get().info("<b>" + info  + "</b>");
    }
    public static void requestPayloadInfo(QueryableRequestSpecification qrs) {
        nodeTest.get().info(MarkupHelper.createCodeBlock(qrs.getBody().toString(), CodeLanguage.JSON));
    }

    public static void responsePayloadInfo(Response response) {
        nodeTest.get().info(MarkupHelper.createCodeBlock(response.prettyPrint(), CodeLanguage.JSON));
    }

    public void reportListeners(ITestResult result) {
        if(ITestResult.SKIP == result.getStatus()) {
            nodeTest.get().skip("Test Skipped");
        }
        else if(ITestResult.FAILURE == result.getStatus()) {
            String showException = "<details><summary>stacktrace...</summary>" +
                    result.getThrowable() +
                    "</details>";
            nodeTest.get().fail("Test Failed");
            nodeTest.get().info(showException);
        }
        else if(ITestResult.SUCCESS == result.getStatus()) {
            nodeTest.get().pass("Test Passed");
        }
        extent.flush();
    }
}
