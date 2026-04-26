package com.codingchallenge.tests.loggedIn;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.codingchallenge.base.BaseTest;
import com.codingchallenge.pages.products.ProductDetailsPage;
import com.codingchallenge.pages.products.ProductsPage;

public class ProductsPageTest extends BaseTest {

    ProductsPage productsPage;

    @BeforeEach
    void setUpPage() {
        signInAs("superman@email.com", "Password123!");
        productsPage = new ProductsPage(page);
        productsPage.navigate();
    }

    @Test
    @DisplayName("Products page should load with correct URL")
    void shouldLoadProductsPageSuccessfully() {
        assertThat(page).hasURL("https://demo.spreecommerce.org/us/en/products");
    }

    @Test
    @DisplayName("Products page should have correct browser tab title")
    void shouldDisplayCorrectProductsTitle() {
        assertThat(page).hasTitle(
                "Products | Spree Commerce Demo | Next.js Ecommerce Storefront");
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

    @Test
    @DisplayName("First product name and price should be visible")
    void shouldDisplayFirstProductNameAndPrice() {
        assertThat(productsPage.getProductName(0)).isVisible();
        assertThat(productsPage.getProductPrice(0)).isVisible();
    }

    @Test
    @DisplayName("Expected product count should be greater than zero")
    void shouldDisplayCorrectProductCount() {
        assertTrue(productsPage.getExpectedProductCount() > 0);
    }

    @Test
    @DisplayName("All products should load after scrolling to bottom")
    void shouldLoadAllProductsOnScroll() {
        int expectedCount = productsPage.getExpectedProductCount();
        productsPage.scrollUntilAllProductsLoaded();
        assertEquals(expectedCount, productsPage.getCurrentProductsCount());
    }

    @Test
    @DisplayName("Every product should have a name and price starting with $")
    void shouldDisplayNameAndPriceForAllProducts() {
        productsPage.scrollUntilAllProductsLoaded();
        int count = productsPage.getCurrentProductsCount();
        for (int i = 0; i < count; i++) {
            assertFalse(productsPage.getProductNameText(i).isEmpty());
            assertTrue(productsPage.getProductPriceText(i).startsWith("$"));
        }
    }

    @Test
    @DisplayName("Clicking a product should navigate to its details page")
    void shouldNavigateToProductDetailsWhenClickingProduct() {
        String expectedSlug = productsPage.getProductSlug(0);
        String expectedName = productsPage.getProductNameText(0);
        String expectedPrice = productsPage.getProductPriceText(0);
        ProductDetailsPage productDetailsPage = productsPage.clickProduct(0);
        assertThat(page).hasURL(Pattern.compile(".*" + expectedSlug + ".*"));
        assertThat(productDetailsPage.getProductName()).hasText(expectedName);
        assertThat(productDetailsPage.getProductPrice()).hasText(expectedPrice);
    }
}