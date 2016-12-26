package be.nmct.unitycard.fragments.retailer;

import android.annotation.SuppressLint;
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
import be.nmct.unitycard.databinding.FragmentRetailerAdminAddRetailerBinding;
import be.nmct.unitycard.fragments.customer.AddRetailerFragment;
import be.nmct.unitycard.models.viewmodels.fragment.RetailerAdminAddRetailerFragmentVM;

import static be.nmct.unitycard.contracts.ContentProviderContract.RETAILERS_URI;

/**
 * Created by lorenzvercoutere on 25/12/16.
 */

public class RetailerAdminAddRetailerFragment extends Fragment implements SearchView.OnQueryTextListener {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private RetailerAdminAddRetailerFragmentListener mListener;
    private FragmentRetailerAdminAddRetailerBinding mBinding;
    private RetailerAdminAddRetailerFragmentVM mRetailerAdminAddRetailerFragmentVM;
    private RetailerAdminAddRetailerFragmentVM.MyContentObserver myContentObserver;

    public RetailerAdminAddRetailerFragment() {
        // Required empty public constructor
    }

    public static RetailerAdminAddRetailerFragment newInstance() {
        return new RetailerAdminAddRetailerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_retailer_admin_add_retailer, container, false);
        mRetailerAdminAddRetailerFragmentVM = new RetailerAdminAddRetailerFragmentVM(mBinding, getActivity());

        setHasOptionsMenu(true);

        mBinding.recyclerViewAddRetailerList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.recyclerViewAddRetailerList.setHasFixedSize(true);
        mBinding.recyclerViewAddRetailerList.setItemAnimator(new DefaultItemAnimator());
        mBinding.recyclerViewAddRetailerList.setLayoutManager(new GridLayoutManager(getContext(),
                getResources().getInteger(R.integer.retailerListColumnCount)
        ));

        return mBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_retailers, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        myContentObserver = mRetailerAdminAddRetailerFragmentVM.new MyContentObserver(new Handler());
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
        if(newText != null){
            if(!newText.equals(mRetailerAdminAddRetailerFragmentVM.mSearchQuery)){
                mRetailerAdminAddRetailerFragmentVM.mSearchQuery = newText;
                mRetailerAdminAddRetailerFragmentVM.updateRecyclerView();
            }
        }

        return false;
    }

    public interface RetailerAdminAddRetailerFragmentListener{
        // todo: events, indien er geen events zijn, onAttach en ondetach overrides wegdoen
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof RetailerAdminAddRetailerFragmentListener){
            mListener = (RetailerAdminAddRetailerFragmentListener) context;
        } else {
            throw new ClassCastException(context.toString() + "must implement " + RetailerAdminAddRetailerFragment.RetailerAdminAddRetailerFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
