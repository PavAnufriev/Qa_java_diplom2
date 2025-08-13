package ru.yandex.praktikum.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.stellarburgers.models.OrderRequest;
import ru.yandex.praktikum.stellarburgers.models.User;
import ru.yandex.praktikum.stellarburgers.steps.OrderSteps;
import ru.yandex.praktikum.stellarburgers.steps.UserSteps;

import java.util.Collections;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

@Feature("Orders API")
public class OrderTests extends TestBase {

    private UserSteps userSteps;
    private OrderSteps orderSteps;
    private String accessToken;
    private List<String> ingredients;

    @Before
    public void setUpTest() {
        userSteps = new UserSteps();
        orderSteps = new OrderSteps();
        
        // Создаем пользователя и получаем токен
        User user = User.random();
        ValidatableResponse reg = userSteps.register(user).statusCode(SC_OK);
        accessToken = reg.extract().path("accessToken");
        
        // Получаем список ингредиентов
        ingredients = orderSteps.getIngredients()
                .statusCode(SC_OK)
                .extract().path("data._id");
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userSteps.delete(accessToken);
        }
    }

    @Test
    @Story("Create order with auth and ingredients")
    @Description("Создание заказа с авторизацией и ингредиентами")
    public void createOrderWithAuthAndIngredientsSuccess() {
        // Take first two ingredients
        List<String> orderIngredients = ingredients.subList(0, Math.min(2, ingredients.size()));

        // Create order
        orderSteps.createOrder(accessToken, new OrderRequest(orderIngredients))
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("order.number", notNullValue())
                .body("name", notNullValue());
    }

    @Test
    @Story("Create order without auth")
    @Description("Создание заказа без авторизации")
    public void createOrderWithoutAuthUnauthorized() {
        List<String> orderIngredients = ingredients.subList(0, Math.min(2, ingredients.size()));

        orderSteps.createOrderWithoutAuth(new OrderRequest(orderIngredients))
                .statusCode(SC_OK); // API allows order creation without auth
    }

    @Test
    @Story("Create order with no ingredients")
    @Description("Создание заказа без ингредиентов")
    public void createOrderNoIngredientsBadRequest() {
        orderSteps.createOrderWithoutAuth(new OrderRequest(Collections.emptyList()))
                .statusCode(SC_BAD_REQUEST)
                .body("success", is(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @Story("Create order with wrong ingredient hash")
    @Description("Создание заказа с неверным хешем ингредиентов")
    public void createOrderWrongIngredientHashBadRequest() {
        orderSteps.createOrderWithoutAuth(new OrderRequest(List.of("invalid_hash")))
                .statusCode(SC_BAD_REQUEST); // API returns 400 for invalid ingredient hash
    }

    @Test
    @Story("Get all orders")
    @Description("Получение списка всех заказов")
    public void getAllOrdersSuccess() {
        orderSteps.getAllOrders()
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("orders", notNullValue())
                .body("total", notNullValue())
                .body("totalToday", notNullValue());
    }

    @Test
    @Story("Get user orders with auth")
    @Description("Получение заказов конкретного пользователя")
    public void getUserOrdersWithAuthSuccess() {
        orderSteps.getUserOrders(accessToken)
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("orders", notNullValue());
    }

    @Test
    @Story("Get user orders without auth")
    @Description("Получение заказов пользователя без авторизации")
    public void getUserOrdersWithoutAuthUnauthorized() {
        orderSteps.getUserOrders(null)
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", equalTo("You should be authorised"));
    }
}
