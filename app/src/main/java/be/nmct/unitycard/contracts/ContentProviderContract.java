package be.nmct.unitycard.contracts;

import android.content.Context;
import android.net.Uri;

/**
 * Created by lorenzvercoutere on 6/11/16.
 */

public class ContentProviderContract {
    public static final String AUTHORITY = "be.nmct.unitycard.provider";

    // Content-URIs
    public static final Uri RETAILERS_URI = Uri.parse("content://" + AUTHORITY + "/retailers");
    public static final Uri RETAILERS_ITEM_URI = Uri.parse("content://" + AUTHORITY + "/retailers/");

    public static final Uri LOYALTYCARDS_URI = Uri.parse("content://" + AUTHORITY + "/loyaltycards");
    public static final Uri LOYALTYCARDS_ITEM_URI = Uri.parse("content://" + AUTHORITY + "/loyaltycards/");

    public static final Uri LOYALTYPOINTS_URI = Uri.parse("content://" + AUTHORITY + "/loyaltypoints");
    public static final Uri LOYALTYPOINTS_ITEM_URI = Uri.parse("content://" + AUTHORITY + "/loyaltypoints/");

    public static final Uri RETAILER_LOCATIONS_URI = Uri.parse("content://" + AUTHORITY + "/retailerlocations");
    public static final Uri RETAILER_LOCATIONS_ITEM_URI = Uri.parse("content://" + AUTHORITY + "/retailerlocations/");

    public static final Uri OFFERS_URI = Uri.parse("content://" + AUTHORITY + "/offers");
    public static final Uri OFFERS_ITEM_URI = Uri.parse("content://" + AUTHORITY + "/offers/");

    public static final Uri RETAILER_CATEGORIES_URI = Uri.parse("content://" + AUTHORITY + "/retailercategories");
    public static final Uri RETAILER_CATEGORIES_ITEM_URI = Uri.parse("content://" + AUTHORITY + "/retailercategories/");

    // MIME-types (vnd = vendor specific)
    public static final String RETAILERS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.unitycard.retailer";
    public static final String RETAILERS_ITEM_CONTENT_TYPE = "vnd.android.cursor.item/vnd.unitycard.retailer";

    public static final String LOYALTYCARDS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.unitycard.loyaltycard";
    public static final String LOYALTYCARDS_ITEM_CONTENT_TYPE = "vnd.android.cursor.item/vnd.unitycard.loyaltycard";

    public static final String LOYALTYPOINTS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.unitycard.loyaltypoint";
    public static final String LOYALTYPOINTS_ITEM_CONTENT_TYPE = "vnd.android.cursor.item/vnd.unitycard.loyaltypoint";

    public static final String RETAILER_LOCATIONS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.unitycard.retailerlocation";
    public static final String RETAILER_LOCATIONS_ITEM_CONTENT_TYPE = "vnd.android.cursor.item/vnd.unitycard.retailerlocation";

    public static final String OFFERS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.unitycard.offer";
    public static final String OFFERS_ITEM_CONTENT_TYPE = "vnd.android.cursor.item/vnd.unitycard.offer";

    public static final String RETAILER_CATEGORIES_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.unitycard.retailercategory";
    public static final String RETAILER_CATEGORIES_ITEM_CONTENT_TYPE = "vnd.android.cursor.item/vnd.unitycard.retailercategory";

    public static void clearAllContent(Context context) {
        context.getContentResolver().delete(ContentProviderContract.RETAILERS_URI, null, null);
        context.getContentResolver().delete(ContentProviderContract.RETAILERS_ITEM_URI, null, null);

        context.getContentResolver().delete(ContentProviderContract.LOYALTYCARDS_URI, null, null);
        context.getContentResolver().delete(ContentProviderContract.LOYALTYCARDS_ITEM_URI, null, null);

        context.getContentResolver().delete(ContentProviderContract.LOYALTYPOINTS_URI, null, null);
        context.getContentResolver().delete(ContentProviderContract.LOYALTYPOINTS_ITEM_URI, null, null);

        context.getContentResolver().delete(ContentProviderContract.RETAILER_LOCATIONS_URI, null, null);
        context.getContentResolver().delete(ContentProviderContract.RETAILER_LOCATIONS_ITEM_URI, null, null);

        context.getContentResolver().delete(ContentProviderContract.OFFERS_URI, null, null);
        context.getContentResolver().delete(ContentProviderContract.OFFERS_ITEM_URI, null, null);

        context.getContentResolver().delete(ContentProviderContract.RETAILER_CATEGORIES_URI, null, null);
        context.getContentResolver().delete(ContentProviderContract.RETAILER_CATEGORIES_ITEM_URI, null, null);
    }
}
