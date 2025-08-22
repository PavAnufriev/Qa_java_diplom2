package ru.yandex.praktikum.stellarburgers.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.stellarburgers.constants.Endpoints;
import ru.yandex.praktikum.stellarburgers.models.LoginRequest;
import ru.yandex.praktikum.stellarburgers.models.User;

import static io.restassured.RestAssured.given;

public class UserSteps {

    @Step("Регистрация пользователя")
    public ValidatableResponse register(User body) {
        return given()
                .body(body)
                .when()
                .post(Endpoints.REGISTER)
                .then();
    }

    @Step("Вход пользователя в систему")
    public ValidatableResponse login(LoginRequest body) {
        return given()
                .body(body)
                .when()
                .post(Endpoints.LOGIN)
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse delete(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .when()
                .delete(Endpoints.USER)
                .then();
    }
}
