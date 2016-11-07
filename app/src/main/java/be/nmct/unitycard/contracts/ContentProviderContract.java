package be.nmct.unitycard.contracts;

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

    // MIME-types (vnd = vendor specific)
    public static final String RETAILERS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.unitycard.retailer";
    public static final String RETAILERS_ITEM_CONTENT_TYPE = "vnd.android.cursor.item/vnd.unitycard.retailer";

    public static final String LOYALTYCARDS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.unitycard.loyaltycard";
    public static final String LOYALTYCARDS_ITEM_CONTENT_TYPE = "vnd.android.cursor.item/vnd.unitycard.loyaltycard";

    public static final String LOYALTYPOINTS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.unitycard.loyaltypoint";
    public static final String LOYALTYPOINTS_ITEM_CONTENT_TYPE = "vnd.android.cursor.item/vnd.unitycard.loyaltypoint";
}
