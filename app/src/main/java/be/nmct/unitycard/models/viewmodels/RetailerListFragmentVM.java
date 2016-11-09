package be.nmct.unitycard.models.viewmodels;

import android.content.Context;
import android.databinding.BaseObservable;

import be.nmct.unitycard.databinding.FragmentRetailerListBinding;

/**
 * Created by Stephen on 9/11/2016.
 */

public class RetailerListFragmentVM extends BaseObservable {

    private FragmentRetailerListBinding mBinding;
    private Context mContext;

    public RetailerListFragmentVM(FragmentRetailerListBinding binding, Context context) {
        this.mBinding = binding;
        this.mContext = context;

        mBinding.setViewmodel(this);
    }
}
