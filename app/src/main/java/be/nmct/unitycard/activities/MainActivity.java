package be.nmct.unitycard.activities;

import android.accounts.Account;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import be.nmct.unitycard.R;
import be.nmct.unitycard.auth.AuthHelper;
import be.nmct.unitycard.databinding.ActivityMainBinding;
import be.nmct.unitycard.fragments.AdvertisingFragment;
import be.nmct.unitycard.fragments.MyLoyaltyCardFragment;
import be.nmct.unitycard.fragments.RetailerListFragment;
import be.nmct.unitycard.models.viewmodels.MainActivityVM;

public class MainActivity extends AppCompatActivity
        implements
        NavigationView.OnNavigationItemSelectedListener,
        MyLoyaltyCardFragment.MyLoyaltyCardFragmentListener,
        RetailerListFragment.RetailerListFragmentListener,
        AdvertisingFragment.AdvertisingFragmentListener {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private ActivityMainBinding mBinding;
    private MainActivityVM mMainActivityVM;
    public static final int REQUEST_LOGIN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(mBinding.toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                mBinding.drawerLayout,
                mBinding.toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mBinding.navView.setNavigationItemSelectedListener(this);
        mBinding.navView.setCheckedItem(R.id.nav_my_loyalty_card);

        mBinding.fabAddRetailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddRetailerActivity();
            }
        });

        mBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // De huidige fragment opzoeken
                // todo

                // Vragen aan die fragment om zijn content te vernieuwen
                // todo
            }
        });

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);


        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Boolean loggedInResult = AuthHelper.isUserLoggedIn(this); // nullable boolean

        if (loggedInResult != null) { // app has permission to access accounts? If null -> no permission
            if (loggedInResult) {
                // Logged in
                showFragmentMyLoyaltyCard();
                displayUsernameInSidebar(AuthHelper.getUser(this));
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

    private void displayUsernameInSidebar(Account user) {
        TextView txtSidebarUsername = (TextView) mBinding.navView.getHeaderView(0).findViewById(R.id.txt_sidebar_username);

        txtSidebarUsername.setText(AuthHelper.getUsername(user));
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

        // Clean up backstack
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
                        Log.d(LOG_TAG, "User " + AuthHelper.getUsername(AuthHelper.getUser(this)) + " logged in from AccountActivity.");
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
        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START);
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

        mBinding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void showFabAddRetailer() {
        mBinding.fabAddRetailer.show();
    }

    @Override
    public void hideFabAddRetailer() {
        mBinding.fabAddRetailer.hide();
    }

    public static final String TASK_LOAD_MY_LOYALTY_CARD = "TASK_LOAD_MY_LOYALTY_CARD";
    public static final String TASK_LOAD_RETAILERS = "TASK_LOAD_RETAILERS";

    // Lijst die bijhoudt welke taken er in progress zijn, voor parallele taken.
    private List<String> pendingTasks = new ArrayList<>();

    @Override
    public void swipeRefreshLayoutAddTask(String task) {
        // Add task
        pendingTasks.add(task);

        // Show refreshing indicator
        mBinding.swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void swipeRefreshLayoutRemoveTask(String task) {
        // Remove the task from the list
        pendingTasks.remove(task);

        // Determine if the refreshing indicator must be hidden
        if (pendingTasks.isEmpty()) {
            mBinding.swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void requestNewLogin() {
        // Something went wrong, toon login scherm
        showAccountActivity();
    }

    @Override
    public void handleError(String error) {
        Snackbar.make(mBinding.drawerLayout, error, Snackbar.LENGTH_LONG).show();
        mBinding.swipeRefreshLayout.setRefreshing(false); // Stop refresh animation
    }
}
