package be.nmct.unitycard.models.viewmodels.activities;

import android.content.Context;
import android.databinding.BaseObservable;

import be.nmct.unitycard.databinding.ActivityAddRetailerBinding;

/**
 * Created by Stephen on 9/11/2016.
 */

public class AddRetailerActivityVM extends BaseObservable {

    private ActivityAddRetailerBinding mBinding;
    private Context mContext;

    public AddRetailerActivityVM(ActivityAddRetailerBinding binding, Context context) {
        this.mBinding = binding;
        this.mContext = context;

        mBinding.setViewmodel(this);
    }
}
