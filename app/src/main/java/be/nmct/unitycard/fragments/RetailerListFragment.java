package be.nmct.unitycard.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import be.nmct.unitycard.R;
import be.nmct.unitycard.activities.MainActivity;
import be.nmct.unitycard.auth.AuthHelper;
import be.nmct.unitycard.models.Retailer;
import be.nmct.unitycard.repositories.RetailerRepository;
import butterknife.ButterKnife;

public class RetailerListFragment extends Fragment {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private RetailerListFragmentListener mListener;

    public RetailerListFragment() {
        // Required empty public constructor
    }

    public static RetailerListFragment newInstance() {
        return new RetailerListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_retailer_list, container, false);
        ButterKnife.bind(this, view);

        mListener.showFabAddRetailer();

        loadRetailers();

        return view;
    }

    private void loadRetailers() {
        // Show loading indication
        mListener.swipeRefreshLayoutAddTask(MainActivity.TASK_LOAD_RETAILERS);

        // Get access token
        AuthHelper.getAccessToken(AuthHelper.getUser(getActivity()), getActivity(), new AuthHelper.GetAccessTokenListener() {
            @Override
            public void tokenReceived(final String accessToken) {
                Log.d(LOG_TAG, "Using access token: " + accessToken);

                final RetailerRepository retailerRepo = new RetailerRepository(getActivity());
                retailerRepo.getAllRetailers(accessToken, new RetailerRepository.GetAllRetailersListener() {
                    @Override
                    public void retailersReceived(List<Retailer> retailers) {
                        Log.d(LOG_TAG, "Received all retailers: " + retailers);

                        // Hide loading indication
                        mListener.swipeRefreshLayoutRemoveTask(MainActivity.TASK_LOAD_RETAILERS);
                    }

                    @Override
                    public void retailersRequestError(String error) {
                        // Invalideer het gebruikte access token, het is niet meer geldig (anders zou er geen error zijn)
                        AuthHelper.invalidateAccessToken(accessToken, getActivity());

                        // Hide loading indication
                        mListener.swipeRefreshLayoutRemoveTask(MainActivity.TASK_LOAD_RETAILERS);

                        // Try again
                        loadRetailers();
                    }
                });
            }

            @Override
            public void requestNewLogin() {
                // Hide loading indication
                mListener.swipeRefreshLayoutRemoveTask(MainActivity.TASK_LOAD_RETAILERS);

                // Something went wrong, toon login scherm
                mListener.requestNewLogin();
            }
        });
    }

    public interface RetailerListFragmentListener {
        void showFabAddRetailer();
        void hideFabAddRetailer();
        void swipeRefreshLayoutAddTask(String task);
        void swipeRefreshLayoutRemoveTask(String task);
        void requestNewLogin();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // zeker zijn dat de container activity de callback geimplementeerd heeft
        if (context instanceof RetailerListFragmentListener) {
            mListener = (RetailerListFragmentListener)context;
        } else {
            throw new ClassCastException(context.toString() + "must implement " + RetailerListFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
