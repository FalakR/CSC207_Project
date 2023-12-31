package use_cases.place_order;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import entities.CommonLocation;
import entities.IceCream;
import entities.Location;
import use_cases.track_order.TrackOrderInputBoundary;
import use_cases.track_order.TrackOrderInteractor;

import java.util.List;

public class PlaceOrderInteractor implements PlaceOrderInputBoundary {

    final PlaceOrderOutputBoundary placeOrderPresenter;
    final PlaceOrderDataAccessInterface placeOrderDataAccessObject;
    final PlaceOrderUserDataAccessInterface placeOrderUserDataAccessObject;
    private final TrackOrderInputBoundary trackOrderInteractor;

    public PlaceOrderInteractor(
            PlaceOrderOutputBoundary placeOrderPresenter,
            PlaceOrderDataAccessInterface placeOrderDataAccessObject,
            PlaceOrderUserDataAccessInterface placeOrderUserDataAccessObject,
            TrackOrderInputBoundary trackOrderInteractor) {
        this.placeOrderPresenter = placeOrderPresenter;
        this.placeOrderDataAccessObject = placeOrderDataAccessObject;
        this.placeOrderUserDataAccessObject = placeOrderUserDataAccessObject;
        this.trackOrderInteractor = trackOrderInteractor;
    }

    @Override
    public void execute(PlaceOrderInputData placeOrderInputData) {
        // Extract necessary information from the input data

        List<IceCream> iceCreams = placeOrderInputData.getIceCreams();// list of icecream objects
        String userAddress = placeOrderInputData.getUserAddress();
        String creditCardNumber = placeOrderInputData.getCreditCardNumber();
        String cvv = placeOrderInputData.getCvv();
        String expiryDate = placeOrderInputData.getExpiryDate();

        System.out.println("userAddress: " + userAddress);
        System.out.println("creditCardNumber: " + creditCardNumber);
        System.out.println("cvv: " + cvv);
        System.out.println("expiryDate: " + expiryDate);


        if (userAddress == null || userAddress.isEmpty()) {
            placeOrderPresenter.prepareFailView("User address cannot be empty.");
        } else if (creditCardNumber == null || creditCardNumber.isEmpty() || expiryDate == null || expiryDate.isEmpty()) {
            placeOrderPresenter.prepareFailView("Invalid credit card information.");
        } else {
            // Process the order


            String orderSummary = createOrderSummary(iceCreams, userAddress);
            PlaceOrderOutputData placeOrderOutputData = new PlaceOrderOutputData(orderSummary, userAddress);

            publishOrder(userAddress,iceCreams);
            trackOrderInteractor.execute();

            placeOrderPresenter.prepareSummaryView(placeOrderOutputData);

//            placeOrderPresenter.prepareChangeView();
        }
    }

    private void publishOrder(String userAddress, List<IceCream> iceCreams) {
        Location userLocation = this.convertToCoordinates(userAddress);
        String orderId = this.placeOrderDataAccessObject.publish(iceCreams, userLocation);

        // Set DB
        this.placeOrderUserDataAccessObject.setOrderId(orderId);
        this.placeOrderUserDataAccessObject.setLocation(userLocation);
    }

    private Location convertToCoordinates(String userAddress) {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyBED9mktM_yVdhhNkF2QWBfrovI6CIlHVQ")
                .build();
        GeocodingResult[] results = new GeocodingResult[0];
        try {
            results = GeocodingApi.geocode(context,
                    userAddress).await();

        } catch (Exception e) {
            System.err.println("Cannot get location from address");
            System.err.println(e);
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        LatLng loc = results[0].geometry.location;

        context.shutdown();
        return new CommonLocation(loc.lat, loc.lng);
    }

    private String createOrderSummary(List<IceCream> iceCreams, String userAddress) {
        if (iceCreams.isEmpty()) {
            return "No items in the order.";
        }
        StringBuilder orderSummaryBuilder = new StringBuilder("Order Summary:\n");

        double totalPrice = 0.0;

        for (int i = 0; i < iceCreams.size(); i++) {
            IceCream iceCream = iceCreams.get(i);
            orderSummaryBuilder.append(i + 1)
                    .append(". ")
                    .append("Name: ").append(iceCream.getName())
                    .append(", Flavor: ").append(iceCream.getFlavour())
                    .append(", Price: $").append(iceCream.getPrice())
                    .append("\n");

            totalPrice += iceCream.getPrice();
        }
        // Add user address to the summary
        orderSummaryBuilder.append("User Address: ").append(userAddress).append("\n");

        // Add total price to the summary
        orderSummaryBuilder.append("Total Price: $").append(totalPrice);

        return orderSummaryBuilder.toString();

    }





}