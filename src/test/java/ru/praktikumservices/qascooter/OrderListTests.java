package ru.praktikumservices.qascooter;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import ru.praktikumservices.qascooter.costants.Url;
import ru.praktikumservices.qascooter.models.Courier;
import ru.praktikumservices.qascooter.utils.Utils;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.hamcrest.Matchers.equalTo;
import static ru.praktikumservices.qascooter.models.CourierGenerator.randomCourierForAuthorization;

public class OrderListTests {
    Utils utils = new Utils();
    Courier courier;
    AuthorizationCourierTests authorizationCourierTests = new AuthorizationCourierTests();
    CreateCourierTests createCourierTests = new CreateCourierTests();

    @Before
    public void setUp() {
        RestAssured.baseURI = Url.URL;
    }

    @Test
    @DisplayName("listOfNewCourierOrders")
    @Description("Получение списка заказов нового курьера")
    @Issue("QASCOOTER-2")//найдено расхождение реализации с апи-документацией, заведен баг.
    public void listOfNewCourierOrders() {
        courier = randomCourierForAuthorization();
        createCourierTests.sendPostRequestV1Courier(courier);

        authorizationCourierTests.sendPostRequestV1CourierLogin(courier);
        authorizationCourierTests.checkResponseIdIsNotEmpty(courier);
        String id = authorizationCourierTests.getResponseId(courier);
        Response response = sendGetRequestOrdersCount(id);
        checkResponseStatusCodeIs(response, 200);
        checkResponseKeyAndValueAre(response, "id", id);
        checkResponseKeyAndValueAre(response, "ordersCount", "0");
    }

    @Test
    @DisplayName("listOfNewOrdersWithoutId")
    @Description("Получение списка заказов без указания id курьера")
    @Issue("QASCOOTER-3")//найдено расхождение реализации с апи-документацией, заведен баг.
    public void listOfNewOrdersWithoutId() {
        courier = randomCourierForAuthorization();
        String id = "";
        Response response = sendGetRequestOrdersCount(id);
        checkResponseStatusCodeIs(response, 400);
        checkResponseKeyAndValueAre(response, "message", "Недостаточно данных для поиска");
        System.out.println(response.body().prettyPeek().toString());
    }

    @Test
    @DisplayName("listOfNewOrdersWithoutId")
    @Description("Получение списка заказов c несуществующим id курьера")
    @Issue("QASCOOTER-4")//найдено расхождение реализации с апи-документацией, заведен баг.
    public void listOfNewOrdersWithNotExistId() {
        courier = randomCourierForAuthorization();
        String id = utils.phoneNumber;
        Response response = sendGetRequestOrdersCount(id);
        checkResponseStatusCodeIs(response, 404);
        checkResponseKeyAndValueAre(response, "message", "Курьер не найден");
    }


    //ШАГИ ДЛЯ МЕТОДА СПИСКА ЗАКАЗОВ КУРЬЕРА
    @Step("Отправить GET запрос /api/v1/courier/79819506677/ordersCount")
    public Response sendGetRequestOrdersCount(String id) {
        return given().header("Content-type", "application/json").get(format("/api/v1/courier/%s/ordersCount", id));
    }

    @Step("Проверить статус-код ответа")
    public void checkResponseStatusCodeIs(Response response, int code) {
        response.then().assertThat().statusCode(code);
    }

    @Step("Проверить ключ и значение ответа")
    public void checkResponseKeyAndValueAre(Response response, String key, Object value) {
        response.then().assertThat().body(key, equalTo(value));
    }

}
