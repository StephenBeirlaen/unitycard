package be.nmct.unitycard.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import be.nmct.unitycard.R;
import be.nmct.unitycard.auth.AuthHelper;
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

    @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.nav_view) NavigationView navigationView;
    @Bind(R.id.fab_add_retailer) FloatingActionButton fabAddRetailer;

    private final String LOG_TAG = this.getClass().getSimpleName();
    public static final int REQUEST_LOGIN = 1;

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
    }

    @Override
    protected void onStart() {
        super.onStart();

        Boolean loggedInResult = AuthHelper.isUserLoggedIn(this); // nullable boolean

        if (loggedInResult != null) { // app has permission to access accounts? If null, no permission
            if (loggedInResult) {
                // Logged in
                showFragmentMyLoyaltyCard();
                displayUsernameInSidebar();
            }
            else {
                // Not logged in, toon login scherm
                showAccountActivity();
            }
        }
        else {
            // Geen permission, skip het checken voor accounts en ga naar login scherm. Later bij het inloggen om permissie vragen
            showAccountActivity();
        }
    }

    private void displayUsernameInSidebar() {
        TextView txtSidebarUsername = (TextView)navigationView.getHeaderView(0).findViewById(R.id.txt_sidebar_username);

        txtSidebarUsername.setText(AuthHelper.getUsername(this));
    }

    private void showAccountActivity() {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN);
    }

    private void showAddRetailerActivity() {
        Intent intent = new Intent(this, AddRetailerActivity.class);
        startActivity(intent);
    }

    private void showSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void logOut() {
        AuthHelper.logUserOff(this);

        // clean up backstack
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        showAccountActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_LOGIN:
                switch (resultCode) {
                    case RESULT_OK:
                        Log.d(LOG_TAG, "User " + AuthHelper.getUsername(this) + " logged in from AccountActivity.");
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
            logOut();
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
        Snackbar.make(drawerLayout, error, Snackbar.LENGTH_LONG).show();
    }
}
