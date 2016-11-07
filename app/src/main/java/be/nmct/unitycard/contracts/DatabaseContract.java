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
        public static final String TABLE_NAME = "Retailers";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_RETAILER_CATEGORY_ID = "RetailerCategoryId";
        public static final String COLUMN_RETAILER_NAME = "RetailerName";
        public static final String COLUMN_TAGLINE = "Tagline";
        public static final String COLUMN_CHAIN = "Chain";
        public static final String COLUMN_LOGOURL= "LogoUrl";
    }

    public static abstract class RetailersDB implements RetailerColumns {

        public static final String CREATE_TABLE = "create table "
                + TABLE_NAME + "("
                + COLUMN_ID + " integer, "
                + COLUMN_RETAILER_CATEGORY_ID + " integer, "
                + COLUMN_RETAILER_NAME + " text not null, "
                + COLUMN_TAGLINE + " text not null, "
                + COLUMN_CHAIN + " real, "
                + COLUMN_LOGOURL + " text not null "
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public interface LoyaltyCardColumns extends BaseColumns{
        public static final String TABLE_NAME = "LoyaltyCards";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_USER_ID = "UserId";
        public static final String COLUMN_CREATED_TIMESTAMP = "CreatedTimestamp";
    }

    public static abstract class LoyaltyCardDB implements LoyaltyCardColumns {
        public static final String CREATE_TABLE = "create table"
                + TABLE_NAME + "("
                + COLUMN_ID + " integer, "
                + COLUMN_USER_ID + " text not null, "
                + COLUMN_CREATED_TIMESTAMP + " text not null"
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
