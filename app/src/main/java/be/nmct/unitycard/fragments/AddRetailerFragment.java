package be.nmct.unitycard.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.nmct.unitycard.R;
import be.nmct.unitycard.adapters.AddRetailerRecyclerViewAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;

import static be.nmct.unitycard.contracts.ContentProviderContract.RETAILERS_URI;

public class AddRetailerFragment extends Fragment {

    @Bind(R.id.recyclerViewAddRetailerFragment) RecyclerView recyclerViewAddRetailerList;

    private final String LOG_TAG = this.getClass().getSimpleName();
    private AddRetailerFragmentListener mListener;
    private AddRetailerRecyclerViewAdapter addRetailerRecyclerViewAdapter;

    public AddRetailerFragment() {
        // Required empty public constructor
    }

    public static AddRetailerFragment newInstance() {
        return new AddRetailerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_retailer, container, false);
        ButterKnife.bind(this, view);

        recyclerViewAddRetailerList.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewAddRetailerList.setHasFixedSize(true);
        recyclerViewAddRetailerList.setItemAnimator(new DefaultItemAnimator());

        loadRetailers();

        return view;
    }

    private void loadRetailers() {
        String[] columns = new String[] {
                "Id",
                "RetailerCategoryId",
                "RetailerName",
                "Tagline",
                "Chain",
                "LogoUrl"
        };// todo: kan dit niet van ergens anders komen?
        Cursor data = getActivity().getContentResolver().query(RETAILERS_URI, columns, null, null, null);

        addRetailerRecyclerViewAdapter = new AddRetailerRecyclerViewAdapter(getActivity(), data/*, mListener*/);
        recyclerViewAddRetailerList.setAdapter(addRetailerRecyclerViewAdapter);
    }

    public interface AddRetailerFragmentListener {
        // todo: events, indien er geen events zijn, onAttach en ondetach overrides wegdoen
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // zeker zijn dat de container activity de callback geimplementeerd heeft
        if (context instanceof AddRetailerFragmentListener) {
            mListener = (AddRetailerFragmentListener)context;
        } else {
            throw new ClassCastException(context.toString() + "must implement " + AddRetailerFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
