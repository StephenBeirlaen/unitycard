package be.nmct.unitycard.fragments;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import be.nmct.unitycard.R;
import be.nmct.unitycard.activities.MainActivity;
import be.nmct.unitycard.auth.AuthHelper;
import be.nmct.unitycard.contracts.LoyaltyCardContract;
import be.nmct.unitycard.databinding.FragmentMyLoyaltyCardBinding;
import be.nmct.unitycard.models.LoyaltyCard;
import be.nmct.unitycard.models.viewmodels.MyLoyaltyCardFragmentVM;
import be.nmct.unitycard.repositories.ApiRepository;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class MyLoyaltyCardFragment extends Fragment {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private MyLoyaltyCardFragmentListener mListener;
    private FragmentMyLoyaltyCardBinding mBinding;
    private MyLoyaltyCardFragmentVM mMyLoyaltyCardFragmentVM;

    public MyLoyaltyCardFragment() {
        // Required empty public constructor
    }

    public static MyLoyaltyCardFragment newInstance() {
        return new MyLoyaltyCardFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_loyalty_card, container, false);
        mMyLoyaltyCardFragmentVM = new MyLoyaltyCardFragmentVM(mBinding, getActivity());

        return mBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        mMyLoyaltyCardFragmentVM.loadQRcode();
    }

    public interface MyLoyaltyCardFragmentListener {
        void handleError(String error);
        // todo: weg indien geen events?
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // zeker zijn dat de container activity de callback geimplementeerd heeft
        if (context instanceof MyLoyaltyCardFragmentListener) {
            mListener = (MyLoyaltyCardFragmentListener)context;
        } else {
            throw new ClassCastException(context.toString() + "must implement " + MyLoyaltyCardFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
