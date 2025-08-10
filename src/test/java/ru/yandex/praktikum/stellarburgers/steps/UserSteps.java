package ru.yandex.praktikum.stellarburgers.steps;

import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.stellarburgers.models.LoginRequest;
import ru.yandex.praktikum.stellarburgers.models.User;

import static io.restassured.RestAssured.given;

public class UserSteps {

    public ValidatableResponse register(User body) {
        return given()
                .body(body)
                .when()
                .post("/api/auth/register")
                .then();
    }

    public ValidatableResponse login(LoginRequest body) {
        return given()
                .body(body)
                .when()
                .post("/api/auth/login")
                .then();
    }

    public ValidatableResponse delete(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .when()
                .delete("/api/auth/user")
                .then();
    }
}
