import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;

@RunWith(Parameterized.class)
public class CreateOrderTest {

    private final Order order;

    public CreateOrderTest(String firstName, String lastName, String address, String metroStation,
                           String phone, Integer rentTime, String deliveryDate, String comment, String[] color) {
        this.order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        }

    @Parameterized.Parameters
    public static Object[][] getParamsForCreateOrderTest() {
        return new Object[][] {
                {"Даниил", "Меньшиков", "Москва", "Сокольники",
                "89211234567", 5, "2022-12-31", "Без комментариев", new String[]{}},
                {"Ольга", "Гюндель", "Москва", "4", "89211234568", 6, "2022-12-29", "No comments!", new String[]{"BLACK"}},
                {"Дмитрий", "Лапкин", "Москва", "6", "89211234512", 8, "2022-12-27", "No comments!", new String[]{"GREY"}},
                {"Петр", "Великий", "Москва", "14", "89211234536", 10, "2022-12-20", "No comments!", new String[]{"BLACK", "GREY"}}
        };
    }

    @Test
    public void createOrderViaApiTest() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        Response response =
        given()
                .header("Content-type", "application/json; charset=utf-8")
                .body(order)
                .post("/api/v1/orders");

        Assert.assertTrue(response.asString().contains("track"));
    }


}

