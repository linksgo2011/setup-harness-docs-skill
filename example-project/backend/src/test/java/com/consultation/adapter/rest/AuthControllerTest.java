package com.consultation.adapter.rest;

import com.consultation.TestBase;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class AuthControllerTest extends TestBase {

    @Test
    void should_register_and_login() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"email\":\"new@test.com\",\"password\":\"password123\",\"name\":\"新用户\"}")
        .when()
            .post("/api/v1/auth/register")
        .then()
            .statusCode(200)
            .body("token", notNullValue())
            .body("user.email", equalTo("new@test.com"));

        given()
            .contentType(ContentType.JSON)
            .body("{\"email\":\"new@test.com\",\"password\":\"password123\"}")
        .when()
            .post("/api/v1/auth/login")
        .then()
            .statusCode(200)
            .body("token", notNullValue());
    }

    @Test
    void should_fail_duplicate_email() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"email\":\"dup@test.com\",\"password\":\"password123\",\"name\":\"用户1\"}")
        .when()
            .post("/api/v1/auth/register")
        .then()
            .statusCode(200);

        given()
            .contentType(ContentType.JSON)
            .body("{\"email\":\"dup@test.com\",\"password\":\"password123\",\"name\":\"用户2\"}")
        .when()
            .post("/api/v1/auth/register")
        .then()
            .statusCode(400)
            .body("errorCode", notNullValue());
    }

    @Test
    void should_fail_wrong_password() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"email\":\"fail@test.com\",\"password\":\"password123\",\"name\":\"用户\"}")
        .when()
            .post("/api/v1/auth/register")
        .then()
            .statusCode(200);

        given()
            .contentType(ContentType.JSON)
            .body("{\"email\":\"fail@test.com\",\"password\":\"wrongpass\"}")
        .when()
            .post("/api/v1/auth/login")
        .then()
            .statusCode(400);
    }
}
