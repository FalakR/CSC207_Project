package interface_adapters.place_order;


import interface_adapters.track_order.TrackOrderViewModel;
import use_cases.place_order.PlaceOrderOutputBoundary;
import interface_adapters.ViewManagerModel;
import use_cases.place_order.PlaceOrderOutputData;

public class PlaceOrderPresenter implements PlaceOrderOutputBoundary {

    private ViewManagerModel viewManagerModel;
    private PlaceOrderViewModel placeOrderViewModel;
    private TrackOrderViewModel trackOrderViewModel;

    public PlaceOrderPresenter(
            PlaceOrderViewModel placeOrderViewModel,
            ViewManagerModel viewManagerModel,
            TrackOrderViewModel trackOrderViewModel
            ) {
        this.placeOrderViewModel = placeOrderViewModel;
        this.viewManagerModel = viewManagerModel;
        this.trackOrderViewModel = trackOrderViewModel;
    }


    @Override
    public void prepareSummaryView(PlaceOrderOutputData response) {
        PlaceOrderState placeOrderState = placeOrderViewModel.getState();
        placeOrderState.setOrderSummary(response.getOrderSummary());
        placeOrderState.setAddress(response.getUserAddress());
        placeOrderViewModel.firePropertyChanged();

        // Switch to the tracking view

        viewManagerModel.setActiveView(trackOrderViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String s) {
        PlaceOrderState placeOrderState = placeOrderViewModel.getState();
        placeOrderState.setAddressError(s);
        placeOrderViewModel.firePropertyChanged();
    }
}
