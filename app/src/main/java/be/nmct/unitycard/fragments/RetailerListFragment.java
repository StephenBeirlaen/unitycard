package be.nmct.unitycard.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.nmct.unitycard.R;
import butterknife.ButterKnife;

public class RetailerListFragment extends Fragment {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private RetailerListFragmentListener mListener;

    public RetailerListFragment() {
        // Required empty public constructor
    }

    public static RetailerListFragment newInstance() {
        return new RetailerListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_retailer_list, container, false);
        ButterKnife.bind(this, view);

        mListener.showFabAddRetailer();

        return view;
    }

    public interface RetailerListFragmentListener {
        void showFabAddRetailer();
        void hideFabAddRetailer();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // zeker zijn dat de container activity de callback geimplementeerd heeft
        if (context instanceof RetailerListFragmentListener) {
            mListener = (RetailerListFragmentListener)context;
        } else {
            throw new ClassCastException(context.toString() + "must implement " + RetailerListFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
