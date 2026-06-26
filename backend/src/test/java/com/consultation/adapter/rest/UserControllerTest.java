package com.consultation.adapter.rest;

import com.consultation.TestBase;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class UserControllerTest extends TestBase {

    String token;

    @BeforeEach
    void initToken() {
        token = loginAsUser();
    }

    @Test
    void should_get_profile() {
        given()
            .header("Authorization", authHeader(token))
        .when()
            .get("/api/v1/users/me")
        .then()
            .statusCode(200)
            .body("email", equalTo("test@test.com"))
            .body("name", equalTo("测试用户"));
    }

    @Test
    void should_update_profile() {
        given()
            .header("Authorization", authHeader(token))
            .contentType(ContentType.JSON)
            .body("{\"name\":\"更新姓名\",\"phone\":\"13900000000\"}")
        .when()
            .put("/api/v1/users/me")
        .then()
            .statusCode(200)
            .body("name", equalTo("更新姓名"))
            .body("phone", equalTo("13900000000"));
    }

    @Test
    void should_change_password() {
        given()
            .header("Authorization", authHeader(token))
            .contentType(ContentType.JSON)
            .body("{\"oldPassword\":\"password123\",\"newPassword\":\"newpassword123\"}")
        .when()
            .put("/api/v1/users/me/password")
        .then()
            .statusCode(200);
    }
}
