package be.nmct.unitycard.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.nmct.unitycard.R;
import be.nmct.unitycard.databinding.FragmentAddRetailerBinding;
import be.nmct.unitycard.models.viewmodels.AddRetailerFragmentVM;

public class AddRetailerFragment extends Fragment {

    //@Bind(R.id.recyclerViewAddRetailerFragment) RecyclerView recyclerViewAddRetailerList; // moved

    private final String LOG_TAG = this.getClass().getSimpleName();
    private AddRetailerFragmentListener mListener;
    private AddRetailerFragmentVM mAddRetailerFragmentVM;
    private FragmentAddRetailerBinding mFragmentAddRetailerBinding;
    //private AddRetailerRecyclerViewAdapter addRetailerRecyclerViewAdapter;

    public AddRetailerFragment() {
        // Required empty public constructor
    }

    public static AddRetailerFragment newInstance() {
        return new AddRetailerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_add_retailer, container, false);
        //ButterKnife.bind(this, view); // niet meer nodig met databinding

        mFragmentAddRetailerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_retailer, container, false);

        mFragmentAddRetailerBinding.recyclerViewAddRetailerList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFragmentAddRetailerBinding.recyclerViewAddRetailerList.setHasFixedSize(true);
        mFragmentAddRetailerBinding.recyclerViewAddRetailerList.setItemAnimator(new DefaultItemAnimator());

        mAddRetailerFragmentVM = new AddRetailerFragmentVM(mFragmentAddRetailerBinding, getActivity());

        //loadRetailers(); // moved to onStart()

        //return view;
        return mFragmentAddRetailerBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        mAddRetailerFragmentVM.loadRetailers();
    }

    private void loadRetailers() {
        /*String[] columns = new String[] { // moved to VM
                "Id",
                "RetailerCategoryId",
                "RetailerName",
                "Tagline",
                "Chain",
                "LogoUrl"
        };// todo: kan dit niet van ergens anders komen?
        Cursor data = getActivity().getContentResolver().query(RETAILERS_URI, columns, null, null, null);*/

        //addRetailerRecyclerViewAdapter = new AddRetailerRecyclerViewAdapter(getActivity(), data/*, mListener*/); // moved to retailersbinder
        //recyclerViewAddRetailerList.setAdapter(addRetailerRecyclerViewAdapter); // moved to retailersbinder
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
