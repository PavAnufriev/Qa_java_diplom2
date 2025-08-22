package ru.yandex.praktikum.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.stellarburgers.steps.OrderSteps;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

@Feature("Ingredients API")
public class IngredientsTests extends TestBase {

    private OrderSteps orderSteps;

    @Before
    public void setUpTest() {
        orderSteps = new OrderSteps();
    }

    @Test
    @Story("Get ingredients list")
    @Description("Получение списка доступных ингредиентов")
    public void getIngredientsSuccess() {
        orderSteps.getIngredients()
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("data", notNullValue())
                .body("data", hasSize(greaterThan(0)));
    }
}
