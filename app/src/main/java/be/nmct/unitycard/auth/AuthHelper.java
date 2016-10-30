package be.nmct.unitycard.auth;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import be.nmct.unitycard.contracts.AccountContract;

/**
 * Created by Stephen on 28/10/2016.
 */

public class AuthHelper {
    public static Account addAccount(String userNameString, AccountManager accountManager) {
        Account account = new Account(userNameString, AccountContract.ACCOUNT_TYPE);
        accountManager.addAccountExplicitly(account, null, null);
        return account;
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

    public static String getUsername(Context context) {
        Account[] accounts = getStoredAccountsByType(context);

        if (accounts != null) {
            if (accounts.length>0){
                return accounts[0].name;
            }
            else return null;
        }
        else {
            // No permission = null
            return null;
        }
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
        Account[] accounts = getStoredAccountsByType(context);

        if (accounts != null) {
            for (int index = 0; index < accounts.length; index++) { // todo: this deletes all accounts, not a specific one
                removeStoredAccount(accounts[index], context);
            }
        }
    }
}
