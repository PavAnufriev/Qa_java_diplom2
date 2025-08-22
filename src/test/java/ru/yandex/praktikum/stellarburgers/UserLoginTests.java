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
    private User testUser;

    @Before
    public void setUpTest() {
        userSteps = new UserSteps();
        // Создаем пользователя заранее для тестов логина
        testUser = User.random();
        userSteps.register(testUser).statusCode(SC_OK);
    }

    @After
    public void tearDown() {
        // Получение токена в методе after для выполнения cleanup даже при падении теста
        try {
            if (testUser != null) {
                // Всегда получаем свежий токен для удаления, независимо от результата теста
                String cleanupToken = userSteps.login(new LoginRequest(testUser.email, testUser.password))
                        .statusCode(SC_OK)
                        .extract().path("accessToken");
                if (cleanupToken != null) {
                    userSteps.delete(cleanupToken);
                }
            }
        } catch (Throwable ignored) {
            // Best-effort cleanup
        }
    }

    @Test
    @Story("Login existing user")
    @Description("Вход под существующим пользователем")
    public void loginExistingUserSuccess() {
        ValidatableResponse resp = userSteps.login(new LoginRequest(testUser.email, testUser.password));
        resp.statusCode(SC_OK)
                .body("success", is(true))
                .body("user.email", equalTo(testUser.email))
                .body("accessToken", notNullValue());
        // Токен НЕ сохраняем здесь - получение для cleanup происходит в @After
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
        userSteps.login(new LoginRequest(testUser.email, "wrongpassword"))
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", equalTo("email or password are incorrect"));
        // Токен для cleanup получаем в @After методе
    }
}
