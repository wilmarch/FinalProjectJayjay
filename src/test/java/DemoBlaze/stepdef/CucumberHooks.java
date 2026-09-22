package DemoBlaze.stepdef;

import DemoBlaze.base.BaseTest;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class CucumberHooks extends BaseTest {

    @Before
    public void setUp() {
        getDriver();
    }

    @After
    public void tearDown() {
        quitDriver();
    }
}