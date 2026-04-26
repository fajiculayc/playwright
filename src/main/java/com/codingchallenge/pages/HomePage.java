package com.codingchallenge.pages;

import com.codingchallenge.base.BasePage;
import com.codingchallenge.pages.auth.SignInPage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class HomePage extends BasePage {

    private static final String URL = "https://demo.spreecommerce.org/us/en";
    // Fields
    private final Locator accountButton;
    private final Locator shopAllButton;

    //Constructor
    public HomePage(Page page){
        super(page);
        this.accountButton = page.getByLabel("Account");
        this.shopAllButton = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Shop All"));
    }

    //Navigate
    public void navigate(){
        super.navigate(URL);
    }
    
    //Getters
    public Locator getAccountButton(){
        return accountButton;
    }

    public Locator getShopAllButton(){
        return shopAllButton;
    }

    //Actions
    public boolean isAccountButtonVisible(){
        return accountButton.isVisible();
    }

    public SignInPage clickAccountButton(){
        accountButton.click();
        return new SignInPage(page);
    }

    public void clickShopAllButton(){
        shopAllButton.click();
    }
    
}
