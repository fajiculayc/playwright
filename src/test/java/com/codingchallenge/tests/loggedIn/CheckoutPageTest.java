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
import com.codingchallenge.pages.products.ProductDetailsPage;
import com.codingchallenge.pages.products.ProductsPage;

public class CheckoutPageTest extends BaseTest {

    ProductsPage productsPage;
    ProductDetailsPage productDetailsPage;
    CartComponent cart;
    CheckoutPage checkoutPage;
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
    }

    @Test
    @DisplayName("Checkout page should load successfully with correct URL")
    void shouldLoadCheckoutPageSuccessfully() {
        assertTrue(checkoutPage.isOnCheckoutPage());
        assertThat(page).hasURL(Pattern.compile(".*/checkout/cart_.*"));
    }

    @Test
    @DisplayName("Contact email should be pre-populated with logged in user's email")
    void shouldPrePopulateContactEmailWithLoggedInUser() {
        assertEquals(testEmail, checkoutPage.getContactEmailValue());
    }

    @Test
    @DisplayName("Shipping address header should be visible")
    void shouldDisplayShippingAddressHeader() {
        assertThat(checkoutPage.getShippingAddressHeader()).isVisible();
    }

    @Test
    @DisplayName("First name should be pre-populated from saved address")
    void shouldPrePopulateFirstName() {
        assertEquals(firstName, checkoutPage.getFirstNameValue());
    }

    @Test
    @DisplayName("Last name should be pre-populated from saved address")
    void shouldPrePopulateLastName() {
        assertEquals(lastName, checkoutPage.getLastNameValue());
    }

    @Test
    @DisplayName("Address text field should be visible")
    void shouldDisplayAddressTextField() {
        assertThat(checkoutPage.getAddressTextField()).isVisible();
    }

    @Test
    @DisplayName("City text field should be visible")
    void shouldDisplayCityTextField() {
        assertThat(checkoutPage.getCityTextField()).isVisible();
    }

    @Test
    @DisplayName("State dropdown should be visible")
    void shouldDisplayStateDropdown() {
        assertThat(checkoutPage.getStateDropdown()).isVisible();
    }

    @Test
    @DisplayName("ZIP postal code field should be visible")
    void shouldDisplayZipPostalCodeTextField() {
        assertThat(checkoutPage.getZipPostalCodeTextField()).isVisible();
    }

    @Test
    @DisplayName("Both shipping methods should be visible after filling address")
    void shouldDisplayShippingMethods() {
        fillShippingAddress();
        checkoutPage.waitForShippingSection();
        assertThat(checkoutPage.getStandardShippingMethodRadioButton()).isVisible();
        assertThat(checkoutPage.getPremiumShippingMethodRadioButton()).isVisible();
    }

    @Test
    @DisplayName("Selected shipping method should be active after selection")
    void shouldSelectShippingMethod() {
        fillShippingAddress();
        checkoutPage.waitForShippingSection();
        checkoutPage.selectShippingMethod(shippingMethod);
        if (shippingMethod.equalsIgnoreCase("Standard")) {
            assertThat(checkoutPage.getStandardShippingMethodRadioButton()).isVisible();
        } else {
            assertThat(checkoutPage.getPremiumShippingMethodRadioButton()).isVisible();
        }
    }

    @Test
    @DisplayName("Payment method header should be visible")
    void shouldDisplayPaymentMethodHeader() {
        assertThat(checkoutPage.getPaymentMethodHeader()).isVisible();
    }

    @Test
    @DisplayName("Same as shipping address checkbox should be visible")
    void shouldDisplaySameAsShippingAddressCheckbox() {
        assertThat(checkoutPage.getSameAsShippingAddressCheckbox()).isVisible();
    }

    @Test
    @DisplayName("Card number error should show when Pay Now clicked with empty card")
    void shouldShowCardNumberErrorWhenPayNowClickedWithEmptyCard() {
        assertThat(checkoutPage.getCardNumberField()).isVisible();
        checkoutPage.clickPayNow();
        page.waitForTimeout(1000);
        assertThat(checkoutPage.getGeneralCardError()).isVisible();
        assertThat(checkoutPage.getGeneralCardError())
                .containsText("Your card number is incomplete.");
    }

    @Test
    @DisplayName("Expiration date error should show when Pay Now clicked with empty card")
    void shouldShowExpirationDateErrorWhenPayNowClickedWithEmptyCard() {
        checkoutPage.waitForPaymentForm();
        checkoutPage.clickPayNow();
        page.waitForTimeout(1000);
        assertThat(checkoutPage.getGeneralCardError()).isVisible();
        assertThat(checkoutPage.getGeneralCardError())
                .containsText("Your card number is incomplete.");
    }

    @Test
    @DisplayName("Security code error should show when Pay Now clicked with empty card")
    void shouldShowSecurityCodeErrorWhenPayNowClickedWithEmptyCard() {
        checkoutPage.waitForPaymentForm();
        checkoutPage.clickPayNow();
        page.waitForTimeout(1000);
        assertThat(checkoutPage.getGeneralCardError()).isVisible();
        assertThat(checkoutPage.getGeneralCardError())
                .containsText("Your card number is incomplete.");
    }

    @Test
    @DisplayName("Product name should be visible in order summary")
    void shouldDisplayProductNameInOrderSummary() {
        assertThat(checkoutPage.getProductNameText()).isVisible();
    }

    @Test
    @DisplayName("Subtotal should be visible in order summary")
    void shouldDisplaySubtotalInOrderSummary() {
        assertThat(checkoutPage.getProductSubtotalValueText()).isVisible();
    }

    @Test
    @DisplayName("Shipping cost should be visible in order summary")
    void shouldDisplayShippingCostInOrderSummary() {
        assertThat(checkoutPage.getProductShippingCostValueText()).isVisible();
    }

    @Test
    @DisplayName("Tax should be visible in order summary")
    void shouldDisplayTaxInOrderSummary() {
        assertThat(checkoutPage.getProductTaxValueText()).isVisible();
    }

    @Test
    @DisplayName("Total should be visible in order summary")
    void shouldDisplayTotalInOrderSummary() {
        assertThat(checkoutPage.getProductTotalValueText()).isVisible();
    }

    @Test
    @DisplayName("Total should equal subtotal + shipping + tax")
    void shouldDisplayCorrectTotalValue() {
        double subtotal = checkoutPage.getProductSubtotalValue();
        double shipping = checkoutPage.getProductShippingCostValue();
        double tax = checkoutPage.getProductTaxValue();
        assertEquals(subtotal + shipping + tax, checkoutPage.getProductTotalValue(), 0.01);
    }

    @Test
    @DisplayName("Pay Now button should disappear after completing checkout with valid card")
    void shouldCompleteCheckoutWithValidCard() {
        assertThat(page).hasURL(Pattern.compile(".*/checkout/cart_.*"));
        assertThat(checkoutPage.getPayNowButton()).isVisible();
        checkoutPage.completeCheckout(
                "US", address, city, state, zipCode,
                shippingMethod, cardNumber, expirationDate,
                securityCode, cardCountry);
        assertThat(page).hasURL(Pattern.compile(".*/checkout/cart_.*"));
        assertThat(checkoutPage.getPayNowButton()).not().isVisible();
    }

    private void fillShippingAddress() {
        checkoutPage.selectCountry("US");
        checkoutPage.fillAddress(address);
        checkoutPage.fillCity(city);
        checkoutPage.selectState(state);
        checkoutPage.fillZipCode(zipCode);
    }
}