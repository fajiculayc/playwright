package com.codingchallenge.tests.loggedOut;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codingchallenge.base.BaseTest;
import com.codingchallenge.pages.products.ProductsPage;

public class ProductsPageTest extends BaseTest {

    // Fields
    ProductsPage productsPage;

    @BeforeEach
    void setUpPage() {
        productsPage = new ProductsPage(page);
        productsPage.navigate();
    }

    @Test
    void shouldLoadProductsPageSuccessfully() {
        assertThat(page).hasURL("https://demo.spreecommerce.org/us/en/products");
    }

    @Test
    void shouldDisplayAllProductsHeader() {
        assertThat(productsPage.getAllProductsHeader()).isVisible();
    }

    @Test
    void shouldDisplayAllProductsCountLabel() {
        assertThat(productsPage.getCurrentProductCountLabel()).isVisible();
    }
}