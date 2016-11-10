package be.nmct.unitycard.contracts;

import android.provider.BaseColumns;

/**
 * Created by lorenzvercoutere on 6/11/16.
 */

public class DatabaseContract {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "unitycarddatabase.db";

    public interface RetailerColumns extends BaseColumns {
        String TABLE_NAME = "Retailers";
        String COLUMN_ID = "Id";
        String COLUMN_RETAILER_CATEGORY_ID = "RetailerCategoryId";
        String COLUMN_RETAILER_NAME = "RetailerName";
        String COLUMN_TAGLINE = "Tagline";
        String COLUMN_CHAIN = "Chain";
        String COLUMN_LOGOURL= "LogoUrl";
    }

    public static abstract class RetailersDB implements RetailerColumns {

        public static final String CREATE_TABLE = "create table "
                + TABLE_NAME + "("
                + COLUMN_ID + " integer primary key, "
                + COLUMN_RETAILER_CATEGORY_ID + " integer, "
                + COLUMN_RETAILER_NAME + " text not null, "
                + COLUMN_TAGLINE + " text not null, "
                + COLUMN_CHAIN + " real, "
                + COLUMN_LOGOURL + " text not null "
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public interface LoyaltyCardColumns extends BaseColumns{
        String TABLE_NAME = "LoyaltyCards";
        String COLUMN_ID = "Id";
        String COLUMN_USER_ID = "UserId";
        String COLUMN_CREATED_TIMESTAMP = "CreatedTimestamp";
    }

    public static abstract class LoyaltyCardDB implements LoyaltyCardColumns {
        public static final String CREATE_TABLE = "create table "
                + TABLE_NAME + "("
                + COLUMN_ID + " integer primary key, "
                + COLUMN_USER_ID + " text not null, "
                + COLUMN_CREATED_TIMESTAMP + " text not null"
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public interface LoyaltyPointsColumns extends BaseColumns {
        String TABLE_NAME = "LoyaltyPoints";
        String COLUMN_ID = "Id";
        String COLUMN_LOYALTYCARD_ID = "LoyaltyCardId";
        String COLUMN_RETAILER_ID = "RetailerId";
        String COLUMN_POINTS = "Points";
    }

    public static abstract class LoyaltyPointsDB implements LoyaltyPointsColumns {
        public static final String CREATE_TABLE = "create table "
                + TABLE_NAME + "("
                + COLUMN_ID + " integer primary key, "
                + COLUMN_LOYALTYCARD_ID + " integer, "
                + COLUMN_RETAILER_ID + " integer, "
                + COLUMN_POINTS + " integer "
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public interface RetailerLocationsColumns extends BaseColumns {
        String TABLE_NAME = "RetailerLocations";
        String COLUMN_ID = "Id";
        String COLUMN_RETAILER_ID = "RetailerId";
        String COLUMN_NAME = "Name";
        String COLUMN_LATITUDE = "Latitude";
        String COLUMN_LONGITUDE = "Longitude";
        String COLUMN_STREET = "Street";
        String COLUMN_NUMBER = "Number";
        String COLUMN_ZIPCODE = "ZipCode";
        String COLUMN_CITY = "City";
        String COLUMN_COUNTRY = "Country";
    }

    public static abstract class RetailerLocationsDB implements RetailerLocationsColumns {
        public static final String CREATE_TABLE = "create table "
                + TABLE_NAME + "("
                + COLUMN_ID + " integer primary key, "
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
        String TABLE_NAME = "Offers";
        String COLUMN_ID = "Id";
        String COLUMN_RETAILER_ID = "RetailerId";
        String COLUMN_OFFER_DEMAND = "OfferDemand";
        String COLUMN_OFFER_RECEIVE = "OfferReceive";
        String COLUMN_CREATED_TIMESTAMP = "CreatedTimestamp";
    }

    public static abstract class OffersDB implements OffersColumns {
        public static final String CREATE_TABLE = "create table "
                + TABLE_NAME + "("
                + COLUMN_ID + " integer primary key, "
                + COLUMN_RETAILER_ID + " integer, "
                + COLUMN_OFFER_DEMAND + " text not null, "
                + COLUMN_OFFER_RECEIVE + " text not null, "
                + COLUMN_CREATED_TIMESTAMP + " text not null"
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public interface RetailerCategoriesColumns extends BaseColumns {
        String TABLE_NAME = "RetailerCategories";
        String COLUMN_ID = "Id";
        String COLUMN_NAME = "CategoryName";
    }

    public static abstract class RetailerCategoriesDB implements RetailerCategoriesColumns {
        public static final String CREATE_TABLE = "create table "
                + TABLE_NAME + "("
                + COLUMN_ID + " integer primary key, "
                + COLUMN_NAME + " text not null"
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
