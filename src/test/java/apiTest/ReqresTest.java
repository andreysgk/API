package apiTest;
import dev.failsafe.internal.util.Assert;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pojos.UserData;

import java.util.List;

import static io.restassured.RestAssured.given;

public class ReqresTest{
    private static final String URL = "https://reqres.in/";
    @Test
    public void getUsers(){
        Specifications.installSpecification(Specifications.requestSpecification(URL),Specifications.responseSpecificationOK200());
        List<UserData> users = given()
//                .contentType(ContentType.JSON)
                .when().get("api/users?page=2")
                .then().log().all()
                .extract().body().jsonPath().getList("data", UserData.class);
        users.forEach(x-> Assertions.assertTrue(x.getAvatar().contains(x.getId().toString())));
//        users.forEach(x-> Assertions.assertTrue(x.getEmail().endsWith("reqres.in")));

    }

}
