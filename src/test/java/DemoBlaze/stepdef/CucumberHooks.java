package DemoBlaze.stepdef;

import DemoBlaze.context.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class CucumberHooks {

    private final TestContext context;

    public CucumberHooks(TestContext context) {
        this.context = context;
    }

    @Before("@web")
    public void setUp() {
        context.getDriver();
    }

    @After("@web")
    public void tearDown() {
        context.quitDriver();
    }
}