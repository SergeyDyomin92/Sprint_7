package ru.praktikumservices.qascooter;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikumservices.qascooter.costants.Url;
import ru.praktikumservices.qascooter.models.Courier;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;
import static ru.praktikumservices.qascooter.models.CourierGenerator.*;

public class AuthorizationCourierTests {

    CreateCourierTests createCourierTests = new CreateCourierTests();
    String id = "";

    @Before
    public void setUp() {
        RestAssured.baseURI = Url.URL;
    }

    Courier courier;

    @Test
    @DisplayName("createNewCourier")
    @Description("Успешная авторизация курьера")
    public void authorizationCourier() {
        courier = randomCourierForAuthorization();
        Response response = createCourierTests.sendPostRequestV1Courier(courier);
        sendPostRequestV1CourierLogin(courier);

        checkResponseStatusCodeIs(response, 201);
        response.body().prettyPeek();
    }

    @Test
    @DisplayName("checkCourierId")
    @Description("Проверка наличия id курьера при авторизации")
    public void checkCourierIdIsNotEmpty() {
        courier = randomCourierForAuthorization();
        Response response = createCourierTests.sendPostRequestV1Courier(courier);

        sendPostRequestV1CourierLogin(courier);
        checkResponseIdIsNotEmpty(courier);
        response.body().prettyPeek();
    }

    @Test
    @DisplayName("authorizationCourierWithoutLogin")
    @Description("Невозможность авторизации курьера без логина")
    public void authorizationCourierWithoutLogin() {
        courier = randomCourierForAuthorizationWithoutLogin();
        Response authorizationResponse = sendPostRequestV1CourierLogin(courier);
        checkResponseStatusCodeIs(authorizationResponse, 400);
        checkResponseKeyAndValueAre(authorizationResponse, "message", "Недостаточно данных для входа");
        authorizationResponse.body().prettyPeek();
    }

    @Test
    @DisplayName("authorizationCourierWithoutPassword")
    @Description("Невозможность авторизации курьера без пароля")
    public void authorizationCourierWithoutPassword() {
        courier = randomCourierForAuthorizationWithoutPassword();
        Response authorizationResponse = sendPostRequestV1CourierLogin(courier);
        checkResponseStatusCodeIs(authorizationResponse, 400);
        checkResponseKeyAndValueAre(authorizationResponse, "message", "Недостаточно данных для входа");
        authorizationResponse.body().prettyPeek();
    }

    @Test
    @DisplayName("authorizationCourierWithWrongLogin")
    @Description("Невозможность авторизации курьера с невалидным логином")
    public void authorizationCourierWithWrongLogin() {
        courier = randomCourierForAuthorization();
        Response authorizationResponse = sendPostRequestV1CourierLogin(courier
                .withLogin("WrongTestLogin")
                .withPassword(courier.getPassword()));
        checkResponseStatusCodeIs(authorizationResponse, 404);
        checkResponseKeyAndValueAre(authorizationResponse, "message", "Учетная запись не найдена");
        authorizationResponse.body().prettyPeek();
    }

    @Test
    @DisplayName("authorizationCourierWithWrongPassword")
    @Description("Невозможность авторизации курьера с невалидным паролем")
    public void authorizationCourierWithWrongPassword() {
        courier = randomCourierForAuthorization();
        Response authorizationResponse = sendPostRequestV1CourierLogin(courier
                .withLogin(courier.getLogin())
                .withPassword("WrongTestPassword"));
        checkResponseStatusCodeIs(authorizationResponse, 404);
        checkResponseKeyAndValueAre(authorizationResponse, "message", "Учетная запись не найдена");
        authorizationResponse.body().prettyPeek();
    }

    @Test
    @DisplayName("authorizationCourierWithWrongLoginAndPassword")
    @Description("Невозможность авторизации курьера с невалидными логином и паролем")
    public void authorizationCourierWithWrongLoginAndPassword() {
        courier = randomCourierForAuthorization();
        Response authorizationResponse = sendPostRequestV1CourierLogin(courier
                .withLogin("WrongTestLogin")
                .withPassword("WrongTestPassword"));
        checkResponseStatusCodeIs(authorizationResponse, 404);
        checkResponseKeyAndValueAre(authorizationResponse, "message", "Учетная запись не найдена");
        authorizationResponse.body().prettyPeek();
    }


    //ШАГИ ДЛЯ МЕТОДА АВТОРИЗАЦИИ КУРЬЕРА
    @Step("Отправить POST запрос /api/v1/courier/login")
    public Response sendPostRequestV1CourierLogin(Courier courier) {
        return given().header("Content-type", "application/json").log().body().body(courier).post("/api/v1/courier/login");
    }

    @Step("Проверить статус-код ответа")
    public void checkResponseStatusCodeIs(Response response, int code) {
        response.then().assertThat().statusCode(code);
    }

    @Step("Проверить ключ и значение ответа")
    public void checkResponseKeyAndValueAre(Response response, String key, Object value) {
        response.then().assertThat().body(key, equalTo(value));
    }

    @Step("Получить id курьера")
    public String getResponseId(Courier courier) {
        given().header("Content-type", "application/json").body(courier).post("/api/v1/courier/login")
                .then().extract().body().path("id");
        return id;
    }

    @Step("Проверить id курьера")
    public void checkResponseIdIsNotEmpty(Courier courier) {
        MatcherAssert.assertThat(getResponseId(courier), notNullValue());
    }

    @Step("Отправить DELETE запрос /api/v1/courier/login")
    public Response sendDeleteRequestV1Courier(Courier courier) {
        String id = getResponseId(courier);
        return given().header("Content-type", "application/json").log().body().body(courier).delete(format("/api/v1/courier/%s", id));
    }

    @After
    public void tearDown() {
        if (courier != null) {
            sendDeleteRequestV1Courier(courier);
            System.out.println("Тестовый курьер удален");
        }
    }
}
