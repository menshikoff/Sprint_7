import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class CourierLoginTest {
    Courier courier;
    Integer id;

    @Before
    public void setUp() { RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru"; }
    @Before
    public void createCourier() {
        courier = new Courier("danmen", "12345", "Daniel");
        given()
                .header("Content-type", "application/json")
                .body(courier)
                .post("/api/v1/courier");
    }

    @Test
    @DisplayName("Check possibility of courier to login")
    public void courierCanLoginToSystem() {
        Response response =
        given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier/login");

        response.then().statusCode(200);
        Assert.assertTrue(response.asString().contains("id"));
    }

    @Test
    @DisplayName("Check possibility of courier to login with wrong login")
    public void courierLoginWithWrongLogin() {
        courier.setLogin("wertyuiouyhg");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(courier)
                        .when()
                        .post("/api/v1/courier/login");

        response.then().statusCode(404);
    }

    @Test
    @DisplayName("Check possibility of courier to login with wrong password")
    public void courierLoginWithWrongPassword() {
        courier.setPassword("wertyuiouyhg");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(courier)
                        .when()
                        .post("/api/v1/courier/login");

        response.then().statusCode(404);
    }

    @Test
    @DisplayName("Check possibility of courier to login without sending login")
    public void courierLoginWithoutLoginField() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body("{\"password\" : \"12345\"}")
                        .when()
                        .post("/api/v1/courier/login");

        response.then().statusCode(400);
    }

    @Test
    @DisplayName("Check possibility of courier to login without sending password")
    public void courierLoginWithoutPasswordField() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body("{\"login\": \"danmen\"}")
                        .when()
                        .post("/api/v1/courier/login");

        response.then().statusCode(504);
    }

    @Test
    @DisplayName("Check possibility of courier to login with nonexistent login")
    public void courierLoginWithNonexistentLogin() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body("{\"login\": \"danmen-non\", \"password\" : \"12345\"}")
                        .when()
                        .post("/api/v1/courier/login");

        response.then().statusCode(404);
    }

    @After
    public void deleteCourierFromDataBase() {
        if (id == null) {
            return;
        }
        given().delete("/api/v1/courier/" + id);
    }
}
