package be.nmct.unitycard.models.viewmodels.activities;

import android.content.Context;
import android.databinding.BaseObservable;

import be.nmct.unitycard.databinding.ActivityRetailerMapBinding;

/**
 * Created by Stephen on 9/11/2016.
 */

public class RetailerMapActivityVM extends BaseObservable {

    private ActivityRetailerMapBinding mBinding;
    private Context mContext;

    public RetailerMapActivityVM(ActivityRetailerMapBinding binding, Context context) {
        this.mBinding = binding;
        this.mContext = context;

        mBinding.setViewmodel(this);
    }
}
