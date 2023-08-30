package etestHandlers;

import org.testng.ITestResult;
import org.testng.annotations.*;
import java.lang.reflect.Method;

public class EtestBase extends Reporter {

    @BeforeSuite
    @Parameters("env")
    public void setup(String env) {
        setReporterToExtent(env);
    }
    @BeforeClass
    public void createTestAtClassLevel() {
        createTestReports();
    }

    @BeforeMethod
    public void createTestAtMethodLevel(Method method) {
        createTestReportForMethods(method);
    }
    @AfterMethod
    public void getReportsForMethods(ITestResult result) {
        reportListeners(result);
    }
}
