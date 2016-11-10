package be.nmct.unitycard.models.viewmodels;

import android.content.Context;
import android.databinding.BaseObservable;

import be.nmct.unitycard.databinding.FragmentLoginBinding;

/**
 * Created by Stephen on 9/11/2016.
 */

public class LoginFragmentVM extends BaseObservable {

    private FragmentLoginBinding mBinding;
    private Context mContext;

    public LoginFragmentVM(FragmentLoginBinding binding, Context context) {
        this.mBinding = binding;
        this.mContext = context;

        mBinding.setViewmodel(this);
    }
}
