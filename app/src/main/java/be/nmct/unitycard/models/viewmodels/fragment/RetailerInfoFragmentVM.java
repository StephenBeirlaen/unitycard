package be.nmct.unitycard.models.viewmodels.fragment;

import android.content.Context;
import android.database.Cursor;
import android.databinding.BaseObservable;
import android.net.ParseException;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import be.nmct.unitycard.BR;
import be.nmct.unitycard.contracts.DatabaseContract;
import be.nmct.unitycard.databinding.FragmentRetailerInfoBinding;
import be.nmct.unitycard.fragments.customer.RetailerInfoFragment;
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

    public RetailerInfoFragmentVM(FragmentRetailerInfoBinding binding, Context context, RetailerInfoFragment.RetailerInfoFragmentListener listener, Integer retailerId) {
        this.mBinding = binding;
        this.mListener = listener;
        this.mContext = context;

        loadRetailer(retailerId);

        mBinding.setViewmodel(this);
    }

    public void loadRetailer(int retailerId){
        String[] retailerColumns = new String[]{
                DatabaseContract.RetailerColumns.COLUMN_SERVER_ID,
                DatabaseContract.RetailerColumns.COLUMN_RETAILER_CATEGORY_ID,
                DatabaseContract.RetailerColumns.COLUMN_RETAILER_NAME,
                DatabaseContract.RetailerColumns.COLUMN_TAGLINE,
                DatabaseContract.RetailerColumns.COLUMN_CHAIN,
                DatabaseContract.RetailerColumns.COLUMN_LOGOURL,
                DatabaseContract.RetailerColumns.COLUMN_UPDATED_TIMESTAMP
        };

        Cursor retailerData = mContext.getContentResolver().query(RETAILERS_ITEM_URI, retailerColumns,
                DatabaseContract.RetailerColumns.COLUMN_SERVER_ID + "=?", new String[] { Integer.toString(retailerId) }, null);

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
                            TimestampHelper.convertStringToDate(retailerData.getString(retailerData.getColumnIndex(DatabaseContract.RetailerColumns.COLUMN_UPDATED_TIMESTAMP)))
                    );
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
            }
            mBinding.setRetailer(retailer);
            notifyPropertyChanged(BR.retailer);

            retailerData.close();

            String[] retailerLocationsColumns = new String[]{
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
                final List<RetailerLocation> retailerLocations = new ArrayList<>();

                while (retailerLocationsData.moveToNext()) {
                    try {
                        RetailerLocation retailerLocation = new RetailerLocation(
                                retailerData.getInt(retailerData.getColumnIndex(DatabaseContract.RetailerLocationsColumns.COLUMN_RETAILER_ID)),
                                retailerData.getString(retailerData.getColumnIndex(DatabaseContract.RetailerLocationsColumns.COLUMN_NAME)),
                                retailerData.getString(retailerData.getColumnIndex(DatabaseContract.RetailerLocationsColumns.COLUMN_STREET)),
                                retailerData.getString(retailerData.getColumnIndex(DatabaseContract.RetailerLocationsColumns.COLUMN_NUMBER)),
                                retailerData.getInt(retailerData.getColumnIndex(DatabaseContract.RetailerLocationsColumns.COLUMN_ZIPCODE)),
                                retailerData.getString(retailerData.getColumnIndex(DatabaseContract.RetailerLocationsColumns.COLUMN_CITY)),
                                retailerData.getString(retailerData.getColumnIndex(DatabaseContract.RetailerLocationsColumns.COLUMN_COUNTRY)),
                                TimestampHelper.convertStringToDate(retailerData.getString(retailerData.getColumnIndex(DatabaseContract.RetailerLocationsColumns.COLUMN_UPDATED_TIMESTAMP)))
                        );
                        retailerLocations.add(retailerLocation);
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }
                }

                final Retailer finalRetailer = retailer;

                mBinding.buttonOpenKaart.setEnabled(true);
                mBinding.buttonOpenKaart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (retailerLocations != null && retailerLocations.size() > 0) {
                            RetailerLocation loc = retailerLocations.get(0);
                            if (loc != null) {
                                String address = loc.getStreet() + " "
                                        + loc.getNumber() + " "
                                        + loc.getZipcode() + " "
                                        + loc.getCity() + " "
                                        + loc.getCountry();
                                mListener.showRetailerMap(finalRetailer.getName(), address); // todo: dichtste locatie pakken
                            }
                            else {
                                mListener.handleError("No retailer location found!");
                            }
                        }
                        else {
                            mListener.handleError("No retailer locations found!");
                        }
                    }
                });

                retailerData.close();
            }
        }
        else {
            mBinding.setRetailer(null);
            notifyPropertyChanged(BR.retailer);
        }

    }
}
