package com.codingchallenge.tests.loggedIn;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codingchallenge.base.BaseTest;
import com.codingchallenge.components.CartComponent;
import com.codingchallenge.pages.products.ProductDetailsPage;
import com.codingchallenge.pages.products.ProductsPage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.WaitForSelectorState;

public class ProductDetailsPageTest extends BaseTest {

     // Fields
    ProductsPage productsPage;
    ProductDetailsPage productDetailsPage;

    // BeforeEach
    @BeforeEach
    void setUpPage() {
        signInAs("superman@email.com", "Password123!");
        productsPage = new ProductsPage(page);
        productsPage.navigate();
        productsPage.getProduct(0).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        int totalProducts = productsPage.getCurrentProductsCount();
        int randomIndex = (int) (Math.random() * totalProducts);
        productsPage.getProduct(randomIndex).scrollIntoViewIfNeeded();
        for (int i = 0; i < totalProducts; i++) {
            try{
                productDetailsPage = productsPage.clickProduct(randomIndex);
                if (productDetailsPage.isProductInStock()) {                        
                    break;
                }
                page.goBack();
                page.waitForLoadState();
                productsPage = new ProductsPage(page);
                randomIndex = (int) (Math.random() * totalProducts);
            } catch (Exception e) {
                page.goBack();
                page.waitForLoadState();
                productsPage = new ProductsPage(page);
                randomIndex = (int) (Math.random() * totalProducts);
            }
        }
    }

    @Test
    void shouldAddProductToCart() {
        assertTrue(productDetailsPage.isProductInStock());
        productDetailsPage.clickAddToCartButton();
        //assertTrue(productDetailsPage.isAddToCartReady());
    }

    @Test
    void shouldDisplayDefaultColor() {
        assertThat(productDetailsPage.getSelectedColorLabel()).isVisible();
    }

    @Test
    void shouldIncreaseQuantityWhenClickingPlus() {
        int currentQuantityBeforeIncrease = productDetailsPage.getQuantity();
        productDetailsPage.increaseQuantity();
        assertEquals(currentQuantityBeforeIncrease + 1, productDetailsPage.getQuantity());
    }

    @Test
    void shouldDecreaseQuantityWhenClickingMinus() {
        if(productDetailsPage.getDecreaseQuantityButton().isDisabled()){
            productDetailsPage.increaseQuantity();
            int currentQuantityBeforeDecrease = productDetailsPage.getQuantity();
            productDetailsPage.decreaseQuantity();
            assertEquals(currentQuantityBeforeDecrease - 1, productDetailsPage.getQuantity());
        }
    }

    @Test
    void shouldChangeColorWhenClickingColorButton() {
        String selectedColor = productDetailsPage.selectRandomColor();
        assertThat(productDetailsPage.getSelectedColorLabel()).hasText(selectedColor);
    }

    @Test
    void shouldReflectQuantityInCartWhenAddedWithIncreasedQuantity() {
        productDetailsPage.increaseQuantity();
        int quantityBeforeAddingToCart = productDetailsPage.getQuantity();
        CartComponent cart = productDetailsPage.clickAddToCartButton();
        assertThat(cart.getCartTitle()).containsText(quantityBeforeAddingToCart + " items");
        assertEquals(quantityBeforeAddingToCart, cart.getCartQuantity());
    }
}
