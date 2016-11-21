package be.nmct.unitycard.models.viewmodels.fragment;

import android.app.Activity;
import android.content.Context;
import android.databinding.BaseObservable;
import android.view.View;

import com.google.zxing.integration.android.IntentIntegrator;

import be.nmct.unitycard.databinding.FragmentRetailerAdminBinding;

/**
 * Created by Stephen on 9/11/2016.
 */

public class RetailerAdminFragmentVM extends BaseObservable {

    private FragmentRetailerAdminBinding mBinding;
    private Context mContext;

    public RetailerAdminFragmentVM(FragmentRetailerAdminBinding binding, final Context context) {
        this.mBinding = binding;
        this.mContext = context;

        mBinding.setViewmodel(this);

        mBinding.btnScanLoyaltycard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Scanning via intent
                // https://github.com/zxing/zxing/wiki/Scanning-Via-Intent
                IntentIntegrator integrator = new IntentIntegrator((Activity) mContext);
                integrator.initiateScan();
            }
        });
    }
}
