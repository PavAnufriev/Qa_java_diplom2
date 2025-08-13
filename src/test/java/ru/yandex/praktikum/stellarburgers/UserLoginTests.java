package ru.yandex.praktikum.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.stellarburgers.models.LoginRequest;
import ru.yandex.praktikum.stellarburgers.models.User;
import ru.yandex.praktikum.stellarburgers.steps.UserSteps;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

@Feature("User Login API")
public class UserLoginTests extends TestBase {

    private UserSteps userSteps;
    private String accessToken;

    @Before
    public void setUpTest() {
        userSteps = new UserSteps();
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userSteps.delete(accessToken);
        }
    }

    @Test
    @Story("Login existing user")
    @Description("Вход под существующим пользователем")
    public void loginExistingUserSuccess() {
        User user = User.random();
        userSteps.register(user).statusCode(SC_OK);
        ValidatableResponse resp = userSteps.login(new LoginRequest(user.email, user.password));
        resp.statusCode(SC_OK)
                .body("success", is(true))
                .body("user.email", equalTo(user.email))
                .body("accessToken", notNullValue());

        accessToken = resp.extract().path("accessToken");
    }

    @Test
    @Story("Login with wrong email")
    @Description("Вход с неверным email")
    public void loginWithWrongEmailUnauthorized() {
        userSteps.login(new LoginRequest("wrong@example.com", "password123"))
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @Story("Login with wrong password")
    @Description("Вход с неверным паролем")
    public void loginWithWrongPasswordUnauthorized() {
        User user = User.random();
        userSteps.register(user).statusCode(SC_OK);

        userSteps.login(new LoginRequest(user.email, "wrongpassword"))
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", equalTo("email or password are incorrect"));

        accessToken = userSteps.login(new LoginRequest(user.email, user.password))
                .statusCode(SC_OK)
                .extract().path("accessToken");
    }
}
