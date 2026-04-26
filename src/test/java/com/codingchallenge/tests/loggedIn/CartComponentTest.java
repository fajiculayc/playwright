package com.codingchallenge.tests.loggedIn;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.codingchallenge.base.BaseTest;
import com.codingchallenge.components.CartComponent;
import com.codingchallenge.pages.checkout.CheckoutPage;
import com.codingchallenge.pages.products.ProductDetailsPage;
import com.codingchallenge.pages.products.ProductsPage;

public class CartComponentTest extends BaseTest {

    ProductsPage productsPage;
    ProductDetailsPage productDetailsPage;
    CartComponent cart;

    @BeforeEach
    void setUpPage() {
        signInAs("superman@email.com", "Password123!");
        productsPage = new ProductsPage(page);
        productsPage.navigate();
        productDetailsPage = findInStockProduct(productsPage);
        cart = productDetailsPage.clickAddToCartButton();
    }

    @Test
    @DisplayName("Cart should be visible after adding a product")
    void shouldShowCartAfterAddingProduct() {
        assertThat(cart.getCartDialog()).isVisible();
    }

    @Test
    @DisplayName("Cart title should show correct item count after adding one product")
    void shouldShowCorrectItemCountInCart() {
        assertThat(cart.getCartTitle()).containsText("1 item");
    }

    @Test
    @DisplayName("Cart should display the product name")
    void shouldDisplayProductNameInCart() {
        assertThat(cart.getCartProductName()).isVisible();
    }

    @Test
    @DisplayName("Cart should display the product color")
    void shouldDisplayProductColorInCart() {
        assertThat(cart.getCartProductColor()).isVisible();
    }

    @Test
    @DisplayName("Cart should display the product price")
    void shouldDisplayProductPriceInCart() {
        assertThat(cart.getCartProductPrice()).isVisible();
    }

    @Test
    @DisplayName("Cart quantity should increase by 1 when plus button is clicked")
    void shouldIncreaseQuantityWhenClickingPlusInCart() {
        int quantityBefore = cart.getCartQuantity();
        cart.increaseQuantity();
        assertEquals(quantityBefore + 1, cart.getCartQuantity());
    }

    @Test
    @DisplayName("Cart quantity should decrease by 1 when minus button is clicked")
    void shouldDecreaseQuantityWhenClickingMinusInCart() {
        cart.increaseQuantity();
        int quantityBefore = cart.getCartQuantity();
        cart.decreaseQuantity();
        assertEquals(quantityBefore - 1, cart.getCartQuantity());
    }

    @Test
    @DisplayName("Cart subtotal should update correctly when quantity is increased")
    void shouldUpdateSubtotalWhenClickingPlusInCart() {
        double unitPrice = cart.getCartProductPriceValue();
        cart.increaseQuantity();
        double expectedSubtotal = unitPrice * cart.getCartQuantity();
        assertEquals(expectedSubtotal, cart.getCartSubtotalValue());
    }

    @Test
    @DisplayName("Clicking checkout should navigate to the checkout page")
    void shouldNavigateToCheckoutPage() {
        CheckoutPage checkoutPage = cart.clickCheckout();
        assertThat(page).hasURL(Pattern.compile(".*/checkout/cart_.*"));
        assertThat(checkoutPage.getShippingAddressHeader()).isVisible();
    }
}