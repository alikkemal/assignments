package services;

import io.restassured.response.ValidatableResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static config.Config.API_BASE_PATH;
import static io.restassured.RestAssured.given;

public class PetService {
    private static final Logger LOGGER = LogManager.getLogger(PetService.class);

    public ValidatableResponse addPet(int id, int categoryId, String categoryName, String name, String photoUrl, int tagsId, String tagsName, String status) {

        ValidatableResponse response = given()
                .contentType("application/json")
                .body("{\n" + "\"id\": \"" + id + "\", \n" + " \"category\": {\"id\": \"" + categoryId + "\", \n" + " \"name\": \"" + categoryName + "\"}, \n" + " \"name\": \"" + name + "\", \n" + " \"photoUrls\" : [ \"" + photoUrl + "\"], \n" + " \"tags\": [{\"id\": \"" + tagsId + "\", \n" + " \"name\": \"" + tagsName + "\"}], \n" + " \"status\": \"" + status + "\"}")
                .when()
                .post(API_BASE_PATH)
                .then();
        LOGGER.info("Add Pet: ");
        response.extract().response().prettyPrint();
        return response;
    }

    public ValidatableResponse updatePet(int id, int categoryId, String categoryName, String name, String photoUrl, int tagsId, String tagsName, String status) {

        ValidatableResponse response = given()
                .contentType("application/json")
                .body("{\n" + "\"id\": \"" + id + "\", \n" + " \"category\": {\"id\": \"" + categoryId + "\", \n" + " \"name\": \"" + categoryName + "\"}, \n" + " \"name\": \"" + name + "\", \n" + " \"photoUrls\" : [ \"" + photoUrl + "\"], \n" + " \"tags\": [{\"id\": \"" + tagsId + "\", \n" + " \"name\": \"" + tagsName + "\"}], \n" + " \"status\": \"" + status + "\"}")
                .when()
                .put(API_BASE_PATH)
                .then();
        LOGGER.info("Update Pet: ");
        response.extract().response().prettyPrint();
        return response;
    }

    public ValidatableResponse getPet(int id) {

        ValidatableResponse response = given()
                .contentType("application/json")
                .when()
                .get(API_BASE_PATH + "/" + id)
                .then();
        LOGGER.info("Get Pet: ");
        response.extract().response().prettyPrint();
        return response;
    }

    public ValidatableResponse deletePet(int id) {

        ValidatableResponse response = given()
                .contentType("application/json")
                .body("{\n" + "\"id\": \"" + id + "\"}")
                .when()
                .delete(API_BASE_PATH + "/" + id)
                .then();
        LOGGER.info("Delete Pet: ");
        response.extract().response().prettyPrint();
        return response;
    }
}
