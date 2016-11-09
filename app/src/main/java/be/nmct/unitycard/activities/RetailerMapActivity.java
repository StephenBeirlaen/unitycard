package be.nmct.unitycard.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import be.nmct.unitycard.R;
import be.nmct.unitycard.databinding.ActivityRetailerMapBinding;
import be.nmct.unitycard.fragments.RetailerMapFragment;
import be.nmct.unitycard.models.viewmodels.RetailerMapActivityVM;

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

        showFragmentRetailerMap();
    }

    private void showFragmentRetailerMap() {
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.content_frame, RetailerMapFragment.newInstance(), RetailerMapFragment.class.getSimpleName()) // replace = remove + add
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { // handle back arrow click
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
