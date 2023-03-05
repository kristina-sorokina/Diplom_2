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

public class ChangeCredsTest {
    private final User user = DataProvider.getDefaultUser();
    private final UserClient client = new UserClient();
    private final DataCleaner cleaner = new DataCleaner();
    private ValidatableResponse createUserResponse;
    private ValidatableResponse changeCredsResponse;
    private String token;

    @Before
    public void init() {
        baseURI = PropertiesReader.PROD_URL;
        createUserResponse = client.createUser(user);
        JsonPath jsonPath = createUserResponse.extract().jsonPath();
        token = jsonPath.getString("accessToken");
    }

    @Test
    public void shouldReturnCode200WhenCredsChangedSuccessfully() {
        changeCredsResponse = client.changeUserCreds(token, DataProvider.getNewCredsUser());
        changeCredsResponse.statusCode(200);
    }

    @Test
    public void shouldReturnCorrectBodyWhenCredsChangedSuccessfully() {
        changeCredsResponse = client.changeUserCreds(token, DataProvider.getNewCredsUser());

        JsonPath jsonPath = changeCredsResponse.extract().jsonPath();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(jsonPath.getString("success")).isEqualTo("true");
        softly.assertThat(jsonPath.getString("user")).isNotNull();
        softly.assertThat(jsonPath.getString("user.email")).isEqualTo("newqauser459@test.fake");
        softly.assertThat(jsonPath.getString("user.name")).isEqualTo("newFaker");
        softly.assertAll();
    }

    @Test
    public void shouldReturnCode401WhenCredsChangedUnauthorized() {
        changeCredsResponse = client.changeUserCredsWithoutAuth(DataProvider.getNewCredsUser());
        changeCredsResponse.statusCode(401);
    }

    @Test
    public void shouldReturnWarningMessageWhenCredsChangedUnauthorized() {
        changeCredsResponse = client.changeUserCredsWithoutAuth(DataProvider.getNewCredsUser());

        JsonPath jsonPath = changeCredsResponse.extract().jsonPath();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(jsonPath.getString("success")).isEqualTo("false");
        softly.assertThat(jsonPath.getString("message")).isEqualTo("You should be authorised");
        softly.assertAll();
    }

    @After
    public void tearDown() {
        cleaner.deleteUser(createUserResponse, user);
    }
}
