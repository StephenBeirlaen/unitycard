package be.nmct.unitycard.models.viewmodels.fragment;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.net.Uri;
import android.os.Handler;

import java.text.ParseException;

import be.nmct.unitycard.BR;
import be.nmct.unitycard.contracts.DatabaseContract;
import be.nmct.unitycard.databinding.FragmentRetailerListBinding;
import be.nmct.unitycard.helpers.TimestampHelper;
import be.nmct.unitycard.models.Retailer;

import static be.nmct.unitycard.contracts.ContentProviderContract.ADDED_RETAILERS_URI;

/**
 * Created by Stephen on 9/11/2016.
 */

public class RetailerListFragmentVM extends BaseObservable {

    private FragmentRetailerListBinding mBinding;
    private Context mContext;

    @Bindable
    private ObservableList<Retailer> retailerList;

    public RetailerListFragmentVM(FragmentRetailerListBinding binding, Context context) {
        this.mBinding = binding;
        this.mContext = context;

        mBinding.setViewmodel(this);

        loadRetailers();
    }

    public class MyContentObserver extends ContentObserver{

        public MyContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            if (uri.equals(ADDED_RETAILERS_URI)){
                loadRetailers();
            }
        }
    }

    public void loadRetailers(){
        String[] columns = new String[]{
                DatabaseContract.RetailerColumns.COLUMN_SERVER_ID,
                DatabaseContract.RetailerColumns.COLUMN_RETAILER_CATEGORY_ID,
                DatabaseContract.RetailerColumns.COLUMN_RETAILER_NAME,
                DatabaseContract.RetailerColumns.COLUMN_TAGLINE,
                DatabaseContract.RetailerColumns.COLUMN_CHAIN,
                DatabaseContract.RetailerColumns.COLUMN_LOGOURL,
                DatabaseContract.RetailerColumns.COLUMN_UPDATED_TIMESTAMP
        };

        Cursor data = mContext.getContentResolver().query(ADDED_RETAILERS_URI, columns, null, null, null);

        if (data != null){
            retailerList = new ObservableArrayList<>();
            while (data.moveToNext()){
                Retailer retailer = null;
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
                } catch (ParseException e){
                    e.printStackTrace();
                }
                retailerList.add(retailer);
            }
            mBinding.setRetailerList(retailerList);
            notifyPropertyChanged(BR.retailerList);

            data.close();
        }
        else {
            mBinding.setRetailerList(null);
            notifyPropertyChanged(BR.retailerList);
        }

    }
}
