package be.nmct.unitycard.models.viewmodels.activities;

import android.content.Context;
import android.databinding.BaseObservable;

import be.nmct.unitycard.databinding.ActivityRetailerAdminBinding;

/**
 * Created by Stephen on 9/11/2016.
 */

public class RetailerAdminActivityVM extends BaseObservable {

    private ActivityRetailerAdminBinding mBinding;
    private Context mContext;

    public RetailerAdminActivityVM(ActivityRetailerAdminBinding binding, Context context) {
        this.mBinding = binding;
        this.mContext = context;

        mBinding.setViewmodel(this);
    }
}
