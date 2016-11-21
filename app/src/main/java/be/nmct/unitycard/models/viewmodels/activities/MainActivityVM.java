package be.nmct.unitycard.models.viewmodels.activities;

import android.content.Context;
import android.databinding.BaseObservable;

import be.nmct.unitycard.databinding.ActivityMainBinding;

/**
 * Created by Stephen on 9/11/2016.
 */

public class MainActivityVM extends BaseObservable {

    private ActivityMainBinding mBinding;
    private Context mContext;

    public MainActivityVM(ActivityMainBinding binding, Context context) {
        this.mBinding = binding;
        this.mContext = context;

        mBinding.setViewmodel(this);
    }
}
