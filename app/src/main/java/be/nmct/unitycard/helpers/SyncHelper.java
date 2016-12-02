package be.nmct.unitycard.helpers;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

import be.nmct.unitycard.auth.AuthHelper;
import be.nmct.unitycard.contracts.ContentProviderContract;

import static be.nmct.unitycard.adapters.SyncAdapter.KEY_SYNC_REQUEST_GET_RETAILER_LOCATIONS;

/**
 * Created by Stephen on 2/12/2016.
 */

public class SyncHelper {
    public static void refreshCachedData(Context context) { // Vernieuw de cached data
        // Pass the settings flags by inserting them in a bundle
        Bundle settingsBundle = new Bundle();
        // Forces a manual sync. The sync adapter framework ignores the existing settings,
        // such as the flag set by setSyncAutomatically().
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        // Forces the sync to start immediately. If you don't set this, the system may wait
        // several seconds before running the sync request, because it tries to optimize
        // battery use by scheduling many requests in a short period of time.
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

        refreshCachedData(context, settingsBundle);
    }

    public static void refreshCachedRetailerLocations(Context context, int retailerId) { // Vernieuw de cached data
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        settingsBundle.putInt(KEY_SYNC_REQUEST_GET_RETAILER_LOCATIONS, retailerId);

        refreshCachedData(context, settingsBundle);
    }

    private static void refreshCachedData(Context context, Bundle settingsBundle) {
        // Request Synchronization
        ContentResolver.requestSync(AuthHelper.getUser(context), ContentProviderContract.AUTHORITY, settingsBundle);
    }
}
