import client.OrderClient;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import testdata.DataProvider;
import util.PropertiesReader;

import static io.restassured.RestAssured.baseURI;

public class CreateOrderTest {
    private final OrderClient client = new OrderClient();

    @Before
    public void init() {
        baseURI = PropertiesReader.PROD_URL;
    }

    @Test
    public void shouldReturnCode200WhenOrderCreated() {
        ValidatableResponse response = client.createOrder(DataProvider.getDefaultOrder());
        response.statusCode(200);
    }

    @Test
    public void shouldReturnCorrectBodyWhenOrderCreate() {
        ValidatableResponse response = client.createOrder(DataProvider.getDefaultOrder());

        JsonPath jsonPath = response.extract().jsonPath();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(jsonPath.getString("success")).isEqualTo("true");
        softly.assertThat(jsonPath.getString("name")).isNotNull();
        softly.assertThat(jsonPath.getString("order")).isNotNull();
        softly.assertThat(jsonPath.getString("order.number")).isNotNull();
        softly.assertAll();
    }

    @Test
    public void shouldReturnCode500WhenInvalidHash() {
        ValidatableResponse response = client.createOrder(DataProvider.getInvalidOrder());
        response.statusCode(500);
    }

    @Test
    public void shouldReturnCode400WhenEmptyIngredients() {
        ValidatableResponse response = client.createOrder(DataProvider.getEmptyIngredientsOrder());
        response.statusCode(400);
    }

    @Test
    public void shouldReturnWarningMessageWhenEmptyIngredients() {
        ValidatableResponse response = client.createOrder(DataProvider.getEmptyIngredientsOrder());

        JsonPath jsonPath = response.extract().jsonPath();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(jsonPath.getString("success")).isEqualTo("false");
        softly.assertThat(jsonPath.getString("message")).isEqualTo("Ingredient ids must be provided");
        softly.assertAll();
    }
}
