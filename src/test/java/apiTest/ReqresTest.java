package apiTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import pojos.userPojo;

import java.util.List;

import static io.restassured.RestAssured.given;

public class ReqresTest{
    @Test
    public void getUsers(){
        List<userPojo> users = given()
                .baseUri("https://reqres.in/api/")
                .basePath("/users")
                .contentType(ContentType.JSON)
                .when().get()
                .then()
                .statusCode(200)
                .extract().jsonPath().getList("data", userPojo.class);

    }

}
