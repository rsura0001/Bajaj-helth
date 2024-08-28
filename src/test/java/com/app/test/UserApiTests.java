package com.app.test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class UserApiTests {

    private static final String BASE_URL = "https://bfhldevapigw.healthrx.co.in/automation-campus";

    @Before
    public void setUp() {
        // Set base URI for REST Assured
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    public void testCreateUserWithValidData() {
        RestAssured.given()
            .header("roll-number", "1")
            .header("Content-Type", "application/json")
            .body("{ \"firstName\": \"John\", \"lastName\": \"Doe\", \"phoneNumber\": 1234567890, \"emailId\": \"john.doe@example.com\" }")
            .when()
            .post("/create/user")
            .then()
            .statusCode(200)
            .body("message", equalTo("User created successfully"));
    }

    @Test
    public void testCreateUserWithoutRollNumber() {
        RestAssured.given()
            .header("Content-Type", "application/json")
            .body("{ \"firstName\": \"John\", \"lastName\": \"Doe\", \"phoneNumber\": 1234567890, \"emailId\": \"john.doe@example.com\" }")
            .when()
            .post("/create/user")
            .then()
            .statusCode(401)
            .body("error", equalTo("Unauthorized: Missing roll number"));
    }

    @Test
    public void testCreateUserWithDuplicatePhoneNumber() {
        // First create a user with the phone number
        RestAssured.given()
            .header("roll-number", "2")
            .header("Content-Type", "application/json")
            .body("{ \"firstName\": \"Jane\", \"lastName\": \"Doe\", \"phoneNumber\": 1234567890, \"emailId\": \"jane.doe@example.com\" }")
            .when()
            .post("/create/user");

        // Attempt to create another user with the same phone number
        RestAssured.given()
            .header("roll-number", "3")
            .header("Content-Type", "application/json")
            .body("{ \"firstName\": \"Alice\", \"lastName\": \"Smith\", \"phoneNumber\": 1234567890, \"emailId\": \"alice.smith@example.com\" }")
            .when()
            .post("/create/user")
            .then()
            .statusCode(400)
            .body("error", equalTo("Phone number already in use"));
    }

    @Test
    public void testCreateUserWithInvalidEmailFormat() {
        RestAssured.given()
            .header("roll-number", "4")
            .header("Content-Type", "application/json")
            .body("{ \"firstName\": \"John\", \"lastName\": \"Doe\", \"phoneNumber\": 1234567890, \"emailId\": \"john.doe.com\" }")
            .when()
            .post("/create/user")
            .then()
            .statusCode(400)
            .body("error", equalTo("Invalid email format"));
    }

    @Test
    public void testCreateUserWithMissingFields() {
        RestAssured.given()
            .header("roll-number", "5")
            .header("Content-Type", "application/json")
            .body("{ \"firstName\": \"John\", \"lastName\": \"Doe\", \"phoneNumber\": 1234567890 }") // Missing emailId
            .when()
            .post("/create/user")
            .then()
            .statusCode(400)
            .body("error", equalTo("Missing required fields"));
    }
}
