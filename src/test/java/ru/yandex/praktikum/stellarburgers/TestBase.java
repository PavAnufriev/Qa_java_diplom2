package ru.yandex.praktikum.stellarburgers;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.BeforeClass;

public class TestBase {

    protected static final String BASE_URL = "https://stellarburgers.nomoreparties.site";

    protected static RequestSpecification spec;

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = BASE_URL;
        spec = new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setContentType(ContentType.JSON)
                .log(LogDetail.URI)
                .addFilter(new AllureRestAssured())
                .build();
        // Set as default spec for all requests
        RestAssured.requestSpecification = spec;
    }
}
