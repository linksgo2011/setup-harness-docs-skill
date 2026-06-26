package com.consultation;

import com.consultation.config.ResetDbService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class TestBase {

    @LocalServerPort
    int port;

    @Autowired
    ResetDbService resetDbService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        resetDbService.resetAll();
    }

    protected String loginAsUser() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"email\":\"test@test.com\",\"password\":\"password123\",\"name\":\"测试用户\"}")
        .when()
            .post("/api/v1/auth/register")
        .then()
            .statusCode(200);
        var r = given()
            .contentType(ContentType.JSON)
            .body("{\"email\":\"test@test.com\",\"password\":\"password123\"}")
        .when()
            .post("/api/v1/auth/login")
        .then()
            .statusCode(200);
        return r.extract().jsonPath().getString("token");
    }

    protected String loginAsAdmin() {
        var r = given()
            .contentType(ContentType.JSON)
            .body("{\"email\":\"admin@test.com\",\"password\":\"password\"}")
        .when()
            .post("/api/v1/auth/login")
        .then()
            .statusCode(200);
        return r.extract().jsonPath().getString("token");
    }

    protected String authHeader(String token) {
        return "Bearer " + token;
    }
}
