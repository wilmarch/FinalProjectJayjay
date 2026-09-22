package DemoBlaze.stepdef;

import DemoBlaze.context.TestContext;
import DemoBlaze.pages.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartStepDef {

    TestContext context;
    HomePage homePage;
    ProductDetailPage detailPage;
    CartPage cartPage;
    OrderPage orderPage;
    NavigationHeader navHeader;

    Map<String, String> addedProductPrices = new HashMap<>();

    public CartStepDef(TestContext context) {
        this.context = context;
        this.homePage = context.getHomePage();
        this.detailPage = context.getProductDetailPage();
        this.cartPage = context.getCartPage();
        this.orderPage = context.getOrderPage();
        this.navHeader = context.getNavigationHeader();
    }

    @Given("user has added {string} to the cart")
    public void userHasAddedToTheCart(String productName) {
        homePage.openHomePage();
        homePage.clickProductByName(productName);
        detailPage.waitForPageToLoad();
        addedProductPrices.put(productName, detailPage.getProductPrice());
        detailPage.clickAddToCart();
        detailPage.getAlertTextAndAccept();
        navHeader.goToHome();
    }

    @When("user navigates to the cart page")
    public void userNavigatesToTheCartPage() {
        navHeader.goToCart();
        cartPage.waitForCartToLoad();
    }

    @Then("the cart should contain {string}")
    public void theCartShouldContain(String productName) {
        cartPage.waitForMinimumRows(1);
        List<CartPage.CartItemInfo> items = cartPage.getCartItems();
        boolean found = items.stream().anyMatch(i -> i.name.equalsIgnoreCase(productName));
        Assert.assertTrue("Produk " + productName + " tidak ditemukan di cart", found);
    }

    @Then("the cart item list should display {string} with the matching price")
    public void theCartItemListShouldDisplayWithMatchingPrice(String productName) {
        cartPage.waitForMinimumRows(1);
        List<CartPage.CartItemInfo> items = cartPage.getCartItems();
        CartPage.CartItemInfo item = items.stream()
                .filter(i -> i.name.equalsIgnoreCase(productName))
                .findFirst()
                .orElse(null);
        Assert.assertNotNull("Produk " + productName + " tidak ditemukan di cart", item);

        String expectedPrice = addedProductPrices.get(productName);
        if (expectedPrice != null) {
            Assert.assertEquals("Harga " + productName + " di cart tidak sesuai dengan detail page",
                    expectedPrice, item.price);
        } else {
            Assert.assertFalse("Harga produk " + productName + " kosong", item.price.isEmpty());
        }
    }

    @Then("the cart should display 2 rows of items")
    public void theCartShouldDisplay2RowsOfItems() {
        cartPage.waitForMinimumRows(2);
        List<CartPage.CartItemInfo> items = cartPage.getCartItems();
        Assert.assertTrue("Harus ada minimal 2 baris item", items.size() >= 2);
    }

    @Then("the total price should equal the sum of all individual item prices")
    public void theTotalPriceShouldEqualTheSumOfAllIndividualItemPrices() {
        cartPage.waitForMinimumRows(1);
        List<CartPage.CartItemInfo> items = cartPage.getCartItems();

        int expectedTotal = 0;
        for (CartPage.CartItemInfo item : items) {
            expectedTotal += Integer.parseInt(item.price);
        }
        int actualTotal = cartPage.getTotalPrice();
        Assert.assertEquals("Total harga tidak sesuai dengan kalkulasi individual", expectedTotal, actualTotal);
    }

    @When("user deletes item {string} from cart")
    public void userDeletesItemFromCart(String productName) {
        cartPage.deleteItemByName(productName);
    }

    @Then("{string} should not be visible in cart")
    public void shouldNotBeVisibleInCart(String productName) {
        List<CartPage.CartItemInfo> items = cartPage.getCartItems();
        boolean found = items.stream().anyMatch(i -> i.name.equalsIgnoreCase(productName));
        Assert.assertFalse("Produk masih ada di keranjang: " + productName, found);
    }

    @When("user deletes all items from cart")
    public void userDeletesAllItemsFromCart() {
        cartPage.deleteAllItems();
    }

    @Given("the cart is empty")
    public void theCartIsEmpty() {
        navHeader.goToCart();
        cartPage.deleteAllItems();
    }

    @Then("the cart should be completely empty")
    public void theCartShouldBeCompletelyEmpty() {
        Assert.assertTrue("Cart seharusnya kosong", cartPage.isCartEmpty());
    }

    @When("user refreshes the browser page")
    public void userRefreshesTheBrowserPage() {
        context.getDriver().navigate().refresh();
        cartPage.waitForCartToLoad();
    }

    @Then("the cart should still retain {string}")
    public void theCartShouldStillRetain(String productName) {
        theCartShouldContain(productName);
    }

    @When("user proceeds to place order")
    public void userProceedsToPlaceOrder() {
        cartPage.clickPlaceOrder();
    }

    @Then("document if checkout modal is allowed to open without items")
    public void documentIfCheckoutModalIsAllowedToOpenWithoutItems() {
        try {
            orderPage.waitForOrderModal();
            System.out.println("[KNOWN BUG DETECTED] DemoBlaze mengizinkan tombol Place Order diklik pada keranjang kosong.");
        } catch (Exception e) {
            System.out.println("Modal tidak terbuka.");
        }
    }
}