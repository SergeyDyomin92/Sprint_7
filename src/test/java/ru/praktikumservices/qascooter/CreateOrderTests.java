package ru.praktikumservices.qascooter;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikumservices.qascooter.costants.Url;
import ru.praktikumservices.qascooter.models.Order;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static ru.praktikumservices.qascooter.models.OrderGenerator.*;

@RunWith(Parameterized.class)
public class CreateOrderTests {

    private final Order order;


    public CreateOrderTests(Order order) {
        this.order = order;
    }

    @Parameterized.Parameters
    public static Object[][] getTestColorData() {
        return new Object[][]{
                {randomOrder()},
                {randomOrderBlack()},
                {randomOrderGray()},
                {randomOrderBlackAndGray()},
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = Url.URL;
    }

    @Test
    @DisplayName("createNewOrder")
    @Description("Успешное создание заказа")
    public void createNewOrder() {
        Response response = sendPostRequestV1Orders(order);
        checkResponseStatusCodeIs(response, 201);
        checkResponseIdIsNotEmpty(order);
    }

    //ШАГИ ДЛЯ МЕТОДА СОЗДАНИЯ ЗАКАЗА
    @Step("Отправить POST запрос /api/v1/orders")
    public Response sendPostRequestV1Orders(Order order) {
        return given().header("Content-type", "application/json").body(order).post("/api/v1/orders");
    }

    @Step("Проверить статус-код ответа")
    public void checkResponseStatusCodeIs(Response response, int code) {
        response.then().assertThat().statusCode(code);
    }

    @Step("Получить track заказа")
    public String getResponseTrack(Order order) {
        return given().header("Content-type", "application/json").body(order).post("/api/v1/orders")
                .then().extract().body().path("track").toString();
    }

    @Step("Проверить track заказа")
    public void checkResponseIdIsNotEmpty(Order order) {
        MatcherAssert.assertThat(getResponseTrack(order), notNullValue());
    }
}
