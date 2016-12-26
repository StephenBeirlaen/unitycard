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
import android.util.Log;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import be.nmct.unitycard.BR;
import be.nmct.unitycard.adapters.SyncAdapter;
import be.nmct.unitycard.auth.AuthHelper;
import be.nmct.unitycard.contracts.AccountContract;
import be.nmct.unitycard.contracts.DatabaseContract;
import be.nmct.unitycard.databinding.FragmentRetailerAdminAddRetailerBinding;
import be.nmct.unitycard.filters.FilterCursorWrapper;
import be.nmct.unitycard.fragments.retailer.RetailerAdminAddRetailerFragment;
import be.nmct.unitycard.helpers.TimestampHelper;
import be.nmct.unitycard.models.Retailer;
import be.nmct.unitycard.repositories.ApiRepository;

import static be.nmct.unitycard.contracts.ContentProviderContract.RETAILERS_URI;

/**
 * Created by lorenzvercoutere on 25/12/16.
 */

public class RetailerAdminAddRetailerFragmentVM extends BaseObservable implements FilterCursorWrapper.OnCursorFilterer {

    private FragmentRetailerAdminAddRetailerBinding mBinding;
    private Context mContext;
    private ApiRepository apiRepository;
    private List<Retailer> lijst = null;

    @Bindable
    private ObservableList<Retailer> retailerList;

    public String mSearchQuery = "";

    public RetailerAdminAddRetailerFragmentVM(FragmentRetailerAdminAddRetailerBinding binding, Context context){
        this.mBinding = binding;
        this.mContext = context;

        apiRepository = new ApiRepository(mContext);

        mBinding.setViewmodel(this);

        AuthHelper.getAccessToken(AuthHelper.getUser(mContext), mContext, new AuthHelper.GetAccessTokenListener() {
            @Override
            public void tokenReceived(String accessToken) {
                Date lastRetailersSyncTimestamp = new Date(0);
                /*try {
                    lastRetailersSyncTimestamp = AuthHelper.getLastSyncTimestamp(mContext, AuthHelper.getUser(mContext), AccountContract.KEY_LAST_SYNC_TIMESTAMP_RETAILERS);
                } catch (ParseException e){
                    Log.d("Error", "Timestamp");
                }*/

                apiRepository.getAllRetailers(accessToken, lastRetailersSyncTimestamp, new ApiRepository.GetResultListener<List<Retailer>>() {
                    @Override
                    public void resultReceived(List<Retailer> result) {
                        lijst = result;
                        loadRetailers();
                    }

                    @Override
                    public void requestError(String error) {
                        Log.d("","");
                    }
                });
            }

            @Override
            public void requestNewLogin() {

            }
        });
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
        retailerList = new ObservableArrayList<>();
        for (Retailer retailer : lijst){

            retailerList.add(retailer);
        }
        mBinding.setRetailerList(retailerList);
        notifyPropertyChanged(BR.retailerList);
    }

    public void updateRecyclerView() {

        retailerList = new ObservableArrayList<>();
        for (Retailer retailer : lijst){

            retailerList.add(retailer);
        }
        mBinding.setRetailerList(retailerList);
        notifyPropertyChanged(BR.retailerList);
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
