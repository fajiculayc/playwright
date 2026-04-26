package com.codingchallenge.components;

import com.codingchallenge.base.BasePage;
import com.codingchallenge.pages.checkout.CheckoutPage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;

public class CartComponent extends BasePage {

    private final Locator cartDialog;
    private final Locator cartTitle;
    private final Locator cartProductName;
    private final Locator cartProductColor;
    private final Locator cartProductPrice;
    private final Locator cartQuantityDisplay;
    private final Locator cartIncreaseQuantityButton;
    private final Locator cartDecreaseQuantityButton;
    private final Locator cartSubtotal;
    private final Locator deleteItemButton;
    private final Locator checkoutButton;
    private final Locator viewCartButton;
    private final Locator closeCartButton;
    private final Locator cartLoadingIcon;

    public CartComponent(Page page) {
        super(page);
        this.cartDialog = page.locator("[role='dialog']");
        this.cartTitle = page.locator("[data-slot='sheet-title']");
        this.cartProductName = page.locator("a.font-medium.text-gray-900.hover\\:text-primary");
        this.cartProductColor = page.locator("p.mt-1.text-sm.text-gray-500");
        this.cartProductPrice = page.locator("div.text-sm.font-medium span.text-gray-900");
        this.cartQuantityDisplay = cartDialog.locator("span.tabular-nums");
        this.cartIncreaseQuantityButton = cartDialog.getByRole(AriaRole.BUTTON,
                new Locator.GetByRoleOptions().setName("Increase quantity"));
        this.cartDecreaseQuantityButton = cartDialog.getByRole(AriaRole.BUTTON,
                new Locator.GetByRoleOptions().setName("Decrease quantity"));
        this.cartLoadingIcon = cartDialog.locator(".w-8");
        this.cartSubtotal = page.locator("div.space-y-2 div.flex.justify-between.items-center")
                .first()
                .locator("span")
                .last();
        this.deleteItemButton = page.locator("[aria-label^='Remove']");
        this.checkoutButton = page.getByRole(AriaRole.LINK,
                new Page.GetByRoleOptions().setName("Checkout"));
        this.viewCartButton = page.getByRole(AriaRole.LINK,
                new Page.GetByRoleOptions().setName("View Cart"));
        this.closeCartButton = page.getByLabel("Close cart");
    }

    public Locator getCartDialog() {
        return cartDialog;
    }

    public Locator getCartTitle() {
        return cartTitle;
    }

    public Locator getCartProductName() {
        return cartProductName;
    }

    public Locator getCartProductColor() {
        return cartProductColor;
    }

    public Locator getCartProductPrice() {
        return cartProductPrice;
    }

    public Locator getCartQuantityDisplay() {
        return cartQuantityDisplay;
    }

    public Locator getCartIncreaseQuantityButton() {
        return cartIncreaseQuantityButton;
    }

    public Locator getCartDecreaseQuantityButton() {
        return cartDecreaseQuantityButton;
    }

    public Locator getCartSubtotal() {
        return cartSubtotal;
    }

    public Locator getDeleteItemButton() {
        return deleteItemButton;
    }

    public Locator getCheckoutButton() {
        return checkoutButton;
    }

    public Locator getViewCartButton() {
        return viewCartButton;
    }

    public String getCartItemCount() {
        return cartTitle.innerText().replaceAll("[^0-9]", "");
    }

    public String getCartProductNameText() {
        return cartProductName.innerText();
    }

    public String getCartProductColorText() {
        return cartProductColor.innerText();
    }

    public double getCartProductPriceValue() {
        return Double.parseDouble(
                cartProductPrice.innerText().trim()
                        .replace("$", "")
                        .replace(",", ""));
    }

    public int getCartQuantity() {
        return Integer.parseInt(
                cartDialog.locator("span.tabular-nums").innerText().trim());
    }

    public double getCartSubtotalValue() {
        return Double.parseDouble(
                cartSubtotal.innerText().trim()
                        .replace("$", "")
                        .replace(",", ""));
    }

    public void increaseQuantity() {
        cartIncreaseQuantityButton.click();
        cartLoadingIcon.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.HIDDEN));
    }

    public void decreaseQuantity() {
        cartDecreaseQuantityButton.click();
        cartLoadingIcon.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.HIDDEN));
    }

    public void deleteItem() {
        deleteItemButton.click();
    }

    public void closeCart() {
        closeCartButton.click();
    }

    public CheckoutPage clickCheckout() {
        checkoutButton.click();
        CheckoutPage checkoutPage = new CheckoutPage(page);
        checkoutPage.waitForLoad();
        return checkoutPage;
    }
}