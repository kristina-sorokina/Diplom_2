import client.UserClient;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import model.User;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import testdata.DataCleaner;
import testdata.DataProvider;
import util.PropertiesReader;

import static io.restassured.RestAssured.baseURI;

public class CreateUserTest {
    private final User user = DataProvider.getDefaultUser();
    private final UserClient client = new UserClient();
    private final DataCleaner cleaner = new DataCleaner();
    private ValidatableResponse response;

    @Before
    public void init() {
        baseURI = PropertiesReader.PROD_URL;
    }

    @Test
    public void shouldReturnCode200WhenUniqueUserCreated() {
        response = client.createUser(user);
        response.statusCode(200);
    }

    @Test
    public void shouldReturnCorrectBodyWhenUniqueUserCreated() {
        response = client.createUser(user);

        JsonPath jsonPath = response.extract().jsonPath();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(jsonPath.getString("success")).isEqualTo("true");
        softly.assertThat(jsonPath.getString("user")).isNotNull();
        softly.assertThat(jsonPath.getString("accessToken")).isNotNull();
        softly.assertThat(jsonPath.getString("refreshToken")).isNotNull();
        softly.assertAll();
    }

    @Test
    public void shouldReturnCode403WhenSameUserCreated() {
        response = client.createUser(user);
        ValidatableResponse sameUserResponse = client.createUser(user);
        sameUserResponse.statusCode(403);
    }

    @Test
    public void shouldReturnWarningMessageBodyWhenSameUserCreated() {
        response = client.createUser(user);
        ValidatableResponse sameUserResponse = client.createUser(user);

        JsonPath jsonPath = sameUserResponse.extract().jsonPath();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(jsonPath.getString("success")).isEqualTo("false");
        softly.assertThat(jsonPath.getString("message")).isEqualTo("User already exists");
        softly.assertAll();
    }

    @Test
    public void shouldReturnCode403WhenInvalidUserCreated() {
        response = client.createUser(DataProvider.getInvalidUser());
        response.statusCode(403);
    }

    @Test
    public void shouldReturnWarningMessageBodyWhenInvalidUserCreated() {
        response = client.createUser(DataProvider.getInvalidUser());

        JsonPath jsonPath = response.extract().jsonPath();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(jsonPath.getString("success")).isEqualTo("false");
        softly.assertThat(jsonPath.getString("message")).isEqualTo("Email, password and name are required fields");
        softly.assertAll();
    }

    @After
    public void tearDown() {
        cleaner.deleteUser(response, user);
    }
}
