package be.nmct.unitycard.contracts;

import android.provider.BaseColumns;

/**
 * Created by lorenzvercoutere on 6/11/16.
 */

public class DatabaseContract {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "unitycarddatabase.db";
    private static final String TEXT_TYPE = " TEXT";

    //interface uitbreiden
    public interface RetailerColumns extends BaseColumns {
        public static final String TABLE_NAME = "retailers"; // todo: ID hierbij?
        public static final String COLUMN_RETAILER_CATEGORY_ID = "retailercategoryid";
        public static final String COLUMN_RETAILER_NAME = "retailername";
        public static final String COLUMN_TAGLINE = "tagline";
        public static final String COLUMN_CHAIN = "chain";
        public static final String COLUMN_LOGOURL= "logourl";
    }

    public static abstract class RetailersDB implements RetailerColumns {

        public static final String CREATE_TABLE = "create table "
                + TABLE_NAME + "(" + _ID
                + " integer primary key autoincrement, "
                + COLUMN_RETAILER_CATEGORY_ID + " integer, "
                + COLUMN_RETAILER_NAME + " text not null, "
                + COLUMN_TAGLINE + " text not null, "
                + COLUMN_CHAIN + " real, "
                + COLUMN_LOGOURL + " text not null "
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
