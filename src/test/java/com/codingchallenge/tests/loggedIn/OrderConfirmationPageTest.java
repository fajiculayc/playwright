package com.codingchallenge.tests.loggedIn;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.codingchallenge.base.BaseTest;
import com.codingchallenge.components.CartComponent;
import com.codingchallenge.pages.checkout.CheckoutPage;
import com.codingchallenge.pages.checkout.OrderConfirmationPage;
import com.codingchallenge.pages.products.ProductDetailsPage;
import com.codingchallenge.pages.products.ProductsPage;

public class OrderConfirmationPageTest extends BaseTest {

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

    @BeforeEach
    void setUpPage() {
        String[] data = getFirstRowFromCsv("/testdata/accountData.csv");
        testEmail = data[0];
        password = data[1];
        firstName = data[2];
        lastName = data[3];
        address = data[4];
        city = data[5];
        state = data[6];
        zipCode = data[7];
        shippingMethod = data[8];
        cardNumber = data[9];
        expirationDate = data[10];
        securityCode = data[11];
        cardCountry = data[12];

        signInAs(testEmail, password);

        productsPage = new ProductsPage(page);
        productsPage.navigate();
        productsPage.scrollUntilAllProductsLoaded();

        productDetailsPage = findInStockProduct(productsPage);

        cart = productDetailsPage.clickAddToCartButton();
        checkoutPage = cart.clickCheckout();
        checkoutPage.waitForLoad();
        checkoutUrl = page.url();

        orderConfirmationPage = checkoutPage.completeCheckout(
                "US", address, city, state, zipCode,
                shippingMethod, cardNumber, expirationDate,
                securityCode, cardCountry);
        orderConfirmationPage.waitForLoad();
    }

    @Test
    @DisplayName("Should be on order confirmation page after completing checkout")
    void shouldBeOnOrderConfirmationPage() {
        assertTrue(orderConfirmationPage.isOnOrderConfirmationPage());
    }

    @Test
    @DisplayName("Order confirmation URL should contain same cart ID as checkout URL")
    void shouldHaveSameCartIdInUrl() {
        String cartId = checkoutUrl.substring(checkoutUrl.lastIndexOf("/") + 1);
        assertTrue(
                page.url().contains(cartId) &&
                page.url().contains("/order-placed/") &&
                checkoutUrl.contains("/checkout/"),
                "Expected URL to contain cart ID '" + cartId + "' and '/order-placed/' " +
                "but was: " + page.url());
    }

    @Test
    @DisplayName("Thank you message should display with logged in user's first name")
    void shouldDisplayThankYouMessageWithFirstName() {
        assertThat(orderConfirmationPage.getThankYouMessage(firstName)).isVisible();
    }

    @Test
    @DisplayName("Order number should be visible")
    void shouldDisplayOrderNumber() {
        assertThat(orderConfirmationPage.getOrderNumber()).isVisible();
    }

    @Test
    @DisplayName("Order number should contain Order # prefix")
    void shouldDisplayOrderNumberWithHash() {
        assertThat(orderConfirmationPage.getOrderNumber()).containsText("Order #");
    }

    @Test
    @DisplayName("Order items header should be visible")
    void shouldDisplayOrderItemsHeader() {
        assertThat(orderConfirmationPage.getOrderItemsHeader()).isVisible();
    }

    @Test
    @DisplayName("Subtotal should be visible in order summary")
    void shouldDisplaySubtotal() {
        assertThat(orderConfirmationPage.getSubtotalValue()).isVisible();
    }

    @Test
    @DisplayName("Shipping cost should be visible in order summary")
    void shouldDisplayShipping() {
        assertThat(orderConfirmationPage.getShippingValue()).isVisible();
    }

    @Test
    @DisplayName("Tax should be visible in order summary")
    void shouldDisplayTax() {
        assertThat(orderConfirmationPage.getTaxValue()).isVisible();
    }

    @Test
    @DisplayName("Total should be visible in order summary")
    void shouldDisplayTotal() {
        assertThat(orderConfirmationPage.getTotalValue()).isVisible();
    }

    @Test
    @DisplayName("Total should equal subtotal + shipping + tax")
    void shouldDisplayCorrectTotal() {
        double subtotal = orderConfirmationPage.getSubtotalAmount();
        double shipping = orderConfirmationPage.getShippingAmount();
        double tax = orderConfirmationPage.getTaxAmount();
        assertEquals(subtotal + shipping + tax, orderConfirmationPage.getTotalAmount(), 0.01);
    }

    @Test
    @DisplayName("Shipping method section should be visible")
    void shouldDisplayShippingMethodSection() {
        assertThat(orderConfirmationPage.getShippingMethodSection()).isVisible();
    }

    @Test
    @DisplayName("Correct shipping method should be displayed")
    void shouldDisplayCorrectShippingMethod() {
        assertThat(orderConfirmationPage.getShippingMethodSection()
                .locator(".."))
                .containsText(shippingMethod);
    }

    @Test
    @DisplayName("Payment section should be visible")
    void shouldDisplayPaymentSection() {
        assertThat(orderConfirmationPage.getPaymentSection()).isVisible();
    }

    @Test
    @DisplayName("Payment info should show last 4 digits of card")
    void shouldDisplayCorrectPaymentInfo() {
        assertThat(orderConfirmationPage.getPaymentInfo("4242")).isVisible();
    }

    @Test
    @DisplayName("Shipping address section should be visible")
    void shouldDisplayShippingAddressSection() {
        assertThat(orderConfirmationPage.getShippingAddressSection()).isVisible();
    }

    @Test
    @DisplayName("Shipping address should contain user's full name")
    void shouldDisplayCorrectShippingAddress() {
        assertThat(orderConfirmationPage.getShippingAddressSection()
                .locator(".."))
                .containsText(firstName + " " + lastName);
    }

    @Test
    @DisplayName("Billing address section should be visible")
    void shouldDisplayBillingAddressSection() {
        assertThat(orderConfirmationPage.getBillingAddressSection()).isVisible();
    }

    @Test
    @DisplayName("Email confirmation should show logged in user's email")
    void shouldDisplayEmailConfirmation() {
        assertThat(orderConfirmationPage.getEmailConfirmation(testEmail)).isVisible();
    }

    @Test
    @DisplayName("Continue Shopping button should be visible")
    void shouldDisplayContinueShoppingButton() {
        assertThat(orderConfirmationPage.getContinueShoppingButton()).isVisible();
    }

    @Test
    @DisplayName("Clicking Continue Shopping should navigate back to store")
    void shouldNavigateToHomePageWhenContinueShoppingClicked() {
        orderConfirmationPage.clickContinueShopping();
        assertThat(page).hasURL(Pattern.compile(".*/us/en.*"));
    }
}