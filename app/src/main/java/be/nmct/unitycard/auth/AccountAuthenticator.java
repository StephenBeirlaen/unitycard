package be.nmct.unitycard.auth;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import be.nmct.unitycard.activities.login.AccountActivity;
import be.nmct.unitycard.contracts.AccountContract;
import be.nmct.unitycard.models.GetTokenResponse;
import be.nmct.unitycard.repositories.AuthRepository;

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
        throw new UnsupportedOperationException();
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        // Adds an account of the specified accountType
        if (!accountType.equals(AccountContract.ACCOUNT_TYPE)) // onbekend type account
            throw new IllegalArgumentException();

        if (!(requiredFeatures == null || requiredFeatures.length == 0)) // de nodige parameters zijn niet meegegeven
            throw new IllegalArgumentException();

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
        throw new UnsupportedOperationException();
    }

    // Deze methode zal pas uitgevoerd worden als er geen token meer in de cache zit (invalidated)
    // We moeten dus niet zelf checken of de accessToken nog bestaat. Direct proberen te refreshen
    @Override
    public Bundle getAuthToken(final AccountAuthenticatorResponse response, final Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        /* If the authenticator cannot synchronously process the request
         * and return a result then it may choose to return null and then
         * use the AccountManagerResponse to send the result when it has completed the request. */

        final AccountManager accountManager = AccountManager.get(mContext);

        /* peekAuthToken(): Get an auth token from the AccountManager's cache.
         * If no auth token is cached for this account, null will be returned -- a new
         * auth token will not be generated, and the server will not be contacted */
        // Check of er een Refresh token aanwezig is
        final String refreshToken = accountManager.peekAuthToken(account, AccountContract.TOKEN_REFRESH);

        // Is er een refresh token?
        if (!TextUtils.isEmpty(refreshToken)) {
            // Refresh access token
            AuthRepository authRepo = new AuthRepository(mContext);
            authRepo.refreshToken(refreshToken, new AuthRepository.TokenResponseListener() {
                @Override
                public void tokenReceived(GetTokenResponse getTokenResponse) {
                    if (getTokenResponse != null) {
                        // Token refresh OK, save the token
                        final String accessToken = getTokenResponse.getAccessToken();

                        // Save nieuwe access token
                        accountManager.setAuthToken(account, AccountContract.TOKEN_ACCESS, accessToken);

                        // Return new access token
                        final Bundle result = new Bundle();
                        result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
                        result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
                        result.putString(AccountManager.KEY_AUTHTOKEN, accessToken);
                        response.onResult(result);
                    }
                    else {
                        // Could not refresh token
                        // Invalidate refresh token
                        accountManager.invalidateAuthToken(AccountContract.ACCOUNT_TYPE, refreshToken);
                        response.onResult(createAuthenticatorActivityBundle(response));
                    }
                }

                @Override
                public void tokenRequestError(String error) {
                    // Could not refresh token
                    // Invalidate refresh token
                    accountManager.invalidateAuthToken(AccountContract.ACCOUNT_TYPE, refreshToken);
                    response.onResult(createAuthenticatorActivityBundle(response));
                }
            });

            return null;
        }
        else { // Geen stored refresh token -> Confirm user credentials, try again
            // If a token cannot be provided without some additional activity,
            // the Bundle should contain KEY_INTENT with an associated Intent.
            response.onResult(createAuthenticatorActivityBundle(response));
            return null;
        }
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) { // authTokenType = the type of the authenticator
        // Get the user-friendly label associated with an authenticator's auth token
        switch (authTokenType) {
            case AccountContract.TOKEN_ACCESS:
                return AccountContract.TOKEN_ACCESS_LABEL;
            case AccountContract.TOKEN_REFRESH:
                return AccountContract.TOKEN_REFRESH_LABEL;
            default:
                return null;
        }
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        throw new UnsupportedOperationException();
    }
}
