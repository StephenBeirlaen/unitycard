package be.nmct.unitycard.models.viewmodels.activities;

import android.content.Context;
import android.databinding.BaseObservable;

import be.nmct.unitycard.databinding.ActivityRetailerBinding;

/**
 * Created by Stephen on 9/11/2016.
 */

public class RetailerActivityVM extends BaseObservable {

    private ActivityRetailerBinding mBinding;
    private Context mContext;

    public RetailerActivityVM(ActivityRetailerBinding binding, Context context) {
        this.mBinding = binding;
        this.mContext = context;

        mBinding.setViewmodel(this);
    }
}
