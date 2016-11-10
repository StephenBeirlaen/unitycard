package be.nmct.unitycard.models.viewmodels;

import android.content.Context;
import android.databinding.BaseObservable;

import be.nmct.unitycard.databinding.FragmentAdvertisingBinding;

/**
 * Created by Stephen on 9/11/2016.
 */

public class AdvertisingFragmentVM extends BaseObservable {

    private FragmentAdvertisingBinding mBinding;
    private Context mContext;

    public AdvertisingFragmentVM(FragmentAdvertisingBinding binding, Context context) {
        this.mBinding = binding;
        this.mContext = context;

        mBinding.setViewmodel(this);
    }
}
