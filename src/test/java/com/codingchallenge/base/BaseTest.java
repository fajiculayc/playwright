package com.codingchallenge.base;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import java.io.InputStreamReader;
import java.util.regex.Pattern;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import com.codingchallenge.pages.auth.SignInPage;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;
import com.opencsv.CSVReader;

public class BaseTest {

    protected static Playwright playwright;
    protected static Browser browser;
    protected static BrowserContext context;
    protected static Page page;

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
            new BrowserType.LaunchOptions()
                .setHeadless(false)
                .setSlowMo(500)
        );
    }

    @AfterAll
    static void closeBrowser() {
        playwright.close();
    }

    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterEach
    void closeContext() {
        context.close();
    }

    protected void signInAs(String email, String password) {
        SignInPage signInPage = new SignInPage(page);
        signInPage.navigate();
        signInPage.signIn(email, password);
        page.waitForURL("**/account");
        page.waitForLoadState();   
        assertThat(page).hasURL(Pattern.compile(".*/account$"));
        assertThat(page.getByRole(AriaRole.HEADING, 
        new Page.GetByRoleOptions().setName("Account Overview"))).isVisible();
    }

    protected String[] getFirstRowFromCsv(String csvPath) {
        try (CSVReader reader = new CSVReader(new InputStreamReader(
                getClass().getResourceAsStream(csvPath)))) {
            reader.skip(1); // skip header
            return reader.readNext();
        } catch (Exception e) {
            throw new RuntimeException("Failed to read CSV: " + csvPath, e);
        }
    }
}
