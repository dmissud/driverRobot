package org.dbs.robot.exposition.bdd;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dbs.robot.driver.arduino.ArduinoController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ArduinoStepDefinitions extends CucumberSpringConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArduinoController arduinoController;


    private MvcResult lastResult;

    @Given("the Arduino is ready")
    public void theArduinoIsReady() {
        reset(arduinoController);
        when(arduinoController.isReady()).thenReturn(true);
    }

    @Given("the Arduino is not ready")
    public void theArduinoIsNotReady() {
        reset(arduinoController);
        when(arduinoController.isReady()).thenReturn(false);
    }

    @When("I check the Arduino status")
    public void iCheckTheArduinoStatus() throws Exception {
        lastResult = mockMvc.perform(get("/api/arduino/status"))
                .andReturn();
    }

    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int httpStatus) throws Exception {
        mockMvc.perform(get("/does-not-matter")).andExpect(result -> {
            // no-op, using lastResult below
        });
        // Assert on the status from lastResult
        if (lastResult == null) throw new AssertionError("No previous HTTP result");
        int actual = lastResult.getResponse().getStatus();
        if (actual != httpStatus) {
            throw new AssertionError("Expected HTTP status " + httpStatus + " but was " + actual);
        }
    }

    @When("I set LED {string} to {string}")
    public void iSetLEDTo(String ledName, String state) throws Exception {
        boolean desired = Boolean.parseBoolean(state);
        when(arduinoController.controlLed(ledName, desired)).thenReturn(true);
        lastResult = mockMvc.perform(post("/api/arduino/led/{name}", ledName)
                        .param("state", String.valueOf(desired))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Given("controlling LED {string} to {string} will fail")
    public void controllingLEDWillFail(String ledName, String state) {
        boolean desired = Boolean.parseBoolean(state);
        reset(arduinoController);
        when(arduinoController.controlLed(ledName, desired)).thenReturn(false);
    }

    @When("I try to set LED {string} to {string}")
    public void iTryToSetLEDTo(String ledName, String state) throws Exception {
        boolean desired = Boolean.parseBoolean(state);
        lastResult = mockMvc.perform(post("/api/arduino/led/{name}", ledName)
                        .param("state", String.valueOf(desired))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Given("positioning servo {string} to angle {int} will succeed")
    public void positioningServoWillSucceed(String name, Integer angle) {
        reset(arduinoController);
        when(arduinoController.positionServo(name, angle)).thenReturn(true);
    }

    @Given("positioning servo {string} to angle {int} will fail")
    public void positioningServoWillFail(String name, Integer angle) {
        reset(arduinoController);
        when(arduinoController.positionServo(name, angle)).thenReturn(false);
    }

    @When("I position servo {string} to angle {int}")
    public void iPositionServoToAngle(String name, Integer angle) throws Exception {
        lastResult = mockMvc.perform(post("/api/arduino/servo/{name}/position", name)
                        .param("angle", String.valueOf(angle)))
                .andReturn();
    }
}
