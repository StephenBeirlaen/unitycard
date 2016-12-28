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
import be.nmct.unitycard.filters.FilterCursorWrapper;
import be.nmct.unitycard.helpers.TimestampHelper;
import be.nmct.unitycard.models.Retailer;
import be.nmct.unitycard.models.viewmodels.RetailerLoyaltyPointVM;

import static be.nmct.unitycard.contracts.ContentProviderContract.ADDED_RETAILERS_URI;
import static be.nmct.unitycard.contracts.ContentProviderContract.LOYALTYPOINTS_ITEM_URI;

/**
 * Created by Stephen on 9/11/2016.
 */

public class RetailerListFragmentVM extends BaseObservable
        implements FilterCursorWrapper.OnCursorFilterer {

    private FragmentRetailerListBinding mBinding;
    private Context mContext;

    @Bindable
    private ObservableList<RetailerLoyaltyPointVM> addedRetailerLoyaltyPointVMList;

    private FilterCursorWrapper filterCursorWrapper;
    private FilterCursorWrapper filterCursorWrapperLoyaltyPoints;
    public String mSearchQuery = "";

    public RetailerListFragmentVM(FragmentRetailerListBinding binding, Context context) {
        this.mBinding = binding;
        this.mContext = context;

        mBinding.setViewmodel(this);

        loadAddedRetailers();
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
                loadAddedRetailers();
            }
        }
    }

    private void loadAddedRetailers() {
        String[] columns = new String[] {
                DatabaseContract.AddedRetailerColumns.COLUMN_SERVER_ID,
                DatabaseContract.AddedRetailerColumns.COLUMN_RETAILER_CATEGORY_ID,
                DatabaseContract.AddedRetailerColumns.COLUMN_RETAILER_NAME,
                DatabaseContract.AddedRetailerColumns.COLUMN_TAGLINE,
                DatabaseContract.AddedRetailerColumns.COLUMN_CHAIN,
                DatabaseContract.AddedRetailerColumns.COLUMN_LOGOURL,
                DatabaseContract.AddedRetailerColumns.COLUMN_UPDATED_TIMESTAMP,
                DatabaseContract.AddedRetailerColumns.COLUMN_LOYALTYPOINTS
        };

        Cursor data = mContext.getContentResolver().query(ADDED_RETAILERS_URI, columns, null, null, null);

        filterCursorWrapper = new FilterCursorWrapper(data, this);

        updateRecyclerView();
    }

    public void updateRecyclerView() {
        if (filterCursorWrapper != null) {
            filterCursorWrapper.filter();

            addedRetailerLoyaltyPointVMList = new ObservableArrayList<>();
            while (filterCursorWrapper.moveToNext()) {
                Retailer retailer = null;
                RetailerLoyaltyPointVM retailerLoyaltyPointVM = null;
                try {
                    retailer = new Retailer(
                            filterCursorWrapper.getInt(filterCursorWrapper.getColumnIndex(DatabaseContract.AddedRetailerColumns.COLUMN_SERVER_ID)),
                            filterCursorWrapper.getInt(filterCursorWrapper.getColumnIndex(DatabaseContract.AddedRetailerColumns.COLUMN_RETAILER_CATEGORY_ID)),
                            filterCursorWrapper.getString(filterCursorWrapper.getColumnIndex(DatabaseContract.AddedRetailerColumns.COLUMN_RETAILER_NAME)),
                            filterCursorWrapper.getString(filterCursorWrapper.getColumnIndex(DatabaseContract.AddedRetailerColumns.COLUMN_TAGLINE)),
                            filterCursorWrapper.getInt(filterCursorWrapper.getColumnIndex(DatabaseContract.AddedRetailerColumns.COLUMN_CHAIN)) > 0,
                            filterCursorWrapper.getString(filterCursorWrapper.getColumnIndex(DatabaseContract.AddedRetailerColumns.COLUMN_LOGOURL)),
                            TimestampHelper.convertStringToDate(filterCursorWrapper.getString(filterCursorWrapper.getColumnIndex(DatabaseContract.AddedRetailerColumns.COLUMN_UPDATED_TIMESTAMP)))
                    );

                    retailerLoyaltyPointVM = new RetailerLoyaltyPointVM(
                            retailer,
                            filterCursorWrapper.getInt(filterCursorWrapper.getColumnIndex(DatabaseContract.AddedRetailerColumns.COLUMN_LOYALTYPOINTS))
                    );
                } catch (ParseException e){
                    e.printStackTrace();
                }
                addedRetailerLoyaltyPointVMList.add(retailerLoyaltyPointVM);
            }
            mBinding.setRetailerLoyaltyPointVMList(addedRetailerLoyaltyPointVMList);
            notifyPropertyChanged(BR.retailerList);
        }
        else {
            mBinding.setRetailerLoyaltyPointVMList(null);
            notifyPropertyChanged(BR.retailerList);
        }
    }

    @Override
    public boolean retainsCurrent(Cursor cursor) {
        int colnr1 = cursor.getColumnIndex(DatabaseContract.RetailerColumns.COLUMN_RETAILER_NAME);
        String retailerName = cursor.getString(colnr1);

        // Filteren op mRetailer naam, non containing return false
        if (!mSearchQuery.equals("")) {
            if (!retailerName.toLowerCase().contains(mSearchQuery.toLowerCase())) return false;
        }

        return true;
    }
}
