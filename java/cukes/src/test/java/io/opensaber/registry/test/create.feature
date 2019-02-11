@controller @create
Feature: Inserting a record into the registry

  Scenario: Issuing a valid record
    Given a valid record
    And a valid auth token
    When issuing the record into the registry
    Then record issuing should be successful

  Scenario: Inserting second valid record into the registry
      Given a valid record
      And a valid auth token
      When another record issued into the registry
      Then record issuing should be successful