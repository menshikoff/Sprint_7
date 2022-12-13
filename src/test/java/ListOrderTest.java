import org.junit.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class ListOrderTest {

    @Test
    public void checkThatListOfOrdersIsInPlace() {
        baseURI = "https://qa-scooter.praktikum-services.ru";

        given().get("/api/v1/orders").then().assertThat().body("orders", notNullValue());
    }
}
