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

public class LoginUserTest {
    private final User user = DataProvider.getDefaultUser();
    private final UserClient client = new UserClient();
    private final DataCleaner cleaner = new DataCleaner();
    private ValidatableResponse createUserResponse;
    private ValidatableResponse loginUserResponse;

    @Before
    public void init() {
        baseURI = PropertiesReader.PROD_URL;
        createUserResponse = client.createUser(user);
    }

    @Test
    public void shouldReturnCode200WhenSuccessfulLogin() {
        loginUserResponse = client.loginUser(user);
        loginUserResponse.statusCode(200);
    }

    @Test
    public void shouldReturnCorrectBodyWhenSuccessfulLogin() {
        loginUserResponse = client.loginUser(user);

        JsonPath jsonPath = loginUserResponse.extract().jsonPath();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(jsonPath.getString("success")).isEqualTo("true");
        softly.assertThat(jsonPath.getString("user")).isNotNull();
        softly.assertThat(jsonPath.getString("accessToken")).isNotNull();
        softly.assertThat(jsonPath.getString("refreshToken")).isNotNull();
        softly.assertAll();
    }

    @Test
    public void shouldReturnCode401WhenLoginWithInvalidPassword() {
        loginUserResponse = client.loginUser(DataProvider.getInvalidUser());
        loginUserResponse.statusCode(401);
    }

    @Test
    public void shouldReturnWarningMessageBodyWhenLoginWithInvalidPassword() {
        loginUserResponse = client.loginUser(DataProvider.getInvalidPasswordUser());

        JsonPath jsonPath = loginUserResponse.extract().jsonPath();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(jsonPath.getString("success")).isEqualTo("false");
        softly.assertThat(jsonPath.getString("message")).isEqualTo("email or password are incorrect");
        softly.assertAll();
    }

    @After
    public void tearDown() {
        cleaner.deleteUser(createUserResponse, user);
    }
}
