package io.opensaber.registry.test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java8.En;
import io.opensaber.pojos.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ReadIntegrationTestsSteps extends RegistryTestBase implements En {

    private String id;
    private static final String READ_ENTITY = "read";
    private static final String READ_API_JSONLD = "read_api.jsonld";
    private static final String TEACHER_JSONLD = "teacher.jsonld";

   /* ReadIntegrationTestsSteps(){
        initialize();
    }*/

    @Before
    public void initializeData() {
        restTemplate = new RestTemplate();
        baseUrl = generateBaseUrl();
    }

    @Given("^a non existent record id$")
    public void a_non_existent_record_id() throws Exception {
        id = generateRandomId();
        setJsonld(READ_API_JSONLD);
        jsonld = jsonld.replace("osid_input",id);
    }

    @And("^a valid read auth token")
    public void setValidAuthToken() {
        setValidAuthHeader();
        assertNotNull(headers);
    }

    @When("^retrieving the record from the registry$")
    public void retrieving_the_record_from_the_registry() {
        response = callRegistryReadAPI();
    }

    @Given("^add a record from the registry for read$")
    public ResponseEntity<Response> add_a_record_from_the_registry_for_read() {
        setValidAuthToken();
        setJsonld(TEACHER_JSONLD);
        headers.setContentType(MediaType.APPLICATION_JSON);
        response = addEntity(jsonld,baseUrl + ADD_ENTITY, headers);
        return response;
    }

    @When("^retrieving the record from the registry with valid id$")
    public ResponseEntity<Response> retrieving_the_record_from_the_registry_with_valid_id() {
        ObjectMapper objectMapper = new ObjectMapper();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JsonNode osidNode = objectMapper.convertValue((HashMap)response.getBody().getResult(),JsonNode.class);
        String osidValue  = osidNode.get("Teacher").get("osid").textValue();
       // HashMap teacherMap = (HashMap) osidMap.get("Teacher");
        //teacherMap.get()
        setJsonld(READ_API_JSONLD);
        jsonld = jsonld.replace("osid_input","1-480dd4f7-a5bc-481d-bdf0-37b36b2f3deb");
        return callRegistryReadAPI();
    }

    /*Given("^retrieving the record from the registry with valid id$", () -> {
        HttpEntity<String> entity = new HttpEntity<>(jsonld,headers);
        response.getBody();
        ResponseEntity<Response> response = restTemplate.postForEntity(baseUrl + READ_ENTITY ,
                entity, Response.class);
        return response;
    });*/

    private ResponseEntity<Response> callRegistryReadAPI() {
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(jsonld,headers);
        ResponseEntity<Response> response = restTemplate.postForEntity(baseUrl + READ_ENTITY ,
                entity, Response.class);
        return response;
    }

    @Given("^a read response")
    public void validReadResponseFormat() {
        try {
            id = generateRandomId();
            setValidAuthHeader();
            response = callRegistryReadAPI();
        } catch (Exception e) {
            response = null;
        }
        assertNotNull(response);
    }

    @Then("^record should never have any associated audit info$")
    public void test_audit_record_unexpected_in_read() throws Exception {
        response = callRegistryReadAPI();
        Map<String, Object> map = (Map) response.getBody().getResult();
        if (map.containsKey("(?i)(?<= |^)audit(?= |$)")) {
            checkUnsuccessfulResponse();
        } else {
            checkSuccessfulResponse();
        }
    }

    @Then("^read record issuing should be successful")
    public void verifyReadSuccessfulResponse() throws IOException {
        checkSuccessfulResponse();
    }

    @Then("^read record issuing should be unsuccessful")
    public void verifyReadUnSuccessfulResponse() throws IOException {
        checkUnsuccessfulResponse();
    }

    @Then("^read record error message is (.*)")
    public void verifyReadErrorMessage(String message) throws JsonParseException, JsonMappingException, IOException {
        assertEquals(message, response.getBody().getParams().getErrmsg());
    }

    @Then("^record retrieval should be successful$")
    public void record_retrieval_should_be_successful() throws Exception {
        checkSuccessfulResponse();
    }

    @Then("^the read record should match actual record$")
    public void the_record_should_match() throws Exception {
        //checkForIsomorphicModel();
        //Need to decide library for checking actual and read data.
    }
}
