package com.codingchallenge.pages.account;

import com.codingchallenge.base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class AccountPage extends BasePage {

    private static final String URL = "https://demo.spreecommerce.org/us/en/account";

    private final Locator accountOverviewHeader;
    private final Locator userFullNameText;
    private final Locator userEmailText;
    private final Locator signOutButton;

    public AccountPage(Page page) {
        super(page);
        this.accountOverviewHeader = page.getByRole(AriaRole.HEADING,
                new Page.GetByRoleOptions().setName("Account Overview"));
        this.userFullNameText = page.locator(".p-4.border-b p:first-child");
        this.userEmailText = page.locator(".p-4.border-b p:last-child");
        this.signOutButton = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Sign Out"));
    }

    public void navigate() {
        super.navigate(URL);
    }

    public Locator getAccountOverviewHeader() {
        return accountOverviewHeader;
    }

    public Locator getUserFullName() {
        return userFullNameText;
    }

    public Locator getUserEmail() {
        return userEmailText;
    }

    public Locator getSignOutButton() {
        return signOutButton;
    }

    public void clickSignOutButton() {
        signOutButton.click();
    }
}