package be.nmct.unitycard.fragments.customer;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.nmct.unitycard.R;
import be.nmct.unitycard.databinding.FragmentMyLoyaltyCardBinding;
import be.nmct.unitycard.models.viewmodels.fragment.MyLoyaltyCardFragmentVM;

import static be.nmct.unitycard.contracts.ContentProviderContract.LOYALTYCARDS_URI;

public class MyLoyaltyCardFragment extends Fragment {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private MyLoyaltyCardFragmentListener mListener;
    private FragmentMyLoyaltyCardBinding mBinding;
    private MyLoyaltyCardFragmentVM mMyLoyaltyCardFragmentVM;
    private MyLoyaltyCardFragmentVM.MyContentObserver myContentObserver;

    public MyLoyaltyCardFragment() {
        // Required empty public constructor
    }

    public static MyLoyaltyCardFragment newInstance() {
        return new MyLoyaltyCardFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_loyalty_card, container, false);
        mMyLoyaltyCardFragmentVM = new MyLoyaltyCardFragmentVM(mBinding, getActivity());

        return mBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Listen for contentprovider changes
        myContentObserver = mMyLoyaltyCardFragmentVM.new MyContentObserver(new Handler());
        getContext().getContentResolver().registerContentObserver(LOYALTYCARDS_URI, false, myContentObserver);
    }

    @Override
    public void onPause() {
        super.onPause();

        getContext().getContentResolver().unregisterContentObserver(myContentObserver);
    }

    public interface MyLoyaltyCardFragmentListener {
        void handleError(String error);
        // todo: weg indien geen events?
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // zeker zijn dat de container activity de callback geimplementeerd heeft
        if (context instanceof MyLoyaltyCardFragmentListener) {
            mListener = (MyLoyaltyCardFragmentListener)context;
        } else {
            throw new ClassCastException(context.toString() + "must implement " + MyLoyaltyCardFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
