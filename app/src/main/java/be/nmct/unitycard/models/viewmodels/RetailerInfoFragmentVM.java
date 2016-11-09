package be.nmct.unitycard.models.viewmodels;

import android.content.Context;
import android.databinding.BaseObservable;

import be.nmct.unitycard.databinding.FragmentRetailerInfoBinding;

/**
 * Created by Stephen on 9/11/2016.
 */

public class RetailerInfoFragmentVM extends BaseObservable {

    private FragmentRetailerInfoBinding mBinding;
    private Context mContext;

    public RetailerInfoFragmentVM(FragmentRetailerInfoBinding binding, Context context) {
        this.mBinding = binding;
        this.mContext = context;

        mBinding.setViewmodel(this);
    }
}
