package ru.yandex.praktikum.stellarburgers.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.stellarburgers.constants.Endpoints;
import ru.yandex.praktikum.stellarburgers.models.OrderRequest;

import static io.restassured.RestAssured.given;

public class OrderSteps {

    @Step("Получение списка ингредиентов")
    public ValidatableResponse getIngredients() {
        return given()
                .when()
                .get(Endpoints.INGREDIENTS)
                .then();
    }

    @Step("Создание заказа с авторизацией")
    public ValidatableResponse createOrder(String token, OrderRequest body) {
        return given()
                .header("Authorization", token)
                .body(body)
                .when()
                .post(Endpoints.ORDERS)
                .then();
    }

    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrderWithoutAuth(OrderRequest body) {
        return given()
                .body(body)
                .when()
                .post(Endpoints.ORDERS)
                .then();
    }

    @Step("Получение всех заказов")
    public ValidatableResponse getAllOrders() {
        return given()
                .when()
                .get(Endpoints.ALL_ORDERS)
                .then();
    }

    @Step("Получение заказов пользователя")
    public ValidatableResponse getUserOrders(String token) {
        if (token != null) {
            return given()
                    .header("Authorization", token)
                    .when()
                    .get(Endpoints.ORDERS)
                    .then();
        } else {
            return given()
                    .when()
                    .get(Endpoints.ORDERS)
                    .then();
        }
    }
}
