package be.nmct.unitycard.provider;

import android.content.ContentProvider;
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

import be.nmct.unitycard.database.DatabaseHelper;

/**
 * Created by lorenzvercoutere on 6/11/16.
 */

public class Provider extends ContentProvider {

    private DatabaseHelper dataBaseHelper;

    private static final int RETAILERS = 1;
    private static final int RETAILERS_ID = 2;

    private static HashMap<String, String> RETAILERS_PROJECTION_MAP;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Contract.AUTHORITY, "retailers", RETAILERS);
        uriMatcher.addURI(Contract.AUTHORITY, "retailers/#", RETAILERS_ID);
    }

    @Override
    public boolean onCreate() {
        dataBaseHelper = DatabaseHelper.getInstance(getContext());
        RETAILERS_PROJECTION_MAP = new HashMap<>();
        RETAILERS_PROJECTION_MAP.put(be.nmct.unitycard.database.Contract.RetailerColumns._ID, be.nmct.unitycard.database.Contract.RetailerColumns._ID);
        RETAILERS_PROJECTION_MAP.put(be.nmct.unitycard.database.Contract.RetailerColumns.COLUMN_RETAILER_CATEGORY_ID, be.nmct.unitycard.database.Contract.RetailerColumns.COLUMN_RETAILER_CATEGORY_ID);
        RETAILERS_PROJECTION_MAP.put(be.nmct.unitycard.database.Contract.RetailerColumns.COLUMN_RETAILER_NAME, be.nmct.unitycard.database.Contract.RetailerColumns.COLUMN_RETAILER_NAME);
        RETAILERS_PROJECTION_MAP.put(be.nmct.unitycard.database.Contract.RetailerColumns.COLUMN_TAGLINE, be.nmct.unitycard.database.Contract.RetailerColumns.COLUMN_TAGLINE);
        RETAILERS_PROJECTION_MAP.put(be.nmct.unitycard.database.Contract.RetailerColumns.COLUMN_CHAIN, be.nmct.unitycard.database.Contract.RetailerColumns.COLUMN_CHAIN);
        RETAILERS_PROJECTION_MAP.put(be.nmct.unitycard.database.Contract.RetailerColumns.COLUMN_LOGOURL, be.nmct.unitycard.database.Contract.RetailerColumns.COLUMN_LOGOURL);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch (uriMatcher.match(uri)){
            case RETAILERS:
                queryBuilder.setTables(be.nmct.unitycard.database.Contract.RetailerColumns.TABLE_NAME);
                queryBuilder.setProjectionMap(RETAILERS_PROJECTION_MAP);
                break;
            case RETAILERS_ID:
                queryBuilder.setTables(be.nmct.unitycard.database.Contract.RetailerColumns.TABLE_NAME);
                queryBuilder.setProjectionMap(RETAILERS_PROJECTION_MAP);
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
                return Contract.RETAILERS_CONTENT_TYPE;
            case RETAILERS_ID:
                return Contract.RETAILERS_ITEM_CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Uknown Uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)){
            case RETAILERS:
                long newRowId = db.insert(
                        be.nmct.unitycard.database.Contract.RetailersDB.TABLE_NAME, null, contentValues);
                if(newRowId > 0){
                    Uri retailerItemUri = ContentUris.withAppendedId(Contract.RETAILERS_ITEM_URI, newRowId);
                    getContext().getContentResolver().notifyChange(retailerItemUri, null);
                    return retailerItemUri;
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
                        be.nmct.unitycard.database.Contract.RetailersDB.TABLE_NAME,
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
                        be.nmct.unitycard.database.Contract.RetailersDB.TABLE_NAME,
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
                        be.nmct.unitycard.database.Contract.RetailersDB.TABLE_NAME,
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
                        be.nmct.unitycard.database.Contract.RetailersDB.TABLE_NAME,
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
