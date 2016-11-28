package be.nmct.unitycard.auth;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import java.text.ParseException;
import java.util.Date;

import be.nmct.unitycard.contracts.AccountContract;
import be.nmct.unitycard.contracts.ContentProviderContract;
import be.nmct.unitycard.helpers.ConnectionChecker;
import be.nmct.unitycard.helpers.FcmTokenHelper;
import be.nmct.unitycard.helpers.TimestampHelper;
import be.nmct.unitycard.models.GetTokenResponse;
import be.nmct.unitycard.repositories.AuthRepository;

import static be.nmct.unitycard.activities.login.AccountActivity.REQUEST_PERMISSION_GET_ACCOUNTS;

/**
 * Created by Stephen on 28/10/2016.
 */

public class AuthHelper {
    private static final String LOG_TAG = AuthHelper.class.getSimpleName();

    public static void signIn(final String userName, String password, Activity activity, Context context, final SignInListener listener) {
        if (ConnectionChecker.isInternetAvailable(activity)) { // check of er verbinding is
            AccountManager accountManager = AccountManager.get(context);

            Account[] accountsByType = AuthHelper.getStoredAccountsByType(activity); // haal alle bestaande accounts op

            if (accountsByType != null) {
                // Permission ok
                Account account = null;
                if (accountsByType.length != 0) {
                    if (!userName.equals(accountsByType[0].name)) {
                        // Er bestaat reeds een account met andere naam, verwijder de vorige account
                        AuthHelper.removeStoredAccount(accountsByType[0], activity);
                    }
                    else {
                        // Account met de zelfde username terug gevonden
                        account = accountsByType[0];

                        // Check of er een Refresh token aanwezig is
                        String refreshToken = accountManager.peekAuthToken(account, AccountContract.TOKEN_REFRESH);
                        if (TextUtils.isEmpty(refreshToken)) { // Is er geen refresh token meer aanwezig?
                            // Remove account
                            AuthHelper.removeStoredAccount(account, activity);
                            account = null;
                        }
                    }
                }

                if (account != null) { // Als er al een account is
                    // Direct melden dat login OK is
                    listener.onSignInSuccessful(userName);
                }
                else { // Als er nog geen account is
                    // Add new account
                    addAccount(userName, password, context, accountManager, new AuthHelper.AddAccountListener() {
                        @Override
                        public void accountAdded(Account account) {
                            listener.onSignInSuccessful(userName);
                        }

                        @Override
                        public void accountAddError(String error) {
                            listener.handleError(error);
                        }
                    });
                }
            }
            else {
                // No permission = null
                listener.handlePermissionRequest(REQUEST_PERMISSION_GET_ACCOUNTS);
            }
        }
        else {
            listener.handleError("No internet connection");
        }
    }

    public interface SignInListener {
        void onSignInSuccessful(String userNameString);
        void handleError(String error);
        void handlePermissionRequest(final int permissionRequestCode);
    }

    public static void addAccount(final String userName, String password, final Context context, final AccountManager accountManager, final AddAccountListener callback) {
        // Verify the user and return tokens
        AuthRepository authRepo = new AuthRepository(context);
        authRepo.requestToken(userName, password, new AuthRepository.TokenResponseListener() {
            @Override
            public void tokenReceived(GetTokenResponse getTokenResponse) {
                if (getTokenResponse != null) {
                    // Save the user locally with token if request was valid
                    final String accessToken = getTokenResponse.getAccessToken();
                    final String refreshToken = getTokenResponse.getRefreshToken();
                    final String userId = getTokenResponse.getUserId();
                    final String userRole = getTokenResponse.getUserRole();

                    if (TextUtils.isEmpty(accessToken) ||
                            TextUtils.isEmpty(refreshToken) ||
                            TextUtils.isEmpty(userId) ||
                            TextUtils.isEmpty(userRole)) {
                        callback.accountAddError("Invalid login - response invalid");
                        return;
                    }

                    // Create and add account
                    Account account = new Account(userName, AccountContract.ACCOUNT_TYPE);
                    accountManager.addAccountExplicitly(account, null, null);

                    // Save nieuwe access en refresh tokens
                    accountManager.setAuthToken(account, AccountContract.TOKEN_ACCESS, accessToken);
                    accountManager.setAuthToken(account, AccountContract.TOKEN_REFRESH, refreshToken);

                    // Save username
                    accountManager.setUserData(account, AccountContract.KEY_USER_ID, userId);
                    // Save user account role
                    if (userRole.equals("Retailer")) {
                        accountManager.setUserData(account, AccountContract.KEY_USER_ROLE, AccountContract.ROLE_RETAILER);
                    }
                    else {
                        accountManager.setUserData(account, AccountContract.KEY_USER_ROLE, AccountContract.ROLE_CUSTOMER);
                    }

                    // Set whether or not the provider is synced when it receives a network tickle.
                    ContentResolver.setSyncAutomatically(account, ContentProviderContract.AUTHORITY, true);

                    // Add periodic sync
                    Bundle extras = new Bundle();
                    final long SECONDS_PER_MINUTE = 60L;
                    final long SYNC_INTERVAL_IN_MINUTES = 60L;
                    final long syncInterval = SYNC_INTERVAL_IN_MINUTES * SECONDS_PER_MINUTE;
                    ContentResolver.addPeriodicSync(account, ContentProviderContract.AUTHORITY, extras, syncInterval);

                    // Set Firebase Cloud Messaging Token
                    String fcmToken = FirebaseInstanceId.getInstance().getToken();
                    FcmTokenHelper.sendRegistrationToServer(fcmToken, context);

                    callback.accountAdded(account);
                }
                else {
                    callback.accountAddError("Invalid login - empty response");
                }
            }

            @Override
            public void tokenRequestError(String error) {
                callback.accountAddError(error);
            }
        });
    }

    public interface AddAccountListener {
        void accountAdded(Account account);
        void accountAddError(String error);
    }

    public static Account[] getStoredAccountsByType(Context context) {
        AccountManager accountManager = AccountManager.get(context);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED) {
            return accountManager.getAccountsByType(AccountContract.ACCOUNT_TYPE);
        } else {
            // No permission = null
            return null;
        }
    }

    public static Account getUser(Context context) {
        Account[] accounts = getStoredAccountsByType(context);

        if (accounts != null) {
            if (accounts.length>0) {
                return accounts[0];
            }
            else return null;
        }
        else {
            // No permission = null
            return null;
        }
    }

    public static void getAccessToken(Account account, Context context, final GetAccessTokenListener callback) {
        AccountManager accountManager = AccountManager.get(context);

        // getAuthToken(): Gets an authtoken for an account. If not null, the resultant Bundle will
        // contain different sets of keys depending on whether a token was successfully
        // issued and, if not, whether one could be issued via some Activity.
        accountManager.getAuthToken(account, AccountContract.TOKEN_ACCESS, null, null, new AccountManagerCallback<Bundle>() {
            // This is a callback to invoke when the accountManager.getAuthToken request completes
            @Override
            public void run(AccountManagerFuture<Bundle> future) {
                Bundle result;
                try {
                    // The result can only be retrieved using method getResult when the
                    // computation has completed, blocking if necessary until it is ready
                    result = future.getResult();

                    if (result == null) { // Should not happen
                        Log.wtf(LOG_TAG, "AccountManager.getAuthToken result was empty!");
                        callback.requestNewLogin();
                        return;
                    }
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Error while retrieving auth token: " + e);
                    callback.requestNewLogin();
                    return;
                }

                String authToken = null;

                if (future.isDone() && !future.isCancelled()) {
                    if (result.containsKey(AccountManager.KEY_INTENT)) {
                        // Als het resultaat een intent bevat om de credentials te
                        // vernieuwen (opnieuw proberen), de accountactivity tonen
                        Log.e(LOG_TAG, "Error: new login requested");
                        callback.requestNewLogin();
                        return;
                    }

                    /* An AccountManagerFuture resolves to a Bundle with at least the following fields:
                    *   - KEY_ACCOUNT_NAME - the name of the account you supplied
                    *   - KEY_ACCOUNT_TYPE - the type of the account
                    *   - KEY_AUTHTOKEN - the auth token you wanted */
                    authToken = result.getString(AccountManager.KEY_AUTHTOKEN);
                }

                if (TextUtils.isEmpty(authToken)) {
                    Log.e(LOG_TAG, "Got null auth token for type: " + AccountContract.TOKEN_ACCESS);
                    callback.requestNewLogin();
                    return;
                }

                // Return the authToken;
                callback.tokenReceived(authToken);
            }
        }, null);
    }

    public interface GetAccessTokenListener {
        void tokenReceived(String accessToken);
        void requestNewLogin();
    }

    public static void invalidateAccessToken(String authToken, Context context) {
        AccountManager accountManager = AccountManager.get(context);

        accountManager.invalidateAuthToken(AccountContract.ACCOUNT_TYPE, authToken);
    }

    public static String getUsername(Account account) {
        return account.name;
    }

    public static String getUserId(Context context) {
        AccountManager accountManager = AccountManager.get(context);

        Account account = getUser(context);

        return accountManager.getUserData(account, AccountContract.KEY_USER_ID);
    }

    public static String getUserRole(Context context) {
        AccountManager accountManager = AccountManager.get(context);

        Account account = getUser(context);

        return accountManager.getUserData(account, AccountContract.KEY_USER_ROLE);
    }

    public static Boolean isUserLoggedIn(Context context) { // return nullable boolean
        Account[] accounts = getStoredAccountsByType(context);

        if (accounts != null) {
            if (accounts.length>0) {
                return true;
            }
            else return false;
        }
        else {
            // No permission = null
            return null;
        }
    }

    public static void removeStoredAccount(Account account, Context context) {
        AccountManager accountManager = AccountManager.get(context);

        if (Build.VERSION.SDK_INT < 22) {
            accountManager.removeAccount(account, null, null);

        } else {
            accountManager.removeAccount(account, null, null, null);
        }
    }

    public static void logUserOff(final Context context) {
        // Wis alle cached data
        ContentProviderContract.clearAllContent(context);

        getAccessToken(getUser(context), context, new GetAccessTokenListener() {
            @Override
            public void tokenReceived(String accessToken) {
                FcmTokenHelper.removeRegistrationToken(context, accessToken);

                clearAccounts(context);
            }

            @Override
            public void requestNewLogin() {
                clearAccounts(context);
            }
        });
    }

    private static void clearAccounts(Context context) {
        Account[] accounts = getStoredAccountsByType(context);

        if (accounts != null) {
            for (int index = 0; index < accounts.length; index++) {
                removeStoredAccount(accounts[index], context);
            }
        }
    }

    public static void setLastSyncTimestamp(Context context, Account account, String timestampTypeKey, Date lastSyncTimestamp) {
        AccountManager accountManager = AccountManager.get(context);

        // Save last sync timestamp in string format
        accountManager.setUserData(account, timestampTypeKey,
                TimestampHelper.convertDateToString(lastSyncTimestamp));
    }

    public static Date getLastSyncTimestamp(Context context, Account account, String timestampTypeKey) throws ParseException {
        AccountManager accountManager = AccountManager.get(context);

        // Get last sync timestamp in string format, convert to Date object
        String timeString = accountManager.getUserData(account, timestampTypeKey);
        if (TextUtils.isEmpty(timeString)) { // er is nog nooit gesynct geweest, geen timestamp gevonden
            return new Date(0); // = "the epoch", namely January 1, 1970, 00:00:00 GMT.
        }
        else {
            return TimestampHelper.convertStringToDate(timeString);
        }
    }
}
