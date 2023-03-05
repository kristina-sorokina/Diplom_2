package client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import util.PropertiesReader;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private final String createOrderPath = PropertiesReader.CREATE_ORDER_PATH;

    @Step("Order is created")
    public ValidatableResponse createOrder(Object body) {
        return given()
                .header("Content-type", "application/json")
                .and().body(body)
                .when().post(createOrderPath)
                .then();
    }

    @Step("User's orders are received")
    public ValidatableResponse getOrderByUser(String token) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .when().get(createOrderPath)
                .then();
    }

    @Step("Order is created")
    public ValidatableResponse createAuthorizedOrder(Object body, String token) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .and().body(body)
                .when().post(createOrderPath)
                .then();
    }
}
