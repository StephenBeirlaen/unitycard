package be.nmct.unitycard.models.viewmodels;

import android.content.Context;
import android.database.Cursor;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import be.nmct.unitycard.BR;
import be.nmct.unitycard.contracts.DatabaseContract;
import be.nmct.unitycard.databinding.FragmentAddRetailerBinding;
import be.nmct.unitycard.models.Retailer;

import static be.nmct.unitycard.contracts.ContentProviderContract.RETAILERS_URI;

/**
 * Created by Stephen on 8/11/2016.
 */

public class AddRetailerFragmentVM extends BaseObservable {

    private FragmentAddRetailerBinding mFragmentAddRetailerBinding;
    private Context mContext;

    @Bindable
    private ObservableList<Retailer> retailerList;

    public AddRetailerFragmentVM(FragmentAddRetailerBinding fragmentAddRetailerBinding, Context context) {
        this.mFragmentAddRetailerBinding = fragmentAddRetailerBinding;
        this.mContext = context;

        mFragmentAddRetailerBinding.setViewmodel(this);
    }

    public void loadRetailers() {
        String[] columns = new String[] {
                DatabaseContract.RetailerColumns.COLUMN_ID,
                DatabaseContract.RetailerColumns.COLUMN_RETAILER_CATEGORY_ID,
                DatabaseContract.RetailerColumns.COLUMN_RETAILER_NAME,
                DatabaseContract.RetailerColumns.COLUMN_TAGLINE,
                DatabaseContract.RetailerColumns.COLUMN_CHAIN,
                DatabaseContract.RetailerColumns.COLUMN_LOGOURL
        };

        Cursor data = mContext.getContentResolver().query(RETAILERS_URI, columns, null, null, null);

        retailerList = new ObservableArrayList<>();
        while (data.moveToNext()) {
            Retailer retailer = new Retailer(
                    data.getInt(data.getColumnIndex(DatabaseContract.RetailerColumns.COLUMN_ID)),
                    data.getInt(data.getColumnIndex(DatabaseContract.RetailerColumns.COLUMN_RETAILER_CATEGORY_ID)),
                    data.getString(data.getColumnIndex(DatabaseContract.RetailerColumns.COLUMN_RETAILER_NAME)),
                    data.getString(data.getColumnIndex(DatabaseContract.RetailerColumns.COLUMN_TAGLINE)),
                    data.getInt(data.getColumnIndex(DatabaseContract.RetailerColumns.COLUMN_CHAIN)) > 0,
                    data.getString(data.getColumnIndex(DatabaseContract.RetailerColumns.COLUMN_LOGOURL))
            );
            retailerList.add(retailer);
        }
        this.mFragmentAddRetailerBinding.setRetailerList(retailerList);
        notifyPropertyChanged(BR.retailerList);

        data.close();
    }
}
