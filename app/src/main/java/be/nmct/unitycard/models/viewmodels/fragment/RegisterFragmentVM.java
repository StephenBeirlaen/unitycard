package be.nmct.unitycard.models.viewmodels.fragment;

import android.content.Context;
import android.databinding.BaseObservable;

import be.nmct.unitycard.databinding.FragmentRegisterBinding;

/**
 * Created by Stephen on 9/11/2016.
 */

public class RegisterFragmentVM extends BaseObservable {

    private FragmentRegisterBinding mBinding;
    private Context mContext;

    public RegisterFragmentVM(FragmentRegisterBinding binding, Context context) {
        this.mBinding = binding;
        this.mContext = context;

        mBinding.setViewmodel(this);
    }
}
