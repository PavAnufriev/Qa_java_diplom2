package ru.yandex.praktikum.stellarburgers.steps;

import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.stellarburgers.constants.Endpoints;
import ru.yandex.praktikum.stellarburgers.models.LoginRequest;
import ru.yandex.praktikum.stellarburgers.models.User;

import static io.restassured.RestAssured.given;

public class UserSteps {

    public ValidatableResponse register(User body) {
        return given()
                .body(body)
                .when()
                .post(Endpoints.REGISTER)
                .then();
    }

    public ValidatableResponse login(LoginRequest body) {
        return given()
                .body(body)
                .when()
                .post(Endpoints.LOGIN)
                .then();
    }

    public ValidatableResponse delete(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .when()
                .delete(Endpoints.USER)
                .then();
    }
}
