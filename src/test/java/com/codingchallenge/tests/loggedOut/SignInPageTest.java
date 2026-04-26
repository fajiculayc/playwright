package com.codingchallenge.tests.loggedOut;

import org.junit.jupiter.api.BeforeEach;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.codingchallenge.base.BaseTest;
import com.codingchallenge.pages.account.AccountPage;
import com.codingchallenge.pages.auth.SignInPage;;

public class SignInPageTest extends BaseTest{
    
    SignInPage signInPage;
    AccountPage accountPage;

    @BeforeEach
    void setUpPage(){
        signInPage = new SignInPage(page);
        signInPage.navigate();
        accountPage = new AccountPage(page);
    }

    @Test
    void shouldLoadSignInPageSuccessfully(){
        assertThat(page).hasURL("https://demo.spreecommerce.org/us/en/account");
    }

    @Test
    void shouldDisplayCorrectSignInTitle(){
        assertThat(page).hasTitle("Spree Commerce Demo | Next.js Ecommerce Storefront | Spree");
    }

    @Test
    void shouldDisplayCreateAccountHeader(){
        assertThat(signInPage.getMyAccountHeader()).isVisible();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/testdata/signInHappyPath.csv", numLinesToSkip = 1)
    void shouldSignInSuccessfully(
            String email, String password){
        signInPage.signIn(email, password);
        assertThat(page).hasURL("https://demo.spreecommerce.org/us/en/account");
        assertThat(accountPage.getAccountOverviewHeader()).isVisible();
        assertThat(accountPage.getUserEmail()).containsText(email);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/testdata/signInBrowserErrors.csv", numLinesToSkip = 1)
    void shouldShowBrowserValidationErrors(
            String email, String password){
        signInPage.signIn(email, password);
        assertThat(page).hasURL("https://demo.spreecommerce.org/us/en/account");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/testdata/signInInlineErrors.csv", numLinesToSkip = 1)
    void shouldShowInlineErrorMessage(
            String email, String password){
        signInPage.signIn(email, password);
        assertThat(page).hasURL("https://demo.spreecommerce.org/us/en/account");
    }
}
