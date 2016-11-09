package be.nmct.unitycard.models.viewmodels;

import android.content.Context;
import android.databinding.BaseObservable;

import be.nmct.unitycard.databinding.FragmentMyLoyaltyCardBinding;

/**
 * Created by Stephen on 9/11/2016.
 */

public class MyLoyaltyCardFragmentVM extends BaseObservable {

    private FragmentMyLoyaltyCardBinding mBinding;
    private Context mContext;

    public MyLoyaltyCardFragmentVM(FragmentMyLoyaltyCardBinding binding, Context context) {
        this.mBinding = binding;
        this.mContext = context;

        mBinding.setViewmodel(this);
    }
}
