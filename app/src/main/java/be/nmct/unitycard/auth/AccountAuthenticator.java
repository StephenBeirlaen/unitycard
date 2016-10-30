package be.nmct.unitycard.auth;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import be.nmct.unitycard.activities.AccountActivity;
import be.nmct.unitycard.contracts.AccountContract;

/**
 * Created by Stephen on 30/10/2016.
 */

public class AccountAuthenticator extends AbstractAccountAuthenticator {

    private final Context mContext;

    public AccountAuthenticator(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        // todo
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        // Adds an account of the specified accountType
        if (!accountType.equals(AccountContract.ACCOUNT_TYPE)) // onbekend type account
            throw new IllegalArgumentException();

        if (!(requiredFeatures == null || requiredFeatures.length == 0)) // de nodige parameters zijn niet meegegeven
            throw new IllegalArgumentException();

        // todo: wss moeten we hier netwerk calls maken?

        return createAuthenticatorActivityBundle(response); // Laat user inloggen
    }

    private Bundle createAuthenticatorActivityBundle(AccountAuthenticatorResponse response) {
        // Creërt een activity waarlangs de gebruiker kan inloggen
        Intent intent = new Intent(mContext, AccountActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response); // Zodat AccountActivity kan weten dat we van de service komen

        Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        // todo
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        // todo
        return null;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        // todo
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        // todo
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        // todo
        return null;
    }
}
