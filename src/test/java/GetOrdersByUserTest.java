import client.OrderClient;
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

public class GetOrdersByUserTest {
    private final UserClient userClient = new UserClient();
    private final OrderClient orderClient = new OrderClient();
    private final DataCleaner cleaner = new DataCleaner();
    private User user;
    private ValidatableResponse createUserResponse;
    private String token;

    @Before
    public void init() {
        baseURI = PropertiesReader.PROD_URL;
        user = DataProvider.getDefaultUser();
        createUserResponse = userClient.createUser(user);
        JsonPath jsonPath = createUserResponse.extract().jsonPath();
        token = jsonPath.getString("accessToken");
        orderClient.createAuthorizedOrder(DataProvider.getDefaultOrder(), token);
    }

    @Test
    public void shouldReturnCode200WhenUserOrdersReceived() {
        ValidatableResponse response = orderClient.getOrderByUser(token);
        response.statusCode(200);
    }

    @Test
    public void shouldReturnCorrectBodyWhenUserOrdersReceived() {
        ValidatableResponse response = orderClient.getOrderByUser(token);

        JsonPath jsonPath = response.extract().jsonPath();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(jsonPath.getString("success")).isEqualTo("true");
        softly.assertThat(jsonPath.getString("orders")).isNotNull();
        softly.assertAll();
    }

    @After
    public void tearDown() {
        cleaner.deleteUser(createUserResponse, user);
    }
}
