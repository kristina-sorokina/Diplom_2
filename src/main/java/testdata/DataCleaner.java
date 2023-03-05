package testdata;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import util.PropertiesReader;

import static io.restassured.RestAssured.given;

public class DataCleaner {
    public DataCleaner() {
    }

    public void deleteUser(ValidatableResponse response, Object body) {
        JsonPath jsonPath = response.extract().jsonPath();
        String token = jsonPath.getString("accessToken");
        if (token != null) {
            given()
                    .header("Content-type", "application/json")
                    .header("Authorization", token)
                    .and().body(body)
                    .when().delete(PropertiesReader.AUTHORIZE_USER_PATH);
        }
    }
}
