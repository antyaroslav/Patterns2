package ru.netology.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private DataGenerator() {
    }

    private static final RequestSpecification REQUEST_SPEC = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .build();

    public static UserInfo getRegisteredUser(String status) {
        UserInfo user = getUser(status);
        register(user);
        return user;
    }

    public static UserInfo getUser(String status) {
        Faker faker = new Faker(new Locale("en"));
        return new UserInfo(
                faker.name().username(),
                faker.internet().password(),
                status
        );
    }

    public static UserInfo getUserWithWrongLogin(String status) {
        UserInfo registeredUser = getRegisteredUser(status);
        Faker faker = new Faker(new Locale("en"));
        return new UserInfo(
                faker.name().username(),
                registeredUser.getPassword(),
                registeredUser.getStatus()
        );
    }

    public static UserInfo getUserWithWrongPassword(String status) {
        UserInfo registeredUser = getRegisteredUser(status);
        Faker faker = new Faker(new Locale("en"));
        return new UserInfo(
                registeredUser.getLogin(),
                faker.internet().password(),
                registeredUser.getStatus()
        );
    }

    private static void register(UserInfo user) {
        given()
                .spec(REQUEST_SPEC)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    @Value
    public static class UserInfo {
        String login;
        String password;
        String status;
    }
}