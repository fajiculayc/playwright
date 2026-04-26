package com.codingchallenge.pages.products;

import com.codingchallenge.base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;

public class ProductsPage extends BasePage {

    private static final String URL = "https://demo.spreecommerce.org/us/en/products";

    private final Locator allProductsHeader;
    private final Locator products;
    private final Locator productCountLabel;
    private final Locator loadingIndicator;
    private final Locator sortButton;

    public ProductsPage(Page page) {
        super(page);
        this.allProductsHeader = page.getByRole(AriaRole.HEADING,
                new Page.GetByRoleOptions().setName("All Products"));
        this.products = page.locator("a.group.block");
        this.productCountLabel = page.locator("span.text-sm.text-gray-500").first();
        this.loadingIndicator = page.getByText("Loading more");
        this.sortButton = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Sort"));
    }

    public void navigate() {
        super.navigate(URL);
    }

    public Locator getAllProductsHeader() {
        return allProductsHeader;
    }

    public Locator getProduct(int index) {
        return products.nth(index);
    }

    public Locator getProductName(int index) {
        return products.nth(index).locator("h3");
    }

    public Locator getProductPrice(int index) {
        return products.nth(index).locator("span.text-lg");
    }

    public String getProductNameText(int index) {
        return products.nth(index).locator("h3").innerText();
    }

    public String getProductPriceText(int index) {
        return products.nth(index).locator("span.text-lg").innerText();
    }

    public String getProductSlug(int index) {
        return getProductNameText(index)
                .toLowerCase()
                .replace(" ", "-");
    }

    public int getCurrentProductsCount() {
        return products.count();
    }

    public Locator getCurrentProductCountLabel() {
        return productCountLabel;
    }

    public int getExpectedProductCount() {
        return Integer.parseInt(productCountLabel.innerText().split(" ")[0]);
    }

    public Locator getSortButton() {
        return sortButton;
    }

    public void scrollUntilAllProductsLoaded() {
        int expectedCount = getExpectedProductCount();
        int maxAttempts = 20;
        int attempts = 0;

        while (getCurrentProductsCount() < expectedCount && attempts < maxAttempts) {
            page.evaluate("window.scrollTo(0, document.body.scrollHeight)");

            try {
                if (loadingIndicator.isVisible()) {
                    loadingIndicator.waitFor(new Locator.WaitForOptions()
                            .setState(WaitForSelectorState.HIDDEN)
                            .setTimeout(30000));
                }
            } catch (Exception e) {
                // Loading indicator timed out - continue scrolling
            }

            page.waitForTimeout(2000);
            attempts++;
        }
    }

    public void scrollToTop() {
        page.evaluate("window.scrollTo(0, 0)");
    }

    public ProductDetailsPage clickProduct(int index) {
        Locator product = products.nth(index);
        product.scrollIntoViewIfNeeded();
        product.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE));
        product.click();
        page.waitForURL("**/products/**");
        return new ProductDetailsPage(page);
    }

    public void clickSortButton() {
        sortButton.click();
    }
}