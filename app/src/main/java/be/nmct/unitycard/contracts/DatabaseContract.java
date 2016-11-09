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

    public interface LoyaltyPointsColumns extends BaseColumns {
        public static final String TABLE_NAME = "LoyaltyPoints";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_LOYALTYCARD_ID = "LoyaltyCardId";
        public static final String COLUMN_RETAILER_ID = "RetailerId";
        public static final String COLUMN_POINTS = "Points";
    }

    public static abstract class LoyaltyPointsDB implements LoyaltyPointsColumns {
        public static final String CREATE_TABLE = "create table"
                + TABLE_NAME + "("
                + COLUMN_ID + " integer, "
                + COLUMN_LOYALTYCARD_ID + " integer, "
                + COLUMN_RETAILER_ID + " integer, "
                + COLUMN_POINTS + " integer "
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public interface RetailerLocationsColumns extends BaseColumns {
        public static final String TABLE_NAME = "RetailerLocations";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_RETAILER_ID = "RetailerId";
        public static final String COLUMN_NAME = "Name";
        public static final String COLUMN_LATITUDE = "Latitude";
        public static final String COLUMN_LONGITUDE = "Longitude";
        public static final String COLUMN_STREET = "Street";
        public static final String COLUMN_NUMBER = "Number";
        public static final String COLUMN_ZIPCODE = "ZipCode";
        public static final String COLUMN_CITY = "City";
        public static final String COLUMN_COUNTRY = "Country";
    }

    public static abstract class RetailerLocationsDB implements RetailerLocationsColumns {
        public static final String CREATE_TABLE = "create table"
                + TABLE_NAME + "("
                + COLUMN_ID + " integer, "
                + COLUMN_RETAILER_ID + " integer, "
                + COLUMN_NAME + " text not null, "
                + COLUMN_LATITUDE + " double, "
                + COLUMN_LONGITUDE + " double, "
                + COLUMN_STREET + " text not null, "
                + COLUMN_NUMBER + " text not null, "
                + COLUMN_ZIPCODE + " integer, "
                + COLUMN_CITY + " text not null, "
                + COLUMN_COUNTRY + " text not null"
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public interface OffersColumns extends BaseColumns {
        public static final String TABLE_NAME = "Offers";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_RETAILER_ID = "RetailerId";
        public static final String COLUMN_OFFER_DEMAND = "OfferDemand";
        public static final String COLUMN_OFFER_RECEIVE = "OfferReceive";
        public static final String COLUMN_CREATED_TIMESTAMP = "CreatedTimestamp";
    }

    public static abstract class OffersDB implements OffersColumns {
        public static final String CREATE_TABLE = "create table"
                + TABLE_NAME + "("
                + COLUMN_ID + " integer, "
                + COLUMN_RETAILER_ID + " integer, "
                + COLUMN_OFFER_DEMAND + " text not null, "
                + COLUMN_OFFER_RECEIVE + " text not null, "
                + COLUMN_CREATED_TIMESTAMP + " text not null"
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public interface RetailerCategoriesColumns extends BaseColumns {
        public static final String TABLE_NAME = "RetailerCategories";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_NAME = "CategoryName";
    }

    public static abstract class RetailerCategoriesDB implements RetailerCategoriesColumns {
        public static final String CREATE_TABLE = "create table"
                + TABLE_NAME + "("
                + COLUMN_ID + " integer, "
                + COLUMN_NAME + " text not null"
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
