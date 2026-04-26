package com.codingchallenge.tests.loggedIn;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codingchallenge.base.BaseTest;
import com.codingchallenge.components.CartComponent;
import com.codingchallenge.pages.checkout.CheckoutPage;
import com.codingchallenge.pages.products.ProductDetailsPage;
import com.codingchallenge.pages.products.ProductsPage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.WaitForSelectorState;

public class CartComponentTest extends BaseTest {
    
    // Fields
    ProductsPage productsPage;
    ProductDetailsPage productDetailsPage;
    CartComponent cart;

    // BeforeEach
    @BeforeEach
    void setUpPage() {
        signInAs("superman@email.com", "Password123!");
        productsPage = new ProductsPage(page);
        productsPage.navigate();
        productsPage.getProduct(0).waitFor(new Locator.WaitForOptions()
            .setState(WaitForSelectorState.VISIBLE));

        int totalProducts = productsPage.getCurrentProductsCount();
        for (int i = 0; i < totalProducts; i++) {
            int randomIndex = (int) (Math.random() * totalProducts);
            productsPage.getProduct(randomIndex).scrollIntoViewIfNeeded();
            productDetailsPage = productsPage.clickProduct(randomIndex);

            if (productDetailsPage.isProductInStock()) {
                break;
            }

            page.goBack();
            page.waitForLoadState();
            productsPage = new ProductsPage(page);
        }
        cart = productDetailsPage.clickAddToCartButton();
    }

    @Test
    void shouldShowCartAfterAddingProduct() {
        assertThat(cart.getCartDialog()).isVisible();
    }

    @Test
    void shouldShowCorrectItemCountInCart() {
        assertThat(cart.getCartTitle()).containsText("1 item");
    }

    @Test
    void shouldDisplayProductNameInCart() {
        assertThat(cart.getCartProductName()).isVisible();
    }

    @Test
    void shouldDisplayProductColorInCart() {
        assertThat(cart.getCartProductColor()).isVisible();
    }

    @Test
    void shouldDisplayProductPriceInCart() {
        assertThat(cart.getCartProductPrice()).isVisible();
    }

    @Test
    void shouldIncreaseQuantityWhenClickingPlusInCart() {
        int currentQuantityBeforeIncrease = cart.getCartQuantity();
        cart.increaseQuantity();
        assertEquals(currentQuantityBeforeIncrease + 1, cart.getCartQuantity());
    }

    @Test
    void shouldDecreaseQuantityWhenClickingMinusInCart() {
        cart.increaseQuantity();
        int currentQuantityBeforeDecrease = cart.getCartQuantity();
        cart.decreaseQuantity();
        assertEquals(currentQuantityBeforeDecrease - 1, cart.getCartQuantity());
    }

    @Test
    void shouldUpdateSubtotalWhenClickingPlusInCart() {
        double unitPrice = cart.getCartProductPriceValue();
        cart.increaseQuantity();
        int currentQuantity = cart.getCartQuantity();
        double expectedSubtotal = unitPrice * currentQuantity;
        double actualSubtotal = cart.getCartSubtotalValue();
        assertEquals(expectedSubtotal, actualSubtotal);
    }

    @Test
    void shouldNavigateToCheckoutPage() {
        CheckoutPage checkoutPage = cart.clickCheckout();
        assertThat(page).hasURL(Pattern.compile(".*/checkout/cart_.*"));
        assertThat(checkoutPage.getShippingAddressHeader()).isVisible();
    }
}
