package com.example.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class APITest {

    @Test
    public void testGetRequest() {
        // Sending a GET request to fetch a specific post
        Response response = RestAssured.get("https://jsonplaceholder.typicode.com/posts/1");

        // Verifying the HTTP status code returned is 200
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200 but found " + response.getStatusCode());

        // Ensuring the response body is not null
        Assert.assertNotNull(response.getBody(), "Response body should not be null");

        // Validating that the response contains the field "userId"
        Assert.assertTrue(response.getBody().asString().contains("\"userId\""), "Response does not contain 'userId'");

        /*
         * Enhanced test to include response body validation and meaningful assertions.
         */
    }

    @Test
    public void testAllPosts() {
        // Sending a GET request to fetch all posts
        Response response = RestAssured.get("https://jsonplaceholder.typicode.com/posts");

        // Verifying the HTTP status code returned is 200
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200 but found " + response.getStatusCode());

        // Ensuring the response contains a list of posts
        int postCount = response.getBody().jsonPath().getList("$").size();
        Assert.assertTrue(postCount > 0, "Response does not contain any posts");

        /*
         * Future improvements can include validating the structure of each post
         * in the response (e.g., check for fields like 'id', 'userId', 'title').
         */
    }

    @Test
    public void testPostRequest() {
        // Sending a POST request to create a new resource
        String requestBody = "{ \"title\": \"foo\", \"body\": \"bar\", \"userId\": 1 }";

        Response response = RestAssured.given()
                .contentType("application/json")  // Setting the content type to JSON
                .body(requestBody)  // Payload
                .post("https://jsonplaceholder.typicode.com/posts");  // Endpoint

        // Log the response body for debugging
        System.out.println("Response Body: " + response.getBody().asString());

        // Verifying the HTTP status code returned is 201 (Created)
        Assert.assertEquals(response.getStatusCode(), 201, "Expected status code 201 but found " + response.getStatusCode());

        // Validating fields in the response using JSONPath
        Assert.assertEquals(response.jsonPath().getString("title"), "foo", "Title does not match");
        Assert.assertEquals(response.jsonPath().getString("body"), "bar", "Body does not match");
        Assert.assertEquals(response.jsonPath().getInt("userId"), 1, "User ID does not match");

        /*
         * Known Issue: This endpoint occasionally fails under heavy load.
         * Documented for future investigation and refactoring.
         */
    }
}