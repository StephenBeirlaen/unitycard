package be.nmct.unitycard.activities.customer;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import be.nmct.unitycard.R;
import be.nmct.unitycard.adapters.RetailerActivityPagerAdapter;
import be.nmct.unitycard.databinding.ActivityRetailerBinding;
import be.nmct.unitycard.fragments.customer.RetailerInfoFragment;
import be.nmct.unitycard.fragments.customer.RetailerOffersFragment;
import be.nmct.unitycard.models.viewmodels.activities.RetailerActivityVM;

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

        RetailerActivityPagerAdapter pagerAdapter = new RetailerActivityPagerAdapter(getSupportFragmentManager());
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { // handle back arrow click
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
