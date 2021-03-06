package be.nmct.unitycard.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import be.nmct.unitycard.contracts.DatabaseContract;

/**
 * Created by lorenzvercoutere on 6/11/16.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper INSTANCE;
    private static Object object = new Object();

    public static DatabaseHelper getInstance(Context context) {
        if (INSTANCE == null) {
            //via keyword synchronized vermijden we dat meerdere threads Databasehelper-object proberen aan te maken
            synchronized (object) {
                INSTANCE = new DatabaseHelper(context.getApplicationContext());     //opm: hier pas object aanmaken!
            }
        }
        return INSTANCE;
    }

    private DatabaseHelper(Context context) {
        //opm: hier wordt database-versienummer doorgegeven
        super(context, DatabaseContract.DATABASE_NAME, null, DatabaseContract.DATABASE_VERSION);
    }

    //zie ook onderaan voor versie bestemd tijdens de ontwikkeling van app
    @Override
    public void onCreate(SQLiteDatabase db) {
        onUpgrade(db, 0, 1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        while (oldVersion < newVersion) {
            switch (oldVersion) {
                case 0:
                    upgradeTo1(db);
                    oldVersion++;
                    break;
                case 1:
                    //upgrade logic from version 1 to 2
                case 2:
                    //upgrade logic from version 2 to 3
                case 3:
                    //upgrade logic from version 3 to 4
                    break;
                default:
                    throw new IllegalStateException(
                            "onUpgrade() with unknown oldVersion " + oldVersion);
            }
        }
    }

    private void upgradeTo1(SQLiteDatabase db) {
        db.execSQL(DatabaseContract.RetailersDB.CREATE_TABLE);
        db.execSQL(DatabaseContract.AddedRetailersDB.CREATE_TABLE);
        db.execSQL(DatabaseContract.LoyaltyCardDB.CREATE_TABLE);
        db.execSQL(DatabaseContract.LoyaltyPointsDB.CREATE_TABLE);
        db.execSQL(DatabaseContract.RetailerLocationsDB.CREATE_TABLE);
        db.execSQL(DatabaseContract.OffersDB.CREATE_TABLE);
        db.execSQL(DatabaseContract.RetailerCategoriesDB.CREATE_TABLE);
        db.execSQL(DatabaseContract.TotalLoyaltyPointsDB.CREATE_TABLE);

        /*db.execSQL(DatabaseContract.RetailersDB.DELETE_TABLE);
        db.execSQL(DatabaseContract.AddedRetailersDB.DELETE_TABLE);
        db.execSQL(DatabaseContract.LoyaltyCardDB.DELETE_TABLE);
        db.execSQL(DatabaseContract.LoyaltyPointsDB.DELETE_TABLE);
        db.execSQL(DatabaseContract.RetailerLocationsDB.DELETE_TABLE);
        db.execSQL(DatabaseContract.OffersDB.DELETE_TABLE);
        db.execSQL(DatabaseContract.RetailerCategoriesDB.DELETE_TABLE);
        db.execSQL(DatabaseContract.TotalLoyaltyPointsDB.DELETE_TABLE);*/
    }
}
