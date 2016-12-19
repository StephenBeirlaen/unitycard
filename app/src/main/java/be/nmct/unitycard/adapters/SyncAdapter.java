package be.nmct.unitycard.adapters;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import be.nmct.unitycard.activities.customer.MainActivity;
import be.nmct.unitycard.auth.AuthHelper;
import be.nmct.unitycard.contracts.AccountContract;
import be.nmct.unitycard.contracts.ContentProviderContract;
import be.nmct.unitycard.contracts.DatabaseContract;
import be.nmct.unitycard.fragments.customer.RetailerInfoFragment;
import be.nmct.unitycard.helpers.TimestampHelper;
import be.nmct.unitycard.models.LoyaltyCard;
import be.nmct.unitycard.models.Offer;
import be.nmct.unitycard.models.Retailer;
import be.nmct.unitycard.models.RetailerCategory;
import be.nmct.unitycard.models.RetailerLocation;
import be.nmct.unitycard.models.viewmodels.RetailerLoyaltyPointVM;
import be.nmct.unitycard.repositories.ApiRepository;

/**
 * Created by Stephen on 4/11/2016.
 */

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private ContentResolver mContentResolver;

    public static final int RESULT_SYNC_SUCCESS = 1;
    public static final int RESULT_SYNC_FAILED = -1;

    public static final String KEY_SYNC_REQUEST_GET_RETAILER_LOCATIONS = "be.nmct.unitycard.key_sync_request_get_retailer_locations";

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
    public void onPerformSync(final Account account, final Bundle extras, String authority, ContentProviderClient providerClient, SyncResult syncResult) {
        // Om de debugger te attachen:
        //android.os.Debug.waitForDebugger();

        // When the framework is ready to sync your application's data, this gets run
        Log.d(LOG_TAG, "Synchronisation started");

        // Get access token
        AuthHelper.getAccessToken(account, getContext(), new AuthHelper.GetAccessTokenListener() {
            @Override
            public void tokenReceived(final String accessToken) {
                Log.d(LOG_TAG, "Using access token: " + accessToken);

                final ApiRepository apiRepo = new ApiRepository(getContext());

                int retailerId = extras.getInt(KEY_SYNC_REQUEST_GET_RETAILER_LOCATIONS, -1);

                if (retailerId != -1) {
                    apiRepo.getRetailerLocations(accessToken, retailerId, new Date(0), new ApiRepository.GetResultListener<List<RetailerLocation>>() {
                        @Override
                        public void resultReceived(List<RetailerLocation> retailerLocations) {
                            Log.d(LOG_TAG, "Received retailer locations: " + retailerLocations);

                            for (RetailerLocation retailerLocation : retailerLocations) {
                                ContentValues contentValues = new ContentValues();

                                contentValues.put(DatabaseContract.RetailerLocationsColumns.COLUMN_SERVER_ID, retailerLocation.getId());
                                contentValues.put(DatabaseContract.RetailerLocationsColumns.COLUMN_RETAILER_ID, retailerLocation.getRetailerId());
                                contentValues.put(DatabaseContract.RetailerLocationsColumns.COLUMN_NAME, retailerLocation.getName());
                                contentValues.put(DatabaseContract.RetailerLocationsColumns.COLUMN_STREET, retailerLocation.getStreet());
                                contentValues.put(DatabaseContract.RetailerLocationsColumns.COLUMN_NUMBER, retailerLocation.getNumber());
                                contentValues.put(DatabaseContract.RetailerLocationsColumns.COLUMN_ZIPCODE, retailerLocation.getZipcode());
                                contentValues.put(DatabaseContract.RetailerLocationsColumns.COLUMN_CITY, retailerLocation.getCity());
                                contentValues.put(DatabaseContract.RetailerLocationsColumns.COLUMN_COUNTRY, retailerLocation.getCountry());
                                contentValues.put(DatabaseContract.RetailerLocationsColumns.COLUMN_UPDATED_TIMESTAMP, TimestampHelper.convertDateToString(retailerLocation.getUpdatedTimestamp()));

                                mContentResolver.insert(ContentProviderContract.RETAILER_LOCATIONS_URI, contentValues);
                            }

                            handleSyncSuccess(account, AccountContract.KEY_LAST_SYNC_TIMESTAMP_RETAILER_LOCATIONS, RetailerInfoFragment.ACTION_FINISHED_RETAILER_LOCATIONS_SYNC, RetailerInfoFragment.ACTION_FINISHED_RETAILER_LOCATIONS_SYNC_RESULT);
                        }

                        @Override
                        public void requestError(String error) {
                            handleSyncError(RetailerInfoFragment.ACTION_FINISHED_RETAILER_LOCATIONS_SYNC, RetailerInfoFragment.ACTION_FINISHED_RETAILER_LOCATIONS_SYNC_RESULT);
                        }
                    });
                }
                else {
                    // geen specifieke Sync request opgegeven, refresh dus de overige data

                    // Get the last sync timestamps
                    Date lastLoyaltyCardSyncTimestamp;
                    Date lastRetailersSyncTimestamp;
                    Date lastRetailerCategoriesSyncTimestamp;
                    Date lastAddedRetailersSyncTimestamp;
                    Date lastRetailerOffersSyncTimestamp;
                    Date lastTotalLoyaltyPointsSyncTimestamp;
                    try {
                        lastLoyaltyCardSyncTimestamp = AuthHelper.getLastSyncTimestamp(getContext(), account, AccountContract.KEY_LAST_SYNC_TIMESTAMP_LOYALTY_CARD);
                        lastAddedRetailersSyncTimestamp = AuthHelper.getLastSyncTimestamp(getContext(), account, AccountContract.KEY_LAST_SYNC_TIMESTAMP_ADDEDRETAILERS);
                        lastRetailerCategoriesSyncTimestamp = AuthHelper.getLastSyncTimestamp(getContext(), account, AccountContract.KEY_LAST_SYNC_TIMESTAMP_RETAILER_CATEGORIES);
                        lastRetailersSyncTimestamp = AuthHelper.getLastSyncTimestamp(getContext(), account, AccountContract.KEY_LAST_SYNC_TIMESTAMP_RETAILERS);
                        lastRetailerOffersSyncTimestamp = AuthHelper.getLastSyncTimestamp(getContext(), account, AccountContract.KEY_LAST_SYNC_TIMESTAMP_RETAILER_OFFERS);
                        lastTotalLoyaltyPointsSyncTimestamp = AuthHelper.getLastSyncTimestamp(getContext(), account, AccountContract.KEY_LAST_SYNC_TIMESTAMP_TOTAL_LOYALTY_POINTS);
                    } catch (ParseException e) {
                        handleSyncError(MainActivity.ACTION_FINISHED_SYNC, MainActivity.ACTION_FINISHED_SYNC_RESULT);
                        return;
                    }
                    /*lastLoyaltyCardSyncTimestamp = new Date(0);
                    lastAddedRetailersSyncTimestamp = new Date(0);
                    lastRetailerCategoriesSyncTimestamp = new Date(0);
                    lastRetailersSyncTimestamp = new Date(0);
                    lastRetailerOffersSyncTimestamp = new Date(0);
                    lastTotalLoyaltyPointsSyncTimestamp = new Date(0);*/

                    apiRepo.getLoyaltyCard(accessToken, AuthHelper.getUserId(getContext()), lastLoyaltyCardSyncTimestamp, new ApiRepository.GetResultListener<LoyaltyCard>() {
                        @Override
                        public void resultReceived(LoyaltyCard loyaltyCard) {
                            Log.d(LOG_TAG, "Received loyalty card: " + loyaltyCard);

                            // todo: temporary
                            if (loyaltyCard != null) {
                                ContentValues contentValues = new ContentValues();

                                contentValues.put(DatabaseContract.LoyaltyCardColumns.COLUMN_SERVER_ID, loyaltyCard.getId());
                                contentValues.put(DatabaseContract.LoyaltyCardColumns.COLUMN_USER_ID, loyaltyCard.getUserId());
                                contentValues.put(DatabaseContract.LoyaltyCardColumns.COLUMN_CREATED_TIMESTAMP, TimestampHelper.convertDateToString(loyaltyCard.getCreatedTimestamp()));
                                contentValues.put(DatabaseContract.LoyaltyCardColumns.COLUMN_UPDATED_TIMESTAMP, TimestampHelper.convertDateToString(loyaltyCard.getUpdatedTimestamp()));

                                mContentResolver.insert(ContentProviderContract.LOYALTYCARDS_URI, contentValues);
                            }

                            handleSyncSuccess(account, AccountContract.KEY_LAST_SYNC_TIMESTAMP_LOYALTY_CARD, MainActivity.ACTION_FINISHED_SYNC, MainActivity.ACTION_FINISHED_SYNC_RESULT);
                        }

                        @Override
                        public void requestError(String error) {
                            // Invalideer het gebruikte access token, het is niet meer geldig (anders zou er geen error zijn)
                            AuthHelper.invalidateAccessToken(accessToken, getContext());

                            handleSyncError(MainActivity.ACTION_FINISHED_SYNC, MainActivity.ACTION_FINISHED_SYNC_RESULT);
                        }
                    });

                    apiRepo.getLoyaltyCardRetailers(accessToken, AuthHelper.getUserId(getContext()), lastAddedRetailersSyncTimestamp, new ApiRepository.GetResultListener<List<RetailerLoyaltyPointVM>>() {
                        @Override
                        public void resultReceived(List<RetailerLoyaltyPointVM> retailerLoyaltyPointList) {
                            Log.d(LOG_TAG, "Received all retailers by loyaltycard: " + retailerLoyaltyPointList);

                            // todo: temporary, met contentprovideroperation werken (batch access)
                            for (RetailerLoyaltyPointVM retailerLoyaltyPoint : retailerLoyaltyPointList) {
                                ContentValues contentValues = new ContentValues();

                                contentValues.put(DatabaseContract.AddedRetailerColumns.COLUMN_SERVER_ID, retailerLoyaltyPoint.getRetailer().getId());
                                contentValues.put(DatabaseContract.AddedRetailerColumns.COLUMN_RETAILER_CATEGORY_ID, retailerLoyaltyPoint.getRetailer().getRetailerCategoryId());
                                contentValues.put(DatabaseContract.AddedRetailerColumns.COLUMN_RETAILER_NAME, retailerLoyaltyPoint.getRetailer().getName());
                                contentValues.put(DatabaseContract.AddedRetailerColumns.COLUMN_TAGLINE, retailerLoyaltyPoint.getRetailer().getTagline());
                                contentValues.put(DatabaseContract.AddedRetailerColumns.COLUMN_CHAIN, retailerLoyaltyPoint.getRetailer().isChain());
                                contentValues.put(DatabaseContract.AddedRetailerColumns.COLUMN_LOGOURL, retailerLoyaltyPoint.getRetailer().getLogoUrl());
                                contentValues.put(DatabaseContract.AddedRetailerColumns.COLUMN_UPDATED_TIMESTAMP, TimestampHelper.convertDateToString(retailerLoyaltyPoint.getRetailer().getUpdatedTimestamp()));
                                contentValues.put(DatabaseContract.AddedRetailerColumns.COLUMN_LOYALTYPOINTS, retailerLoyaltyPoint.getLoyaltyPoints());

                                mContentResolver.insert(ContentProviderContract.ADDED_RETAILERS_URI, contentValues);

                                handleSyncSuccess(account, AccountContract.KEY_LAST_SYNC_TIMESTAMP_ADDEDRETAILERS, MainActivity.ACTION_FINISHED_SYNC, MainActivity.ACTION_FINISHED_SYNC_RESULT);
                            }
                        }

                        @Override
                        public void requestError(String error) {
                            AuthHelper.invalidateAccessToken(accessToken, getContext());

                            handleSyncError(MainActivity.ACTION_FINISHED_SYNC, MainActivity.ACTION_FINISHED_SYNC_RESULT);
                        }
                    });

                    apiRepo.getAllRetailerCategories(lastRetailerCategoriesSyncTimestamp, new ApiRepository.GetResultListener<List<RetailerCategory>>() {
                        @Override
                        public void resultReceived(List<RetailerCategory> retailerCategories) {
                            Log.d(LOG_TAG, "Received all retailer categories: " + retailerCategories);

                            // todo: temporary, met contentprovideroperation werken (batch access)
                            for (RetailerCategory retailerCategory : retailerCategories) {
                                ContentValues contentValues = new ContentValues();

                                contentValues.put(DatabaseContract.RetailerCategoriesColumns.COLUMN_SERVER_ID, retailerCategory.getId());
                                contentValues.put(DatabaseContract.RetailerCategoriesColumns.COLUMN_NAME, retailerCategory.getName());
                                contentValues.put(DatabaseContract.RetailerCategoriesColumns.COLUMN_UPDATED_TIMESTAMP, TimestampHelper.convertDateToString(retailerCategory.getUpdatedTimestamp()));

                                mContentResolver.insert(ContentProviderContract.RETAILER_CATEGORIES_URI, contentValues);

                                handleSyncSuccess(account, AccountContract.KEY_LAST_SYNC_TIMESTAMP_RETAILER_CATEGORIES, MainActivity.ACTION_FINISHED_SYNC, MainActivity.ACTION_FINISHED_SYNC_RESULT);
                            }
                        }

                        @Override
                        public void requestError(String error) {
                            // Invalideer het gebruikte access token, het is niet meer geldig (anders zou er geen error zijn)
                            AuthHelper.invalidateAccessToken(accessToken, getContext());

                            handleSyncError(MainActivity.ACTION_FINISHED_SYNC, MainActivity.ACTION_FINISHED_SYNC_RESULT);
                        }
                    });

                    apiRepo.getAllRetailers(accessToken, lastRetailersSyncTimestamp, new ApiRepository.GetResultListener<List<Retailer>>() {
                        @Override
                        public void resultReceived(List<Retailer> retailers) {
                            Log.d(LOG_TAG, "Received all retailers: " + retailers);

                            // todo: temporary, met contentprovideroperation werken (batch access)
                            for (Retailer retailer : retailers) {
                                ContentValues contentValues = new ContentValues();

                                contentValues.put(DatabaseContract.RetailerColumns.COLUMN_SERVER_ID, retailer.getId());
                                contentValues.put(DatabaseContract.RetailerColumns.COLUMN_RETAILER_CATEGORY_ID, retailer.getRetailerCategoryId());
                                contentValues.put(DatabaseContract.RetailerColumns.COLUMN_RETAILER_NAME, retailer.getName());
                                contentValues.put(DatabaseContract.RetailerColumns.COLUMN_TAGLINE, retailer.getTagline());
                                contentValues.put(DatabaseContract.RetailerColumns.COLUMN_CHAIN, retailer.isChain());
                                contentValues.put(DatabaseContract.RetailerColumns.COLUMN_LOGOURL, retailer.getLogoUrl());
                                contentValues.put(DatabaseContract.RetailerColumns.COLUMN_UPDATED_TIMESTAMP, TimestampHelper.convertDateToString(retailer.getUpdatedTimestamp()));

                                mContentResolver.insert(ContentProviderContract.RETAILERS_URI, contentValues);

                                handleSyncSuccess(account, AccountContract.KEY_LAST_SYNC_TIMESTAMP_RETAILERS, MainActivity.ACTION_FINISHED_SYNC, MainActivity.ACTION_FINISHED_SYNC_RESULT);
                            }
                        }

                        @Override
                        public void requestError(String error) {
                            // Invalideer het gebruikte access token, het is niet meer geldig (anders zou er geen error zijn)
                            AuthHelper.invalidateAccessToken(accessToken, getContext());

                            handleSyncError(MainActivity.ACTION_FINISHED_SYNC, MainActivity.ACTION_FINISHED_SYNC_RESULT);
                        }
                    });

                    apiRepo.getAllRetailerOffers(accessToken, AuthHelper.getUserId(getContext()), lastRetailerOffersSyncTimestamp, new ApiRepository.GetResultListener<List<Offer>>() {
                        @Override
                        public void resultReceived(List<Offer> offers) {
                            Log.d(LOG_TAG, "Received all offers: " + offers);

                            for(Offer offer : offers){
                                ContentValues contentValues = new ContentValues();

                                contentValues.put(DatabaseContract.OffersColumns.COLUMN_SERVER_ID, offer.getId());
                                contentValues.put(DatabaseContract.OffersColumns.COLUMN_RETAILER_ID, offer.getRetailerId());
                                contentValues.put(DatabaseContract.OffersColumns.COLUMN_OFFER_DEMAND, offer.getOfferDemand());
                                contentValues.put(DatabaseContract.OffersColumns.COLUMN_OFFER_RECEIVE, offer.getOfferReceive());
                                contentValues.put(DatabaseContract.OffersColumns.COLUMN_CREATED_TIMESTAMP, TimestampHelper.convertDateToString(offer.getCreatedTimestamp()));
                                contentValues.put(DatabaseContract.OffersColumns.COLUMN_UPDATED_TIMESTAMP, TimestampHelper.convertDateToString(offer.getUpdatedTimestamp()));

                                mContentResolver.insert(ContentProviderContract.OFFERS_URI, contentValues);
                                handleSyncSuccess(account, AccountContract.KEY_LAST_SYNC_TIMESTAMP_RETAILER_OFFERS, MainActivity.ACTION_FINISHED_SYNC, MainActivity.ACTION_FINISHED_SYNC_RESULT);
                            }
                        }

                        @Override
                        public void requestError(String error) {
                            handleSyncError(MainActivity.ACTION_FINISHED_SYNC, MainActivity.ACTION_FINISHED_SYNC_RESULT);
                        }
                    });

                    apiRepo.getTotalLoyaltyPoints(accessToken, AuthHelper.getUserId(getContext()), lastTotalLoyaltyPointsSyncTimestamp, new ApiRepository.GetResultListener<Integer>() {
                        @Override
                        public void resultReceived(Integer result) {
                            Log.d(LOG_TAG, "Gelukt");
                        }

                        @Override
                        public void requestError(String error) {

                        }
                    });
                }
            }

            @Override
            public void requestNewLogin() {
                handleSyncError(MainActivity.ACTION_FINISHED_SYNC, MainActivity.ACTION_FINISHED_SYNC_RESULT);
            }
        });
    }

    private void handleSyncSuccess(Account account, String timestampTypeKey, String action, String syncResultType) {
        Date newTimeStamp = new Date(new Date().getTime() - 10000l); // -10 seconden voor precisie fouten te verhinderen
        AuthHelper.setLastSyncTimestamp(getContext(), account, timestampTypeKey, newTimeStamp); // Date object has the current date and time on init

        getContext().sendBroadcast(new Intent(action)
                .putExtra(syncResultType, RESULT_SYNC_SUCCESS));
    }

    private void handleSyncError(String action, String syncResultType) {
        getContext().sendBroadcast(new Intent(action)
                .putExtra(syncResultType, RESULT_SYNC_FAILED)
        );
    }
}
