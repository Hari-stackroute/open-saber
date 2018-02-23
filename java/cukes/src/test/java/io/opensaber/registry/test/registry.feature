#Author: jyotsna.raveendran@tarento.com

@controller
Feature: Inserting a record into the registry

  Scenario: Inserting first valid record
    Given First input data and base url are valid
    When Inserting first valid record into the registry
    Then Response for first valid record is success

  Scenario: Inserting a duplicate record
    Given Valid duplicate data
    When Inserting a duplicate record into the registry
    Then Response for duplicate record is Cannot insert duplicate record

  Scenario: Inserting second valid record into the registry
    Given Second input data and base url are valid
    When Inserting second valid record into the registry
    Then Response for second valid record is success

  Scenario: Inserting record with invalid type
    Given Base url is valid but input data has invalid type
    When Inserting record with invalid type into the registry
    Then Response for invalid record is Failed to insert due to invalid type
    