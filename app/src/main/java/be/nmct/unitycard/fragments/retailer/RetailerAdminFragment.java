package be.nmct.unitycard.fragments.retailer;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.FileOutputStream;

import be.nmct.unitycard.R;
import be.nmct.unitycard.auth.AuthHelper;
import be.nmct.unitycard.databinding.FragmentRetailerAdminBinding;
import be.nmct.unitycard.models.Retailer;
import be.nmct.unitycard.models.viewmodels.fragment.RetailerAdminAddRetailerFragmentVM;
import be.nmct.unitycard.models.viewmodels.fragment.RetailerAdminFragmentVM;
import be.nmct.unitycard.repositories.ApiRepository;

public class RetailerAdminFragment extends Fragment {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private RetailerAdminFragmentListener mListener;
    private FragmentRetailerAdminBinding mBinding;
    private RetailerAdminFragmentVM mRetailerAdminFragmentVM;

    public static Retailer retailer = null;
    public static int loyaltyCardId = 0;

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
    public void onResume() {
        super.onResume();
        if(retailer != null){
            mBinding.txtRetailer.setText(retailer.getName());
            mRetailerAdminFragmentVM.retailer = retailer;
            mRetailerAdminFragmentVM.mLoyaltyCardId = loyaltyCardId;
        }
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
