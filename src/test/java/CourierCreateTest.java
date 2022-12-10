import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class CourierCreateTest {

    Courier courier;

    @Before
    public void setUp() { RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru"; }
    @Before
    public void createCourierData() {
        courier = new Courier("danmen", "12345", "Daniel");
    }

    @Test
    @DisplayName("Check possibility to create courier")
    public void createCourierApi() {
        Response response =
        given()
                .header("Content-type", "application/json")
                .body(courier)
                .post("/api/v1/courier");

        Assert.assertEquals(response.statusCode(), 201);
        Assert.assertEquals(response.asString().substring(1, 10), "\"ok\":true");
    }

    @Test
    @DisplayName("Check possibility to create two identical courier")
    public void createTwoIdenticalCourier() throws InterruptedException {
            given()
                .header("Content-type", "application/json")
                .body(courier)
                .post("/api/v1/courier");

        Thread.sleep(5000);

        Response response =
            given()
                .header("Content-type", "application/json")
                .body(courier)
                .post("/api/v1/courier");

        Assert.assertEquals(response.statusCode(), 409);
    }

    @Test
    @DisplayName("Check possibility to create courier without login")
    public void createCourierWithoutLogin() {

        courier.setLogin("");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(courier)
                        .post("/api/v1/courier");

        Assert.assertEquals(response.statusCode(), 400);
    }

    @Test
    @DisplayName("Check possibility to create courier without password")
    public void createCourierWithoutPassword() {

        courier.setPassword("");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(courier)
                        .post("/api/v1/courier");

        Assert.assertEquals(response.statusCode(), 400);
    }

    @Test
    @DisplayName("Check possibility to create courier")
    public void createCourierWithRequiredFields() {
        courier.setFirstName("");

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(courier)
                        .post("/api/v1/courier");

        Assert.assertEquals(response.statusCode(), 201);
    }

    @After
    public void deleteCourierFromDataBase() {
        if (courier.getLogin() == "" || courier.getPassword() == "") {
            return;}

        int id =
                given()
                        .header("Content-type", "application/json")
                        .body(courier)
                        .when()
                        .post("/api/v1/courier/login")
                        .then()
                        .extract()
                        .path("id");

        given().delete("/api/v1/courier/" + id);
    }
}
