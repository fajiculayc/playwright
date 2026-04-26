package com.codingchallenge.tests.loggedOut;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.codingchallenge.base.BaseTest;
import com.codingchallenge.pages.auth.SignUpPage;

public class SignUpPageTest extends BaseTest {

    SignUpPage signUpPage;

    @BeforeEach
    void setUpPage() {
        signUpPage = new SignUpPage(page);
        signUpPage.navigate();
    }

    @Test
    @DisplayName("Sign up page should load with correct URL")
    void shouldLoadSignUpPageSuccessfully() {
        assertThat(page).hasURL("https://demo.spreecommerce.org/us/en/account/register");
    }

    @Test
    @DisplayName("Sign up page should have correct browser tab title")
    void shouldDisplayCorrectSignUpTitle() {
        assertThat(page).hasTitle(
                "Spree Commerce Demo | Next.js Ecommerce Storefront | Spree");
    }

    @Test
    @DisplayName("Create Account header should be visible on sign up page")
    void shouldDisplayCreateAccountHeader() {
        assertThat(signUpPage.getCreateAccountHeader()).isVisible();
    }

    @ParameterizedTest
    @DisplayName("User should sign up successfully with valid credentials")
    @CsvFileSource(resources = "/testdata/signUpHappyPath.csv", numLinesToSkip = 1)
    void shouldSignUpSuccessfully(
            String firstName, String lastName, String email, String password,
            String confirmPassword, boolean acceptTerms, String scenario) {
        signUpPage.signUp(firstName, lastName, email, password, confirmPassword, acceptTerms);
        assertThat(page).hasURL("https://demo.spreecommerce.org/us/en/account");
    }

    @ParameterizedTest
    @DisplayName("Browser validation errors should show with invalid inputs")
    @CsvFileSource(resources = "/testdata/signUpBrowserErrors.csv", numLinesToSkip = 1)
    void shouldShowBrowserValidationErrors(
            String firstName, String lastName, String email, String password,
            String confirmPassword, boolean acceptTerms, String scenario) {
        signUpPage.signUp(firstName, lastName, email, password, confirmPassword, acceptTerms);
        assertThat(page).hasURL("https://demo.spreecommerce.org/us/en/account/register");
    }

    @ParameterizedTest
    @DisplayName("Inline error message should show with invalid inputs")
    @CsvFileSource(resources = "/testdata/signUpInlineErrors.csv", numLinesToSkip = 1)
    void shouldShowInlineErrorMessage(
            String firstName, String lastName, String email,
            String password, String confirmPassword, boolean acceptTerms,
            String expectedError, String scenario) {
        signUpPage.signUp(firstName, lastName, email, password, confirmPassword, acceptTerms);
        assertThat(signUpPage.getFormErrorMessage()).hasText(expectedError);
    }

    @Test
    @DisplayName("Password should be visible when eye icon is clicked")
    void shouldShowPasswordWhenClickingEyeIcon() {
        signUpPage.inputPassword("Password123!");
        signUpPage.togglePasswordVisibility();
        assertThat(signUpPage.getPasswordField()).hasAttribute("type", "text");
    }

    @Test
    @DisplayName("Password should be hidden when eye icon is clicked again")
    void shouldHidePasswordWhenClickingEyeIconAgain() {
        signUpPage.inputPassword("Password123!");
        signUpPage.togglePasswordVisibility();
        signUpPage.togglePasswordVisibility();
        assertThat(signUpPage.getPasswordField()).hasAttribute("type", "password");
    }

    @Test
    @DisplayName("Confirm password should be visible when eye icon is clicked")
    void shouldShowConfirmPasswordWhenClickingEyeIcon() {
        signUpPage.inputPassword("Password123!");
        signUpPage.toggleConfirmPasswordVisibility();
        assertThat(signUpPage.getConfirmPasswordField()).hasAttribute("type", "text");
    }

    @Test
    @DisplayName("Confirm password should be hidden when eye icon is clicked again")
    void shouldHideConfirmPasswordWhenClickingEyeIconAgain() {
        signUpPage.inputPassword("Password123!");
        signUpPage.toggleConfirmPasswordVisibility();
        signUpPage.toggleConfirmPasswordVisibility();
        assertThat(signUpPage.getConfirmPasswordField()).hasAttribute("type", "password");
    }

    @Test
    @DisplayName("Clicking Sign In link should navigate to sign in page")
    void shouldNavigateToSignInPageWhenClickingSignInLink() {
        signUpPage.clickSignInLink();
        assertThat(page).hasURL("https://demo.spreecommerce.org/us/en/account");
    }
}