package DemoBlaze.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/web",
        glue = {"DemoBlaze.stepdef"},
        tags = "@web",
        plugin = {
                "pretty",
                "html:build/reports/cucumber-web.html",
                "json:build/reports/cucumber-web.json"
        },
        monochrome = true
)
public class WebTestRunner {
}