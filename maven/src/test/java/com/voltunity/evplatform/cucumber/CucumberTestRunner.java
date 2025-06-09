package com.voltunity.evplatform.cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    plugin = {"pretty"},
    features = "classpath:com/voltunity/evplatform/cucumber/features",
    glue = {"com.voltunity.evplatform.cucumber", "com.voltunity.evplatform.cucumber.stepdefs"}
)
public class CucumberTestRunner {
}