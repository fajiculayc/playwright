package com.codingchallenge.tests.loggedIn;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.codingchallenge.base.BaseTest;
import com.codingchallenge.components.CartComponent;
import com.codingchallenge.pages.products.ProductDetailsPage;
import com.codingchallenge.pages.products.ProductsPage;

public class ProductDetailsPageTest extends BaseTest {

    ProductsPage productsPage;
    ProductDetailsPage productDetailsPage;

    @BeforeEach
    void setUpPage() {
        signInAs("superman@email.com", "Password123!");
        productsPage = new ProductsPage(page);
        productsPage.navigate();
        productDetailsPage = findInStockProduct(productsPage);
    }

    @Test
    @DisplayName("Add to Cart button should successfully add an in-stock product to cart")
    void shouldAddProductToCart() {
        assertTrue(productDetailsPage.isProductInStock());
        productDetailsPage.clickAddToCartButton();
    }

    @Test
    @DisplayName("Default color label should be visible on product details page")
    void shouldDisplayDefaultColor() {
        assertThat(productDetailsPage.getSelectedColorLabel()).isVisible();
    }

    @Test
    @DisplayName("Quantity should increase by 1 when plus button is clicked")
    void shouldIncreaseQuantityWhenClickingPlus() {
        int quantityBefore = productDetailsPage.getQuantity();
        productDetailsPage.increaseQuantity();
        assertEquals(quantityBefore + 1, productDetailsPage.getQuantity());
    }

    @Test
    @DisplayName("Quantity should decrease by 1 when minus button is clicked")
    void shouldDecreaseQuantityWhenClickingMinus() {
        productDetailsPage.increaseQuantity();
        int quantityBefore = productDetailsPage.getQuantity();
        productDetailsPage.decreaseQuantity();
        assertEquals(quantityBefore - 1, productDetailsPage.getQuantity());
    }

    @Test
    @DisplayName("Selected color label should update when a color button is clicked")
    void shouldChangeColorWhenClickingColorButton() {
        String selectedColor = productDetailsPage.selectRandomColor();
        assertThat(productDetailsPage.getSelectedColorLabel()).hasText(selectedColor);
    }

    @Test
    @DisplayName("Cart quantity should reflect the quantity set on the product details page")
    void shouldReflectQuantityInCartWhenAddedWithIncreasedQuantity() {
        productDetailsPage.increaseQuantity();
        int quantityBeforeAddingToCart = productDetailsPage.getQuantity();
        CartComponent cart = productDetailsPage.clickAddToCartButton();
        assertThat(cart.getCartTitle()).containsText(quantityBeforeAddingToCart + " items");
        assertEquals(quantityBeforeAddingToCart, cart.getCartQuantity());
    }
}