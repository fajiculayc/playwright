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
import com.codingchallenge.pages.products.ProductDetailsPage;
import com.codingchallenge.pages.products.ProductsPage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.WaitForSelectorState;

public class CheckoutPageTest extends BaseTest {

    // Fields
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
        signInAs(testEmail, password);
        productsPage = new ProductsPage(page);
        productsPage.navigate();
        page.waitForLoadState();
        productsPage.getProduct(0).waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE));
        productsPage.scrollUntilAllProductsLoaded();
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
    }

    @Test
    void shouldLoadCheckoutPageSuccessfully() {
        assertTrue(checkoutPage.isOnCheckoutPage());
        assertThat(page).hasURL(Pattern.compile(".*/checkout/cart_.*"));
    }

    @Test
    void shouldPrePopulateContactEmailWithLoggedInUser() {
        assertEquals(testEmail, checkoutPage.getContactEmailValue());
    }

    @Test
    void shouldDisplayShippingAddressHeader() {
        assertThat(checkoutPage.getShippingAddressHeader()).isVisible();
    }

    @Test
    void shouldPrePopulateFirstName() {
        assertEquals(firstName, checkoutPage.getFirstNameValue());
    }

    @Test
    void shouldPrePopulateLastName() {
        assertEquals(lastName, checkoutPage.getLastNameValue());
    }

    @Test
    void shouldDisplayAddressTextField() {
        assertThat(checkoutPage.getAddressTextField()).isVisible();
    }

    @Test
    void shouldDisplayCityTextField() {
        assertThat(checkoutPage.getCityTextField()).isVisible();
    }

    @Test
    void shouldDisplayStateDropdown() {
        assertThat(checkoutPage.getStateDropdown()).isVisible();
    }

    @Test
    void shouldDisplayZipPostalCodeTextField() {
        assertThat(checkoutPage.getZipPostalCodeTextField()).isVisible();
    }

    @Test
    void shouldDisplayShippingMethods() {
        checkoutPage.selectCountry("US");
        checkoutPage.fillAddress(address);
        checkoutPage.fillCity(city);
        checkoutPage.selectState(state);
        checkoutPage.fillZipCode(zipCode);
        checkoutPage.waitForShippingSection();
        assertThat(checkoutPage.getStandardShippingMethodRadioButton()).isVisible();
        assertThat(checkoutPage.getPremiumShippingMethodRadioButton()).isVisible();
    }

    @Test
    void shouldSelectShippingMethod() {
        checkoutPage.selectCountry("US");
        checkoutPage.fillAddress(address);
        checkoutPage.fillCity(city);
        checkoutPage.selectState(state);
        checkoutPage.fillZipCode(zipCode);
        checkoutPage.waitForShippingSection();
        checkoutPage.selectShippingMethod(shippingMethod);
        if (shippingMethod.equalsIgnoreCase("Standard")) {
            assertThat(checkoutPage.getStandardShippingMethodRadioButton()).isVisible();
        } else {
            assertThat(checkoutPage.getPremiumShippingMethodRadioButton()).isVisible();
        }
    }

    @Test
    void shouldDisplayPaymentMethodHeader() {
        assertThat(checkoutPage.getPaymentMethodHeader()).isVisible();
    }

    @Test
    void shouldDisplaySameAsShippingAddressCheckbox() {
        assertThat(checkoutPage.getSameAsShippingAddressCheckbox()).isVisible();
    }

    @Test
    void shouldShowCardNumberErrorWhenPayNowClickedWithEmptyCard() {
        assertThat(checkoutPage.getCardNumberField()).isVisible();
        checkoutPage.clickPayNow();
        page.waitForTimeout(1000);
        assertThat(checkoutPage.getGeneralCardError()).isVisible();
        assertThat(checkoutPage.getGeneralCardError()).containsText("Your card number is incomplete.");
    }

    @Test
    void shouldShowExpirationDateErrorWhenPayNowClickedWithEmptyCard() {
        checkoutPage.waitForPaymentForm();
        checkoutPage.clickPayNow();
        page.waitForTimeout(1000);
        assertThat(checkoutPage.getGeneralCardError()).isVisible();
        assertThat(checkoutPage.getGeneralCardError()).containsText("Your card number is incomplete.");
    }

    @Test
    void shouldShowSecurityCodeErrorWhenPayNowClickedWithEmptyCard() {
        checkoutPage.waitForPaymentForm();
        checkoutPage.clickPayNow();
        page.waitForTimeout(1000);
        assertThat(checkoutPage.getGeneralCardError()).isVisible();
        assertThat(checkoutPage.getGeneralCardError()).containsText("Your card number is incomplete.");
    }

    @Test
    void shouldDisplayProductNameInOrderSummary() {
        assertThat(checkoutPage.getProductNameText()).isVisible();
    }

    @Test
    void shouldDisplaySubtotalInOrderSummary() {
        assertThat(checkoutPage.getProductSubtotalValueText()).isVisible();
    }

    @Test
    void shouldDisplayShippingCostInOrderSummary() {
        assertThat(checkoutPage.getProductShippingCostValueText()).isVisible();
    }

    @Test
    void shouldDisplayTaxInOrderSummary() {
        assertThat(checkoutPage.getProductTaxValueText()).isVisible();
    }

    @Test
    void shouldDisplayTotalInOrderSummary() {
        assertThat(checkoutPage.getProductTotalValueText()).isVisible();
    }

    @Test
    void shouldDisplayCorrectTotalValue() {
        double subtotal = checkoutPage.getProductSubtotalValue();
        double shipping = checkoutPage.getProductShippingCostValue();
        double tax = checkoutPage.getProductTaxValue();
        double expectedTotal = subtotal + shipping + tax;
        double actualTotal = checkoutPage.getProductTotalValue();
        assertEquals(expectedTotal, actualTotal, 0.01);
    }

    @Test
    void shouldCompleteCheckoutWithValidCard() {
        assertThat(page).hasURL(Pattern.compile(".*/checkout/cart_.*"));
        assertThat(checkoutPage.getPayNowButton()).isVisible();
        checkoutPage.completeCheckout(
                "US",
                address,
                city,
                state,
                zipCode,
                shippingMethod,
                cardNumber,
                expirationDate,
                securityCode,
                cardCountry
        );
        assertThat(page).hasURL(Pattern.compile(".*/checkout/cart_.*"));
        assertThat(checkoutPage.getPayNowButton()).not().isVisible();        
    }
}