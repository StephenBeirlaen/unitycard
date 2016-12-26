package be.nmct.unitycard.models.viewmodels.activities;

import android.content.Context;

import be.nmct.unitycard.databinding.ActivityRetailerAdminAddRetailerBinding;

/**
 * Created by lorenzvercoutere on 25/12/16.
 */

public class RetailerAdminAddRetailerActivityVM {

    private ActivityRetailerAdminAddRetailerBinding mBinding;
    private Context mContext;

    public RetailerAdminAddRetailerActivityVM(Context context, ActivityRetailerAdminAddRetailerBinding binding){
        this.mContext = context;
        this.mBinding = binding;

        mBinding.setViewmodel(this);
    }
}
