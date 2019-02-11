@controller @read
Feature: Reading a record from the registry

  Scenario: Reading a record which doesn't exist
    Given a non existent record id
    And a valid read auth token
    When retrieving the record from the registry
    Then read record issuing should be unsuccessful
    And read record error message is Invalid id

  Scenario: Reading a record which does exist
    Given add a record from the registry for read
    And a valid read auth token
    When retrieving the record from the registry with valid id
    Then record retrieval should be successful
    And the read record should match actual record