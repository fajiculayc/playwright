package com.codingchallenge.tests.loggedIn;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.codingchallenge.base.BaseTest;
import com.codingchallenge.pages.account.AccountPage;
import com.codingchallenge.pages.auth.SignInPage;

public class AccountPageTest extends BaseTest {

    SignInPage signInPage;
    AccountPage accountPage;

    @BeforeEach
    void setUpPage() {
        signInAs("superman@email.com", "Password123!");
        accountPage = new AccountPage(page);
    }

    @Test
    void shouldLoadAccountPageSuccessfully(){
        assertThat(accountPage.getUserFullName()).isVisible();
        assertThat(accountPage.getUserEmail()).isVisible();
        assertThat(accountPage.getAccountOverviewHeader());
        assertThat(accountPage.getSignOutButton());
        assertThat(page).hasURL("https://demo.spreecommerce.org/us/en/account");
    }

    @Test
    void shouldDisplayCorrectAccountPageTitle(){
        assertThat(page).hasTitle("Spree Commerce Demo | Next.js Ecommerce Storefront | Spree");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/testdata/accountData.csv", numLinesToSkip = 1)
    void shouldDisplayCorrectUserInfo(
            String email, String password, String expectedName, String scenario) {
        signInAs(email, password);
        accountPage = new AccountPage(page);
        assertThat(accountPage.getUserFullName()).hasText(expectedName);
        assertThat(accountPage.getUserEmail()).containsText(email);
    }

    @Test
    void shouldSignOutUserSuccessfully(){
        accountPage.clickSignOutButton();
        assertThat(page).hasURL("https://demo.spreecommerce.org/us/en/account");
    }
    
}
