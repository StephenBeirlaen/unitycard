package be.nmct.unitycard.models.viewmodels.fragment;

import android.content.Context;
import android.databinding.BaseObservable;

import be.nmct.unitycard.databinding.FragmentRetailerMapBinding;

/**
 * Created by Stephen on 9/11/2016.
 */

public class RetailerMapFragmentVM extends BaseObservable {

    private FragmentRetailerMapBinding mBinding;
    private Context mContext;

    public RetailerMapFragmentVM(FragmentRetailerMapBinding binding, Context context) {
        this.mBinding = binding;
        this.mContext = context;

        mBinding.setViewmodel(this);
    }
}
