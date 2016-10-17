package be.nmct.unitycard.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.nmct.unitycard.R;
import butterknife.ButterKnife;

public class RetailerOffersFragment extends Fragment {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private RetailerOffersFragmentListener mListener;

    public RetailerOffersFragment() {
        // Required empty public constructor
    }

    public static RetailerOffersFragment newInstance() {
        return new RetailerOffersFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_retailer_offers, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public interface RetailerOffersFragmentListener {
        // todo: events, indien er geen events zijn, onAttach en ondetach overrides wegdoen
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // zeker zijn dat de container activity de callback geimplementeerd heeft
        if (context instanceof RetailerOffersFragmentListener) {
            mListener = (RetailerOffersFragmentListener)context;
        } else {
            throw new ClassCastException(context.toString() + "must implement " + RetailerOffersFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
