package be.nmct.unitycard.fragments.retailer;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.nmct.unitycard.R;
import be.nmct.unitycard.databinding.FragmentRetailerAdminBinding;
import be.nmct.unitycard.models.viewmodels.fragment.RetailerAdminFragmentVM;

public class RetailerAdminFragment extends Fragment {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private RetailerAdminFragmentListener mListener;
    private FragmentRetailerAdminBinding mBinding;
    private RetailerAdminFragmentVM mRetailerAdminFragmentVM;

    public RetailerAdminFragment() {
        // Required empty public constructor
    }

    public static RetailerAdminFragment newInstance() {
        return new RetailerAdminFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_retailer_admin, container, false);
        mRetailerAdminFragmentVM = new RetailerAdminFragmentVM(mBinding, getActivity(), mListener);

        return mBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public interface RetailerAdminFragmentListener {
        void requestLogOut();
        void handleError(String error);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // zeker zijn dat de container activity de callback geimplementeerd heeft
        if (context instanceof RetailerAdminFragmentListener) {
            mListener = (RetailerAdminFragmentListener)context;
        } else {
            throw new ClassCastException(context.toString() + "must implement " + RetailerAdminFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
