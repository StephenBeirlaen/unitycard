package be.nmct.unitycard.models.viewmodels.fragment;

import android.content.Context;
import android.databinding.BaseObservable;

import be.nmct.unitycard.databinding.FragmentAssociateAccountBinding;

/**
 * Created by Stephen on 9/11/2016.
 */

public class AssociateAccountFragmentVM extends BaseObservable {

    private FragmentAssociateAccountBinding mBinding;
    private Context mContext;

    public AssociateAccountFragmentVM(FragmentAssociateAccountBinding binding, Context context) {
        this.mBinding = binding;
        this.mContext = context;

        mBinding.setViewmodel(this);
    }
}
