package com.ethercis.vehr;

import com.jayway.restassured.response.Response;
import cucumber.api.PendingException;
import cucumber.api.java.After;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.junit.Assert.assertTrue;

public class CompositionAPISteps {

    private final RestAPIBackgroundSteps backgroundSteps;
    private String body;
    private String compositionUid;

    public CompositionAPISteps(RestAPIBackgroundSteps pBackgroundSteps){
        backgroundSteps = pBackgroundSteps;
    }

    @When("^Flat json file ([a-zA-Z \\-\\.0-9]+\\.json) with template id ([a-zA-Z \\-\\.0-9]+) is committed to service$")
    public void flatJsonFileIsCommittedToService(String pTemplateFileName, String pTemplateId) throws Exception {
        Path jsonFilePath =
            Paths
                .get(backgroundSteps.resourcesRootPath + "/test_data/" + pTemplateFileName);
        byte[] fileContents = Files.readAllBytes(jsonFilePath);

        Response commitCompositionResponse =
            given()
                .header(backgroundSteps.secretSessionId, backgroundSteps.SESSION_ID_TEST_SESSION)
                .header(backgroundSteps.CONTENT_TYPE, backgroundSteps.CONTENT_TYPE_JSON)
            .content(fileContents)
            .when()
            .post("/rest/v1/composition?format=FLAT&templateId=" + pTemplateId)
            .then().statusCode(200).extract().response();

        compositionUid = commitCompositionResponse.body().jsonPath().getString("compositionUid");
    }

    @After
    public void cleanUp() throws Exception {
        backgroundSteps.launcher.stop();
    }

    @Then("^A composition id should be returned by the API$")
    public void aCompositionIdShouldBeReturnedByTheAPI() throws Throwable {
        assertTrue(compositionUid.split("::").length == 3);
    }
}
