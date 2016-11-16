package be.nmct.unitycard.adapters;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import be.nmct.unitycard.activities.MainActivity;
import be.nmct.unitycard.auth.AuthHelper;
import be.nmct.unitycard.contracts.ContentProviderContract;
import be.nmct.unitycard.contracts.DatabaseContract;
import be.nmct.unitycard.contracts.LoyaltyCardContract;
import be.nmct.unitycard.helpers.DatabaseHelper;
import be.nmct.unitycard.models.LoyaltyCard;
import be.nmct.unitycard.models.Retailer;
import be.nmct.unitycard.repositories.ApiRepository;

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
        Log.d(LOG_TAG, "Synchronisation started");

        // Get access token
        AuthHelper.getAccessToken(account, getContext(), new AuthHelper.GetAccessTokenListener() {
            @Override
            public void tokenReceived(final String accessToken) {
                Log.d(LOG_TAG, "Using access token: " + accessToken);

                final ApiRepository apiRepo = new ApiRepository(getContext());

                apiRepo.getLoyaltyCard(accessToken, AuthHelper.getUserId(getContext()), new ApiRepository.GetResultListener<LoyaltyCard>() {
                    @Override
                    public void resultReceived(LoyaltyCard loyaltyCard) {
                        Log.d(LOG_TAG, "Received loyalty card: " + loyaltyCard);

                        // todo: temporary
                        ContentValues contentValues = new ContentValues();

                        contentValues.put(DatabaseContract.LoyaltyCardColumns.COLUMN_ID, loyaltyCard.getId());
                        contentValues.put(DatabaseContract.LoyaltyCardColumns.COLUMN_USER_ID, loyaltyCard.getUserId());
                        contentValues.put(DatabaseContract.LoyaltyCardColumns.COLUMN_CREATED_TIMESTAMP, DatabaseHelper.convertDateToString(loyaltyCard.getCreatedTimestamp()));

                        getContext().getContentResolver().insert(ContentProviderContract.LOYALTYCARDS_URI, contentValues);

                        handleSyncSuccess();
                    }

                    @Override
                    public void requestError(String error) {
                        // Invalideer het gebruikte access token, het is niet meer geldig (anders zou er geen error zijn)
                        AuthHelper.invalidateAccessToken(accessToken, getContext());

                        handleSyncError();
                    }
                });

                apiRepo.getAllRetailers(accessToken, new ApiRepository.GetResultListener<List<Retailer>>() {
                    @Override
                    public void resultReceived(List<Retailer> retailers) {
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

                            handleSyncSuccess();
                        }
                    }

                    @Override
                    public void requestError(String error) {
                        // Invalideer het gebruikte access token, het is niet meer geldig (anders zou er geen error zijn)
                        AuthHelper.invalidateAccessToken(accessToken, getContext());

                        handleSyncError();
                    }
                });

            }

            @Override
            public void requestNewLogin() {
                handleSyncError();
            }
        });
    }

    private void handleSyncSuccess() {
        getContext().sendBroadcast(new Intent(MainActivity.ACTION_FINISHED_SYNC));
    }

    private void handleSyncError() {
        getContext().sendBroadcast(new Intent(MainActivity.ACTION_FINISHED_SYNC)
                .putExtra(MainActivity.ACTION_FINISHED_SYNC_RESULT, MainActivity.RESULT_SYNC_FAILED)
        );
    }
}
