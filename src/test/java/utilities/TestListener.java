package utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    private static final Logger logger = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        logger.info("-------------------------------------------------------------------------------------------------------");
        logger.info("🚀 Starting test: {}", methodName);

        String os = System.getProperty("os.name");
        String browser = System.getProperty("browser", "chrome");

        logger.info("🖥️  Operating System: {}", os);
        logger.info("🌐 Browser: {}", browser);
        logger.info("-------------------------------------------------------------------------------------------------------");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        logger.info("✅ Test passed: {}", methodName);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        logger.warn("⏭️ Test skipped: {}", methodName);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String methodName = result.getMethod().getMethodName();

        logger.info("-------------------------------------------------------------------------------------------------------");
        Throwable failureReason = result.getThrowable();
        if (failureReason != null) {
            logger.error("❌ Test failed: {} | Reason: {}", methodName, failureReason.getClass().getSimpleName());
            logger.error("📝 Error Message: {}", failureReason.getMessage());
        } else {
            logger.error("❌ Test failed: {} | No exception found", methodName);
        }
        logger.info("-------------------------------------------------------------------------------------------------------");
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("=======================================================================================================");
        logger.info("📦 Test suite: {}", context.getName());

        logger.info("-------------------------------------------------------------------------------------------------------");
        logger.info("✅ Passed Tests:");
        context.getPassedTests().getAllResults().forEach(result ->
                logger.info("   - {}", result.getMethod().getMethodName())
        );

        logger.info("❌ Failed Tests:");
        context.getFailedTests().getAllResults().forEach(result ->
                logger.info("   - {}", result.getMethod().getMethodName())
        );

        logger.info("⏭️ Skipped Tests:");
        context.getSkippedTests().getAllResults().forEach(result ->
                logger.info("   - {}", result.getMethod().getMethodName())
        );
        logger.info("=======================================================================================================");
    }

}
