package com.codingchallenge.tests.loggedIn;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codingchallenge.base.BaseTest;
import com.codingchallenge.pages.products.ProductDetailsPage;
import com.codingchallenge.pages.products.ProductsPage;

public class ProductsPageTest extends BaseTest {

    // Fields
    ProductsPage productsPage;

    // BeforeEach
    @BeforeEach
    void setUpPage() {
        signInAs("superman@email.com", "Password123!");
        productsPage = new ProductsPage(page);
        productsPage.navigate();
    }

    @Test
    void shouldLoadProductsPageSuccessfully() {
        assertThat(page).hasURL("https://demo.spreecommerce.org/us/en/products");
    }

    @Test
    void shouldDisplayCorrectProductsTitle() {
        assertThat(page).hasTitle("Products | Spree Commerce Demo | Next.js Ecommerce Storefront");
    }

    @Test
    void shouldDisplayAllProductsHeader() {
        assertThat(productsPage.getAllProductsHeader()).isVisible();
    }

    @Test
    void shouldDisplayAllProductsCountLabel() {
        assertThat(productsPage.getCurrentProductCountLabel()).isVisible();
    }

    @Test
    void shouldDisplayFirstProductNameAndPrice() {
        assertThat(productsPage.getProductName(0)).isVisible();
        assertThat(productsPage.getProductPrice(0)).isVisible();
    }

    @Test
    void shouldDisplayCorrectProductCount() {
        int expectedCount = productsPage.getExpectedProductCount();
        assertTrue(expectedCount > 0);
    }

    // Scroll tests
    @Test
    void shouldLoadAllProductsOnScroll() {
        int expectedCount = productsPage.getExpectedProductCount();
        productsPage.scrollUntilAllProductsLoaded();
        int actualCount = productsPage.getCurrentProductsCount();
        assertEquals(expectedCount, actualCount);
    }

    @Test
    void shouldDisplayNameAndPriceForAllProducts() {
        productsPage.scrollUntilAllProductsLoaded();
        int count = productsPage.getCurrentProductsCount();
        for (int i = 0; i < count; i++) {
            assertFalse(productsPage.getProductNameText(i).isEmpty());
            assertTrue(productsPage.getProductPriceText(i).startsWith("$"));
        }
    }

    // Navigation tests
    @Test
    void shouldNavigateToProductDetailsWhenClickingProduct() {
        String expectedSlug = productsPage.getProductSlug(0);
        String expectedName = productsPage.getProductNameText(0);
        String expectedPrice = productsPage.getProductPriceText(0);
        ProductDetailsPage productDetailsPage = productsPage.clickProduct(0);
        assertThat(page).hasURL(java.util.regex.Pattern.compile(".*" + expectedSlug + ".*"));
        assertThat(productDetailsPage.getProductName()).hasText(expectedName);
        assertThat(productDetailsPage.getProductPrice()).hasText(expectedPrice);
    }
}