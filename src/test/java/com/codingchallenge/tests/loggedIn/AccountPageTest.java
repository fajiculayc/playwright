package com.codingchallenge.tests.loggedIn;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.codingchallenge.base.BaseTest;
import com.codingchallenge.pages.account.AccountPage;

public class AccountPageTest extends BaseTest {

    AccountPage accountPage;

    @BeforeEach
    void setUpPage() {
        signInAs("superman@email.com", "Password123!");
        accountPage = new AccountPage(page);
    }

    @Test
    @DisplayName("Account page should load successfully with all key elements visible")
    void shouldLoadAccountPageSuccessfully() {
        assertThat(accountPage.getUserFullName()).isVisible();
        assertThat(accountPage.getUserEmail()).isVisible();
        assertThat(accountPage.getAccountOverviewHeader()).isVisible();
        assertThat(accountPage.getSignOutButton()).isVisible();
        assertThat(page).hasURL("https://demo.spreecommerce.org/us/en/account");
    }

    @Test
    @DisplayName("Account page should display correct browser tab title")
    void shouldDisplayCorrectAccountPageTitle() {
        assertThat(page).hasTitle(
                "Spree Commerce Demo | Next.js Ecommerce Storefront | Spree");
    }

    @ParameterizedTest
    @DisplayName("Account page should display correct user info from CSV")
    @CsvFileSource(resources = "/testdata/accountData.csv", numLinesToSkip = 1)
    void shouldDisplayCorrectUserInfo(
            String email, String password, String firstName, String lastName,
            String address, String city, String state, String zipCode,
            String shippingMethod, String cardNumber, String expirationDate,
            String securityCode, String cardCountry, String scenario) {
        signInAs(email, password);
        accountPage = new AccountPage(page);
        assertThat(accountPage.getUserFullName()).containsText(firstName);
        assertThat(accountPage.getUserEmail()).containsText(email);
    }

    @Test
    @DisplayName("User should be redirected to account page after signing out")
    void shouldSignOutUserSuccessfully() {
        accountPage.clickSignOutButton();
        assertThat(page).hasURL("https://demo.spreecommerce.org/us/en/account");
    }
}