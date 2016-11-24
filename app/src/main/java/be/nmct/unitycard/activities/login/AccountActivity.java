package be.nmct.unitycard.activities.login;

import android.Manifest;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import be.nmct.unitycard.R;
import be.nmct.unitycard.contracts.AccountContract;
import be.nmct.unitycard.databinding.ActivityAccountBinding;
import be.nmct.unitycard.fragments.login.LoginFragment;
import be.nmct.unitycard.fragments.login.RegisterFragment;
import be.nmct.unitycard.models.viewmodels.activities.AccountActivityVM;

public class AccountActivity extends AppCompatActivity
        implements
        LoginFragment.LoginFragmentListener,
        RegisterFragment.RegisterFragmentListener {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private ActivityAccountBinding mBinding;
    private AccountActivityVM mAccountActivityVM;
    private AccountAuthenticatorResponse mAccountAuthenticatorResponse;
    public static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_account);

        if (savedInstanceState == null) {
            // Komen we vanuit de authenticator service?
            mAccountAuthenticatorResponse = this.getIntent().getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);

            if (mAccountAuthenticatorResponse != null) { // We komen vanuit de service (via settings interface - AccountManager)
                mAccountAuthenticatorResponse.onRequestContinued(); // Antwoord op de request verder samenstellen
            }

            // add initial fragment
            if (getSupportFragmentManager().findFragmentById(R.id.content_frame) == null) {
                // er is nog geen fragment actief, voeg er 1 toe
                showFragmentLogin();
            }
        }
    }

    private void showFragmentLogin() {
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.content_frame, LoginFragment.newInstance(), LoginFragment.class.getSimpleName()) // replace = remove + add
                .commit();
    }

    private void showFragmentRegister() {
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.content_frame, RegisterFragment.newInstance(), LoginFragment.class.getSimpleName()) // replace = remove + add
                .addToBackStack("Transaction_to_RegisterFragment")
                .commit();
    }

    @Override
    public void handlePermissionRequest(final int permissionRequestCode) {
        // Permission was niet gegeven, probeer user te vragen
        final String permission;
        String reason;
        switch (permissionRequestCode) { // Zoek uit welke permission
            case REQUEST_PERMISSION_GET_ACCOUNTS:
                permission = Manifest.permission.GET_ACCOUNTS;
                reason = "Access to your accounts is required to log in."; // todo: multi language
                break;
            default:
                finish();
                return;
        }

        // Heeft de gebruiker deze permission al eens geweigerd?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            // Uitleggen waarom deze permission toch wel nodig is
            Snackbar.make(mBinding.relativeLayout, reason, Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Permission opnieuw proberen aanvragen
                            requestPermission(permissionRequestCode, permission);
                        }
                    }).show();
        }
        else {
            // User heeft nog nooit toestemming gegeven, vraag permission
            requestPermission(permissionRequestCode, permission);
        }
    }

    private void requestPermission(int permissionRequestCode, String permission) {
        // Vraag permission aan via Android dialog
        ActivityCompat.requestPermissions(this, new String[] {permission}, permissionRequestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Dit is de callback van ActivityCompat.requestPermissions
        switch (requestCode) {
            case REQUEST_PERMISSION_GET_ACCOUNTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission ok
                    handleError("Permission OK, you can now continue."); // todo: multi language
                    // todo: hier doorgeven aan fragment dat we klaar zijn? automatisch doordoen ipv user nog eens laten drukken. systeem voor vinden
                }
                else {
                    // no permission
                    handleError("Permission is required to use this app."); // todo: multi language
                }
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.getBackStackEntryCount() == 0) {
            setResult(RESULT_CANCELED);
            finish(); // teruggeven aan MainActivity
        }
        else {
            fragmentManager.popBackStack();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onRegisterCanceled();
        }

        return true;
    }

    @Override
    public void handleError(String error) {
        Snackbar.make(mBinding.relativeLayout, error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void btnRegisterClicked() {
        showFragmentRegister();
    }

    @Override
    public void onLoginSuccessful(String userNameString) {
        if (mAccountAuthenticatorResponse != null) { // We komen vanuit de service (via settings interface - AccountManager)
            Bundle bundle = new Bundle();
            bundle.putString(AccountManager.KEY_ACCOUNT_NAME, userNameString);
            bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, AccountContract.ACCOUNT_TYPE);
            mAccountAuthenticatorResponse.onResult(bundle); // Geef terug aan de authenticator
        }

        setResult(RESULT_OK);
        finish();
    }

    private void onRegisterCanceled() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.popBackStack();
    }
}
