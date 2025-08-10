package ru.yandex.praktikum.stellarburgers.steps;

import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.stellarburgers.models.OrderRequest;

import static io.restassured.RestAssured.given;

public class OrderSteps {

    public ValidatableResponse getIngredients() {
        return given()
                .when()
                .get("/api/ingredients")
                .then();
    }

    public ValidatableResponse createOrder(String token, OrderRequest body) {
        return given()
                .header("Authorization", token)
                .body(body)
                .when()
                .post("/api/orders")
                .then();
    }

    public ValidatableResponse createOrderWithoutAuth(OrderRequest body) {
        return given()
                .body(body)
                .when()
                .post("/api/orders")
                .then();
    }
}
