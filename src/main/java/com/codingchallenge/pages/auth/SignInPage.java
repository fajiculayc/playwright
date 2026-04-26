package com.codingchallenge.pages.auth;

import com.codingchallenge.base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class SignInPage extends BasePage {

    private static final String URL = "https://demo.spreecommerce.org/us/en/account/";
    //Fields
    private final Locator signUpLink;
    private final Locator emailTextField;
    private final Locator passwordTextField;
    private final Locator showPasswordButton;
    private final Locator hidePasswordButton;
    private final Locator forgotPasswordLink;
    private final Locator signInButton;
    private final Locator myAccountHeader;

    //Constructor
    public SignInPage(Page page){
        super(page);
        this.signUpLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Sign up"));
        this.emailTextField = page.getByPlaceholder("you@example.com");
        this.passwordTextField = page.getByPlaceholder("••••••••");
        this.showPasswordButton = page.getByLabel("Show password");
        this.hidePasswordButton = page.getByLabel("Hide password");
        this.forgotPasswordLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Forgot password?"));
        this.signInButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In"));
        this.myAccountHeader = page.getByRole(AriaRole.MAIN).getByText("My Account");
    }

    //Navigate
    public void navigate(){
        super.navigate(URL);
    }

    //Getter
    public Locator getMyAccountHeader(){
        return myAccountHeader;
    }
    
    //Actions
    public void signIn(String email, String password){
        emailTextField.fill(email != null ? email : "");
        passwordTextField.fill(password != null ? password : "");
        signInButton.click();
    }

    public void inputPassword(String password){
        passwordTextField.fill(password);
    }

    public void togglePasswordVisibility(){
        if(passwordTextField.getAttribute("type").equals("password")){
            showPasswordButton.click();
        } else {
            hidePasswordButton.click();
        }
    }

    public void clickForgotPassword(){
        forgotPasswordLink.click();
    }

    public SignUpPage clickSignUpLink(){
        signUpLink.click();
        return new SignUpPage(page);
    }
}
