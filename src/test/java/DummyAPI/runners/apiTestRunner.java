package DummyAPI.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/api",
        glue = "DummyAPI.stepdef",
        tags = "@api",
        plugin = {
                "pretty",
                "html:build/reports/cucumber-api.html",
                "json:build/reports/cucumber-api.json"
        }
)
public class apiTestRunner {
}