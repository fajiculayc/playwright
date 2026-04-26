package com.codingchallenge.tests.smoke;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.codingchallenge.base.BaseTest;
import com.codingchallenge.components.CartComponent;
import com.codingchallenge.pages.HomePage;
import com.codingchallenge.pages.account.AccountPage;
import com.codingchallenge.pages.auth.SignInPage;
import com.codingchallenge.pages.auth.SignUpPage;
import com.codingchallenge.pages.checkout.CheckoutPage;
import com.codingchallenge.pages.checkout.OrderConfirmationPage;
import com.codingchallenge.pages.products.ProductDetailsPage;
import com.codingchallenge.pages.products.ProductsPage;

public class SmokeTest extends BaseTest {

    private String uniqueEmail;
    private String password;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String shippingMethod;
    private String cardNumber;
    private String expirationDate;
    private String securityCode;
    private String cardCountry;

    @BeforeEach
    void setUpTestData() {
        // Unique email prevents duplicate sign up errors on each run
        uniqueEmail = "smoketest_" + System.currentTimeMillis() + "@email.com";
        password = "Password123!";
        firstName = "Smoke";
        lastName = "Test";
        address = "344 Clinton Street";
        city = "New York";
        state = "NY";
        zipCode = "10028";
        shippingMethod = "Standard";
        cardNumber = "4242 4242 4242 4242";
        expirationDate = "12/30";
        securityCode = "123";
        cardCountry = "US";
    }

    @Test
    @DisplayName("Full purchase flow should complete successfully from sign up to order confirmation")
    void shouldCompleteFullPurchaseFlow() {

        // Step 1: Navigate to store
        HomePage homePage = new HomePage(page);
        homePage.navigate();

        assertThat(page).hasURL("https://demo.spreecommerce.org/us/en");
        assertThat(page).hasTitle("Spree Commerce Demo | Next.js Ecommerce Storefront");
        assertThat(homePage.getAccountButton()).isVisible();

        // Step 2: Sign up as new user
        homePage.clickAccountButton();
        assertThat(page).hasURL("https://demo.spreecommerce.org/us/en/account");
        assertThat(page).hasTitle("Spree Commerce Demo | Next.js Ecommerce Storefront | Spree");

        SignInPage signInPage = new SignInPage(page);
        SignUpPage signUpPage = signInPage.clickSignUpLink();

        assertThat(page).hasURL("https://demo.spreecommerce.org/us/en/account/register");
        assertThat(page).hasTitle("Spree Commerce Demo | Next.js Ecommerce Storefront | Spree");
        assertThat(signUpPage.getCreateAccountHeader()).isVisible();

        signUpPage.signUp(firstName, lastName, uniqueEmail, password, password, true);
        assertThat(page).hasURL("https://demo.spreecommerce.org/us/en/account");

        // Step 3: Verify logged in
        AccountPage accountPage = new AccountPage(page);
        assertThat(page).hasTitle("Spree Commerce Demo | Next.js Ecommerce Storefront | Spree");
        assertThat(accountPage.getAccountOverviewHeader()).isVisible();
        assertThat(accountPage.getUserEmail()).containsText(uniqueEmail);

        // Step 4: Browse products and find in-stock product
        ProductsPage productsPage = new ProductsPage(page);
        productsPage.navigate();

        assertThat(page).hasURL("https://demo.spreecommerce.org/us/en/products");
        assertThat(page).hasTitle("Products | Spree Commerce Demo | Next.js Ecommerce Storefront");
        assertThat(productsPage.getAllProductsHeader()).isVisible();
        assertTrue(productsPage.getExpectedProductCount() > 0);

        productsPage.scrollUntilAllProductsLoaded();
        ProductDetailsPage productDetailsPage = findInStockProduct(productsPage);

        // Step 5: Verify product details page
        assertThat(page).hasURL(Pattern.compile(".*/products/.*"));
        assertThat(productDetailsPage.getProductName()).isVisible();
        assertThat(productDetailsPage.getProductPrice()).isVisible();
        assertThat(productDetailsPage.getAddToCartButton()).isVisible();
        assertTrue(productDetailsPage.isProductInStock());

        // Select a random color and verify it updates the label
        String selectedColor = productDetailsPage.selectRandomColor();
        assertThat(productDetailsPage.getSelectedColorLabel()).hasText(selectedColor);

        String productName = productDetailsPage.getProductName().innerText();
        int quantityBeforeAddingToCart = productDetailsPage.getQuantity();

        // Step 6: Add product to cart and verify cart details
        CartComponent cart = productDetailsPage.clickAddToCartButton();

        assertThat(cart.getCartDialog()).isVisible();
        assertThat(cart.getCartTitle()).containsText(quantityBeforeAddingToCart + " item");
        assertThat(cart.getCartProductName()).isVisible();
        assertThat(cart.getCartProductName()).containsText(productName);
        assertThat(cart.getCartProductColor()).containsText(selectedColor);
        assertThat(cart.getCartProductPrice()).isVisible();
        assertEquals(quantityBeforeAddingToCart, cart.getCartQuantity());

        // Step 7: Proceed to checkout
        CheckoutPage checkoutPage = cart.clickCheckout();
        checkoutPage.waitForLoad();

        assertThat(page).hasURL(Pattern.compile(".*/checkout/cart_.*"));
        assertTrue(checkoutPage.isOnCheckoutPage());
        assertThat(checkoutPage.getShippingAddressHeader()).isVisible();

        String checkoutUrl = page.url();
        String cartId = checkoutUrl.substring(checkoutUrl.lastIndexOf("/") + 1);

        // Step 7a: Verify contact email
        assertEquals(uniqueEmail, checkoutPage.getContactEmailValue());

        // Step 7b: Fill shipping address
        assertThat(checkoutPage.getAddressTextField()).isVisible();
        assertThat(checkoutPage.getCityTextField()).isVisible();
        assertThat(checkoutPage.getStateDropdown()).isVisible();
        assertThat(checkoutPage.getZipPostalCodeTextField()).isVisible();

        checkoutPage.selectCountry("US");
        checkoutPage.fillAddress(address);
        checkoutPage.fillCity(city);
        checkoutPage.selectState(state);
        checkoutPage.fillZipCode(zipCode);

        // Step 7c: Select shipping method — retry with page reload if not visible
        checkoutPage.waitForShippingSection();
        if (!checkoutPage.getStandardShippingMethodRadioButton().isVisible() ||
                !checkoutPage.getPremiumShippingMethodRadioButton().isVisible()) {
            page.reload();
            checkoutPage.waitForLoad();
            checkoutPage.selectCountry("US");
            checkoutPage.fillAddress(address);
            checkoutPage.fillCity(city);
            checkoutPage.selectState(state);
            checkoutPage.fillZipCode(zipCode);
            checkoutPage.waitForShippingSection();
        }

        assertThat(checkoutPage.getStandardShippingMethodRadioButton()).isVisible();
        assertThat(checkoutPage.getPremiumShippingMethodRadioButton()).isVisible();
        checkoutPage.selectShippingMethod(shippingMethod);

        // Step 7d: Verify order summary
        assertThat(checkoutPage.getProductNameText()).isVisible();
        assertThat(checkoutPage.getProductSubtotalValueText()).isVisible();
        assertThat(checkoutPage.getProductShippingCostValueText()).isVisible();
        assertThat(checkoutPage.getProductTaxValueText()).isVisible();
        assertThat(checkoutPage.getProductTotalValueText()).isVisible();

        double subtotal = checkoutPage.getProductSubtotalValue();
        double shipping = checkoutPage.getProductShippingCostValue();
        double tax = checkoutPage.getProductTaxValue();
        assertEquals(subtotal + shipping + tax, checkoutPage.getProductTotalValue(), 0.01);

        // Step 7e: Fill payment method
        assertThat(checkoutPage.getPaymentMethodHeader()).isVisible();
        checkoutPage.fillCardDetails(cardNumber, expirationDate, securityCode);
        checkoutPage.fillCardCountry(cardCountry);
        checkoutPage.fillCardZipCode(zipCode);
        assertThat(checkoutPage.getSameAsShippingAddressCheckbox()).isVisible();
        checkoutPage.checkSameAsShippingAddress();

        // Step 7f: Pay Now
        assertThat(checkoutPage.getPayNowButton()).isVisible();
        OrderConfirmationPage orderConfirmationPage = checkoutPage.clickPayNow();

        // Step 8: Verify order confirmation
        orderConfirmationPage.waitForLoad();

        assertTrue(
                page.url().contains(cartId) &&
                page.url().contains("/order-placed/") &&
                checkoutUrl.contains("/checkout/"),
                "Expected URL to contain cart ID '" + cartId + "' and '/order-placed/' " +
                "but was: " + page.url());

        assertTrue(orderConfirmationPage.isOnOrderConfirmationPage());
        assertThat(orderConfirmationPage.getThankYouMessage(firstName)).isVisible();
        assertThat(orderConfirmationPage.getOrderNumber()).isVisible();
        assertThat(orderConfirmationPage.getOrderNumber()).containsText("Order #");
        assertThat(orderConfirmationPage.getOrderItemsHeader()).isVisible();
        assertThat(orderConfirmationPage.getShippingMethodSection()).isVisible();
        assertThat(orderConfirmationPage.getPaymentSection()).isVisible();
        assertThat(orderConfirmationPage.getPaymentInfo("4242")).isVisible();
        assertThat(orderConfirmationPage.getShippingAddressSection()).isVisible();
        assertThat(orderConfirmationPage.getBillingAddressSection()).isVisible();
        assertThat(orderConfirmationPage.getEmailConfirmation(uniqueEmail)).isVisible();
        assertThat(orderConfirmationPage.getContinueShoppingButton()).isVisible();

        assertEquals(
                orderConfirmationPage.getSubtotalAmount() +
                orderConfirmationPage.getShippingAmount() +
                orderConfirmationPage.getTaxAmount(),
                orderConfirmationPage.getTotalAmount(), 0.01);
    }
}