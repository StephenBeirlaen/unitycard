package be.nmct.unitycard.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import be.nmct.unitycard.R;
import be.nmct.unitycard.fragments.AdvertisingFragment;
import be.nmct.unitycard.fragments.MyLoyaltyCardFragment;
import be.nmct.unitycard.fragments.RetailerListFragment;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements
        NavigationView.OnNavigationItemSelectedListener,
        MyLoyaltyCardFragment.MyLoyaltyCardFragmentListener,
        RetailerListFragment.RetailerListFragmentListener,
        AdvertisingFragment.AdvertisingFragmentListener {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;
    @Bind(R.id.nav_view) NavigationView navigationView;
    @Bind(R.id.fab_add_retailer) FloatingActionButton fabAddRetailer;

    private final String LOG_TAG = this.getClass().getSimpleName();
    public static final int LOGIN_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_my_loyalty_card);

        fabAddRetailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddRetailerActivity();
            }
        });

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        showFragmentMyLoyaltyCard(); //showAccountActivity();
        /*if (userStorage.getStoredUser() == null) { // niet ingelogd
            showAccountActivity();
            // todo: login logic
        }
        else {
            gcmTopicsSubscriptionHelper.getDeviceTokenAndSubscribe();
            displayUsernameInSidebar();
        }*/
    }

    private void showAccountActivity() {
        Intent intent = new Intent(MainActivity.this, AccountActivity.class);
        startActivityForResult(intent, LOGIN_REQUEST);
    }

    private void showAddRetailerActivity() {
        Intent intent = new Intent(MainActivity.this, AddRetailerActivity.class);
        startActivity(intent);
    }

    private void showSettingsActivity() {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LOGIN_REQUEST:
                switch (resultCode) {
                    case RESULT_OK:
                        // logged in successfully
                        break;
                    case RESULT_CANCELED:
                        finish(); // afsluiten
                        break;
                }
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void showFragmentMyLoyaltyCard() {
        hideFabAddRetailer();

        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.content_frame, MyLoyaltyCardFragment.newInstance(), MyLoyaltyCardFragment.class.getSimpleName()) // replace = remove + add
                .commit();
    }

    private void showFragmentRetailerList() {
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.content_frame, RetailerListFragment.newInstance(), RetailerListFragment.class.getSimpleName()) // replace = remove + add
                .commit();
    }

    private void showFragmentAdvertising() {
        hideFabAddRetailer();

        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.content_frame, AdvertisingFragment.newInstance(), AdvertisingFragment.class.getSimpleName()) // replace = remove + add
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();

            if (fragmentManager.getBackStackEntryCount() == 0) {
                // ik zit reeds in een top level fragment. We beëindigen de app.
                super.onBackPressed();
            }
            else {
                // de gebruiker gebruikt de Back toets om terug naar een top level fragment te gaan
                // ik zit in een andere fragment (settings) en wens terug te keren naar een top level fragment
                // Backstack: laatste transaction terug spoelen
                fragmentManager.popBackStack();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_my_loyalty_card) {
            showFragmentMyLoyaltyCard();
        } else if (id == R.id.nav_retailers) {
            showFragmentRetailerList();
        } else if (id == R.id.nav_advertising) {
            showFragmentAdvertising();
        } else if (id == R.id.nav_settings) {
            showSettingsActivity();
        } else if (id == R.id.nav_logout) {
            // todo: nav_logout button
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void showFabAddRetailer() {
        fabAddRetailer.show();
    }

    @Override
    public void hideFabAddRetailer() {
        fabAddRetailer.hide();
    }

    @Override
    public void handleError(String error) {
        // todo: handleError
    }
}
