package be.nmct.unitycard.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.nmct.unitycard.R;
import butterknife.ButterKnife;

public class AdvertisingFragment extends Fragment {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private AdvertisingFragmentListener mListener;

    public AdvertisingFragment() {
        // Required empty public constructor
    }

    public static AdvertisingFragment newInstance() {
        return new AdvertisingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_advertising, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public interface AdvertisingFragmentListener {
        // todo: events
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // zeker zijn dat de container activity de callback geimplementeerd heeft
        if (context instanceof AdvertisingFragmentListener) {
            mListener = (AdvertisingFragmentListener)context;
        } else {
            throw new ClassCastException(context.toString() + "must implement " + AdvertisingFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
