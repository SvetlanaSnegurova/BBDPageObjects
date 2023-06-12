package ru.netology.Page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.Data.DataGenerator;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class UserInfoPage {

    private SelenideElement loginField = $("[data-test-id=login] input");
    private SelenideElement passwordField = $("[data-test-id=password] input");
    private SelenideElement loginButton = $("[data-test-id=action-login]");

    public VerificationPage validLogin(DataGenerator.AuthInfo info) {
        loginField.setValue(info.getLogin());
        passwordField.setValue(info.getPassword());
        loginButton.click();
        return page(VerificationPage.class);
    }
}

