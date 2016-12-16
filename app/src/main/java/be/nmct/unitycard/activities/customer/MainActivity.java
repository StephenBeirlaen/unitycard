package be.nmct.unitycard.activities.customer;

import android.accounts.Account;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import be.nmct.unitycard.R;
import be.nmct.unitycard.activities.login.AccountActivity;
import be.nmct.unitycard.activities.retailer.RetailerAdminActivity;
import be.nmct.unitycard.auth.AuthHelper;
import be.nmct.unitycard.contracts.AccountContract;
import be.nmct.unitycard.databinding.ActivityMainBinding;
import be.nmct.unitycard.fragments.customer.AdvertisingFragment;
import be.nmct.unitycard.fragments.customer.MyLoyaltyCardFragment;
import be.nmct.unitycard.fragments.customer.RetailerListFragment;
import be.nmct.unitycard.helpers.SyncHelper;
import be.nmct.unitycard.models.viewmodels.activities.MainActivityVM;

import static be.nmct.unitycard.adapters.SyncAdapter.RESULT_SYNC_SUCCESS;

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
                SyncHelper.refreshCachedData(MainActivity.this);
            }
        });

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Boolean loggedInResult = AuthHelper.isUserLoggedIn(this); // nullable boolean

        if (loggedInResult != null) { // app has permission to access accounts? If null -> no permission
            if (loggedInResult) {
                // Logged in

                // Check user account role
                String role = AuthHelper.getUserRole(this);
                if (TextUtils.isEmpty(role)) {
                    showAccountActivity();
                }
                else if (role.equals(AccountContract.ROLE_CUSTOMER)) {
                    if (getSupportFragmentManager().findFragmentById(R.id.content_frame) == null) {
                        // er is nog geen fragment actief, voeg er 1 toe
                        showFragmentMyLoyaltyCard();
                    }

                    displayUsernameInSidebar(AuthHelper.getUser(this));

                    // Registreren bij Firebase Cloud Messaging // todo: weg ?
                    //FirebaseMessaging.getInstance().subscribeToTopic("advertisements/1");
                }
                else if (role.equals(AccountContract.ROLE_RETAILER)) {
                    showRetailerAdminActivity();
                    finish();
                }
                else {
                    showAccountActivity();
                }
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

    // Listener for synchronization changes
    public static final String ACTION_FINISHED_SYNC = "be.nmct.unitycard.ACTION_FINISHED_SYNC";
    public static final String ACTION_FINISHED_SYNC_RESULT = "be.nmct.unitycard.ACTION_FINISHED_SYNC_RESULT";

    private static IntentFilter syncIntentFilter = new IntentFilter(ACTION_FINISHED_SYNC);
    private BroadcastReceiver syncBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Hide the refreshing indicator
            mBinding.swipeRefreshLayout.setRefreshing(false);

            // Check if there was an error
            if (intent.getAction().equals(ACTION_FINISHED_SYNC)) {
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    int result = extras.getInt(ACTION_FINISHED_SYNC_RESULT, RESULT_SYNC_SUCCESS);
                    if (result == RESULT_SYNC_SUCCESS) { // als er geen error was
                        return; // niets doen
                    }
                }
            }

            requestNewLogin(); // nieuwe login aanvragen
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        // Register broadcastreceiver for synchronization changes
        registerReceiver(syncBroadcastReceiver, syncIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Unregister broadcastreceiver for synchronization changes
        unregisterReceiver(syncBroadcastReceiver);
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

    private void showRetailerAdminActivity() {
        Intent intent = new Intent(this, RetailerAdminActivity.class);
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

                        // De eerste keer een sync forceren
                        SyncHelper.refreshCachedData(this);
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

    // Lijst die bijhoudt welke taken er in progress zijn, voor parallele taken.
    private int pendingTasks = 0;

    public void swipeRefreshLayoutAddTask() {
        // Add task
        pendingTasks++;

        // Show refreshing indicator
        mBinding.swipeRefreshLayout.setRefreshing(true);
    }

    public void swipeRefreshLayoutRemoveTask() {
        if (pendingTasks > 0) {
            // Remove the task from the list
            pendingTasks--;

            // Determine if the refreshing indicator must be hidden
            if (pendingTasks != 0) return;
        }

        // Hide the refreshing indicator
        mBinding.swipeRefreshLayout.setRefreshing(false);
    }

    public void requestNewLogin() { // todo: dit callen of rechtstreeks showaccountactivity() callen?
        // Something went wrong, toon login scherm
        showAccountActivity();
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

    @Override
    public void handleError(String error) {
        Snackbar.make(mBinding.drawerLayout, error, Snackbar.LENGTH_LONG).show();
        mBinding.swipeRefreshLayout.setRefreshing(false); // Stop refresh animation
    }
}
