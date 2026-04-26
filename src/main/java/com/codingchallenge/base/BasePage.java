package com.codingchallenge.base;

import com.microsoft.playwright.Page;

public class BasePage {

    protected final Page page;

    public BasePage(Page page) {
        this.page = page;
    }

    public void navigate(String url) {
        page.navigate(url);
    }
}