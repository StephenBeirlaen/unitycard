package be.nmct.unitycard.models.viewmodels;

import android.content.Context;
import android.databinding.BaseObservable;

import be.nmct.unitycard.databinding.ActivitySettingsBinding;

/**
 * Created by Stephen on 9/11/2016.
 */

public class SettingsActivityVM extends BaseObservable {

    private ActivitySettingsBinding mBinding;
    private Context mContext;

    public SettingsActivityVM(ActivitySettingsBinding binding, Context context) {
        this.mBinding = binding;
        this.mContext = context;

        mBinding.setViewmodel(this);
    }
}
