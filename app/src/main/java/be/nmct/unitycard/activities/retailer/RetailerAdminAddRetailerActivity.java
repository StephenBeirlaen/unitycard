package be.nmct.unitycard.activities.retailer;

import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import be.nmct.unitycard.R;
import be.nmct.unitycard.databinding.ActivityRetailerAdminAddRetailerBinding;
import be.nmct.unitycard.fragments.retailer.RetailerAdminAddRetailerFragment;
import be.nmct.unitycard.models.viewmodels.activities.RetailerAdminAddRetailerActivityVM;

public class RetailerAdminAddRetailerActivity extends AppCompatActivity implements RetailerAdminAddRetailerFragment.RetailerAdminAddRetailerFragmentListener{

    private final String LOG_TAG = this.getClass().getSimpleName();
    private ActivityRetailerAdminAddRetailerBinding mBinding;
    private RetailerAdminAddRetailerActivityVM mRetailerAdminAddRetailerActivityVM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_retailer_admin_add_retailer);

        if (getSupportActionBar() != null) { // back arrow op toolbar plaatsen
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        showFragmentRetailerAdminAddRetailer();
    }

    private void showFragmentRetailerAdminAddRetailer(){
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.content_frame, RetailerAdminAddRetailerFragment.newInstance(), RetailerAdminAddRetailerFragment.class.getSimpleName())
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
