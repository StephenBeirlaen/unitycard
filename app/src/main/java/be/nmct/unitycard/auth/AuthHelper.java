package be.nmct.unitycard.auth;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.util.Date;

import be.nmct.unitycard.contracts.AccountContract;
import be.nmct.unitycard.contracts.ContentProviderContract;
import be.nmct.unitycard.helpers.TimestampHelper;
import be.nmct.unitycard.models.GetTokenResponse;
import be.nmct.unitycard.repositories.AuthRepository;

/**
 * Created by Stephen on 28/10/2016.
 */

public class AuthHelper {
    private static final String LOG_TAG = AuthHelper.class.getSimpleName();

    public static void addAccount(final String userName, String password, Context context, final AccountManager accountManager, final AddAccountListener callback) {
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

    public static void logUserOff(Context context) {
        // Wis alle cached data
        ContentProviderContract.clearAllContent(context);

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
