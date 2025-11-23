Feature: Arduino status
  As a client of the robot system
  I want to know whether the Arduino controller is ready
  So that I can decide to send commands

  Scenario: Arduino is ready
    Given the Arduino is ready
    When I check the Arduino status
    Then the response status should be 200

  Scenario: Arduino is not ready
    Given the Arduino is not ready
    When I check the Arduino status
    Then the response status should be 503
