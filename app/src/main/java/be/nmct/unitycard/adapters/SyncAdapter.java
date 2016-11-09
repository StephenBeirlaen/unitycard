package be.nmct.unitycard.adapters;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import be.nmct.unitycard.auth.AuthHelper;
import be.nmct.unitycard.contracts.ContentProviderContract;
import be.nmct.unitycard.contracts.DatabaseContract;
import be.nmct.unitycard.models.Retailer;
import be.nmct.unitycard.repositories.RetailerRepository;

/**
 * Created by Stephen on 4/11/2016.
 */

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private final String LOG_TAG = this.getClass().getSimpleName();
    ContentResolver mContentResolver;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public Context getContext() {
        return super.getContext();
    }

    @Override
    public void onSecurityException(Account account, Bundle extras, String authority, SyncResult syncResult) {
        super.onSecurityException(account, extras, authority, syncResult);
    }

    @Override
    public void onSyncCanceled() {
        super.onSyncCanceled();
    }

    @Override
    public void onSyncCanceled(Thread thread) {
        super.onSyncCanceled(thread);
    }

    @Override
    public void onPerformSync(final Account account, Bundle extras, String authority, ContentProviderClient providerClient, SyncResult syncResult) {
        // When the framework is ready to sync your application's data, this gets run

        AuthHelper.getAccessToken(account, getContext(), new AuthHelper.GetAccessTokenListener() {
            @Override
            public void tokenReceived(final String accessToken) {
                Log.d(LOG_TAG, "Using access token: " + accessToken);

                final RetailerRepository retailerRepo = new RetailerRepository(getContext());
                retailerRepo.getAllRetailers(accessToken, new RetailerRepository.GetAllRetailersListener() {
                    @Override
                    public void retailersReceived(List<Retailer> retailers) {
                        Log.d(LOG_TAG, "Received all retailers: " + retailers);

                        // todo: temporary
                        for (Retailer retailer : retailers) {
                            ContentValues contentValues = new ContentValues();

                            contentValues.put(DatabaseContract.RetailerColumns.COLUMN_ID, retailer.getId());
                            contentValues.put(DatabaseContract.RetailerColumns.COLUMN_RETAILER_CATEGORY_ID, retailer.getRetailerCategoryId());
                            contentValues.put(DatabaseContract.RetailerColumns.COLUMN_RETAILER_NAME, retailer.getName());
                            contentValues.put(DatabaseContract.RetailerColumns.COLUMN_TAGLINE, retailer.getTagline());
                            contentValues.put(DatabaseContract.RetailerColumns.COLUMN_CHAIN, retailer.isChain());
                            contentValues.put(DatabaseContract.RetailerColumns.COLUMN_LOGOURL, retailer.getLogoUrl());

                            getContext().getContentResolver().insert(ContentProviderContract.RETAILERS_URI, contentValues);
                        }
                    }

                    @Override
                    public void retailersRequestError(String error) {
                        // Invalideer het gebruikte access token, het is niet meer geldig (anders zou er geen error zijn)
                        AuthHelper.invalidateAccessToken(accessToken, getContext());
                    }
                });

            }

            @Override
            public void requestNewLogin() {

            }
        });
    }
}