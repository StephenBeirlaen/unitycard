package be.nmct.unitycard.fragments.customer;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.nmct.unitycard.R;
import be.nmct.unitycard.adapters.RetailerRecyclerViewAdapter;
import be.nmct.unitycard.databinding.FragmentRetailerInfoBinding;
import be.nmct.unitycard.models.Retailer;
import be.nmct.unitycard.models.viewmodels.fragment.RetailerInfoFragmentVM;

public class RetailerInfoFragment extends Fragment {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private RetailerInfoFragmentListener mListener;
    private FragmentRetailerInfoBinding mBinding;
    private RetailerInfoFragmentVM mRetailerInfoFragmentVM;

    public RetailerInfoFragment() {
        // Required empty public constructor
    }

    public static RetailerInfoFragment newInstance(int retailerId) {
        RetailerInfoFragment retailerInfoFragment = new RetailerInfoFragment();

        Bundle args = new Bundle();
        args.putInt(RetailerRecyclerViewAdapter.EXTRA_RETAILER_ID, retailerId);
        retailerInfoFragment.setArguments(args);

        return retailerInfoFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_retailer_info, container, false);

        Integer retailerId = null;

        Bundle args = getArguments();
        if (args != null && args.containsKey(RetailerRecyclerViewAdapter.EXTRA_RETAILER_ID)){
            retailerId = args.getInt(RetailerRecyclerViewAdapter.EXTRA_RETAILER_ID);

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

        mRetailerInfoFragmentVM = new RetailerInfoFragmentVM(mBinding, getActivity(), retailerId);

        return mBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        // todo: perform load actions here
    }

    public interface RetailerInfoFragmentListener {
        void handleError(String error);
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
