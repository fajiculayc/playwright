package com.codingchallenge.tests.loggedOut;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.codingchallenge.base.BaseTest;
import com.codingchallenge.components.HeaderComponent;
import com.codingchallenge.pages.HomePage;

public class HeaderComponentTest extends BaseTest {

    HeaderComponent header;
    HomePage homePage;

    @BeforeEach
    void setUpPage() {
        homePage = new HomePage(page);
        homePage.navigate();
        header = new HeaderComponent(page);
    }

    @Test
    @DisplayName("Home logo should be visible in header")
    void shouldDisplayHomeLogo() {
        assertThat(header.getHomeLogo()).isVisible();
    }

    @Test
    @DisplayName("Account button should be visible in header")
    void shouldDisplayAccountButton() {
        assertThat(header.getAccountButton()).isVisible();
    }

    @Test
    @DisplayName("Menu button should be visible in header")
    void shouldDisplayMenuButton() {
        assertThat(header.getMenuButton()).isVisible();
    }
}