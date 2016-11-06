package be.nmct.unitycard.provider;

import android.net.Uri;

/**
 * Created by lorenzvercoutere on 6/11/16.
 */

public class Contract {

    public static final String AUTHORITY = "be.nmct.unitycard";

    //content-uris
    public static final Uri RETAILERS_URI = Uri.parse("content://" + AUTHORITY + "/retailers");
    public static final Uri RETAILERS_ITEM_URI = Uri.parse("content://" + AUTHORITY + "/retailers/");

    //mime-types
    public static final String RETAILERS_CONTENT_TYPE = "vnd.android.dir/vnd.unitycard.retailer";
    public static final String RETAILERS_ITEM_CONTENT_TYPE = "vnd.android.item/vnd.unitycard.retailer";

    public static final int RETAILER_ID_PATH_POSITION = 1;

}
