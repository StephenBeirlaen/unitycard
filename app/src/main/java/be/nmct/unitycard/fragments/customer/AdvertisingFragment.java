package be.nmct.unitycard.fragments.customer;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.nmct.unitycard.R;
import be.nmct.unitycard.databinding.FragmentAdvertisingBinding;
import be.nmct.unitycard.models.viewmodels.fragment.AdvertisingFragmentVM;

public class AdvertisingFragment extends Fragment {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private AdvertisingFragmentListener mListener;
    private FragmentAdvertisingBinding mBinding;
    private AdvertisingFragmentVM mAdvertisingFragmentVM;

    public AdvertisingFragment() {
        // Required empty public constructor
    }

    public static AdvertisingFragment newInstance() {
        return new AdvertisingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_advertising, container, false);
        mAdvertisingFragmentVM = new AdvertisingFragmentVM(mBinding, getActivity());

        mBinding.recyclerViewAdvertising.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.recyclerViewAdvertising.setHasFixedSize(true);
        mBinding.recyclerViewAdvertising.setItemAnimator(new DefaultItemAnimator());
        // aantal kolommen instellen
        mBinding.recyclerViewAdvertising.setLayoutManager(new GridLayoutManager(getContext(),
                getResources().getInteger(R.integer.retailerListColumnCount)
        ));

        return mBinding.getRoot();
    }

    public interface AdvertisingFragmentListener {
        // todo: events
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // zeker zijn dat de container activity de callback geimplementeerd heeft
        if (context instanceof AdvertisingFragmentListener) {
            mListener = (AdvertisingFragmentListener)context;
        } else {
            throw new ClassCastException(context.toString() + "must implement " + AdvertisingFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
