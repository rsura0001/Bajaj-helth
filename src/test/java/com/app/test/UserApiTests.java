package com.app.test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class UserApiTests {

    private static final String BASE_URL = "https://bfhldevapigw.healthrx.co.in/automation-campus";
    private static final String ROLL_NUMBER = "1"; 

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    public void testCreateUserWithValidData() {
        String requestBody = "{ \"firstName\": \"Rahul\", \"lastName\": \"Sura\", \"phoneNumber\": 1234567890, \"emailId\": \"rsura@example.com\" }";

        Response response = given()
                .header("roll-number", ROLL_NUMBER)
                .contentType("application/json")
                .body(requestBody)
                .post("/create/user");

        response.then().statusCode(200); 
    }

    @Test
    public void testCreateUserWithDuplicatePhoneNumber() {
        String requestBody = "{ \"firstName\": \"Neha\", \"lastName\": \"sura\", \"phoneNumber\": 1234567888, \"emailId\": \"nehasura@example.com\" }";

        Response response = given()
                .header("roll-number", ROLL_NUMBER)
                .contentType("application/json")
                .body(requestBody)
                .post("/create/user");

        response.then().statusCode(400);
    }

    @Test
    public void testCreateUserWithoutRollNumber() {
        String requestBody = "{ \"firstName\": \"Test\", \"lastName\": \"User\", \"phoneNumber\": 9876543210, \"emailId\": \"test.user@example.com\" }";

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .post("/create/user");

        response.then().statusCode(400); 
    }

    @Test
    public void testCreateUserWithDuplicateEmail() {
        String requestBody = "{ \"firstName\": \"Neha\", \"lastName\": \"Sura\", \"phoneNumber\": 1112223333, \"emailId\": \"nehasura@example.com\" }";

        Response response = given()
                .header("roll-number", ROLL_NUMBER)
                .contentType("application/json")
                .body(requestBody)
                .post("/create/user");

        response.then().statusCode(400); 
    }

    @Test
    public void testCreateUserWithMissingFirstName() {
        String requestBody = "{ \"lastName\": \"Sura\", \"phoneNumber\": 1234567890, \"emailId\": \"devsura@example.com\" }";

        Response response = given()
                .header("roll-number", ROLL_NUMBER)
                .contentType("application/json")
                .body(requestBody)
                .post("/create/user");

        response.then().statusCode(400); 
    }

    @Test
    public void testCreateUserWithInvalidPhoneNumber() {
        String requestBody = "{ \"firstName\": \"Rahul\", \"lastName\": \"sura\", \"phoneNumber\": 12345, \"emailId\": \"rsura@example.com\" }";

        Response response = given()
                .header("roll-number", ROLL_NUMBER)
                .contentType("application/json")
                .body(requestBody)
                .post("/create/user");

        response.then().statusCode(400); 
    }

    @Test
    public void testCreateUserWithInvalidEmailFormat() {
        String requestBody = "{ \"firstName\": \"Rahul\", \"lastName\": \"Sura\", \"phoneNumber\": 1234567890, \"emailId\": \"invalid.email.com\" }";

        Response response = given()
                .header("roll-number", ROLL_NUMBER)
                .contentType("application/json")
                .body(requestBody)
                .post("/create/user");

        response.then().statusCode(400);
    }
}
