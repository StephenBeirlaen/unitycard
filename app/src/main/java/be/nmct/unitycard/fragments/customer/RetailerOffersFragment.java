package be.nmct.unitycard.fragments.customer;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.nmct.unitycard.R;
import be.nmct.unitycard.adapters.RetailerRecyclerViewAdapter;
import be.nmct.unitycard.databinding.FragmentRetailerOffersBinding;
import be.nmct.unitycard.models.viewmodels.fragment.RetailerOffersFragmentVM;

public class RetailerOffersFragment extends Fragment {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private RetailerOffersFragmentListener mListener;
    private FragmentRetailerOffersBinding mBinding;
    private RetailerOffersFragmentVM mRetailerOffersFragmentVM;

    public RetailerOffersFragment() {
        // Required empty public constructor
    }

    public static RetailerOffersFragment newInstance(int retailerId) {
        RetailerOffersFragment retailerOffersFragment = new RetailerOffersFragment();

        Bundle args = new Bundle();
        args.putInt(RetailerRecyclerViewAdapter.EXTRA_RETAILER_ID, retailerId);
        retailerOffersFragment.setArguments(args);

        return retailerOffersFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_retailer_offers, container, false);

        Integer retailerId = null;

        Bundle args = getArguments();
        if (args != null && args.containsKey(RetailerRecyclerViewAdapter.EXTRA_RETAILER_ID)){
            retailerId = args.getInt(RetailerRecyclerViewAdapter.EXTRA_RETAILER_ID);

            if (retailerId != null) {
                Log.d(LOG_TAG, "showing retailer offers, retailerid: " + retailerId);


            }
            else {
                mListener.handleError("Retailer is null");
            }
        }
        else {
            mListener.handleError("Extras is null or doesn't contains EXTRA_RETAILER_ID key");
        }

        mRetailerOffersFragmentVM = new RetailerOffersFragmentVM(mBinding, getActivity(), retailerId);

        mBinding.recyclerViewRetailerOffers.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.recyclerViewRetailerOffers.setHasFixedSize(true);
        mBinding.recyclerViewRetailerOffers.setItemAnimator(new DefaultItemAnimator());
        // aantal kolommen instellen
        mBinding.recyclerViewRetailerOffers.setLayoutManager(new GridLayoutManager(getContext(),
                getResources().getInteger(R.integer.retailerListColumnCount)
        ));

        return mBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        // todo: perform load actions here
    }

    public interface RetailerOffersFragmentListener {
        // todo: events, indien er geen events zijn, onAttach en ondetach overrides wegdoen
        void handleError(String error);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // zeker zijn dat de container activity de callback geimplementeerd heeft
        if (context instanceof RetailerOffersFragmentListener) {
            mListener = (RetailerOffersFragmentListener)context;
        } else {
            throw new ClassCastException(context.toString() + "must implement " + RetailerOffersFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
