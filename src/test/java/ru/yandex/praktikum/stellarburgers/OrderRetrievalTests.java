package ru.yandex.praktikum.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.stellarburgers.models.User;
import ru.yandex.praktikum.stellarburgers.steps.OrderSteps;
import ru.yandex.praktikum.stellarburgers.steps.UserSteps;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

@Feature("Order Retrieval API")
public class OrderRetrievalTests extends TestBase {

    private UserSteps userSteps;
    private OrderSteps orderSteps;
    private String accessToken;

    @Before
    public void setUpTest() {
        userSteps = new UserSteps();
        orderSteps = new OrderSteps();
        
        // Создаем пользователя и получаем токен
        User user = User.random();
        ValidatableResponse reg = userSteps.register(user).statusCode(SC_OK);
        accessToken = reg.extract().path("accessToken");
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userSteps.delete(accessToken);
        }
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
