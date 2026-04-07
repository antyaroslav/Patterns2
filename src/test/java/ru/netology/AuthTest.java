package ru.netology;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataGenerator;
import ru.netology.data.DataGenerator.UserInfo;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class AuthTest {
    @BeforeAll
    static void setUpAll() {
        Configuration.baseUrl = "http://localhost:9999";
    }

    @BeforeEach
    void setUp() {
        open("/");
    }

    @Test
    void shouldLoginWithActiveRegisteredUser() {
        UserInfo user = DataGenerator.getRegisteredUser("active");

        $("[data-test-id='login'] input").setValue(user.getLogin());
        $("[data-test-id='password'] input").setValue(user.getPassword());
        $("[data-test-id='action-login']").click();

        $("h2").shouldHave(text("Личный кабинет"), Duration.ofSeconds(15));
    }

    @Test
    void shouldShowErrorForBlockedUser() {
        UserInfo user = DataGenerator.getRegisteredUser("blocked");

        $("[data-test-id='login'] input").setValue(user.getLogin());
        $("[data-test-id='password'] input").setValue(user.getPassword());
        $("[data-test-id='action-login']").click();

        $("[data-test-id='error-notification']")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("Ошибка! Пользователь заблокирован"));
    }

    @Test
    void shouldShowErrorForWrongLogin() {
        UserInfo user = DataGenerator.getUserWithWrongLogin("active");

        $("[data-test-id='login'] input").setValue(user.getLogin());
        $("[data-test-id='password'] input").setValue(user.getPassword());
        $("[data-test-id='action-login']").click();

        $("[data-test-id='error-notification']")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void shouldShowErrorForWrongPassword() {
        UserInfo user = DataGenerator.getUserWithWrongPassword("active");

        $("[data-test-id='login'] input").setValue(user.getLogin());
        $("[data-test-id='password'] input").setValue(user.getPassword());
        $("[data-test-id='action-login']").click();

        $("[data-test-id='error-notification']")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }
}