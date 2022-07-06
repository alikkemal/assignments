package base;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class TestContextInitializer implements TestRule {

    private static Statement statement(final Statement base, final Description description) {
        return new Statement() {

            @Override
            public void evaluate() throws Throwable {
                TestContext context = TestContext.init(description.getClassName(), description.getMethodName());
                Runtime.getRuntime().addShutdownHook(new Thread(context::whenTestStoppedByJvm, "shutdown-thread-when-test-stopping-somehow"));

                try {
                    base.evaluate();
                } finally {
                    TestContext.remove();
                }
            }
        };
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return statement(base, description);
    }
}
