package api;

import io.restassured.response.ValidatableResponse;
import junitparams.FileParameters;
import org.junit.Test;
import services.PetService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PetTest {

    @Test
    @FileParameters("src/main/resources/data.csv")
    public void shouldAddPet() {
        int id = 572022;
        int categoryId = 1;
        String categoryName = "Evcil Hayvan";
        String name = "Pita";
        String photoUrl = "url:";
        int tagsId = 1;
        String tagsName = "Kedi";
        String status = "Available";

        PetService petService = new PetService();
        ValidatableResponse response = petService.addPet(id, categoryId, categoryName, name, photoUrl, tagsId, tagsName, status);
        int responseCode = response.extract().statusCode();

        assertThat("When user should tries to add ", responseCode, is(200));
    }

    @Test
    public void shouldUpdatePet() {
        int id = 572022;
        int categoryId = 1;
        String categoryName = "Evcil Hayvan";
        String name = "Pita";
        String photoUrl = "url:";
        int tagsId = 1;
        String tagsName = "Kedi";
        String status = "Not Available";

        PetService petService = new PetService();
        ValidatableResponse response = petService.updatePet(id, categoryId, categoryName, name, photoUrl, tagsId, tagsName, status);
        int responseCode = response.extract().statusCode();

        assertThat("When user should tries to update ", responseCode, is(200));
    }

    @Test
    public void shouldGetPet() {
        int id = 572022;

        PetService petService = new PetService();
        ValidatableResponse response = petService.getPet(id);
        int responseCode = response.extract().statusCode();

        assertThat("When user should tries to add ", responseCode, is(200));
        assertThat("When user should tries to add ", id, is(id));
    }

    @Test
    public void shouldNotGetPet() {
        int id = 57202211;

        PetService petService = new PetService();
        ValidatableResponse response = petService.getPet(id);
        int responseCode = response.extract().statusCode();
        String responseMessage = response.extract().path("message");

        assertThat("When user should tries to add ", responseCode, is(404));
        assertThat("When user should tries to update ", responseMessage, is("Pet not found"));
    }

    @Test
    public void shouldDeletePet() {
        int id = 572022;

        PetService petService = new PetService();
        ValidatableResponse response = petService.deletePet(id);
        int responseCode = response.extract().statusCode();

        assertThat("When user should tries to add ", responseCode, is(200));
    }

    @Test
    public void shouldNotDeletePet() {
        int id = 57202211;

        PetService petService = new PetService();
        ValidatableResponse response = petService.deletePet(id);
        int responseCode = response.extract().statusCode();

        assertThat("When user should tries to add ", responseCode, is(404));
    }
}
