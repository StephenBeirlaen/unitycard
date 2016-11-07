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
import be.nmct.unitycard.database.DatabaseHelper;
import be.nmct.unitycard.models.LoyaltyCard;

/**
 * Created by lorenzvercoutere on 6/11/16.
 */

public class ContentProvider extends android.content.ContentProvider {

    private DatabaseHelper dataBaseHelper;

    private static final int RETAILERS = 1;
    private static final int RETAILERS_ID = 2;
    private static final int LOYALTYCARDS = 3;
    private static final int LOYALTYCARDS_ID = 4;

    private static HashMap<String, String> UNITYCARD_PROJECTION_MAP;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        //Retailers
        uriMatcher.addURI(ContentProviderContract.AUTHORITY, "retailers", RETAILERS);
        uriMatcher.addURI(ContentProviderContract.AUTHORITY, "retailers/#", RETAILERS_ID);

        //Loyaltycards
        uriMatcher.addURI(ContentProviderContract.AUTHORITY, "loyaltycards", LOYALTYCARDS);
        uriMatcher.addURI(ContentProviderContract.AUTHORITY, "loyaltycards/#", LOYALTYCARDS_ID);
    }

    @Override
    public boolean onCreate() {
        dataBaseHelper = DatabaseHelper.getInstance(getContext());
        UNITYCARD_PROJECTION_MAP = new HashMap<>();

        //inladen Retailers
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.RetailerColumns.COLUMN_ID, DatabaseContract.RetailerColumns.COLUMN_ID);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.RetailerColumns.COLUMN_RETAILER_CATEGORY_ID, DatabaseContract.RetailerColumns.COLUMN_RETAILER_CATEGORY_ID);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.RetailerColumns.COLUMN_RETAILER_NAME, DatabaseContract.RetailerColumns.COLUMN_RETAILER_NAME);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.RetailerColumns.COLUMN_TAGLINE, DatabaseContract.RetailerColumns.COLUMN_TAGLINE);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.RetailerColumns.COLUMN_CHAIN, DatabaseContract.RetailerColumns.COLUMN_CHAIN);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.RetailerColumns.COLUMN_LOGOURL, DatabaseContract.RetailerColumns.COLUMN_LOGOURL);

        //inladen Loyaltycards
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.LoyaltyCardColumns.COLUMN_ID, DatabaseContract.LoyaltyCardColumns.COLUMN_ID);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.LoyaltyCardColumns.COLUMN_USER_ID, DatabaseContract.LoyaltyCardColumns.COLUMN_USER_ID);
        UNITYCARD_PROJECTION_MAP.put(DatabaseContract.LoyaltyCardColumns.COLUMN_CREATED_TIMESTAMP, DatabaseContract.LoyaltyCardColumns.COLUMN_CREATED_TIMESTAMP);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch (uriMatcher.match(uri)){
            case RETAILERS:
                queryBuilder.setTables(DatabaseContract.RetailerColumns.TABLE_NAME);
                queryBuilder.setProjectionMap(UNITYCARD_PROJECTION_MAP);
                break;
            case RETAILERS_ID:
                queryBuilder.setTables(DatabaseContract.RetailerColumns.TABLE_NAME);
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
            default:
                throw new IllegalArgumentException("Uknown Uri: " + uri);
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
            case LOYALTYCARDS:
                return ContentProviderContract.LOYALTYCARDS_CONTENT_TYPE;
            case LOYALTYCARDS_ID:
                return ContentProviderContract.LOYALTYCARDS_ITEM_CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Uknown Uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        long newRowId;
        switch (uriMatcher.match(uri)){
            case RETAILERS:
                newRowId = db.insert(
                        DatabaseContract.RetailersDB.TABLE_NAME, null, contentValues);
                if(newRowId > 0){
                    Uri retailerItemUri = ContentUris.withAppendedId(ContentProviderContract.RETAILERS_ITEM_URI, newRowId);
                    // Na insert content observers verwittigen dat data mogelijk gewijzigd is
                    getContext().getContentResolver().notifyChange(retailerItemUri, null);
                    return retailerItemUri;
                }
                break;
            case LOYALTYCARDS:
                newRowId = db.insert(
                        DatabaseContract.LoyaltyCardDB.TABLE_NAME, null, contentValues);
                if(newRowId > 0){
                    Uri loyaltyCardItemUri = ContentUris.withAppendedId(ContentProviderContract.LOYALTYCARDS_ITEM_URI, newRowId);
                    getContext().getContentResolver().notifyChange(loyaltyCardItemUri, null);
                    return loyaltyCardItemUri;
                }
                break;
            default:
                throw new IllegalArgumentException("Uknown URI: " + uri);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();

        String finalWhere;
        int count;

        switch (uriMatcher.match(uri)){
            case RETAILERS:
                count = db.delete(
                        DatabaseContract.RetailersDB.TABLE_NAME,
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
                        DatabaseContract.RetailersDB.TABLE_NAME,
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
            default:
                throw new IllegalArgumentException("Uknown URI: " + uri);
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
                        DatabaseContract.RetailersDB.TABLE_NAME,
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
                        DatabaseContract.RetailersDB.TABLE_NAME,
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
            default:
                throw new IllegalArgumentException("Uknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
