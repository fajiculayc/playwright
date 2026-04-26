package com.codingchallenge.pages.checkout;

import com.codingchallenge.base.BasePage;
import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;

public class CheckoutPage extends BasePage {

    // Fields
    private final Locator checkoutAddressSection;
    private final Locator savedAddressName;
    private final Locator savedAddressDetails;
    private final Locator useDifferentAddressRadioButton;
    private final Locator shippingAddressHeader;
    private final Locator countryDropdown;
    private final Locator firstNameTextField;
    private final Locator lastNameTextField;
    private final Locator addressTextField;
    private final Locator cityTextField;
    private final Locator stateDropdown;
    private final Locator zipPostalCodeTextField;
    private final Locator standardShippingMethodRadioButton;
    private final Locator premiumShippingMethodRadioButton;
    private final Locator paymentMethodHeader;
    private final Locator sameAsShippingAddressCheckbox;
    private final Locator payNowButton;
    private final Locator productNameText;
    private final Locator productCostWithTaxValueText;
    private final Locator productSubtotalValueText;
    private final Locator productShippingCostValueText;
    private final Locator productTaxValueText;
    private final Locator productTotalValueText;

    // Constructor
    public CheckoutPage(Page page) {
        super(page);
        this.checkoutAddressSection = page.locator("#checkout-section-address");
        this.savedAddressName = page.locator("#checkout-section-address")
                .locator("span.text-sm.text-gray-900").first();
        this.savedAddressDetails = page.locator("#checkout-section-address")
                .locator("p.text-sm.text-gray-500").first();
        this.useDifferentAddressRadioButton = page.locator("#checkout-section-address")
                .locator("label").filter(
                        new Locator.FilterOptions().setHasText("Use a different address"));
        this.shippingAddressHeader = page.getByRole(AriaRole.HEADING,
                new Page.GetByRoleOptions().setName("Shipping Address"));
        this.countryDropdown = page.locator("#ship-country");
        this.firstNameTextField = page.locator("#ship-first_name");
        this.lastNameTextField = page.locator("#ship-last_name");
        this.addressTextField = page.locator("#ship-address1");
        this.cityTextField = page.locator("#ship-city");
        this.stateDropdown = page.locator("#ship-state");
        this.zipPostalCodeTextField = page.locator("#ship-postal_code");
        this.standardShippingMethodRadioButton = page.locator("#checkout-section-shipping")
                .locator("label").filter(
                        new Locator.FilterOptions().setHasText("Standard"));
        this.premiumShippingMethodRadioButton = page.locator("#checkout-section-shipping")
                .locator("label").filter(
                        new Locator.FilterOptions().setHasText("Premium"));
        this.paymentMethodHeader = page.getByRole(AriaRole.HEADING,
                new Page.GetByRoleOptions().setName("Payment Method"));
        this.sameAsShippingAddressCheckbox = page.locator("label").filter(
                new Locator.FilterOptions().setHasText("Same as shipping address"));
        this.payNowButton = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Pay Now"));
        this.productNameText = page.locator("p.text-sm.font-medium.text-gray-900");
        this.productCostWithTaxValueText = page.locator("div.text-sm.text-gray-900").first();
        this.productSubtotalValueText = page.locator("div.flex.justify-between.text-sm")
                .filter(new Locator.FilterOptions().setHasText("Subtotal"))
                .locator("span.text-gray-900");
        this.productShippingCostValueText = page.locator("div.flex.justify-between.text-sm")
                .filter(new Locator.FilterOptions().setHasText("Shipping"))
                .locator("span.text-gray-900");
        this.productTaxValueText = page.locator("div.flex.justify-between.text-sm")
                .filter(new Locator.FilterOptions().setHasText("Tax"))
                .locator("span.text-gray-900");
        this.productTotalValueText = page.locator("span.text-xl.font-bold.text-gray-900");
    }

    // Getters
    public Locator getCheckoutAddressSection() {
        return checkoutAddressSection;
    }

    public Locator getSavedAddressName() {
        return savedAddressName;
    }

    public Locator getSavedAddressDetails() {
        return savedAddressDetails;
    }

    public Locator getUseDifferentAddressRadioButton() {
        return useDifferentAddressRadioButton;
    }

    public Locator getShippingAddressHeader() {
        return shippingAddressHeader;
    }

    public Locator getCountryDropdown() {
        return countryDropdown;
    }

    public Locator getFirstNameTextField() {
        return firstNameTextField;
    }

    public Locator getLastNameTextField() {
        return lastNameTextField;
    }

    public Locator getAddressTextField() {
        return addressTextField;
    }

    public Locator getCityTextField() {
        return cityTextField;
    }

    public Locator getStateDropdown() {
        return stateDropdown;
    }

    public Locator getZipPostalCodeTextField() {
        return zipPostalCodeTextField;
    }

    public Locator getStandardShippingMethodRadioButton() {
        return standardShippingMethodRadioButton;
    }

    public Locator getPremiumShippingMethodRadioButton() {
        return premiumShippingMethodRadioButton;
    }

    public Locator getPaymentMethodHeader() {
        return paymentMethodHeader;
    }

    public Locator getSameAsShippingAddressCheckbox() {
        return sameAsShippingAddressCheckbox;
    }

    public Locator getPayNowButton() {
        return payNowButton;
    }

    public Locator getProductNameText() {
        return productNameText;
    }

    public Locator getProductCostWithTaxValueText() {
        return productCostWithTaxValueText;
    }

    public Locator getProductSubtotalValueText() {
        return productSubtotalValueText;
    }

    public Locator getProductShippingCostValueText() {
        return productShippingCostValueText;
    }

    public Locator getProductTaxValueText() {
        return productTaxValueText;
    }

    public Locator getProductTotalValueText() {
        return productTotalValueText;
    }

    public Locator getGeneralCardError() {
        return page.locator("[data-slot='alert-description']");
    }


    public Locator getCardNumberField() {
        return stripeFrame().getByPlaceholder("1234 1234 1234");
    }

    public Locator getExpirationDateField() {
        return stripeFrame().locator("#payment-expiryInput");
    }

    public Locator getSecurityCodeField() {
        return stripeFrame().getByPlaceholder("CVC");
    }

    public String getContactEmailValue() {
        return (String) page.evaluate(
                "document.querySelector('input#email').value");
    }

    public String getFirstNameValue() {
        return (String) page.evaluate(
                "document.querySelector('#ship-first_name').value");
    }

    public String getLastNameValue() {
        return (String) page.evaluate(
                "document.querySelector('#ship-last_name').value");
    }

    public String getSavedAddressNameText() {
        return savedAddressName.innerText();
    }

    public String getSavedAddressDetailsText() {
        return savedAddressDetails.innerText();
    }

    public String getProductNameString() {
        return productNameText.innerText();
    }

    public double getProductCostWithTaxValue() {
        return Double.parseDouble(productCostWithTaxValueText.innerText()
                .replace("$", "").replace(",", "").trim());
    }

    public double getProductSubtotalValue() {
        return Double.parseDouble(productSubtotalValueText.innerText()
                .replace("$", "").replace(",", "").trim());
    }

    public double getProductShippingCostValue() {
        return Double.parseDouble(productShippingCostValueText.innerText()
                .replace("$", "").replace(",", "").trim());
    }

    public double getProductTaxValue() {
        return Double.parseDouble(productTaxValueText.innerText()
                .replace("$", "").replace(",", "").trim());
    }

    public double getProductTotalValue() {
        return Double.parseDouble(productTotalValueText.innerText()
                .replace("$", "").replace(",", "").trim());
    }

    private FrameLocator stripeFrame() {
        return page.frameLocator("iframe[title='Secure payment input frame']").first();
    }

    public Locator getCardCountryDropdown() {
        return stripeFrame().locator("#payment-countryInput");
    }

    // Actions
    public void clickUseDifferentAddress() {
        useDifferentAddressRadioButton.click();
    }

    public void selectCountry(String countryCode) {
        countryDropdown.selectOption(countryCode);
    }

    public void fillAddress(String address) {
        addressTextField.fill(address);
    }

    public void fillCity(String city) {
        cityTextField.fill(city);
    }

    public void selectState(String stateCode) {
        stateDropdown.selectOption(stateCode);
    }

    public void fillZipCode(String zipCode) {
        zipPostalCodeTextField.pressSequentially(zipCode,
            new Locator.PressSequentiallyOptions().setDelay(100));
        zipPostalCodeTextField.press("Tab");
    }

    public void selectStandardShipping() {
        standardShippingMethodRadioButton.click();
    }

    public void selectPremiumShipping() {
        premiumShippingMethodRadioButton.click();
    }

    public void selectShippingMethod(String method) {
        if (method.equalsIgnoreCase("Standard")) {
            selectStandardShipping();
        } else if (method.equalsIgnoreCase("Premium")) {
            selectPremiumShipping();
        }
    }

    public void fillCardNumber(String cardNumber) {
        stripeFrame().getByPlaceholder("1234 1234 1234").fill(cardNumber);
    }

    public void fillExpirationDate(String expiration) {
        stripeFrame().getByPlaceholder("MM / YY").fill(expiration);
    }

    public void fillSecurityCode(String cvc) {
        stripeFrame().getByPlaceholder("CVC").fill(cvc);
    }

    public void fillCardCountry(String country) {
        stripeFrame().locator("#payment-countryInput").selectOption(country);
    }

    public void fillCardDetails(String cardNumber, String expiration, String cvc) {
        fillCardNumber(cardNumber);
        fillExpirationDate(expiration);
        fillSecurityCode(cvc);
    }

    public void fillCardZipCode(String zipCode) {
        stripeFrame().locator("#payment-postalCodeInput")
            .pressSequentially(zipCode, 
                new Locator.PressSequentiallyOptions().setDelay(100));
    }

    public void checkSameAsShippingAddress() {
        if (!sameAsShippingAddressCheckbox.isChecked()) {
            sameAsShippingAddressCheckbox.click();
        }
    }

    public OrderConfirmationPage clickPayNow() {
        payNowButton.click();
        page.waitForURL("**/order-placed/**", 
            new Page.WaitForURLOptions().setTimeout(30000));
        return new OrderConfirmationPage(page);
    }

    public void waitForShippingSection() {
        try {
            page.locator("#checkout-section-shipping").waitFor(
                new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(60000));
            page.locator("#checkout-section-shipping")
                .locator("label").first()
                .waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(60000));
        } catch (Exception e) {
            page.frameLocator("iframe[title='Secure payment input frame']").first()
                .getByTestId("afterpay_clearpay").click();
            page.waitForTimeout(1000);
            page.frameLocator("iframe[title='Secure payment input frame']").first()
                .getByTestId("card").click();
            page.waitForTimeout(1000);

            page.locator("#checkout-section-shipping").waitFor(
                new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(60000));
            page.locator("#checkout-section-shipping")
                .locator("label").first()
                .waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(60000));
        }
    }

    public OrderConfirmationPage completeCheckout(
            String countryCode, String address, String city,
            String stateCode, String zipCode, String shippingMethod,
            String cardNumber, String expiration,
            String securityCode, String cardCountry) {
        selectCountry(countryCode);
        fillAddress(address);
        fillCity(city);
        selectState(stateCode);
        fillZipCode(zipCode);
        waitForShippingSection();
        selectShippingMethod(shippingMethod);
        fillCardDetails(cardNumber, expiration, securityCode);
        fillCardCountry(cardCountry);         
        fillCardZipCode(zipCode);
        checkSameAsShippingAddress();
        return clickPayNow();
    }

    public void waitForLoad() {
        page.waitForURL("**/checkout/cart_**");
        page.waitForLoadState();
        page.waitForFunction("() => document.querySelector('#checkout-section-address') !== null");
    }

    public boolean isOnCheckoutPage() {
        return page.url().contains("/checkout/cart_");
    }

    public void waitForPaymentForm() {
        page.getByText("Loading payment form...").waitFor(
            new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
    }
}