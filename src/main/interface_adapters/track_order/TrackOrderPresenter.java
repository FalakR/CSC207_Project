package interface_adapters.track_order;

import entities.Location;
import interface_adapters.ViewManagerModel;
import use_cases.track_order.TrackOrderOutputBoundary;
import use_cases.track_order.TrackOrderOutputData;

import javax.swing.text.View;

public class TrackOrderPresenter implements TrackOrderOutputBoundary {
    private final TrackOrderViewModel viewModel;
    private ViewManagerModel viewManagerModel;

    public TrackOrderPresenter(ViewManagerModel viewManagerModel,
                          TrackOrderViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareView(TrackOrderOutputData data) {
        TrackOrderState state = this.viewModel.getState();
        if (state == null) {
            state = new TrackOrderState();
        }
        // modify state according to the data
        state.userLocation = data.userLocation;
        state.deliveryAgentLocation = data.deliveryAgentLocation;

        this.viewModel.setState(state);

        this.viewModel.firePropertyChanged();
    }

    @Override
    public void prepareSuccessView() {
        TrackOrderState state = this.viewModel.getState();
        state.isSuccess = true;
        this.viewModel.setState(state);
        this.viewModel.firePropertyChanged();
    }
}
