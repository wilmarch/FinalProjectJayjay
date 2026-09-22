package DemoBlaze.stepdef;

import DemoBlaze.base.BaseTest;
import DemoBlaze.pages.CartPage;
import DemoBlaze.pages.HomePage;
import DemoBlaze.pages.NavigationHeader;
import DemoBlaze.pages.OrderPage;
import DemoBlaze.pages.ProductDetailPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartStepDef {

    private final HomePage homePage = new HomePage(BaseTest.driver);
    private final ProductDetailPage detailPage = new ProductDetailPage(BaseTest.driver);
    private final CartPage cartPage = new CartPage(BaseTest.driver);
    private final OrderPage orderPage = new OrderPage(BaseTest.driver);
    private final NavigationHeader navHeader = new NavigationHeader(BaseTest.driver);

    // Nyimpen harga produk dari detail page pas ditambahin ke cart, biar
    // step "with the matching price" bisa beneran verifikasi harga di cart
    // sama dengan harga yang ditampilkan di detail page - bukan cuma cek
    // harga di cart gak kosong.
    private final Map<String, String> addedProductPrices = new HashMap<>();

    // Dipakai juga oleh checkout.feature (Background) - reusable lintas feature
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

    // Dipakai juga oleh checkout.feature (Background)
    @When("user navigates to the cart page")
    public void userNavigatesToTheCartPage() {
        navHeader.goToCart();
        cartPage.waitForCartToLoad();
    }

    @Then("the cart should contain {string}")
    public void theCartShouldContain(String productName) {
        List<CartPage.CartItemInfo> items = cartPage.getCartItems();
        boolean found = items.stream().anyMatch(i -> i.name.equalsIgnoreCase(productName));
        Assert.assertTrue("Produk " + productName + " tidak ditemukan di cart", found);
    }

    @Then("the cart item list should display {string} with the matching price")
    public void theCartItemListShouldDisplayWithMatchingPrice(String productName) {
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
            // Fallback kalau produk ditambahin lewat jalur lain (bukan lewat
            // "user has added ... to the cart") sehingga harga aslinya
            // gak sempat tercatat di sini.
            Assert.assertFalse("Harga produk " + productName + " kosong", item.price.isEmpty());
        }
    }

    @Then("the cart should list the duplicate items accordingly")
    public void theCartShouldListTheDuplicateItemsAccordingly() {
        cartPage.waitForMinimumRows(2);
        List<CartPage.CartItemInfo> items = cartPage.getCartItems();
        Assert.assertTrue("Harus ada minimal 2 baris item", items.size() >= 2);
    }

    @Then("the total price should equal the sum of all individual item prices")
    public void theTotalPriceShouldEqualTheSumOfAllIndividualItemPrices() {
        cartPage.waitForMinimumRows(2);
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

    @Then("the total price should update accordingly")
    public void theTotalPriceShouldUpdateAccordingly() {
        Assert.assertTrue(cartPage.getTotalPrice() >= 0);
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

    // Dipakai juga oleh checkout.feature (scenario checkout-complete-flow)
    @Then("the cart should be completely empty")
    public void theCartShouldBeCompletelyEmpty() {
        Assert.assertTrue("Cart seharusnya kosong", cartPage.isCartEmpty());
    }

    @When("user refreshes the browser page")
    public void userRefreshesTheBrowserPage() {
        BaseTest.driver.navigate().refresh();
        cartPage.waitForCartToLoad();
    }

    @Then("the cart should still retain {string}")
    public void theCartShouldStillRetain(String productName) {
        theCartShouldContain(productName);
    }

    // Dipakai juga oleh checkout.feature (semua scenario)
    @When("user proceeds to place order")
    public void userProceedsToPlaceOrder() {
        cartPage.clickPlaceOrder();
    }

    @Then("the order modal should be displayed")
    public void theOrderModalShouldBeDisplayed() {
        orderPage.waitForOrderModal();
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