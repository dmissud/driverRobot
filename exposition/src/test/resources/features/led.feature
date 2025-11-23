Feature: Control LED
  In order to indicate robot states
  As a client
  I want to turn LEDs on and off

  Scenario: Turn on an LED successfully
    Given the Arduino is ready
    When I set LED "red" to "true"
    Then the response status should be 200

  Scenario: Fail to turn off an LED
    Given controlling LED "green" to "false" will fail
    When I try to set LED "green" to "false"
    Then the response status should be 500
