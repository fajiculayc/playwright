package com.codingchallenge.tests.loggedOut;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.codingchallenge.base.BaseTest;
import com.codingchallenge.pages.account.AccountPage;
import com.codingchallenge.pages.auth.SignInPage;

public class SignInPageTest extends BaseTest {

    SignInPage signInPage;
    AccountPage accountPage;

    @BeforeEach
    void setUpPage() {
        signInPage = new SignInPage(page);
        signInPage.navigate();
        accountPage = new AccountPage(page);
    }

    @Test
    @DisplayName("Sign in page should load with correct URL")
    void shouldLoadSignInPageSuccessfully() {
        assertThat(page).hasURL("https://demo.spreecommerce.org/us/en/account");
    }

    @Test
    @DisplayName("Sign in page should have correct browser tab title")
    void shouldDisplayCorrectSignInTitle() {
        assertThat(page).hasTitle(
                "Spree Commerce Demo | Next.js Ecommerce Storefront | Spree");
    }

    @Test
    @DisplayName("My Account header should be visible on sign in page")
    void shouldDisplayCreateAccountHeader() {
        assertThat(signInPage.getMyAccountHeader()).isVisible();
    }

    @ParameterizedTest
    @DisplayName("User should sign in successfully with valid credentials")
    @CsvFileSource(resources = "/testdata/signInHappyPath.csv", numLinesToSkip = 1)
    void shouldSignInSuccessfully(String email, String password) {
        signInPage.signIn(email, password);
        assertThat(page).hasURL("https://demo.spreecommerce.org/us/en/account");
        assertThat(accountPage.getAccountOverviewHeader()).isVisible();
        assertThat(accountPage.getUserEmail()).containsText(email);
    }

    @ParameterizedTest
    @DisplayName("Browser validation errors should show with invalid inputs")
    @CsvFileSource(resources = "/testdata/signInBrowserErrors.csv", numLinesToSkip = 1)
    void shouldShowBrowserValidationErrors(String email, String password) {
        signInPage.signIn(email, password);
        assertThat(page).hasURL("https://demo.spreecommerce.org/us/en/account");
    }

    @ParameterizedTest
    @DisplayName("Inline error message should show with invalid credentials")
    @CsvFileSource(resources = "/testdata/signInInlineErrors.csv", numLinesToSkip = 1)
    void shouldShowInlineErrorMessage(String email, String password) {
        signInPage.signIn(email, password);
        assertThat(page).hasURL("https://demo.spreecommerce.org/us/en/account");
    }
}