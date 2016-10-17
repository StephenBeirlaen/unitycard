package be.nmct.unitycard.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.nmct.unitycard.R;
import butterknife.ButterKnife;

public class RetailerInfoFragment extends Fragment {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private RetailerInfoFragmentListener mListener;

    public RetailerInfoFragment() {
        // Required empty public constructor
    }

    public static RetailerInfoFragment newInstance() {
        return new RetailerInfoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_retailer_info, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public interface RetailerInfoFragmentListener {
        // todo: events, indien er geen events zijn, onAttach en ondetach overrides wegdoen
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // zeker zijn dat de container activity de callback geimplementeerd heeft
        if (context instanceof RetailerInfoFragmentListener) {
            mListener = (RetailerInfoFragmentListener)context;
        } else {
            throw new ClassCastException(context.toString() + "must implement " + RetailerInfoFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
