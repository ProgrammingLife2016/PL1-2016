package io.github.programminglife2016.pl1_2016.cucumber;

import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty"},
        snippets = SnippetType.CAMELCASE,
        features = "classpath:features")
public class CucumberTest {
}
