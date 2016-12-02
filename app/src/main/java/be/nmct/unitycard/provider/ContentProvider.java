package be.nmct.unitycard.provider;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.util.HashMap;

import be.nmct.unitycard.contracts.ContentProviderContract;
import be.nmct.unitycard.contracts.DatabaseContract;
import be.nmct.unitycard.helpers.DatabaseHelper;

/**
 * Created by lorenzvercoutere on 6/11/16.
 */

public class ContentProvider extends android.content.ContentProvider {

    private DatabaseHelper dataBaseHelper;

    private static final int RETAILERS = 1;
    private static final int RETAILERS_ID = 2;
    private static final int ADDED_RETAILERS = 3;
    private static final int ADDED_RETAILERS_ID = 4;
    private static final int LOYALTYCARDS = 5;
    private static final int LOYALTYCARDS_ID = 6;
    private static final int LOYALTYPOINTS = 7;
    private static final int LOYALTYPOINTS_ID = 8;
    private static final int RETAILERLOCATIONS = 9;
    private static final int RETAILERLOCATIONS_ID = 10;
    private static final int OFFERS = 11;
    private static final int OFFERS_ID = 12;
    private static final int RETAILERCATEGORIES = 13;
    private static final int RETAILERCATEGORIES_ID = 14;

    private static HashMap<String, String> UNITYCARD_PROJECTION_MAP;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        //Retailers
        uriMatcher.addURI(ContentProviderContract.AUTHORITY, "retailers", RETAILERS);
        uriMatcher.addURI(ContentProviderContract.AUTHORITY, "retailers/#", RETAILERS_ID);

        //AddedRetailers
        uriMatcher.addURI(ContentProviderContract.AUTHORITY, "addedretailers", ADDED_RETAILERS);
        uriMatcher.addURI(ContentProviderContract.AUTHORITY, "addedretailers/#", ADDED_RETAILERS_ID);

        //Loyaltycards
        uriMatcher.addURI(ContentProviderContract.AUTHORITY, "loyaltycards", LOYALTYCARDS);
        uriMatcher.addURI(ContentProviderContract.AUTHORITY, "loyaltycards/#", LOYALTYCARDS_ID);

        //LoyaltyPoints
        uriMatcher.addURI(ContentProviderContract.AUTHORITY, "loyaltypoints", LOYALTYPOINTS);
        uriMatcher.addURI(ContentProviderContract.AUTHORITY, "loyaltypoints/#", LOYALTYPOINTS_ID);

        //RetailerLocations
        uriMatcher.addURI(ContentProviderContract.AUTHORITY, "retailerlocations", RETAILERLOCATIONS);
        uriMatcher.addURI(ContentProviderContract.AUTHORITY, "retailerlocations/#", RETAILERLOCATIONS_ID);

        //Offers
        uriMatcher.addURI(ContentProviderContract.AUTHORITY, "offers", OFFERS);
        uriMatcher.addURI(ContentProviderContract.AUTHORITY, "offers/#", OFFERS_ID);

        //RetailerCategories
        uriMatcher.addURI(ContentProviderContract.AUTHORITY, "retailercategories", RETAILERCATEGORIES);
        uriMatcher.addURI(ContentProviderContract.AUTHORITY, "retailercategories/#", RETAILERCATEGORIES_ID);
    }

    @Override
    public boolean onCreate() {
        dataBaseHelper = DatabaseHelper.getInstance(getContext());
        UNITYCARD_PROJECTION_MAP = new HashMap<>(); // todo: voor wat is dit??

        //inladen Retailers
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.RetailerColumns._ID, DatabaseContract.RetailerColumns._ID);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.RetailerColumns.COLUMN_SERVER_ID, DatabaseContract.RetailerColumns.COLUMN_SERVER_ID);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.RetailerColumns.COLUMN_RETAILER_CATEGORY_ID, DatabaseContract.RetailerColumns.COLUMN_RETAILER_CATEGORY_ID);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.RetailerColumns.COLUMN_RETAILER_NAME, DatabaseContract.RetailerColumns.COLUMN_RETAILER_NAME);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.RetailerColumns.COLUMN_TAGLINE, DatabaseContract.RetailerColumns.COLUMN_TAGLINE);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.RetailerColumns.COLUMN_CHAIN, DatabaseContract.RetailerColumns.COLUMN_CHAIN);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.RetailerColumns.COLUMN_LOGOURL, DatabaseContract.RetailerColumns.COLUMN_LOGOURL);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.RetailerColumns.COLUMN_UPDATED_TIMESTAMP, DatabaseContract.RetailerColumns.COLUMN_UPDATED_TIMESTAMP);

        //inladen LoyaltyCards
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.LoyaltyCardColumns._ID, DatabaseContract.LoyaltyCardColumns._ID);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.LoyaltyCardColumns.COLUMN_SERVER_ID, DatabaseContract.LoyaltyCardColumns.COLUMN_SERVER_ID);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.LoyaltyCardColumns.COLUMN_USER_ID, DatabaseContract.LoyaltyCardColumns.COLUMN_USER_ID);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.LoyaltyCardColumns.COLUMN_CREATED_TIMESTAMP, DatabaseContract.LoyaltyCardColumns.COLUMN_CREATED_TIMESTAMP);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.LoyaltyCardColumns.COLUMN_UPDATED_TIMESTAMP, DatabaseContract.LoyaltyCardColumns.COLUMN_UPDATED_TIMESTAMP);

        //inladen LoyaltyPoints
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.LoyaltyPointsColumns._ID, DatabaseContract.LoyaltyPointsColumns._ID);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.LoyaltyPointsColumns.COLUMN_SERVER_ID, DatabaseContract.LoyaltyPointsColumns.COLUMN_SERVER_ID);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.LoyaltyPointsColumns.COLUMN_LOYALTYCARD_ID, DatabaseContract.LoyaltyPointsColumns.COLUMN_LOYALTYCARD_ID);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.LoyaltyPointsColumns.COLUMN_RETAILER_ID, DatabaseContract.LoyaltyPointsColumns.COLUMN_RETAILER_ID);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.LoyaltyPointsColumns.COLUMN_POINTS, DatabaseContract.LoyaltyPointsColumns.COLUMN_POINTS);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.LoyaltyPointsColumns.COLUMN_UPDATED_TIMESTAMP, DatabaseContract.LoyaltyPointsColumns.COLUMN_UPDATED_TIMESTAMP);

        //inladen RetailerLocations
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.RetailerLocationsColumns._ID, DatabaseContract.RetailerLocationsColumns._ID);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.RetailerLocationsColumns.COLUMN_SERVER_ID, DatabaseContract.RetailerLocationsColumns.COLUMN_SERVER_ID);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.RetailerLocationsColumns.COLUMN_RETAILER_ID, DatabaseContract.RetailerLocationsColumns.COLUMN_RETAILER_ID);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.RetailerLocationsColumns.COLUMN_NAME, DatabaseContract.RetailerLocationsColumns.COLUMN_NAME);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.RetailerLocationsColumns.COLUMN_STREET, DatabaseContract.RetailerLocationsColumns.COLUMN_STREET);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.RetailerLocationsColumns.COLUMN_NUMBER, DatabaseContract.RetailerLocationsColumns.COLUMN_NUMBER);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.RetailerLocationsColumns.COLUMN_ZIPCODE, DatabaseContract.RetailerLocationsColumns.COLUMN_ZIPCODE);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.RetailerLocationsColumns.COLUMN_CITY, DatabaseContract.RetailerLocationsColumns.COLUMN_CITY);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.RetailerLocationsColumns.COLUMN_COUNTRY, DatabaseContract.RetailerLocationsColumns.COLUMN_COUNTRY);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.RetailerLocationsColumns.COLUMN_UPDATED_TIMESTAMP, DatabaseContract.RetailerLocationsColumns.COLUMN_UPDATED_TIMESTAMP);

        //inladen Offers
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.OffersColumns._ID, DatabaseContract.OffersColumns._ID);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.OffersColumns.COLUMN_SERVER_ID, DatabaseContract.OffersColumns.COLUMN_SERVER_ID);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.OffersColumns.COLUMN_RETAILER_ID, DatabaseContract.OffersColumns.COLUMN_RETAILER_ID);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.OffersColumns.COLUMN_OFFER_DEMAND, DatabaseContract.OffersColumns.COLUMN_OFFER_DEMAND);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.OffersColumns.COLUMN_OFFER_RECEIVE, DatabaseContract.OffersColumns.COLUMN_OFFER_RECEIVE);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.OffersColumns.COLUMN_CREATED_TIMESTAMP, DatabaseContract.OffersColumns.COLUMN_CREATED_TIMESTAMP);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.OffersColumns.COLUMN_UPDATED_TIMESTAMP, DatabaseContract.OffersColumns.COLUMN_UPDATED_TIMESTAMP);

        //inladen RetailerCategories
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.RetailerCategoriesColumns._ID, DatabaseContract.RetailerCategoriesColumns._ID);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.RetailerCategoriesColumns.COLUMN_SERVER_ID, DatabaseContract.RetailerCategoriesColumns.COLUMN_SERVER_ID);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.RetailerCategoriesColumns.COLUMN_NAME, DatabaseContract.RetailerCategoriesColumns.COLUMN_NAME);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.RetailerCategoriesColumns.COLUMN_UPDATED_TIMESTAMP, DatabaseContract.RetailerCategoriesColumns.COLUMN_UPDATED_TIMESTAMP);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch (uriMatcher.match(uri)){
            case RETAILERS:
                queryBuilder.setTables(DatabaseContract.RetailerColumns.TABLE_NAME_ALL_RETAILERS);
                queryBuilder.setProjectionMap(UNITYCARD_PROJECTION_MAP);
                break;
            case RETAILERS_ID:
                queryBuilder.setTables(DatabaseContract.RetailerColumns.TABLE_NAME_ALL_RETAILERS);
                queryBuilder.setProjectionMap(UNITYCARD_PROJECTION_MAP);
                break;
            case ADDED_RETAILERS:
                queryBuilder.setTables(DatabaseContract.RetailerColumns.TABLE_NAME_ADDED_RETAILERS);
                queryBuilder.setProjectionMap(UNITYCARD_PROJECTION_MAP);
                break;
            case ADDED_RETAILERS_ID:
                queryBuilder.setTables(DatabaseContract.RetailerColumns.TABLE_NAME_ADDED_RETAILERS);
                queryBuilder.setProjectionMap(UNITYCARD_PROJECTION_MAP);
                break;
            case LOYALTYCARDS:
                queryBuilder.setTables(DatabaseContract.LoyaltyCardColumns.TABLE_NAME);
                queryBuilder.setProjectionMap(UNITYCARD_PROJECTION_MAP);
                break;
            case LOYALTYCARDS_ID:
                queryBuilder.setTables(DatabaseContract.LoyaltyCardColumns.TABLE_NAME);
                queryBuilder.setProjectionMap(UNITYCARD_PROJECTION_MAP);
                break;
            case LOYALTYPOINTS:
                queryBuilder.setTables(DatabaseContract.LoyaltyPointsColumns.TABLE_NAME);
                queryBuilder.setProjectionMap(UNITYCARD_PROJECTION_MAP);
                break;
            case LOYALTYPOINTS_ID:
                queryBuilder.setTables(DatabaseContract.LoyaltyPointsColumns.TABLE_NAME);
                queryBuilder.setProjectionMap(UNITYCARD_PROJECTION_MAP);
                break;
            case RETAILERLOCATIONS:
                queryBuilder.setTables(DatabaseContract.RetailerLocationsColumns.TABLE_NAME);
                queryBuilder.setProjectionMap(UNITYCARD_PROJECTION_MAP);
                break;
            case RETAILERLOCATIONS_ID:
                queryBuilder.setTables(DatabaseContract.RetailerLocationsColumns.TABLE_NAME);
                queryBuilder.setProjectionMap(UNITYCARD_PROJECTION_MAP);
                break;
            case OFFERS:
                queryBuilder.setTables(DatabaseContract.OffersColumns.TABLE_NAME);
                queryBuilder.setProjectionMap(UNITYCARD_PROJECTION_MAP);
                break;
            case OFFERS_ID:
                queryBuilder.setTables(DatabaseContract.OffersColumns.TABLE_NAME);
                queryBuilder.setProjectionMap(UNITYCARD_PROJECTION_MAP);
                break;
            case RETAILERCATEGORIES:
                queryBuilder.setTables(DatabaseContract.RetailerCategoriesColumns.TABLE_NAME);
                queryBuilder.setProjectionMap(UNITYCARD_PROJECTION_MAP);
                break;
            case RETAILERCATEGORIES_ID:
                queryBuilder.setTables(DatabaseContract.RetailerCategoriesColumns.TABLE_NAME);
                queryBuilder.setProjectionMap(UNITYCARD_PROJECTION_MAP);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }

        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();

        Cursor data = queryBuilder.query(
                db,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        data.getCount();

        data.setNotificationUri(getContext().getContentResolver(), uri);

        return data;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case RETAILERS:
                return ContentProviderContract.RETAILERS_CONTENT_TYPE;
            case RETAILERS_ID:
                return ContentProviderContract.RETAILERS_ITEM_CONTENT_TYPE;
            case ADDED_RETAILERS:
                return ContentProviderContract.RETAILERS_CONTENT_TYPE;
            case ADDED_RETAILERS_ID:
                return ContentProviderContract.RETAILERS_ITEM_CONTENT_TYPE;
            case LOYALTYCARDS:
                return ContentProviderContract.LOYALTYCARDS_CONTENT_TYPE;
            case LOYALTYCARDS_ID:
                return ContentProviderContract.LOYALTYCARDS_ITEM_CONTENT_TYPE;
            case LOYALTYPOINTS:
                return ContentProviderContract.LOYALTYPOINTS_CONTENT_TYPE;
            case LOYALTYPOINTS_ID:
                return ContentProviderContract.LOYALTYPOINTS_ITEM_CONTENT_TYPE;
            case RETAILERLOCATIONS:
                return ContentProviderContract.RETAILER_LOCATIONS_CONTENT_TYPE;
            case RETAILERLOCATIONS_ID:
                return ContentProviderContract.RETAILER_LOCATIONS_ITEM_CONTENT_TYPE;
            case OFFERS:
                return ContentProviderContract.OFFERS_CONTENT_TYPE;
            case OFFERS_ID:
                return ContentProviderContract.OFFERS_ITEM_CONTENT_TYPE;
            case RETAILERCATEGORIES:
                return ContentProviderContract.RETAILER_CATEGORIES_CONTENT_TYPE;
            case RETAILERCATEGORIES_ID:
                return ContentProviderContract.RETAILER_CATEGORIES_ITEM_CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        long newRowId;
        switch (uriMatcher.match(uri)){
            case RETAILERS:
                newRowId = insertOrUpdate(db,
                        DatabaseContract.RetailersDB.TABLE_NAME_ALL_RETAILERS,
                        DatabaseContract.RetailersDB.COLUMN_SERVER_ID, contentValues);
                if (newRowId > 0) {
                    Uri retailerItemUri = ContentUris.withAppendedId(ContentProviderContract.RETAILERS_ITEM_URI, newRowId);
                    // Na insert content observers verwittigen dat data mogelijk gewijzigd is
                    // Call notifychange on the entire SET and not on one ROW:
                    getContext().getContentResolver().notifyChange(ContentProviderContract.RETAILERS_URI, null);
                    return retailerItemUri;
                }
                break;
            case ADDED_RETAILERS:
                newRowId = insertOrUpdate(db,
                        DatabaseContract.RetailersDB.TABLE_NAME_ADDED_RETAILERS,
                        DatabaseContract.RetailersDB.COLUMN_SERVER_ID, contentValues);
                if (newRowId > 0) {
                    Uri retailerItemUri = ContentUris.withAppendedId(ContentProviderContract.ADDED_RETAILERS_ITEM_URI, newRowId);
                    getContext().getContentResolver().notifyChange(ContentProviderContract.ADDED_RETAILERS_URI, null);
                    return retailerItemUri;
                }
                break;
            case LOYALTYCARDS:
                newRowId = insertOrUpdate(db,
                        DatabaseContract.LoyaltyCardDB.TABLE_NAME,
                        DatabaseContract.LoyaltyCardDB.COLUMN_SERVER_ID, contentValues);
                if(newRowId > 0){
                    Uri loyaltyCardItemUri = ContentUris.withAppendedId(ContentProviderContract.LOYALTYCARDS_ITEM_URI, newRowId);
                    getContext().getContentResolver().notifyChange(ContentProviderContract.LOYALTYCARDS_URI, null);
                    return loyaltyCardItemUri;
                }
                break;
            case LOYALTYPOINTS:
                newRowId = insertOrUpdate(db,
                        DatabaseContract.LoyaltyPointsDB.TABLE_NAME,
                        DatabaseContract.LoyaltyPointsDB.COLUMN_SERVER_ID, contentValues);
                if(newRowId > 0){
                    Uri loyaltyPointItemUri = ContentUris.withAppendedId(ContentProviderContract.LOYALTYPOINTS_ITEM_URI, newRowId);
                    getContext().getContentResolver().notifyChange(ContentProviderContract.LOYALTYPOINTS_URI, null);
                    return loyaltyPointItemUri;
                }
                break;
            case RETAILERLOCATIONS:
                newRowId = insertOrUpdate(db,
                        DatabaseContract.RetailerLocationsDB.TABLE_NAME,
                        DatabaseContract.RetailerLocationsDB.COLUMN_SERVER_ID, contentValues);
                if(newRowId > 0){
                    Uri retailerLocationItemUri = ContentUris.withAppendedId(ContentProviderContract.RETAILER_LOCATIONS_ITEM_URI, newRowId);
                    getContext().getContentResolver().notifyChange(ContentProviderContract.RETAILER_LOCATIONS_URI, null);
                    return retailerLocationItemUri;
                }
                break;
            case OFFERS:
                newRowId = insertOrUpdate(db,
                        DatabaseContract.OffersDB.TABLE_NAME,
                        DatabaseContract.OffersDB.COLUMN_SERVER_ID, contentValues);
                if(newRowId > 0){
                    Uri offerItemUri = ContentUris.withAppendedId(ContentProviderContract.OFFERS_ITEM_URI, newRowId);
                    getContext().getContentResolver().notifyChange(ContentProviderContract.OFFERS_URI, null);
                    return offerItemUri;
                }
                break;
            case RETAILERCATEGORIES:
                newRowId = insertOrUpdate(db,
                        DatabaseContract.RetailerCategoriesDB.TABLE_NAME,
                        DatabaseContract.RetailerCategoriesDB.COLUMN_SERVER_ID, contentValues);
                if(newRowId > 0){
                    Uri retailerCategoryItemUri = ContentUris.withAppendedId(ContentProviderContract.RETAILER_CATEGORIES_ITEM_URI, newRowId);
                    getContext().getContentResolver().notifyChange(ContentProviderContract.RETAILER_CATEGORIES_URI, null);
                    return retailerCategoryItemUri;
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        throw new IllegalArgumentException();
    }

    private long insertOrUpdate(SQLiteDatabase db, String table, String nullColumnHack, ContentValues contentValues) {
        // Deze methode zal proberen inserten, maar wanneer er een conflict
        // optreedt (de Id bestaat al) zal de record vervangen worden (replace)
        // Het gaat hier wel om server id's! Niet de record id van de lokale db
        /* int CONFLICT_REPLACE: (https://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html#CONFLICT_REPLACE)
        * When a UNIQUE constraint violation occurs, the pre-existing rows that are causing
        * the constraint violation are removed prior to inserting or updating the current row.
        * Thus the insert or update always occurs. The command continues executing normally.
        * No error is returned. If a NOT NULL constraint violation occurs, the NULL value is
        * replaced by the default value for that column. If the column has no default value,
        * then the ABORT algorithm is used. If a CHECK constraint violation occurs then the
        * IGNORE algorithm is used. When this conflict resolution strategy deletes rows in
        * order to satisfy a constraint, it does not invoke delete triggers on those rows.
        * This behavior might change in a future release.*/
        return db.insertWithOnConflict(table, nullColumnHack, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();

        String finalWhere;
        int count;

        switch (uriMatcher.match(uri)){
            case RETAILERS:
                count = db.delete(
                        DatabaseContract.RetailersDB.TABLE_NAME_ALL_RETAILERS,
                        selection,
                        selectionArgs);
                break;
            case RETAILERS_ID:
                String retailerItemId = uri.getPathSegments().get(1);
                finalWhere = "Id = " + retailerItemId;

                if(selection != null){
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, selection);
                }

                count = db.delete(
                        DatabaseContract.RetailersDB.TABLE_NAME_ALL_RETAILERS,
                        selection,
                        selectionArgs);
                break;
            case ADDED_RETAILERS:
                count = db.delete(
                        DatabaseContract.RetailersDB.TABLE_NAME_ADDED_RETAILERS,
                        selection,
                        selectionArgs);
                break;
            case ADDED_RETAILERS_ID:
                String addedRetailerItemId = uri.getPathSegments().get(1);
                finalWhere = "Id = " + addedRetailerItemId;

                if(selection != null){
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, selection);
                }

                count = db.delete(
                        DatabaseContract.RetailersDB.TABLE_NAME_ADDED_RETAILERS,
                        selection,
                        selectionArgs);
                break;
            case LOYALTYCARDS:
                count = db.delete(
                        DatabaseContract.LoyaltyCardDB.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case LOYALTYCARDS_ID:
                String loyaltyCardItemId = uri.getPathSegments().get(1);
                finalWhere = "Id = " + loyaltyCardItemId;

                if(selection != null){
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, selection);
                }

                count = db.delete(
                        DatabaseContract.LoyaltyCardDB.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case LOYALTYPOINTS:
                count = db.delete(
                        DatabaseContract.LoyaltyPointsDB.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case LOYALTYPOINTS_ID:
                String loyaltyPointsItemId = uri.getPathSegments().get(1);
                finalWhere = "Id = " + loyaltyPointsItemId;

                if(selection != null){
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, selection);
                }

                count = db.delete(
                        DatabaseContract.LoyaltyPointsDB.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case RETAILERLOCATIONS:
                count = db.delete(
                        DatabaseContract.RetailerLocationsDB.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case RETAILERLOCATIONS_ID:
                String retailerLocationsItemId = uri.getPathSegments().get(1);
                finalWhere = "Id = " + retailerLocationsItemId;

                if(selection != null){
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, selection);
                }

                count = db.delete(
                        DatabaseContract.RetailerLocationsDB.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case OFFERS:
                count = db.delete(
                        DatabaseContract.OffersDB.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case OFFERS_ID:
                String offersItemId = uri.getPathSegments().get(1);
                finalWhere = "Id = " + offersItemId;

                if(selection != null){
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, selection);
                }

                count = db.delete(
                        DatabaseContract.OffersDB.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case RETAILERCATEGORIES:
                count = db.delete(
                        DatabaseContract.RetailerCategoriesDB.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case RETAILERCATEGORIES_ID:
                String retailerCategoriesItemId = uri.getPathSegments().get(1);
                finalWhere = "Id = " + retailerCategoriesItemId;

                if(selection != null){
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, selection);
                }

                count = db.delete(
                        DatabaseContract.RetailerCategoriesDB.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        int count;
        String finalWhere;

        switch (uriMatcher.match(uri)){
            case RETAILERS:
                count = db.update(
                        DatabaseContract.RetailersDB.TABLE_NAME_ALL_RETAILERS,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            case RETAILERS_ID:
                String retailerItemId = uri.getPathSegments().get(1);
                finalWhere = "Id = " + retailerItemId;

                if(selection != null){
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, selection);
                }

                count = db.update(
                        DatabaseContract.RetailersDB.TABLE_NAME_ALL_RETAILERS,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            case ADDED_RETAILERS:
                count = db.update(
                        DatabaseContract.RetailersDB.TABLE_NAME_ADDED_RETAILERS,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            case ADDED_RETAILERS_ID:
                String addedRetailerItemId = uri.getPathSegments().get(1);
                finalWhere = "Id = " + addedRetailerItemId;

                if(selection != null){
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, selection);
                }

                count = db.update(
                        DatabaseContract.RetailersDB.TABLE_NAME_ADDED_RETAILERS,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            case LOYALTYCARDS:
                count = db.update(
                        DatabaseContract.LoyaltyCardDB.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            case LOYALTYCARDS_ID:
                String loyaltyCardItemId = uri.getPathSegments().get(1);
                finalWhere = "Id = " + loyaltyCardItemId;

                if(selection != null){
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, selection);
                }

                count = db.update(
                        DatabaseContract.LoyaltyCardDB.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            case LOYALTYPOINTS:
                count = db.update(
                        DatabaseContract.LoyaltyPointsDB.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);
                break;

            case LOYALTYPOINTS_ID:
                String loyaltyPointsItemId = uri.getPathSegments().get(1);
                finalWhere = "Id = " + loyaltyPointsItemId;

                if(selection != null){
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, selection);
                }

                count = db.update(
                        DatabaseContract.LoyaltyPointsDB.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            case RETAILERLOCATIONS:
                count = db.update(
                        DatabaseContract.RetailerLocationsDB.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);
                break;

            case RETAILERLOCATIONS_ID:
                String retailerLocationsItemId = uri.getPathSegments().get(1);
                finalWhere = "Id = " + retailerLocationsItemId;

                if(selection != null){
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, selection);
                }

                count = db.update(
                        DatabaseContract.RetailerLocationsDB.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            case OFFERS:
                count = db.update(
                        DatabaseContract.OffersDB.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);
                break;

            case OFFERS_ID:
                String offersItemId = uri.getPathSegments().get(1);
                finalWhere = "Id = " + offersItemId;

                if(selection != null){
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, selection);
                }

                count = db.update(
                        DatabaseContract.OffersDB.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            case RETAILERCATEGORIES:
                count = db.update(
                        DatabaseContract.RetailerCategoriesDB.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);
                break;

            case RETAILERCATEGORIES_ID:
                String retailerCategoriesItemId = uri.getPathSegments().get(1);
                finalWhere = "Id = " + retailerCategoriesItemId;

                if(selection != null){
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, selection);
                }

                count = db.update(
                        DatabaseContract.RetailerCategoriesDB.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
