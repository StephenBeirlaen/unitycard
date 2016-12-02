package be.nmct.unitycard.activities.customer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import be.nmct.unitycard.R;
import be.nmct.unitycard.databinding.ActivityRetailerMapBinding;
import be.nmct.unitycard.fragments.customer.RetailerMapFragment;
import be.nmct.unitycard.models.viewmodels.activities.RetailerMapActivityVM;

import static be.nmct.unitycard.adapters.SyncAdapter.RESULT_SYNC_SUCCESS;
import static be.nmct.unitycard.fragments.customer.RetailerMapFragment.ARG_ADDRESS;
import static be.nmct.unitycard.fragments.customer.RetailerMapFragment.ARG_RETAILER_NAME;

public class RetailerMapActivity extends AppCompatActivity
        implements
        RetailerMapFragment.RetailerMapFragmentListener {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private ActivityRetailerMapBinding mBinding;
    private RetailerMapActivityVM mRetailerMapActivityVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_retailer_map);

        if (getSupportActionBar() != null) { // back arrow op toolbar plaatsen
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Bundle extras = getIntent().getExtras();
        String retailerName = extras.getString(ARG_RETAILER_NAME);
        String address = extras.getString(ARG_ADDRESS);

        if (retailerName != null && address != null) {
            showFragmentRetailerMap(retailerName, address);
        }
        else {
            finish();
        }
    }

    private void showFragmentRetailerMap(String retailerName, String address) {
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.content_frame, RetailerMapFragment.newInstance(retailerName, address), RetailerMapFragment.class.getSimpleName()) // replace = remove + add
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { // handle back arrow click
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void handleError(String error) {
        Snackbar.make(mBinding.contentFrame, error, Snackbar.LENGTH_LONG).show();
    }
}
