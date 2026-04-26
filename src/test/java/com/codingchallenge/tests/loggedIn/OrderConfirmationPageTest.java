package com.codingchallenge.tests.loggedIn;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codingchallenge.base.BaseTest;
import com.codingchallenge.components.CartComponent;
import com.codingchallenge.pages.checkout.CheckoutPage;
import com.codingchallenge.pages.checkout.OrderConfirmationPage;
import com.codingchallenge.pages.products.ProductDetailsPage;
import com.codingchallenge.pages.products.ProductsPage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.WaitForSelectorState;

public class OrderConfirmationPageTest extends BaseTest {

    // Fields
    ProductsPage productsPage;
    ProductDetailsPage productDetailsPage;
    CartComponent cart;
    CheckoutPage checkoutPage;
    OrderConfirmationPage orderConfirmationPage;
    String testEmail;
    String password;
    String firstName;
    String lastName;
    String address;
    String city;
    String state;
    String zipCode;
    String shippingMethod;
    String cardNumber;
    String expirationDate;
    String securityCode;
    String cardCountry;
    String checkoutUrl;

    // BeforeEach
    @BeforeEach
    void setUpPage() {
        String[] credentials = getFirstRowFromCsv("/testdata/accountData.csv");
        testEmail = credentials[0];
        password = credentials[1];
        firstName = credentials[2];
        lastName = credentials[3];
        address = credentials[4];
        city = credentials[5];
        state = credentials[6];
        zipCode = credentials[7];
        shippingMethod = credentials[8];
        cardNumber = credentials[9];
        expirationDate = credentials[10];
        securityCode = credentials[11];
        cardCountry = credentials[12];

        // Sign in
        signInAs(testEmail, password);

        // Navigate to products
        productsPage = new ProductsPage(page);
        productsPage.navigate();
        page.waitForLoadState();

        // Wait for first product to load
        productsPage.getProduct(0).waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE));

        // Scroll until all products loaded
        productsPage.scrollUntilAllProductsLoaded();

        // Find in-stock product
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
        checkoutPage = cart.clickCheckout();
        checkoutPage.waitForLoad();
        checkoutUrl = page.url();

        orderConfirmationPage = checkoutPage.completeCheckout(
                "US", address, city, state, zipCode,
                shippingMethod, cardNumber, expirationDate,
                securityCode, cardCountry
        );
        orderConfirmationPage.waitForLoad();
    }

    // Page load tests
    @Test
    void shouldBeOnOrderConfirmationPage() {
        assertTrue(orderConfirmationPage.isOnOrderConfirmationPage());
    }

    @Test
    void shouldHaveSameCartIdInUrl() {
        String cartId = checkoutUrl.substring(checkoutUrl.lastIndexOf("/") + 1);
        
        assertTrue(page.url().contains(cartId));
        
        // Verify URL changed from /checkout to /order-placed
        assertTrue(checkoutUrl.contains("/checkout/"));
        assertTrue(page.url().contains("/order-placed/"));
    }

    @Test
    void shouldDisplayThankYouMessageWithFirstName() {
        assertThat(orderConfirmationPage.getThankYouMessage(firstName)).isVisible();
    }

    @Test
    void shouldDisplayOrderNumber() {
        assertThat(orderConfirmationPage.getOrderNumber()).isVisible();
    }

    @Test
    void shouldDisplayOrderNumberWithHash() {
        assertThat(orderConfirmationPage.getOrderNumber()).containsText("Order #");
    }

    @Test
    void shouldDisplayOrderItemsHeader() {
        assertThat(orderConfirmationPage.getOrderItemsHeader()).isVisible();
    }

    @Test
    void shouldDisplaySubtotal() {
        assertThat(orderConfirmationPage.getSubtotalValue()).isVisible();
    }

    @Test
    void shouldDisplayShipping() {
        assertThat(orderConfirmationPage.getShippingValue()).isVisible();
    }

    @Test
    void shouldDisplayTax() {
        assertThat(orderConfirmationPage.getTaxValue()).isVisible();
    }

    @Test
    void shouldDisplayTotal() {
        assertThat(orderConfirmationPage.getTotalValue()).isVisible();
    }

    @Test
    void shouldDisplayCorrectTotal() {
        double subtotal = orderConfirmationPage.getSubtotalValue_();
        double shipping = orderConfirmationPage.getShippingValue_();
        double tax = orderConfirmationPage.getTaxValue_();
        double expectedTotal = subtotal + shipping + tax;
        double actualTotal = orderConfirmationPage.getTotalValue_();
        assertEquals(expectedTotal, actualTotal, 0.01);
    }

    @Test
    void shouldDisplayShippingMethodSection() {
        assertThat(orderConfirmationPage.getShippingMethodSection()).isVisible();
    }

    @Test
    void shouldDisplayCorrectShippingMethod() {
        assertThat(orderConfirmationPage.getShippingMethodSection()
                .locator(".."))
                .containsText(shippingMethod);
    }

    @Test
    void shouldDisplayPaymentSection() {
        assertThat(orderConfirmationPage.getPaymentSection()).isVisible();
    }

    @Test
    void shouldDisplayCorrectPaymentInfo() {
        assertThat(orderConfirmationPage.getPaymentInfo("4242")).isVisible();
    }

    @Test
    void shouldDisplayShippingAddressSection() {
        assertThat(orderConfirmationPage.getShippingAddressSection()).isVisible();
    }

    @Test
    void shouldDisplayCorrectShippingAddress() {
        assertThat(orderConfirmationPage.getShippingAddressSection()
                .locator(".."))
                .containsText(firstName + " " + lastName);
    }

    @Test
    void shouldDisplayBillingAddressSection() {
        assertThat(orderConfirmationPage.getBillingAddressSection()).isVisible();
    }

    @Test
    void shouldDisplayEmailConfirmation() {
        assertThat(orderConfirmationPage.getEmailConfirmation(testEmail)).isVisible();
    }

    @Test
    void shouldDisplayContinueShoppingButton() {
        assertThat(orderConfirmationPage.getContinueShoppingButton()).isVisible();
    }

    @Test
    void shouldNavigateToHomePageWhenContinueShoppingClicked() {
        orderConfirmationPage.clickContinueShopping();
        assertThat(page).hasURL(Pattern.compile(".*/us/en.*"));
    }
}