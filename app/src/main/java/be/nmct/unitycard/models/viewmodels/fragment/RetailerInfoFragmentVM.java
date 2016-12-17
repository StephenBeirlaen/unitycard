package be.nmct.unitycard.models.viewmodels.fragment;

import android.content.Context;
import android.database.Cursor;
import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.view.View;

import java.util.ArrayList;

import be.nmct.unitycard.BR;
import be.nmct.unitycard.contracts.DatabaseContract;
import be.nmct.unitycard.databinding.FragmentRetailerInfoBinding;
import be.nmct.unitycard.fragments.customer.RetailerInfoFragment;
import be.nmct.unitycard.helpers.SyncHelper;
import be.nmct.unitycard.helpers.TimestampHelper;
import be.nmct.unitycard.models.Retailer;
import be.nmct.unitycard.models.RetailerLocation;

import static be.nmct.unitycard.contracts.ContentProviderContract.RETAILERS_ITEM_URI;
import static be.nmct.unitycard.contracts.ContentProviderContract.RETAILER_LOCATIONS_ITEM_URI;

/**
 * Created by Stephen on 9/11/2016.
 */

public class RetailerInfoFragmentVM extends BaseObservable {

    private FragmentRetailerInfoBinding mBinding;
    private RetailerInfoFragment.RetailerInfoFragmentListener mListener;
    private Context mContext;

    private Integer mRetailerId;
    public Integer getRetailerId() {
        return mRetailerId;
    }

    private ArrayList<RetailerLocation> retailerLocations;
    public ArrayList<RetailerLocation> getRetailerLocations() {
        return retailerLocations;
    }

    private RetailerLocation closestRetailerLocation;
    public RetailerLocation getClosestRetailerLocation() {
        return closestRetailerLocation;
    }

    public RetailerInfoFragmentVM(FragmentRetailerInfoBinding binding, Context context, RetailerInfoFragment.RetailerInfoFragmentListener listener, Integer retailerId) {
        this.mBinding = binding;
        this.mListener = listener;
        this.mContext = context;
        this.mRetailerId = retailerId;

        mBinding.setViewmodel(this);

        mBinding.buttonToonAlleWinkels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (retailerLocations != null && retailerLocations.size() > 0) {
                    mListener.showAllRetailersMap(retailerLocations);
                }
                else {
                    mListener.handleError("No retailer locations found!");
                }
            }
        });

        mBinding.buttonOpenKaart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (closestRetailerLocation != null) {
                    mListener.showRetailerMap(closestRetailerLocation);
                }
                else {
                    mListener.handleError("No closest retailer location found!");
                }
            }
        });
    }

    public void loadRetailerInfo() {
        String[] retailerColumns = new String[] {
                DatabaseContract.RetailerColumns.COLUMN_SERVER_ID,
                DatabaseContract.RetailerColumns.COLUMN_RETAILER_CATEGORY_ID,
                DatabaseContract.RetailerColumns.COLUMN_RETAILER_NAME,
                DatabaseContract.RetailerColumns.COLUMN_TAGLINE,
                DatabaseContract.RetailerColumns.COLUMN_CHAIN,
                DatabaseContract.RetailerColumns.COLUMN_LOGOURL,
                DatabaseContract.RetailerColumns.COLUMN_UPDATED_TIMESTAMP,
                DatabaseContract.RetailerColumns.COLUMN_LOYALTYPOINT
        };

        Cursor retailerData = mContext.getContentResolver().query(RETAILERS_ITEM_URI, retailerColumns,
                DatabaseContract.RetailerColumns.COLUMN_SERVER_ID + "=?", new String[] { Integer.toString(mRetailerId) }, null);

        if (retailerData != null){
            Retailer retailer = null;
            while (retailerData.moveToNext()){
                try {
                    retailer = new Retailer(
                            retailerData.getInt(retailerData.getColumnIndex(DatabaseContract.RetailerColumns.COLUMN_SERVER_ID)),
                            retailerData.getInt(retailerData.getColumnIndex(DatabaseContract.RetailerColumns.COLUMN_RETAILER_CATEGORY_ID)),
                            retailerData.getString(retailerData.getColumnIndex(DatabaseContract.RetailerColumns.COLUMN_RETAILER_NAME)),
                            retailerData.getString(retailerData.getColumnIndex(DatabaseContract.RetailerColumns.COLUMN_TAGLINE)),
                            retailerData.getInt(retailerData.getColumnIndex(DatabaseContract.RetailerColumns.COLUMN_CHAIN)) > 0,
                            retailerData.getString(retailerData.getColumnIndex(DatabaseContract.RetailerColumns.COLUMN_LOGOURL)),
                            TimestampHelper.convertStringToDate(retailerData.getString(retailerData.getColumnIndex(DatabaseContract.RetailerColumns.COLUMN_UPDATED_TIMESTAMP))),
                            retailerData.getInt(retailerData.getColumnIndex(DatabaseContract.RetailerColumns.COLUMN_LOYALTYPOINT))
                    );
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
            }
            mBinding.setRetailer(retailer);
            notifyPropertyChanged(BR.retailer);

            retailerData.close();

            updateRetailerLocationsInfo(retailer.getId());

            // Refresh de cached retailer locations voor deze retailer
            SyncHelper.refreshCachedRetailerLocations(mContext, mRetailerId);

            mListener.showRefreshingIndicator(true);
        }
        else {
            mBinding.setRetailer(null);
            notifyPropertyChanged(BR.retailer);
        }
    }

    public void updateRetailerLocationsInfo(int retailerId) {
        String[] retailerLocationsColumns = new String[] {
                DatabaseContract.RetailerLocationsColumns.COLUMN_SERVER_ID,
                DatabaseContract.RetailerLocationsColumns.COLUMN_RETAILER_ID,
                DatabaseContract.RetailerLocationsColumns.COLUMN_NAME,
                DatabaseContract.RetailerLocationsColumns.COLUMN_STREET,
                DatabaseContract.RetailerLocationsColumns.COLUMN_NUMBER,
                DatabaseContract.RetailerLocationsColumns.COLUMN_ZIPCODE,
                DatabaseContract.RetailerLocationsColumns.COLUMN_CITY,
                DatabaseContract.RetailerLocationsColumns.COLUMN_COUNTRY,
                DatabaseContract.RetailerLocationsColumns.COLUMN_UPDATED_TIMESTAMP
        };

        Cursor retailerLocationsData = mContext.getContentResolver().query(RETAILER_LOCATIONS_ITEM_URI, retailerLocationsColumns,
                DatabaseContract.RetailerLocationsColumns.COLUMN_RETAILER_ID + "=?", new String[] { Integer.toString(retailerId) }, null);

        if (retailerLocationsData != null) {
            final ArrayList<RetailerLocation> retailerLocations = new ObservableArrayList<>();

            while (retailerLocationsData.moveToNext()) {
                try {
                    RetailerLocation retailerLocation = new RetailerLocation(
                            retailerLocationsData.getInt(retailerLocationsData.getColumnIndex(DatabaseContract.RetailerLocationsColumns.COLUMN_RETAILER_ID)),
                            retailerLocationsData.getString(retailerLocationsData.getColumnIndex(DatabaseContract.RetailerLocationsColumns.COLUMN_NAME)),
                            retailerLocationsData.getString(retailerLocationsData.getColumnIndex(DatabaseContract.RetailerLocationsColumns.COLUMN_STREET)),
                            retailerLocationsData.getString(retailerLocationsData.getColumnIndex(DatabaseContract.RetailerLocationsColumns.COLUMN_NUMBER)),
                            retailerLocationsData.getInt(retailerLocationsData.getColumnIndex(DatabaseContract.RetailerLocationsColumns.COLUMN_ZIPCODE)),
                            retailerLocationsData.getString(retailerLocationsData.getColumnIndex(DatabaseContract.RetailerLocationsColumns.COLUMN_CITY)),
                            retailerLocationsData.getString(retailerLocationsData.getColumnIndex(DatabaseContract.RetailerLocationsColumns.COLUMN_COUNTRY)),
                            TimestampHelper.convertStringToDate(retailerLocationsData.getString(retailerLocationsData.getColumnIndex(DatabaseContract.RetailerLocationsColumns.COLUMN_UPDATED_TIMESTAMP)))
                    );
                    retailerLocations.add(retailerLocation);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
            }

            if (retailerLocations.size() > 0) {
                this.retailerLocations = retailerLocations;
                this.closestRetailerLocation = retailerLocations.get(0);

                mBinding.setClosestRetailerLocation(this.closestRetailerLocation); // todo: dichtste locatie pakken
                notifyPropertyChanged(BR.closestRetailerLocation);
            }

            retailerLocationsData.close();
        }
    }
}
