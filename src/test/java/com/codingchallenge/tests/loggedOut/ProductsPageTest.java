package com.codingchallenge.tests.loggedOut;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.codingchallenge.base.BaseTest;
import com.codingchallenge.pages.products.ProductsPage;

public class ProductsPageTest extends BaseTest {

    ProductsPage productsPage;

    @BeforeEach
    void setUpPage() {
        productsPage = new ProductsPage(page);
        productsPage.navigate();
    }

    @Test
    @DisplayName("Products page should load with correct URL")
    void shouldLoadProductsPageSuccessfully() {
        assertThat(page).hasURL("https://demo.spreecommerce.org/us/en/products");
    }

    @Test
    @DisplayName("All Products header should be visible")
    void shouldDisplayAllProductsHeader() {
        assertThat(productsPage.getAllProductsHeader()).isVisible();
    }

    @Test
    @DisplayName("Product count label should be visible")
    void shouldDisplayAllProductsCountLabel() {
        assertThat(productsPage.getCurrentProductCountLabel()).isVisible();
    }
}