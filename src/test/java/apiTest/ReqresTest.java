package apiTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pojos.*;

import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class ReqresTest{
    private static final String URL = "https://reqres.in/";
    @Test
    @DisplayName("GET")
    public void getUsers(){
        Specifications.installSpecification(Specifications.requestSpecification(URL),Specifications.responseSpecificationOK200());
        List<UserData> users = given()
//                .contentType(ContentType.JSON)
                .when()
                .get("api/users?page=2")
                .then().log().all()
                .extract().body().jsonPath().getList("data", UserData.class);
        users.forEach(x-> Assertions.assertTrue(x.getAvatar().contains(x.getId().toString())));
//        users.forEach(x-> Assertions.assertTrue(x.getEmail().endsWith("reqres.in")));
    }

    @Test
    @DisplayName("POST")
    public void successRegistration(){
        Specifications.installSpecification(Specifications.requestSpecification(URL),Specifications.responseSpecificationOK200());
        Integer id = 4;
        String token = "QpwL5tke4Pnpja7X4";
        Registration user = new Registration("eve.holt@reqres.in", "pistol");
        SuccessReg successReg = given()
                .body(user)
                .when()
                .post("api/register")
                .then().log().all()
                .extract().as(SuccessReg.class);
        Assertions.assertNotNull(successReg.getId());
        Assertions.assertNotNull(successReg.getToken());
        Assertions.assertEquals(id, successReg.getId());
        Assertions.assertEquals(token, successReg.getToken());
    }

    @Test
    @DisplayName("POST")
    public void unSuccessRegistration(){
        Specifications.installSpecification(Specifications.requestSpecification(URL),Specifications.responseSpecificationError400());
        Registration user = new Registration("sydney@fife","");
        UnSuccessReg unSuccessReg = given()
                .body(user)
                .when()
                .post("api/register")
                .then().log().all()
                .extract().as(UnSuccessReg.class);
        Assertions.assertEquals("Missing password", unSuccessReg.getError());
    }

    @Test
    @DisplayName("GET, отсортированы ли года?")
    public void getResourсes(){
        Specifications.installSpecification(Specifications.requestSpecification(URL),Specifications.responseSpecificationOK200());
        List<Resourсes> resourсes = given()
                .when()
                .get("api/unknown")
                .then().log().all()
                .extract().body().jsonPath().getList("data", Resourсes.class);
        List<Integer> years = resourсes.stream().map(Resourсes::getYear).collect(Collectors.toList());
        List<Integer> sortYears = years.stream().sorted().collect(Collectors.toList());
        Assertions.assertEquals(sortYears, years);
//        System.out.println(years);
//        System.out.println(sortYears);
    }

}
