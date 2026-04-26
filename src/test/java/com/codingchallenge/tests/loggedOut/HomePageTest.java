package com.codingchallenge.tests.loggedOut;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.codingchallenge.base.BaseTest;
import com.codingchallenge.pages.HomePage;

public class HomePageTest extends BaseTest {

    HomePage homePage;

    @BeforeEach
    void setUpPage() {
        homePage = new HomePage(page);
        homePage.navigate();
    }

    @Test
    @DisplayName("Home page should load with correct URL")
    void shouldLoadHomePageSuccessfully() {
        assertThat(page).hasURL("https://demo.spreecommerce.org/us/en");
    }

    @Test
    @DisplayName("Home page should have correct browser tab title")
    void shouldDisplayCorrectHomePageTitle() {
        assertThat(page).hasTitle("Spree Commerce Demo | Next.js Ecommerce Storefront");
    }

    @Test
    @DisplayName("Account button should be visible in header")
    void shouldShowSignUpButtonInHeader() {
        assertThat(homePage.getAccountButton()).isVisible();
    }

    @Test
    @DisplayName("Clicking account icon should navigate to sign in page")
    void shouldNavigateToSignInPageWhenClickingAccountIcon() {
        homePage.clickAccountButton();
        assertThat(page).hasURL("https://demo.spreecommerce.org/us/en/account");
    }
}