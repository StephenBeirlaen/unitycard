package be.nmct.unitycard.fragments.customer;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import be.nmct.unitycard.R;
import be.nmct.unitycard.databinding.FragmentAddRetailerBinding;
import be.nmct.unitycard.models.viewmodels.fragment.AddRetailerFragmentVM;

import static be.nmct.unitycard.contracts.ContentProviderContract.RETAILERS_URI;

public class AddRetailerFragment extends Fragment
        implements SearchView.OnQueryTextListener {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private AddRetailerFragmentListener mListener;
    private FragmentAddRetailerBinding mBinding;
    private AddRetailerFragmentVM mAddRetailerFragmentVM;
    private AddRetailerFragmentVM.MyContentObserver myContentObserver;

    public AddRetailerFragment() {
        // Required empty public constructor
    }

    public static AddRetailerFragment newInstance() {
        return new AddRetailerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_retailer, container, false);
        mAddRetailerFragmentVM = new AddRetailerFragmentVM(mBinding, getActivity());

        setHasOptionsMenu(true); // voegt de search knop toe

        mBinding.recyclerViewAddRetailerList.setLayoutManager(new LinearLayoutManager(getActivity())); // todo: naamgeving..?
        mBinding.recyclerViewAddRetailerList.setHasFixedSize(true);
        mBinding.recyclerViewAddRetailerList.setItemAnimator(new DefaultItemAnimator());
        // aantal kolommen instellen
        mBinding.recyclerViewAddRetailerList.setLayoutManager(new GridLayoutManager(getContext(),
                getResources().getInteger(R.integer.retailerListColumnCount)
        ));

        return mBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_retailers, menu);
        // Databinding werkt nog niet voor menu resources :(
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setOnQueryTextListener(this);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Listen for contentprovider changes
        myContentObserver = mAddRetailerFragmentVM.new MyContentObserver(new Handler());
        getContext().getContentResolver().registerContentObserver(RETAILERS_URI, false, myContentObserver);
    }

    @Override
    public void onPause() {
        super.onPause();

        getContext().getContentResolver().unregisterContentObserver(myContentObserver);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText != null) {
            // als de tekst verschillend is
            if (!newText.equals(mAddRetailerFragmentVM.mSearchQuery)) {
                mAddRetailerFragmentVM.mSearchQuery = newText;

                mAddRetailerFragmentVM.updateRecyclerView();
            }
        }

        return false;
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
