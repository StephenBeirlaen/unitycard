package be.nmct.unitycard.fragments.customer;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.nmct.unitycard.R;
import be.nmct.unitycard.databinding.FragmentRetailerListBinding;
import be.nmct.unitycard.models.viewmodels.fragment.RetailerListFragmentVM;

import static be.nmct.unitycard.contracts.ContentProviderContract.ADDED_RETAILERS_URI;
import static be.nmct.unitycard.contracts.ContentProviderContract.RETAILERS_URI;

public class RetailerListFragment extends Fragment {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private RetailerListFragmentListener mListener;
    private FragmentRetailerListBinding mBinding;
    private RetailerListFragmentVM mRetailerListFragmentVM;
    private RetailerListFragmentVM.MyContentObserver myContentObserver;

    public RetailerListFragment() {
        // Required empty public constructor
    }

    public static RetailerListFragment newInstance() {
        return new RetailerListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_retailer_list, container, false);
        mRetailerListFragmentVM = new RetailerListFragmentVM(mBinding, getActivity());

        mBinding.recyclerViewListRetailer.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.recyclerViewListRetailer.setHasFixedSize(true);
        mBinding.recyclerViewListRetailer.setItemAnimator(new DefaultItemAnimator());

        mListener.showFabAddRetailer();

        return mBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        // todo: perform load actions here

    }

    @Override
    public void onResume() {
        super.onResume();
        myContentObserver = mRetailerListFragmentVM.new MyContentObserver(new Handler());
        getContext().getContentResolver().registerContentObserver(ADDED_RETAILERS_URI, false, myContentObserver);
    }

    @Override
    public void onPause() {
        super.onPause();

        getContext().getContentResolver().unregisterContentObserver(myContentObserver);
    }

    public interface RetailerListFragmentListener {
        void showFabAddRetailer();
        void hideFabAddRetailer();
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
