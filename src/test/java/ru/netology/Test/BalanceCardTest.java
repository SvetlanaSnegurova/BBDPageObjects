package ru.netology.Test;

import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.Data.DataGenerator;
import ru.netology.Page.UserInfoPage;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.Data.DataGenerator.*;

public class BalanceCardTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldTransferFromCardToCardTest() {
        val UserInfoPage = new UserInfoPage();
        val authInfo = getAuthInfo();
        val verificationPage = UserInfoPage.validLogin(authInfo);
        val verificationCode = getVerificationCodeFor(authInfo);
        val dashboardPage = verificationPage.validVerify(verificationCode);
        val firstCardBalance = dashboardPage.getCardBalance(getFirstCardNumber().getCardNumber());
        val secondCardBalance = dashboardPage.getCardBalance(getSecondCardNumber().getCardNumber());
        val transferPage = dashboardPage.depositToFirstCard();
        int amount = 1_000;
        transferPage.transferMoney(amount, getSecondCardNumber());
        val expectedFirstCardBalanceAfter = firstCardBalance + amount;
        val expectedSecondCardBalanceAfter = secondCardBalance - amount;
        Assertions.assertEquals(expectedFirstCardBalanceAfter, dashboardPage.getCardBalance(getFirstCardNumber().getCardNumber()));
        Assertions.assertEquals(expectedSecondCardBalanceAfter, dashboardPage.getCardBalance(getSecondCardNumber().getCardNumber()));

    }

    @Test
    void shouldZeroSumTransferTest() {
        val UserInfoPage = new UserInfoPage();
        val authInfo = DataGenerator.getAuthInfo();
        val verificationPage = UserInfoPage.validLogin(authInfo);
        val verificationCode = DataGenerator.getVerificationCodeFor(authInfo);
        val dashboardPage = verificationPage.validVerify(verificationCode);
        val transferPage = dashboardPage.depositToSecondCard();
        int amount = 0;
        transferPage.transferMoney(amount, DataGenerator.getFirstCardNumber());
        transferPage.emptyAmountField();

    }

    @Test
    void shouldAmountMoreBalanceTest() {
        val UserInfoPage = new UserInfoPage();
        val authInfo = getAuthInfo();
        val verificationPage = UserInfoPage.validLogin(authInfo);
        val verificationCode = getVerificationCodeFor(authInfo);
        val dashboardPage = verificationPage.validVerify(verificationCode);
        val secondCardBalance = dashboardPage.getCardBalance(getSecondCardNumber().getCardNumber());
        val transferPage = dashboardPage.depositToFirstCard();
        int amount = DataGenerator.getImpossibleTransfer(secondCardBalance);
        transferPage.transferMoney(amount, getSecondCardNumber());
        transferPage.amountMoreThanBalance();

    }

    @Test
    void shouldTransferFromCardToSameCardTest() {
        val UserInfoPage = new UserInfoPage();
        val authInfo = getAuthInfo();
        val verificationPage = UserInfoPage.validLogin(authInfo);
        val verificationCode = getVerificationCodeFor(authInfo);
        val dashboardPage = verificationPage.validVerify(verificationCode);
        val transferPage = dashboardPage.depositToFirstCard();
        int amount = 1_000;
        transferPage.transferMoney(amount, getFirstCardNumber());
        transferPage.enterAnotherCard();
    }
}
