package com.codingchallenge.components;

import com.codingchallenge.base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class HeaderComponent extends BasePage {

    private final Locator homeLogo;
    private final Locator accountButton;
    private final Locator menuButton;

    public HeaderComponent(Page page) {
        super(page);
        this.homeLogo = page.getByRole(AriaRole.LINK,
                new Page.GetByRoleOptions().setName("Spree").setExact(true));
        this.accountButton = page.getByLabel("Account");
        this.menuButton = page.getByLabel("Open menu");
    }

    public Locator getHomeLogo() {
        return homeLogo;
    }

    public Locator getAccountButton() {
        return accountButton;
    }

    public Locator getMenuButton() {
        return menuButton;
    }
}