package testdata;

import model.Order;
import model.User;

import java.util.Collections;
import java.util.List;

public class DataProvider {

    private DataProvider() {
    }

    public static User getDefaultUser() {
        return User.builder()
                .email("qauser459@test.fake")
                .password("qa459")
                .name("Faker")
                .build();
    }

    public static User getInvalidUser() {
        return User.builder()
                .email(null)
                .password("dummy333")
                .name("Dummy")
                .build();
    }

    public static User getInvalidPasswordUser() {
        return User.builder()
                .email("qauser459@test.fake")
                .password("invalid789")
                .name("Faker")
                .build();
    }

    public static User getNewCredsUser() {
        return User.builder()
                .email("newqauser459@test.fake")
                .password("newqa459")
                .name("newFaker")
                .build();
    }

    public static Order getDefaultOrder() {
        return new Order(List.of("61c0c5a71d1f82001bdaaa6c",
                "61c0c5a71d1f82001bdaaa78",
                "61c0c5a71d1f82001bdaaa74"));
    }

    public static Order getInvalidOrder() {
        return new Order(List.of("invalidhash123"));
    }

    public static Order getEmptyIngredientsOrder() {
        return new Order(Collections.emptyList());
    }
}
