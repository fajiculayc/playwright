package com.codingchallenge.pages.checkout;

import com.codingchallenge.base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class OrderConfirmationPage extends BasePage {

    // Fields
    private final Locator orderNumber;
    private final Locator orderItemsHeader;
    private final Locator subtotalValue;
    private final Locator shippingValue;
    private final Locator taxValue;
    private final Locator totalValue;
    private final Locator shippingMethodSection;
    private final Locator paymentSection;
    private final Locator shippingAddressSection;
    private final Locator billingAddressSection;
    private final Locator continueShoppingButton;

    // Constructor
    public OrderConfirmationPage(Page page) {
        super(page);
        this.orderNumber = page.locator("p.text-gray-500").filter(
                new Locator.FilterOptions().setHasText("Order #"));
        this.orderItemsHeader = page.getByRole(AriaRole.HEADING,
                new Page.GetByRoleOptions().setName("Order Items"));
        this.subtotalValue = page.locator("div.space-y-2 div.flex.justify-between.text-sm")
                .filter(new Locator.FilterOptions().setHasText("Subtotal"))
                .locator("span.text-gray-900");
        this.shippingValue = page.locator("div.space-y-2 div.flex.justify-between.text-sm")
                .filter(new Locator.FilterOptions().setHasText("Shipping"))
                .locator("span.text-gray-900");
        this.taxValue = page.locator("div.space-y-2 div.flex.justify-between.text-sm")
                .filter(new Locator.FilterOptions().setHasText("Tax"))
                .locator("span.text-gray-900");
        this.totalValue = page.locator("div.flex.justify-between.pt-2")
                .locator("span.font-semibold.text-gray-900").last();
        this.shippingMethodSection = page.getByRole(AriaRole.HEADING,
                new Page.GetByRoleOptions().setName("Shipping Method"));
        this.paymentSection = page.getByRole(AriaRole.HEADING,
                new Page.GetByRoleOptions().setName("Payment"));
        this.shippingAddressSection = page.getByRole(AriaRole.HEADING,
                new Page.GetByRoleOptions().setName("Shipping Address"));
        this.billingAddressSection = page.getByRole(AriaRole.HEADING,
                new Page.GetByRoleOptions().setName("Billing Address"));
        this.continueShoppingButton = page.getByRole(AriaRole.LINK,
                new Page.GetByRoleOptions().setName("Continue Shopping"));
    }

    // Getters
    public Locator getOrderNumber() {
        return orderNumber;
    }

    public Locator getOrderItemsHeader() {
        return orderItemsHeader;
    }

    public Locator getSubtotalValue() {
        return subtotalValue;
    }

    public Locator getShippingValue() {
        return shippingValue;
    }

    public Locator getTaxValue() {
        return taxValue;
    }

    public Locator getTotalValue() {
        return totalValue;
    }

    public Locator getShippingMethodSection() {
        return shippingMethodSection;
    }

    public Locator getPaymentSection() {
        return paymentSection;
    }

    public Locator getShippingAddressSection() {
        return shippingAddressSection;
    }

    public Locator getBillingAddressSection() {
        return billingAddressSection;
    }

    public Locator getContinueShoppingButton() {
        return continueShoppingButton;
    }

    // Dynamic getters
    public Locator getThankYouMessage(String firstName) {
        return page.getByText("Thanks for your order, " + firstName + "!");
    }

    public Locator getEmailConfirmation(String email) {
        return page.getByText(email);
    }

    public Locator getPaymentInfo(String lastFourDigits) {
        return page.getByText("Visa ending in " + lastFourDigits);
    }

    // String value getters
    public double getSubtotalValue_() {
        return Double.parseDouble(subtotalValue.innerText()
                .replace("$", "").replace(",", "").trim());
    }

    public double getShippingValue_() {
        return Double.parseDouble(shippingValue.innerText()
                .replace("$", "").replace(",", "").trim());
    }

    public double getTaxValue_() {
        return Double.parseDouble(taxValue.innerText()
                .replace("$", "").replace(",", "").trim());
    }

    public double getTotalValue_() {
        return Double.parseDouble(totalValue.innerText()
                .replace("$", "").replace(",", "").trim());
    }

    // Actions
    public void waitForLoad() {
        page.waitForURL("**/order-placed/**");
        page.waitForFunction(
            "() => document.querySelector('h1')?.innerText?.includes('Thanks for your order')"
        );
    }

    public boolean isOnOrderConfirmationPage() {
        return page.url().contains("/order-placed/");
    }

    public void clickContinueShopping() {
        continueShoppingButton.click();
        page.waitForLoadState();
    }
}