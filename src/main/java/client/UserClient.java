package client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import util.PropertiesReader;

import static io.restassured.RestAssured.given;

public class UserClient {
    private final String createUserPath = PropertiesReader.CREATE_USER_PATH;
    private final String loginUserPath = PropertiesReader.LOGIN_USER_PATH;
    private final String authorizeUserPath = PropertiesReader.AUTHORIZE_USER_PATH;

    @Step("User is created")
    public ValidatableResponse createUser(Object body) {
        return given()
                .header("Content-type", "application/json")
                .and().body(body)
                .when().post(createUserPath)
                .then();
    }

    @Step("User logs in")
    public ValidatableResponse loginUser(Object body) {
        return given()
                .header("Content-type", "application/json")
                .and().body(body)
                .when().post(loginUserPath)
                .then();
    }

    @Step("User changes credentials")
    public ValidatableResponse changeUserCreds(String token, Object body) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .and().body(body)
                .when().patch(authorizeUserPath)
                .then();
    }

    @Step("User changes credentials without authorization")
    public ValidatableResponse changeUserCredsWithoutAuth(Object body) {
        return given()
                .header("Content-type", "application/json")
                .and().body(body)
                .when().patch(authorizeUserPath)
                .then();
    }
}
