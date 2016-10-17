package be.nmct.unitycard.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.nmct.unitycard.R;
import butterknife.ButterKnife;

public class MyLoyaltyCardFragment extends Fragment {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private MyLoyaltyCardFragmentListener mListener;

    public MyLoyaltyCardFragment() {
        // Required empty public constructor
    }

    public static MyLoyaltyCardFragment newInstance() {
        return new MyLoyaltyCardFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_loyalty_card, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public interface MyLoyaltyCardFragmentListener {
        void handleError(String error);
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
