package be.nmct.unitycard.fragments.customer;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.nmct.unitycard.R;
import be.nmct.unitycard.databinding.FragmentRetailerMapBinding;
import be.nmct.unitycard.models.viewmodels.fragment.RetailerMapFragmentVM;

public class RetailerMapFragment extends Fragment {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private RetailerMapFragmentListener mListener;
    private FragmentRetailerMapBinding mBinding;
    private RetailerMapFragmentVM mRetailerMapFragmentVM;

    public RetailerMapFragment() {
        // Required empty public constructor
    }

    public static RetailerMapFragment newInstance() {
        return new RetailerMapFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_retailer_map, container, false);
        mRetailerMapFragmentVM = new RetailerMapFragmentVM(mBinding, getActivity());

        return mBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        // todo: perform load actions here
    }

    public interface RetailerMapFragmentListener {
        // todo: events, indien er geen events zijn, onAttach en ondetach overrides wegdoen
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // zeker zijn dat de container activity de callback geimplementeerd heeft
        if (context instanceof RetailerMapFragmentListener) {
            mListener = (RetailerMapFragmentListener)context;
        } else {
            throw new ClassCastException(context.toString() + "must implement " + RetailerMapFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
