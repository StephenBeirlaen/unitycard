package be.nmct.unitycard.activities.customer;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;

import be.nmct.unitycard.R;
import be.nmct.unitycard.activities.login.AccountActivity;
import be.nmct.unitycard.adapters.RetailerActivityPagerAdapter;
import be.nmct.unitycard.adapters.RetailerRecyclerViewAdapter;
import be.nmct.unitycard.databinding.ActivityRetailerBinding;
import be.nmct.unitycard.fragments.customer.RetailerInfoFragment;
import be.nmct.unitycard.fragments.customer.RetailerOffersFragment;
import be.nmct.unitycard.models.RetailerLocation;
import be.nmct.unitycard.models.viewmodels.activities.RetailerActivityVM;

import static be.nmct.unitycard.activities.customer.MainActivity.REQUEST_LOGIN;
import static be.nmct.unitycard.fragments.customer.RetailerMapFragment.ARG_RETAILER_LOCATION;
import static be.nmct.unitycard.fragments.customer.RetailerMapFragment.ARG_RETAILER_LOCATION_LIST;

public class RetailerActivity extends AppCompatActivity
        implements
        RetailerInfoFragment.RetailerInfoFragmentListener,
        RetailerOffersFragment.RetailerOffersFragmentListener {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private ActivityRetailerBinding mBinding;
    private RetailerActivityVM mRetailerActivityVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_retailer);

        setSupportActionBar(mBinding.toolbar);

        if (getSupportActionBar() != null) { // back arrow op toolbar plaatsen
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Integer retailerId = null;

        Intent intent = getIntent();
        if(intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null && extras.containsKey(RetailerRecyclerViewAdapter.EXTRA_RETAILER_ID)){
                retailerId = extras.getInt(RetailerRecyclerViewAdapter.EXTRA_RETAILER_ID);

                if (retailerId != null) {
                    Log.d(LOG_TAG, "showing retailer info, retailerid: " + retailerId);
                }
                else {
                    finish();
                    Log.d(LOG_TAG, "retailer is null");
                }
            }
            else {
                finish();
                Log.d(LOG_TAG, "extras is null or doesn't contains EXTRA_RETAILER_ID key");
            }
        }
        else {
            finish();
            Log.d(LOG_TAG, "intent is null");
        }

        RetailerActivityPagerAdapter pagerAdapter = new RetailerActivityPagerAdapter(getSupportFragmentManager(), retailerId);
        mBinding.viewpager.setAdapter(pagerAdapter);
        mBinding.tablayout.setupWithViewPager(mBinding.viewpager); // tabs instellen, o.a. de labels
        mBinding.tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mBinding.viewpager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        mBinding.swipeRefreshLayoutRetailer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showRefreshingIndicator(false);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { // handle back arrow click
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showRetailerMapActivity(RetailerLocation retailerLocation) {
        Intent intent = new Intent(this, RetailerMapActivity.class);

        Bundle extras = new Bundle();
        extras.putParcelable(ARG_RETAILER_LOCATION, retailerLocation);
        intent.putExtras(extras);

        startActivity(intent);
    }

    private void showAllRetailersMapActivity(ArrayList<RetailerLocation> retailerLocations) {
        Intent intent = new Intent(this, RetailerMapActivity.class);

        Bundle extras = new Bundle();
        extras.putParcelableArrayList(ARG_RETAILER_LOCATION_LIST, retailerLocations);
        intent.putExtras(extras);

        startActivity(intent);
    }

    private void showAccountActivity() {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN);
    }

    @Override
    public void requestNewLogin() {
        // Something went wrong, toon login scherm
        showAccountActivity();
    }

    @Override
    public void handleError(String error) {
        Snackbar.make(mBinding.retailerCoordinatorLayout, error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showRetailerMap(RetailerLocation retailerLocation) {
        showRetailerMapActivity(retailerLocation);
    }

    @Override
    public void showAllRetailersMap(ArrayList<RetailerLocation> retailerLocations) {
        showAllRetailersMapActivity(retailerLocations);
    }

    @Override
    public void showRefreshingIndicator(Boolean refreshing) {
        mBinding.swipeRefreshLayoutRetailer.setRefreshing(refreshing);
    }
}
