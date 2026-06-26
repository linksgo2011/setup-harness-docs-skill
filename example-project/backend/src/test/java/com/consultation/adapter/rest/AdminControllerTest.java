package com.consultation.adapter.rest;

import com.consultation.TestBase;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class AdminControllerTest extends TestBase {

    String adminToken;

    @BeforeEach
    void initToken() {
        adminToken = loginAsAdmin();
    }

    @Test
    void should_get_dashboard() {
        given()
            .header("Authorization", authHeader(adminToken))
        .when()
            .get("/api/v1/admin/dashboard")
        .then()
            .statusCode(200)
            .body("totalUsers", notNullValue())
            .body("totalConsultants", notNullValue())
            .body("totalAppointments", notNullValue());
    }

    @Test
    void should_list_users() {
        given()
            .header("Authorization", authHeader(adminToken))
        .when()
            .get("/api/v1/admin/users")
        .then()
            .statusCode(200)
            .body("$", not(empty()));
    }

    @Test
    void should_toggle_user_status() {
        var firstUserId = given()
            .header("Authorization", authHeader(adminToken))
        .when()
            .get("/api/v1/admin/users")
        .then()
            .statusCode(200)
            .extract().jsonPath().getString("[0].id");

        given()
            .header("Authorization", authHeader(adminToken))
        .when()
            .put("/api/v1/admin/users/{id}/toggle-status", firstUserId)
        .then()
            .statusCode(200);
    }

    @Test
    void should_branch_crud() {
        var branchId = given()
            .header("Authorization", authHeader(adminToken))
            .contentType(ContentType.JSON)
            .body("{\"name\":\"测试网点\",\"address\":\"测试地址\",\"phone\":\"010-88888888\"}")
        .when()
            .post("/api/v1/admin/branches")
        .then()
            .statusCode(200)
            .extract().jsonPath().getString("id");

        given()
            .header("Authorization", authHeader(adminToken))
            .contentType(ContentType.JSON)
            .body("{\"name\":\"更新网点\"}")
        .when()
            .put("/api/v1/admin/branches/{id}", branchId)
        .then()
            .statusCode(200);

        given()
            .header("Authorization", authHeader(adminToken))
        .when()
            .delete("/api/v1/admin/branches/{id}", branchId)
        .then()
            .statusCode(200);
    }

    @Test
    void should_crud_data_dictionary() {
        given()
        .when()
            .get("/api/v1/data-dictionaries")
        .then()
            .statusCode(200)
            .body("$", not(empty()));

        given()
        .when()
            .get("/api/v1/data-dictionaries/USER_ROLE")
        .then()
            .statusCode(200)
            .body("typeName", equalTo("USER_ROLE"))
            .body("items", not(empty()));
    }

    @Test
    void should_list_business_rules() {
        given()
        .when()
            .get("/api/v1/business-rules")
        .then()
            .statusCode(200)
            .body("$", not(empty()));
    }
}
