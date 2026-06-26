package com.consultation.adapter.rest;

import com.consultation.TestBase;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class AppointmentControllerTest extends TestBase {

    String userToken;

    @BeforeEach
    void initToken() {
        userToken = loginAsUser();
    }

    @Test
    void should_create_and_list() {
        given()
            .header("Authorization", authHeader(userToken))
            .contentType(ContentType.JSON)
            .body("{\"consultantId\":\"c-001\",\"date\":\"2026-12-10\",\"startTime\":\"10:00\",\"endTime\":\"11:00\",\"note\":\"测试预约\"}")
        .when()
            .post("/api/v1/appointments")
        .then()
            .statusCode(200)
            .body("id", notNullValue())
            .body("status", equalTo("PENDING"));

        given()
            .header("Authorization", authHeader(userToken))
        .when()
            .get("/api/v1/appointments")
        .then()
            .statusCode(200)
            .body("$", not(empty()));
    }

    @Test
    void should_reject_conflict() {
        given()
            .header("Authorization", authHeader(userToken))
            .contentType(ContentType.JSON)
            .body("{\"consultantId\":\"c-001\",\"date\":\"2026-12-10\",\"startTime\":\"11:00\",\"endTime\":\"12:00\"}")
        .when()
            .post("/api/v1/appointments")
        .then()
            .statusCode(200);

        given()
            .header("Authorization", authHeader(userToken))
            .contentType(ContentType.JSON)
            .body("{\"consultantId\":\"c-001\",\"date\":\"2026-12-10\",\"startTime\":\"11:00\",\"endTime\":\"12:00\"}")
        .when()
            .post("/api/v1/appointments")
        .then()
            .statusCode(409);
    }

    @Test
    void should_cancel_appointment() {
        var id = given()
            .header("Authorization", authHeader(userToken))
            .contentType(ContentType.JSON)
            .body("{\"consultantId\":\"c-001\",\"date\":\"2026-12-10\",\"startTime\":\"14:00\",\"endTime\":\"15:00\"}")
        .when()
            .post("/api/v1/appointments")
        .then()
            .statusCode(200)
            .extract().jsonPath().getString("id");

        given()
            .header("Authorization", authHeader(userToken))
            .contentType(ContentType.JSON)
            .body("{\"reason\":\"测试取消\"}")
        .when()
            .put("/api/v1/appointments/{id}/cancel", id)
        .then()
            .statusCode(200);
    }
}
