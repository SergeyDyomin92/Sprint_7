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
import static org.hamcrest.Matchers.equalTo;
import static ru.praktikumservices.qascooter.models.CourierGenerator.*;

public class CreateCourierTests {

    Utils utils = new Utils();

    Courier courier = randomCourier();

    Courier courierWithoutLogin = randomCourierWithoutLogin();
    Courier courierWithoutPassword = randomCourierWithoutPassword();

    @Before
    public void setUp() {
        RestAssured.baseURI = Url.URL;
    }

    @Test
    @DisplayName("createNewCourier")
    @Description("Успешное создание нового курьера")
    public void createNewCourier() {
        Response response = sendPostRequestV1Courier(courier);
        checkResponseStatusCodeIs(response, 201);
        checkResponseKeyAndValueAre(response, "ok", true);
        response.body().prettyPeek();
    }

    @Test
    @DisplayName("createExistsCourier")
    @Description("Невозможность создания существующего курьера")
    public void createExistsCourierStatusCode() {
        Response firstResponse = sendPostRequestV1Courier(courier);
        Response secondResponse = sendPostRequestV1Courier(courier);
        checkResponseStatusCodeIs(secondResponse, 409);
        checkResponseKeyAndValueAre(secondResponse, "message", "Этот логин уже используется. Попробуйте другой.");
    }

    @Test
    @DisplayName("createCourierWithoutLogin")
    @Description("Невозможность создания курьера без логина")
    public void createCourierWithoutLogin() {
        Response response = sendPostRequestV1Courier(courierWithoutLogin);
        checkResponseStatusCodeIs(response, 400);
        checkResponseKeyAndValueAre(response, "message", "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("createCourierWithoutPassword")
    @Description("Невозможность создания курьера без пароля")
    public void createCourierWithoutPassword() {
        Response response = sendPostRequestV1Courier(courierWithoutPassword);
        checkResponseStatusCodeIs(response, 400);
        checkResponseKeyAndValueAre(response, "message", "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("createCourierWithExistLogin")
    @Description("Невозможность создания курьера с уже существующим логином")
    @Issue("QASCOOTER-1")//найдено расхождение реализации с апи-документацией, заведен баг.
    public void createCourierWithExistLogin() {
        Response firstResponse = sendPostRequestV1Courier(courier);
        Response secondResponse = sendPostRequestV1Courier(courier
                .withLogin(courier.getLogin())
                .withPassword(utils.password)
                .withFirstName(utils.firstName));

        checkResponseStatusCodeIs(secondResponse, 409);
        checkResponseKeyAndValueAre(secondResponse, "message", "Этот логин уже используется");
    }


    //ШАГИ ДЛЯ МЕТОДА СОЗДАНИЯ КУРЬЕРА
    @Step("Отправить POST запрос /api/v1/courier")
    public Response sendPostRequestV1Courier(Courier courier) {
        return given().header("Content-type", "application/json").log().body().body(courier).post("/api/v1/courier");
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

