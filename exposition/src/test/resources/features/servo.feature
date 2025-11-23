Feature: Position servo
  As a client
  I want to position a servo to a specific angle
  So that the robot can perform precise movements

  Scenario: Position a servo successfully
    Given positioning servo "arm" to angle 90 will succeed
    When I position servo "arm" to angle 90
    Then the response status should be 200

  Scenario: Fail to position a servo
    Given positioning servo "wrist" to angle 180 will fail
    When I position servo "wrist" to angle 180
    Then the response status should be 500
