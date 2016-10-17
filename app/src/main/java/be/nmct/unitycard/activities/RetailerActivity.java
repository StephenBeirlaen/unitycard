package be.nmct.unitycard.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import be.nmct.unitycard.R;
import be.nmct.unitycard.adapters.RetailerActivityPagerAdapter;
import be.nmct.unitycard.fragments.RetailerInfoFragment;
import be.nmct.unitycard.fragments.RetailerOffersFragment;
import butterknife.Bind;
import butterknife.ButterKnife;

public class RetailerActivity extends AppCompatActivity
        implements
        RetailerInfoFragment.RetailerInfoFragmentListener,
        RetailerOffersFragment.RetailerOffersFragmentListener {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.tablayout) TabLayout tabLayout;
    @Bind(R.id.viewpager) ViewPager viewPager;
    RetailerActivityPagerAdapter pagerAdapter;

    private final String LOG_TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) { // back arrow op toolbar plaatsen
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        pagerAdapter = new RetailerActivityPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager); // tabs instellen, o.a. de labels
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
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
