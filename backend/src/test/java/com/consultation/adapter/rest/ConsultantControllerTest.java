package com.consultation.adapter.rest;

import com.consultation.TestBase;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class ConsultantControllerTest extends TestBase {

    @Test
    void should_list_consultants() {
        given()
        .when()
            .get("/api/v1/consultants")
        .then()
            .statusCode(200)
            .body("$", not(empty()));
    }

    @Test
    void should_return_slots() {
        given()
            .queryParam("date", "2026-12-01")
        .when()
            .get("/api/v1/consultants/c-001/slots")
        .then()
            .statusCode(200)
            .body("$", not(empty()))
            .body("[0].startTime", notNullValue());
    }

    @Test
    void should_return_empty_for_invalid_consultant() {
        given()
            .queryParam("date", "2026-12-01")
        .when()
            .get("/api/v1/consultants/invalid-id/slots")
        .then()
            .statusCode(200)
            .body("$", empty());
    }
}
