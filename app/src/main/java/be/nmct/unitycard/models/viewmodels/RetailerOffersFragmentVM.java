package be.nmct.unitycard.models.viewmodels;

import android.content.Context;
import android.databinding.BaseObservable;

import be.nmct.unitycard.databinding.FragmentRetailerOffersBinding;

/**
 * Created by Stephen on 9/11/2016.
 */

public class RetailerOffersFragmentVM extends BaseObservable {

    private FragmentRetailerOffersBinding mBinding;
    private Context mContext;

    public RetailerOffersFragmentVM(FragmentRetailerOffersBinding binding, Context context) {
        this.mBinding = binding;
        this.mContext = context;

        mBinding.setViewmodel(this);
    }
}
