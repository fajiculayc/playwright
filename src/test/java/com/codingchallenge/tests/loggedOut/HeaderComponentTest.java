package com.codingchallenge.tests.loggedOut;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codingchallenge.base.BaseTest;
import com.codingchallenge.components.HeaderComponent;
import com.codingchallenge.pages.HomePage;

public class HeaderComponentTest extends BaseTest {
    
    HeaderComponent header;
    HomePage homePage;

    @BeforeEach
    void setUpPage(){
        homePage = new HomePage(page);
        homePage.navigate();
        header = new HeaderComponent(page);
    }

    @Test
    void shouldDisplayHomeLogo(){
        assertThat(header.getHomeLogo()).isVisible();
    }

    @Test
    void shouldDisplayAccountButton(){
        assertThat(header.getAccountButton()).isVisible();
    }

    @Test
    void shouldDisplayMenuButton(){
        assertThat(header.getMenuButton()).isVisible();
    }
}
