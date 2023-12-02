package use_cases.place_order;
import entities.CommonCart;
import entities.IceCream;
import java.util.List;
public class PlaceOrderInputData {
    private final CommonCart cart;
    private final String userAddress;
    private final String creditCardNumber;
    private final int cvv;
    private final String expiryDate;

    public PlaceOrderInputData(CommonCart cart,  String userAddress, String creditCardNumber, int cvv, String expiryDate) {
        this.cart = cart;
        this.userAddress = userAddress;
        this.creditCardNumber = creditCardNumber;
        this.cvv = cvv;
        this.expiryDate = expiryDate;
    }
    public CommonCart getCart() {
        return cart;
    }
    public List<IceCream> getIceCreams() {
        return cart.getItems();
    }
    public String getUserAddress() {
        return userAddress;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public int getCvv() {
        return cvv;
    }

    public String getExpiryDate() {
        return expiryDate;
    }
}