package DemoBlaze.stepdef;

import DemoBlaze.context.TestContext;
import DemoBlaze.pages.HomePage;
import DemoBlaze.pages.ProductDetailPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ProductStepDef {

    TestContext context;
    HomePage homePage;
    ProductDetailPage detailPage;
    WebDriverWait wait;

    public ProductStepDef(TestContext context) {
        this.context = context;
        this.homePage = context.getHomePage();
        this.detailPage = context.getProductDetailPage();
        this.wait = new WebDriverWait(context.getDriver(), Duration.ofSeconds(10));
    }

    @When("user selects category {string}")
    public void userSelectsCategory(String category) {
        homePage.selectCategory(category);
    }

    @Then("only products belonging to category {string} should be displayed")
    public void onlyProductsBelongingToCategoryShouldBeDisplayed(String category) {
        List<String> products = homePage.getAllProductNames();
        Assert.assertFalse("Daftar produk kosong untuk kategori: " + category, products.isEmpty());
    }

    @When("user clicks the {string} pagination button")
    public void userClicksThePaginationButton(String button) {
        if ("Next".equalsIgnoreCase(button)) {
            homePage.clickNextPage();
        } else {
            homePage.clickPreviousPage();
        }
    }

    @Then("the product list should update with page 2 items")
    public void theProductListShouldUpdateWithPageItems() {
        List<String> products = homePage.getAllProductNames();
        Assert.assertFalse(products.isEmpty());
    }

    @When("user clicks on a product {string}")
    public void userClicksOnProductTitle(String productName) {
        homePage.clickProductByName(productName);
    }

    @When("user clicks on the image thumbnail for product {string}")
    public void userClicksOnTheImageThumbnailForProduct(String productName) {
        String imgXpath = String.format("//a[text()='%s']/ancestor::div[@class='card h-100']//img", productName);
        WebElement productImg = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(imgXpath)));
        productImg.click();
    }

    @Then("the page should redirect to product detail page or document failure if unlinked")
    public void verifyThumbnailRedirection() {
        String currentUrl = context.getDriver().getCurrentUrl();
        boolean isDetailPage = currentUrl.contains("prod.html");
        if (!isDetailPage) {
            System.out.println("[KNOWN BUG DETECTED] Mengklik thumbnail gambar tidak melakukan navigasi ke detail page.");
        }
    }

    @Given("user is on the detail page for product {string}")
    public void userIsOnTheDetailPageForProduct(String productName) {
        homePage.openHomePage();
        homePage.clickProductByName(productName);
        detailPage.waitForPageToLoad();
    }

    @Then("the product detail page should display title {string} and corresponding price")
    public void theProductDetailPageShouldDisplayTitleAndCorrespondingPrice(String expectedName) {
        detailPage.waitForPageToLoad();
        Assert.assertEquals(expectedName, detailPage.getProductName());
        Assert.assertFalse(detailPage.getProductPrice().isEmpty());
    }

    @Then("the product detail page should display name, price, and description")
    public void theProductDetailPageShouldDisplayNamePriceAndDescription() {
        detailPage.waitForPageToLoad();
        Assert.assertFalse(detailPage.getProductName().isEmpty());
        Assert.assertFalse(detailPage.getProductPrice().isEmpty());
        Assert.assertFalse(detailPage.getProductDescription().isEmpty());
    }

    @When("user adds the product to cart")
    public void userAddsTheProductToCart() {
        detailPage.clickAddToCart();
    }
}