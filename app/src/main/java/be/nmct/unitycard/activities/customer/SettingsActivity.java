package be.nmct.unitycard.activities.customer;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import be.nmct.unitycard.R;
import be.nmct.unitycard.databinding.ActivitySettingsBinding;
import be.nmct.unitycard.fragments.customer.SettingsFragment;
import be.nmct.unitycard.models.viewmodels.activities.SettingsActivityVM;

public class SettingsActivity extends AppCompatActivity {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private ActivitySettingsBinding mBinding;
    private SettingsActivityVM mSettingsActivityVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings);

        if (getSupportActionBar() != null) { // back arrow op toolbar plaatsen
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        showFragmentSettings();
    }

    private void showFragmentSettings() {
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.content_frame, SettingsFragment.newInstance(), SettingsFragment.class.getSimpleName()) // replace = remove + add
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
