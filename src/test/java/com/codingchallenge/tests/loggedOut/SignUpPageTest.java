package com.codingchallenge.tests.loggedOut;

import org.junit.jupiter.api.BeforeEach;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.codingchallenge.base.BaseTest;
import com.codingchallenge.pages.auth.SignUpPage;

public class SignUpPageTest extends BaseTest{

    SignUpPage signUpPage;

    @BeforeEach
    void setUpPage(){
        signUpPage = new SignUpPage(page);
        signUpPage.navigate();
    }

    @Test
    void shouldLoadSignUpPageSuccessfully(){
        assertThat(page).hasURL("https://demo.spreecommerce.org/us/en/account/register");
    }

    @Test
    void shouldDisplayCorrectSignUpTitle(){
        assertThat(page).hasTitle("Spree Commerce Demo | Next.js Ecommerce Storefront | Spree");
    }

    @Test
    void shouldDisplayCreateAccountHeader(){
        assertThat(signUpPage.getCreateAccountHeader()).isVisible();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/testdata/signUpHappyPath.csv", numLinesToSkip = 1)
    void shouldSignUpSuccessfully(
            String firstName, String lastName, String email, String password,
            String confirmPassword, boolean acceptTerms, String scenario){
        signUpPage.signUp(firstName, lastName, email, password, confirmPassword, acceptTerms);
        assertThat(page).hasURL("https://demo.spreecommerce.org/us/en/account");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/testdata/signUpBrowserErrors.csv", numLinesToSkip = 1)
    void shouldShowBrowserValidationErrors(
            String firstName, String lastName, String email, String password,
            String confirmPassword, boolean acceptTerms, String scenario) {
        signUpPage.signUp(firstName, lastName, email, password, confirmPassword, acceptTerms);
        assertThat(page).hasURL("https://demo.spreecommerce.org/us/en/account/register");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/testdata/signUpInlineErrors.csv", numLinesToSkip = 1)
    void shouldShowInlineErrorMessage(
            String firstName, String lastName, String email, 
            String password, String confirmPassword, boolean acceptTerms, 
            String expectedError, String scenario) {
        signUpPage.signUp(firstName, lastName, email, password, confirmPassword, acceptTerms);
        assertThat(signUpPage.getFormErrorMessage()).hasText(expectedError);
    }

    @Test
    void shouldShowPasswordWhenClickingEyeIcon(){
        signUpPage.inputPassword("Password123!");
        signUpPage.togglePasswordVisibility();
        assertThat(signUpPage.getPasswordField()).hasAttribute("type", "text");
    }

    @Test
    void shouldHidePasswordWhenClickingEyeIconAgain(){
        signUpPage.inputPassword("Password123!");
        signUpPage.togglePasswordVisibility();
        signUpPage.togglePasswordVisibility();
        assertThat(signUpPage.getPasswordField()).hasAttribute("type", "password");
    }

    @Test
    void shouldShowConfirmPasswordWhenClickingEyeIcon(){
        signUpPage.inputPassword("Password123!");
        signUpPage.toggleConfirmPasswordVisibility();
        assertThat(signUpPage.getConfirmPasswordField()).hasAttribute("type", "text");
    }

    @Test
    void shouldHideConfirmPasswordWhenClickingEyeIconAgain(){
        signUpPage.inputPassword("Password123!");
        signUpPage.toggleConfirmPasswordVisibility();
        signUpPage.toggleConfirmPasswordVisibility();
        assertThat(signUpPage.getConfirmPasswordField()).hasAttribute("type", "password");
    }

    @Test
    void shouldNavigatetToSignInPageWhenClickingSignInLink(){
        signUpPage.clickSignInLink();
        assertThat(page).hasURL("https://demo.spreecommerce.org/us/en/account");
    }
}
