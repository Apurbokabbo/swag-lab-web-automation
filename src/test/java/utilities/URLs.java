package utilities;

public class URLs {

    public static final String BASE_URL = ConfigReader.get("BASE_URL", "");

    public static final String HOME_URL = BASE_URL + "/inventory.html";

    public static final String CART_URL = BASE_URL + "/cart.html";

    public static final String CHECKOUT_URL = BASE_URL + "/checkout-step-one.html";
}