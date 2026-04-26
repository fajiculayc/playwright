package com.codingchallenge.pages.products;

import com.codingchallenge.base.BasePage;
import com.codingchallenge.components.CartComponent;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;

public class ProductDetailsPage extends BasePage {

    // Fields
    private final Locator productPrice;
    private final Locator productInStock;
    private final Locator addToCartButton;
    private final Locator addingToCartButton;
    private final Locator increaseQuantityButton;
    private final Locator decreaseQuantityButton;
    private final Locator quantityDisplay;
    private final Locator selectedColorLabel;
    private int addToCartClickCount = 0;

    // Constructor
    public ProductDetailsPage(Page page) {
        super(page);
        this.productPrice = page.locator("div.mt-4 span.text-3xl.font-bold.text-gray-900").first();
        this.productInStock = page.locator("span.text-green-600");
        this.addToCartButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add to Cart"));
        this.addingToCartButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Adding..."));
        this.increaseQuantityButton = page.getByLabel("Increase quantity");
        this.decreaseQuantityButton = page.getByLabel("Decrease quantity");
        this.quantityDisplay = page.locator("span.tabular-nums");
        this.selectedColorLabel = page.locator("div.flex.items-center.justify-between.mb-3 span.text-sm.text-gray-500");
    }

    // Getters
    public Locator getProductName() {
        return page.locator("h1.text-3xl.font-bold.text-gray-900").first();
    }

    public Locator getProductPrice() {
        return productPrice;
    }

    public Locator getProductInStock() {
        return productInStock;
    }

    public Locator getAddToCartButton() {
        return addToCartButton;
    }

    public Locator getAddingToCartButton() {
        return addingToCartButton;
    }

    public Locator getIncreaseQuantityButton() {
        return increaseQuantityButton;
    }

    public Locator getDecreaseQuantityButton() {
        return decreaseQuantityButton;
    }

    public Locator getQuantityDisplay() {
        return quantityDisplay;
    }

    public Locator getSelectedColorLabel() {
        return selectedColorLabel;
    }

    public Locator getColorButtonByTitle(String colorName) {
        return page.locator("button[title='" + colorName + "']");
    }

    public String getSelectedColor() {
        return selectedColorLabel.innerText();
    }

    public int getColorOptionsCount() {
        return page.locator("button[title]").count();
    }

    public String getColorNameByIndex(int index) {
        return page.locator("button[title]").nth(index).getAttribute("title");
    }

    public int getAddToCartClickCount() {
        return addToCartClickCount;
    }

    public int getQuantity() {
        return Integer.parseInt(quantityDisplay.innerText().trim());
    }

    // Actions
    public boolean isProductInStock() {
        return productInStock.isVisible();
    }

    public boolean isAddingToCart() {
        return addingToCartButton.isVisible();
    }

    public boolean isAddToCartReady() {
        return addToCartButton.isVisible() && addToCartButton.isEnabled();
    }

    public CartComponent clickAddToCartButton() {
        if (!productInStock.isVisible()) {
            throw new IllegalStateException("Cannot add to cart - product is out of stock");
        }
        addToCartButton.click();
        addToCartClickCount++;
        addingToCartButton.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.HIDDEN));
        return new CartComponent(page);
    }

    public CartComponent getCart() {
        return new CartComponent(page);
    }

    public void increaseQuantity() {
        increaseQuantityButton.click();
    }

    public void decreaseQuantity() {
        decreaseQuantityButton.click();
    }

    public void selectColor(String colorName) {
        page.locator("button[title='" + colorName + "']").click();
    }

    public String selectRandomColor() {
        int colorCount = getColorOptionsCount();
        int randomIndex = (int) (Math.random() * colorCount);
        String colorName = getColorNameByIndex(randomIndex);
        selectColor(colorName);
        return colorName;
    }
}