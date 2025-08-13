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

@Feature("User Registration API")
public class UserRegistrationTests extends TestBase {

    private UserSteps userSteps;
    private String accessToken;
    private User cleanupUser;

    @Before
    public void setUpTest() {
        userSteps = new UserSteps();
    }

    @After
    public void tearDown() {
        try {
            if (accessToken == null && cleanupUser != null) {
                accessToken = userSteps.login(new LoginRequest(cleanupUser.email, cleanupUser.password))
                        .statusCode(SC_OK)
                        .extract().path("accessToken");
            }
            if (accessToken != null) {
                userSteps.delete(accessToken);
            }
        } catch (Throwable ignored) {
            // Best-effort cleanup
        }
    }

    @Test
    @Story("Register unique user")
    @Description("Создание уникального пользователя")
    public void registerUniqueUserSuccess() {
        User user = User.random();
        cleanupUser = user;
        ValidatableResponse resp = userSteps.register(user);
        resp.statusCode(SC_OK)
                .body("success", is(true))
                .body("user.email", equalTo(user.email))
                .body("user.name", equalTo(user.name))
                .body("accessToken", notNullValue());

        accessToken = resp.extract().path("accessToken");
    }

    @Test
    @Story("Register existing user")
    @Description("Регистрация уже существующего пользователя")
    public void registerExistingUserForbidden() {
        User user = User.random();
        cleanupUser = user;
        String token = userSteps.register(user).statusCode(SC_OK).extract().path("accessToken");

        userSteps.register(user)
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", equalTo("User already exists"));

        accessToken = token;
    }

    @Test
    @Story("Register user missing email")
    @Description("Регистрация пользователя без email")
    public void registerUserMissingEmailForbidden() {
        User user = new User(null, "password123", "TestUser");
        userSteps.register(user)
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @Story("Register user missing password")
    @Description("Регистрация пользователя без пароля")
    public void registerUserMissingPasswordForbidden() {
        User user = new User("test@example.com", null, "TestUser");
        userSteps.register(user)
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @Story("Register user missing name")
    @Description("Регистрация пользователя без имени")
    public void registerUserMissingNameForbidden() {
        User user = new User("test@example.com", "password123", null);
        userSteps.register(user)
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
