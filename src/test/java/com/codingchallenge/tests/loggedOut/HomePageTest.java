package com.codingchallenge.tests.loggedOut;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codingchallenge.base.BaseTest;
import com.codingchallenge.pages.HomePage;

public class HomePageTest extends BaseTest{

    HomePage homePage;

    @BeforeEach
    void setUpPage(){
        homePage = new HomePage(page);
        homePage.navigate("https://demo.spreecommerce.org/us/en");
    }

    @Test
    void shouldLoadHomePageSuccessfully(){
        assertThat(page).hasURL("https://demo.spreecommerce.org/us/en");
    }

    @Test
    void shouldDisplayCorrectHomePageTitle() {
        assertThat(page).hasTitle("Spree Commerce Demo | Next.js Ecommerce Storefront");
    }

    @Test
    void shouldShowSignUpButtonInHeader(){
        assertThat(homePage.getAccountButton()).isVisible();
    }

    @Test
    void shouldNavigateToSignInPageWhenClickingAccountIcon(){
        homePage.clickAccountButton();
        assertThat(page).hasURL("https://demo.spreecommerce.org/us/en/account");
    }
}