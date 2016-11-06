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

    // MIME-types (vnd = vendor specific)
    public static final String RETAILERS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.unitycard.retailer";
    public static final String RETAILERS_ITEM_CONTENT_TYPE = "vnd.android.cursor.item/vnd.unitycard.retailer";

    public static final int RETAILER_ID_PATH_POSITION = 1; // todo: lorenz, why this?
}
