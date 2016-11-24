package be.nmct.unitycard.activities.retailer;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import be.nmct.unitycard.R;
import be.nmct.unitycard.activities.login.AccountActivity;
import be.nmct.unitycard.auth.AuthHelper;
import be.nmct.unitycard.databinding.ActivityRetailerAdminBinding;
import be.nmct.unitycard.fragments.retailer.RetailerAdminFragment;
import be.nmct.unitycard.models.viewmodels.activities.RetailerAdminActivityVM;

public class RetailerAdminActivity extends AppCompatActivity
        implements
        RetailerAdminFragment.RetailerAdminFragmentListener {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private ActivityRetailerAdminBinding mBinding;
    private RetailerAdminActivityVM mRetailerAdminActivityVM;
    public static final int REQUEST_LOGIN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_retailer_admin);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Boolean loggedInResult = AuthHelper.isUserLoggedIn(this); // nullable boolean

        if (loggedInResult != null) { // app has permission to access accounts? If null -> no permission
            if (loggedInResult) {
                // Logged in
                if (getSupportFragmentManager().findFragmentById(R.id.content_frame) == null) {
                    // er is nog geen fragment actief, voeg er 1 toe
                    showFragmentRetailerAdmin();
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void showAccountActivity() {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN);
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
        // Scanning via intent
        // https://github.com/zxing/zxing/wiki/Scanning-Via-Intent
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null) {
            // Handle scan result
            handleError("Scanned Loyalty card: " + scanResult.getContents());
        }
        else {
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
    }

    private void showFragmentRetailerAdmin() {
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.content_frame, RetailerAdminFragment.newInstance(), RetailerAdminFragment.class.getSimpleName()) // replace = remove + add
                .commit();
    }

    public void requestNewLogin() { // todo: dit callen of rechtstreeks showaccountactivity() callen?
        // Something went wrong, toon login scherm
        showAccountActivity();
    }

    @Override
    public void onBackPressed() {
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

    @Override
    public void handleError(String error) {
        Snackbar.make(mBinding.contentFrame, error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void requestLogOut() {
        logOut();
    }
}
