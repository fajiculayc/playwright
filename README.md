# Spree Commerce Playwright Test Automation

Automated end-to-end test suite for [Spree Commerce Demo Store](https://demo.spreecommerce.org) built with Playwright Java, JUnit 5, and Maven.

## Tech Stack

| Tool | Version |
|---|---|
| Java | 17 |
| Playwright | Latest |
| JUnit | 5 |
| Maven | 3.6+ |
| OpenCSV | 5.9 |

## Project Structure
## Project Structure

```
src/
├── main/java/com/codingchallenge/
│   ├── base/BasePage.java
│   ├── components/
│   │   ├── CartComponent.java
│   │   └── HeaderComponent.java
│   └── pages/
│       ├── HomePage.java
│       ├── account/AccountPage.java
│       ├── auth/
│       │   ├── SignInPage.java
│       │   └── SignUpPage.java
│       ├── checkout/
│       │   ├── CheckoutPage.java
│       │   └── OrderConfirmationPage.java
│       └── products/
│           ├── ProductDetailsPage.java
│           └── ProductsPage.java
└── test/java/com/codingchallenge/
    ├── base/BaseTest.java
    └── tests/
        ├── smoke/SmokeTest.java
        ├── loggedIn/
        │   ├── AccountPageTest.java
        │   ├── CartComponentTest.java
        │   ├── CheckoutPageTest.java
        │   ├── OrderConfirmationPageTest.java
        │   ├── ProductDetailsPageTest.java
        │   └── ProductsPageTest.java
        └── loggedOut/
            ├── HeaderComponentTest.java
            ├── HomePageTest.java
            ├── ProductsPageTest.java
            ├── SignInPageTest.java
            └── SignUpPageTest.java
```

## Prerequisites

- Java 17+
- Maven 3.6+
- Git

## Setup

**1. Clone the repository:**
```bash
git clone https://github.com/fajiculayc/playwright.git
cd playwright
```

**2. Install dependencies:**
```bash
mvn install -DskipTests
```

**3. Install Playwright browsers:**
```bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install chromium"
```

## Running Tests

**Run smoke test only (recommended):**
```bash
mvn test -Dtest="SmokeTest"
```

**Run all tests:**
```bash
mvn test
```

**Run a specific test class:**
```bash
mvn test -Dtest="CheckoutPageTest"
```

**Run a specific test method:**
```bash
mvn test -Dtest="CheckoutPageTest#shouldCompleteCheckoutWithValidCard"
```

**Run logged in tests only:**
```bash
mvn test -Dtest="AccountPageTest,CartComponentTest,CheckoutPageTest,OrderConfirmationPageTest,ProductDetailsPageTest,ProductsPageTest"
```

**Run logged out tests only:**
```bash
mvn test -Dtest="HeaderComponentTest,HomePageTest,SignInPageTest,SignUpPageTest"
```

## Test Data

Test data is stored in `src/test/resources/testdata/`:

| File | Description |
|---|---|
| `accountData.csv` | Valid user credentials and checkout data |
| `signInHappyPath.csv` | Valid sign in credentials |
| `signInBrowserErrors.csv` | Invalid inputs triggering browser validation |
| `signInInlineErrors.csv` | Invalid inputs triggering inline errors |
| `signUpHappyPath.csv` | Valid sign up data |
| `signUpBrowserErrors.csv` | Invalid inputs triggering browser validation |
| `signUpInlineErrors.csv` | Invalid inputs triggering inline errors |

> **Note:** The smoke test generates a unique email on each run using a timestamp (e.g. `smoketest_1234567890@email.com`) to avoid duplicate email errors on sign up.

## Smoke Test Coverage

The smoke test (`SmokeTest.java`) covers the full end-to-end purchase flow:

| Step | Description |
|---|---|
| 1 | Navigate to Spree Commerce demo store |
| 2 | Sign up as a new user |
| 3 | Verify successful login |
| 4 | Browse and select an in-stock product |
| 5 | Add product to cart |
| 6 | Verify cart details (name, quantity, price) |
| 7 | Proceed to checkout and fill shipping address |
| 8 | Select shipping method |
| 9 | Verify order summary and pricing |
| 10 | Fill payment details with Stripe test card |
| 11 | Complete the order |
| 12 | Verify order confirmation page |

## Stripe Test Card

Use these test card details for checkout:

| Field | Value |
|---|---|
| Card Number | `4242 4242 4242 4242` |
| Expiration | `12/30` |
| CVC | `123` |
| Country | `US` |
| ZIP | `10028` |

## GitHub Actions Pipeline

The CI pipeline runs the smoke test automatically on:

| Trigger | Description |
|---|---|
| Push to `main` | Runs on every code push |
| Pull Request | Runs on every PR to `main` |
| Manual trigger | Can be triggered manually from GitHub Actions UI |

**To trigger manually:**
1. Go to your repository on GitHub
2. Click the **Actions** tab
3. Click **Playwright Tests** on the left
4. Click **Run workflow** → select branch → **Run workflow**

**To view test results:**
1. Go to the **Actions** tab
2. Click on the latest workflow run
3. ✅ Green = tests passed
4. ❌ Red = tests failed — click to see logs
5. Test result artifacts are available for download for 7 days

## Configuration

**Headless mode** is automatically detected:
- **Locally** → runs in headed mode (you can see the browser)
- **GitHub Actions** → runs in headless mode (no browser UI needed)

No manual configuration needed — this is handled automatically in `BaseTest.java`.

## Notes

- The smoke test generates a unique email on each run to avoid duplicate sign up errors
- Stripe payment uses test cards only — no real money is charged
- Tests use `slowMo(500)` locally for better visibility — this is disabled in CI for faster execution
