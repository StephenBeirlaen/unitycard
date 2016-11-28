package be.nmct.unitycard.models.viewmodels.fragment;

import android.content.Context;
import android.database.Cursor;
import android.databinding.BaseObservable;
import android.net.ParseException;

import be.nmct.unitycard.BR;
import be.nmct.unitycard.contracts.DatabaseContract;
import be.nmct.unitycard.databinding.FragmentRetailerInfoBinding;
import be.nmct.unitycard.helpers.TimestampHelper;
import be.nmct.unitycard.models.Retailer;

import static be.nmct.unitycard.contracts.ContentProviderContract.RETAILERS_ITEM_URI;

/**
 * Created by Stephen on 9/11/2016.
 */

public class RetailerInfoFragmentVM extends BaseObservable {

    private FragmentRetailerInfoBinding mBinding;
    private Context mContext;

    public RetailerInfoFragmentVM(FragmentRetailerInfoBinding binding, Context context, Integer retailerId) {
        this.mBinding = binding;
        this.mContext = context;

        loadRetailer(retailerId);

        mBinding.setViewmodel(this);
    }

    public void loadRetailer(int retailerId){
        String[] columns = new String[]{
                DatabaseContract.RetailerColumns.COLUMN_SERVER_ID,
                DatabaseContract.RetailerColumns.COLUMN_RETAILER_CATEGORY_ID,
                DatabaseContract.RetailerColumns.COLUMN_RETAILER_NAME,
                DatabaseContract.RetailerColumns.COLUMN_TAGLINE,
                DatabaseContract.RetailerColumns.COLUMN_CHAIN,
                DatabaseContract.RetailerColumns.COLUMN_LOGOURL,
                DatabaseContract.RetailerColumns.COLUMN_UPDATED_TIMESTAMP
        };

        Cursor data = mContext.getContentResolver().query(RETAILERS_ITEM_URI, columns,
                DatabaseContract.RetailerColumns.COLUMN_SERVER_ID + "=?", new String[] { Integer.toString(retailerId) }, null);

        if (data != null){
            Retailer retailer = null;
            while (data.moveToNext()){
                try {
                    retailer = new Retailer(
                            data.getInt(data.getColumnIndex(DatabaseContract.RetailerColumns.COLUMN_SERVER_ID)),
                            data.getInt(data.getColumnIndex(DatabaseContract.RetailerColumns.COLUMN_RETAILER_CATEGORY_ID)),
                            data.getString(data.getColumnIndex(DatabaseContract.RetailerColumns.COLUMN_RETAILER_NAME)),
                            data.getString(data.getColumnIndex(DatabaseContract.RetailerColumns.COLUMN_TAGLINE)),
                            data.getInt(data.getColumnIndex(DatabaseContract.RetailerColumns.COLUMN_CHAIN)) > 0,
                            data.getString(data.getColumnIndex(DatabaseContract.RetailerColumns.COLUMN_LOGOURL)),
                            TimestampHelper.convertStringToDate(data.getString(data.getColumnIndex(DatabaseContract.RetailerColumns.COLUMN_UPDATED_TIMESTAMP)))
                    );
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
            }
            mBinding.setRetailer(retailer);
            notifyPropertyChanged(BR.retailer);

            data.close();
        }
        else {
            mBinding.setRetailer(null);
            notifyPropertyChanged(BR.retailer);
        }

    }
}
