package be.nmct.unitycard.fragments.customer;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import be.nmct.unitycard.R;
import be.nmct.unitycard.adapters.RetailerRecyclerViewAdapter;
import be.nmct.unitycard.databinding.FragmentRetailerInfoBinding;
import be.nmct.unitycard.models.RetailerLocation;
import be.nmct.unitycard.models.viewmodels.fragment.RetailerInfoFragmentVM;

import static be.nmct.unitycard.adapters.SyncAdapter.RESULT_SYNC_SUCCESS;

public class RetailerInfoFragment extends Fragment {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private RetailerInfoFragmentListener mListener;
    private FragmentRetailerInfoBinding mBinding;
    private RetailerInfoFragmentVM mRetailerInfoFragmentVM;

    public RetailerInfoFragment() {
        // Required empty public constructor
    }

    public static RetailerInfoFragment newInstance(int retailerId, int loyaltyPoints) {
        RetailerInfoFragment retailerInfoFragment = new RetailerInfoFragment();

        Bundle args = new Bundle();
        args.putInt(RetailerRecyclerViewAdapter.EXTRA_RETAILER_ID, retailerId);
        args.putInt(RetailerRecyclerViewAdapter.EXTRA_RETAILER_LOYALTY_POINTS, loyaltyPoints);
        retailerInfoFragment.setArguments(args);

        return retailerInfoFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_retailer_info, container, false);

        Integer retailerId = null;
        Integer loyaltyPoints = null;

        Bundle args = getArguments();
        if (args != null && args.containsKey(RetailerRecyclerViewAdapter.EXTRA_RETAILER_ID)){
            retailerId = args.getInt(RetailerRecyclerViewAdapter.EXTRA_RETAILER_ID);
            loyaltyPoints = args.getInt(RetailerRecyclerViewAdapter.EXTRA_RETAILER_LOYALTY_POINTS);

            if (retailerId != null) {
                Log.d(LOG_TAG, "showing retailer info, retailerid: " + retailerId);
            }
            else {
                mListener.handleError("Retailer is null");
            }
        }
        else {
            mListener.handleError("Extras is null or doesn't contains EXTRA_RETAILER_ID key");
        }

        mRetailerInfoFragmentVM = new RetailerInfoFragmentVM(mBinding, getActivity(), mListener, retailerId, loyaltyPoints);

        return mBinding.getRoot();
    }

    // Listener for synchronization changes
    public static final String ACTION_FINISHED_RETAILER_LOCATIONS_SYNC = "be.nmct.unitycard.ACTION_FINISHED_RETAILER_LOCATIONS_SYNC";
    public static final String ACTION_FINISHED_RETAILER_LOCATIONS_SYNC_RESULT = "be.nmct.unitycard.ACTION_FINISHED_RETAILER_LOCATIONS_SYNC_RESULT";

    private static IntentFilter syncIntentFilter = new IntentFilter(ACTION_FINISHED_RETAILER_LOCATIONS_SYNC);
    private BroadcastReceiver syncBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Hide the refreshing indicator
            mListener.showRefreshingIndicator(false);

            // Check if there was an error
            if (intent.getAction().equals(ACTION_FINISHED_RETAILER_LOCATIONS_SYNC)) {
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    int result = extras.getInt(ACTION_FINISHED_RETAILER_LOCATIONS_SYNC_RESULT, RESULT_SYNC_SUCCESS);
                    if (result == RESULT_SYNC_SUCCESS) { // als er geen error was
                        // Update retailer location data
                        mRetailerInfoFragmentVM.updateRetailerLocationsInfo(mRetailerInfoFragmentVM.getRetailerId());

                        return;
                    }
                }
            }

            mListener.requestNewLogin(); // nieuwe login aanvragen
        }
    };

    @Override
    public void onResume() {
        super.onResume();

        // Register broadcastreceiver for synchronization changes
        getActivity().registerReceiver(syncBroadcastReceiver, syncIntentFilter);

        mRetailerInfoFragmentVM.loadRetailerInfo();
    }

    @Override
    public void onPause() {
        super.onPause();

        // Unregister broadcastreceiver for synchronization changes
        getActivity().unregisterReceiver(syncBroadcastReceiver);
    }

    public interface RetailerInfoFragmentListener {
        void requestNewLogin();
        void handleError(String error);
        void showRetailerMap(RetailerLocation retailerLocation);
        void showAllRetailersMap(ArrayList<RetailerLocation> retailerLocations);
        void showRefreshingIndicator(Boolean refreshing);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // zeker zijn dat de container activity de callback geimplementeerd heeft
        if (context instanceof RetailerInfoFragmentListener) {
            mListener = (RetailerInfoFragmentListener)context;
        } else {
            throw new ClassCastException(context.toString() + "must implement " + RetailerInfoFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
