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
import be.nmct.unitycard.databinding.FragmentAddRetailerBinding;
import be.nmct.unitycard.filters.FilterCursorWrapper;
import be.nmct.unitycard.helpers.TimestampHelper;
import be.nmct.unitycard.models.Retailer;

import static be.nmct.unitycard.contracts.ContentProviderContract.RETAILERS_URI;

/**
 * Created by Stephen on 8/11/2016.
 */

public class AddRetailerFragmentVM extends BaseObservable
        implements FilterCursorWrapper.OnCursorFilterer {

    private FragmentAddRetailerBinding mBinding;
    private Context mContext;

    @Bindable
    private ObservableList<Retailer> retailerList;

    private FilterCursorWrapper filterCursorWrapper;
    public String mSearchQuery = "";

    public AddRetailerFragmentVM(FragmentAddRetailerBinding binding, Context context) {
        this.mBinding = binding;
        this.mContext = context;

        mBinding.setViewmodel(this);

        loadRetailers();
    }

    public class MyContentObserver extends ContentObserver {
        public MyContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            if (uri.equals(RETAILERS_URI)) {
                loadRetailers();
            }
        }
    }

    private void loadRetailers() {
        String[] columns = new String[] {
                DatabaseContract.RetailerColumns.COLUMN_SERVER_ID,
                DatabaseContract.RetailerColumns.COLUMN_RETAILER_CATEGORY_ID,
                DatabaseContract.RetailerColumns.COLUMN_RETAILER_NAME,
                DatabaseContract.RetailerColumns.COLUMN_TAGLINE,
                DatabaseContract.RetailerColumns.COLUMN_CHAIN,
                DatabaseContract.RetailerColumns.COLUMN_LOGOURL,
                DatabaseContract.RetailerColumns.COLUMN_UPDATED_TIMESTAMP,
                DatabaseContract.RetailerColumns.COLUMN_LOYALTYPOINT
        };

        Cursor data = mContext.getContentResolver().query(RETAILERS_URI, columns, null, null, null);

        filterCursorWrapper = new FilterCursorWrapper(data, this);
        updateRecyclerView();
    }

    public void updateRecyclerView() {
        if (filterCursorWrapper != null) {
            filterCursorWrapper.filter();

            retailerList = new ObservableArrayList<>();
            while (filterCursorWrapper.moveToNext()) {
                Retailer retailer = null;
                try {
                    retailer = new Retailer(
                            filterCursorWrapper.getInt(filterCursorWrapper.getColumnIndex(DatabaseContract.RetailerColumns.COLUMN_SERVER_ID)),
                            filterCursorWrapper.getInt(filterCursorWrapper.getColumnIndex(DatabaseContract.RetailerColumns.COLUMN_RETAILER_CATEGORY_ID)),
                            filterCursorWrapper.getString(filterCursorWrapper.getColumnIndex(DatabaseContract.RetailerColumns.COLUMN_RETAILER_NAME)),
                            filterCursorWrapper.getString(filterCursorWrapper.getColumnIndex(DatabaseContract.RetailerColumns.COLUMN_TAGLINE)),
                            filterCursorWrapper.getInt(filterCursorWrapper.getColumnIndex(DatabaseContract.RetailerColumns.COLUMN_CHAIN)) > 0,
                            filterCursorWrapper.getString(filterCursorWrapper.getColumnIndex(DatabaseContract.RetailerColumns.COLUMN_LOGOURL)),
                            TimestampHelper.convertStringToDate(filterCursorWrapper.getString(filterCursorWrapper.getColumnIndex(DatabaseContract.RetailerColumns.COLUMN_UPDATED_TIMESTAMP))),
                            filterCursorWrapper.getInt(filterCursorWrapper.getColumnIndex(DatabaseContract.RetailerColumns.COLUMN_LOYALTYPOINT))
                    );
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                retailerList.add(retailer);
            }
            mBinding.setRetailerList(retailerList);
            notifyPropertyChanged(BR.retailerList);


        }
        else {
            mBinding.setRetailerList(null);
            notifyPropertyChanged(BR.retailerList);
        }
    }

    @Override
    public boolean retainsCurrent(Cursor cursor) {
        int colnr1 = cursor.getColumnIndex(DatabaseContract.RetailerColumns.COLUMN_RETAILER_NAME);
        String retailerName = cursor.getString(colnr1);

        // Filteren op retailer naam, non containing return false
        if (!mSearchQuery.equals("")) {
            if (!retailerName.toLowerCase().contains(mSearchQuery.toLowerCase())) return false;
        }

        return true;
    }
}
