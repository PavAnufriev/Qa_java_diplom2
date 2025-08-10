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

import static org.hamcrest.Matchers.*;

@Feature("User API")
public class UserTests extends TestBase {

    private UserSteps userSteps;
    private String accessToken; // Bearer ...

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
    @Story("Register unique user")
    @Description("Api test + Allure")
    public void registerUniqueUser_success() {
        User user = User.random();
        ValidatableResponse resp = userSteps.register(user);
        resp.statusCode(200)
                .body("success", is(true))
                .body("user.email", equalTo(user.email))
                .body("user.name", equalTo(user.name))
                .body("accessToken", notNullValue());

        accessToken = resp.extract().path("accessToken");
    }

    @Test
    @Story("Register existing user")
    @Description("Api test + Allure")
    public void registerExistingUser_forbidden() {
        User user = User.random();
        String token = userSteps.register(user).statusCode(200).extract().path("accessToken");

        userSteps.register(user)
                .statusCode(403)
                .body("success", is(false))
                .body("message", equalTo("User already exists"));

        accessToken = token;
    }

    @Test
    @Story("Register user missing required field")
    @Description("Api test + Allure")
    public void registerUser_missingField_forbidden() {
        User user = new User(null, "Passw0rd!", "NoEmail");
        userSteps.register(user)
                .statusCode(403)
                .body("success", is(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @Story("Login existing user")
    @Description("Api test + Allure")
    public void loginExistingUser_success() {
        User user = User.random();
        userSteps.register(user).statusCode(200);
        ValidatableResponse resp = userSteps.login(new LoginRequest(user.email, user.password));
        resp.statusCode(200)
                .body("success", is(true))
                .body("user.email", equalTo(user.email))
                .body("accessToken", notNullValue());

        accessToken = resp.extract().path("accessToken");
    }

    @Test
    @Story("Login with wrong credentials")
    @Description("Api test + Allure")
    public void loginWrongCredentials_unauthorized() {
        userSteps.login(new LoginRequest("wrong@example.com", "badpass"))
                .statusCode(401)
                .body("success", is(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}
