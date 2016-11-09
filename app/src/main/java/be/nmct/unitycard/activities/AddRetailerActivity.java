package be.nmct.unitycard.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import be.nmct.unitycard.R;
import be.nmct.unitycard.databinding.ActivityAddRetailerBinding;
import be.nmct.unitycard.fragments.AddRetailerFragment;
import be.nmct.unitycard.models.viewmodels.AddRetailerActivityVM;

public class AddRetailerActivity extends AppCompatActivity
        implements
        AddRetailerFragment.AddRetailerFragmentListener {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private ActivityAddRetailerBinding mBinding;
    private AddRetailerActivityVM mAddRetailerActivityVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_retailer);

        if (getSupportActionBar() != null) { // back arrow op toolbar plaatsen
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        showFragmentAddRetailer();
    }

    private void showFragmentAddRetailer() {
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.content_frame, AddRetailerFragment.newInstance(), AddRetailerFragment.class.getSimpleName()) // replace = remove + add
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
