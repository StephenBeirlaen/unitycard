package be.nmct.unitycard.models.viewmodels.activities;

import android.content.Context;
import android.databinding.BaseObservable;

import be.nmct.unitycard.databinding.ActivityAccountBinding;

/**
 * Created by Stephen on 9/11/2016.
 */

public class AccountActivityVM extends BaseObservable {

    private ActivityAccountBinding mBinding;
    private Context mContext;

    public AccountActivityVM(ActivityAccountBinding binding, Context context) {
        this.mBinding = binding;
        this.mContext = context;

        mBinding.setViewmodel(this);
    }
}
