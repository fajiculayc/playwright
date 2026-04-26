package com.codingchallenge.pages.auth;

import java.util.regex.Pattern;

import com.codingchallenge.base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class SignUpPage extends BasePage{
    
    private static final String URL = "https://demo.spreecommerce.org/us/en/account/register";
    //Fields
    private final Locator signInLink;
    private final Locator createAccountHeader;
    private final Locator firstNameTextField;
    private final Locator lastNameTextField;
    private final Locator emailTextField;
    private final Locator passwordTextField;
    private final Locator showPasswordButton;
    private final Locator hidePasswordButton;
    private final Locator confirmPasswordTextField;
    private final Locator showConfirmPasswordButton;
    private final Locator hideConfirmPasswordButton;
    private final Locator agreeCheckbox;
    private final Locator createAccountButton;
    private final Locator formErrorMessage;

    //Constructor
    public SignUpPage(Page page){
        super(page);
        this.signInLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Sign in"));
        this.createAccountHeader = page.getByText("Create Account").first();
        this.firstNameTextField = page.getByPlaceholder("John");
        this.lastNameTextField = page.getByPlaceholder("Doe");
        this.emailTextField = page.getByLabel("Email");
        this.passwordTextField = page.getByLabel("Password", new Page.GetByLabelOptions().setExact(true));
        this.showPasswordButton = page.getByLabel("Show password").first();
        this.hidePasswordButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Hide password"));
        this.confirmPasswordTextField = page.getByLabel("Confirm Password");
        this.showConfirmPasswordButton = page.locator("div").filter(new Locator.FilterOptions().setHasText(Pattern.compile("^Confirm Password$"))).getByLabel("Show password");
        this.hideConfirmPasswordButton = page.locator("div").filter(new Locator.FilterOptions().setHasText(Pattern.compile("^Confirm Password$"))).getByLabel("Hide password");
        this.agreeCheckbox = page.getByLabel("I agree to the Privacy Policy");
        this.createAccountButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Create Account"));
        this.formErrorMessage = page.locator("[data-slot='alert'] [data-slot='alert-description']");
    }

    //Navigate
    public void navigate(){
        super.navigate(URL);
    }

    //Getters
    public Locator getCreateAccountHeader(){
        return createAccountHeader;
    }
    
    public Locator getPasswordField(){
        return passwordTextField;
    }

    public Locator getConfirmPasswordField(){
        return confirmPasswordTextField;
    }

    public Locator getAgreeCheckbox(){
        return agreeCheckbox;
    }

    public Locator getFormErrorMessage(){
        return formErrorMessage;
    }

    //Actions
    public void signUp(String firstName, String lastName, String email, String password, String confirmPassword, boolean acceptTerms){
        firstNameTextField.fill(firstName != null ? firstName : "");
        lastNameTextField.fill(lastName != null ? lastName : "");
        emailTextField.fill(email != null ? email : "");
        passwordTextField.fill(password != null ? password : "");
        confirmPasswordTextField.fill(confirmPassword != null ? confirmPassword : "");
        if (acceptTerms) agreeCheckbox.click();
        createAccountButton.click();
    }

    public void inputPassword(String password){
        passwordTextField.fill(password);
    }

    public void inputConfirmPassword(String confirmPassword){
        confirmPasswordTextField.fill(confirmPassword);
    }

    public void togglePasswordVisibility(){
        if(passwordTextField.getAttribute("type").equals("password")){
            showPasswordButton.click();
        } else {
            hidePasswordButton.click();
        }
    }

    public void toggleConfirmPasswordVisibility(){
        if(confirmPasswordTextField.getAttribute("type").equals("password")){
            showConfirmPasswordButton.click();
        } else {
            hideConfirmPasswordButton.click();
        }
    }

    public SignInPage clickSignInLink(){
        signInLink.click();
        return new SignInPage(page);
    }
}
