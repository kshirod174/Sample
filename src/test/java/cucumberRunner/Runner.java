package cucumberRunner;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;
import cucumber.api.testng.CucumberFeatureWrapper;
import cucumber.api.testng.TestNGCucumberRunner;

@Test
@CucumberOptions(
        features = "src/test/resources/feature",
        glue = {"functionLibrary"},
        tags = {"@example"}
        
        )


public class Runner extends AbstractTestNGCucumberTests {

	
}
