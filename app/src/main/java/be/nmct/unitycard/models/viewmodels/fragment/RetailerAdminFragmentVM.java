package be.nmct.unitycard.models.viewmodels.fragment;

import android.content.Context;
import android.database.Cursor;
import android.databinding.BaseObservable;
import android.graphics.Bitmap;

import be.nmct.unitycard.databinding.FragmentRetailerAdminBinding;

/**
 * Created by Stephen on 9/11/2016.
 */

public class RetailerAdminFragmentVM extends BaseObservable {

    private FragmentRetailerAdminBinding mBinding;
    private Context mContext;

    public RetailerAdminFragmentVM(FragmentRetailerAdminBinding binding, Context context) {
        this.mBinding = binding;
        this.mContext = context;

        mBinding.setViewmodel(this);
    }
}
