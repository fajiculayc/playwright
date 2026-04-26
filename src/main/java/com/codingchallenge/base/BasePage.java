// BasePage.java
package com.codingchallenge.base;

import com.microsoft.playwright.Page;

public class BasePage {
    //Fields
    protected final Page page;

    //Constructor
    public BasePage(Page page) {
        this.page = page;
    }

    //Actions
    public void navigate(String url) {
        page.navigate(url);
    }
}