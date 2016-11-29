package be.nmct.unitycard.models.viewmodels.fragment;

import android.content.Context;
import android.database.Cursor;
import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import java.text.ParseException;

import be.nmct.unitycard.BR;
import be.nmct.unitycard.contracts.DatabaseContract;
import be.nmct.unitycard.databinding.FragmentRetailerOffersBinding;
import be.nmct.unitycard.helpers.TimestampHelper;
import be.nmct.unitycard.models.Offer;

import static be.nmct.unitycard.contracts.ContentProviderContract.OFFERS_URI;

/**
 * Created by Stephen on 9/11/2016.
 */

public class RetailerOffersFragmentVM extends BaseObservable {

    private FragmentRetailerOffersBinding mBinding;
    private Context mContext;

    public RetailerOffersFragmentVM(FragmentRetailerOffersBinding binding, Context context, Integer retailerId) {
        this.mBinding = binding;
        this.mContext = context;

        loadOffers(retailerId);

        mBinding.setViewmodel(this);
    }

    private void loadOffers(int retailerId){
        String[] columns = new String[]{
                DatabaseContract.OffersColumns.COLUMN_RETAILER_ID,
                DatabaseContract.OffersColumns.COLUMN_OFFER_DEMAND,
                DatabaseContract.OffersColumns.COLUMN_OFFER_RECEIVE,
                DatabaseContract.OffersColumns.COLUMN_CREATED_TIMESTAMP,
                DatabaseContract.OffersColumns.COLUMN_UPDATED_TIMESTAMP
        };

        Cursor data = mContext.getContentResolver().query(OFFERS_URI, columns,
                DatabaseContract.OffersColumns.COLUMN_RETAILER_ID + "=?", new String[] { Integer.toString(retailerId) }, null);

        if(data != null){
            ObservableList<Offer> offersRetailerList = new ObservableArrayList<>();
            while(data.moveToNext()){
                Offer offer = null;
                try{
                    offer = new Offer(
                            data.getInt(data.getColumnIndex(DatabaseContract.OffersColumns.COLUMN_RETAILER_ID)),
                            data.getString(data.getColumnIndex(DatabaseContract.OffersColumns.COLUMN_OFFER_DEMAND)),
                            data.getString(data.getColumnIndex(DatabaseContract.OffersColumns.COLUMN_OFFER_RECEIVE)),
                            TimestampHelper.convertStringToDate(data.getString(data.getColumnIndex(DatabaseContract.OffersColumns.COLUMN_CREATED_TIMESTAMP))),
                            TimestampHelper.convertStringToDate(data.getString(data.getColumnIndex(DatabaseContract.OffersColumns.COLUMN_UPDATED_TIMESTAMP)))
                    );
                } catch (ParseException e){
                    e.printStackTrace();
                }
                offersRetailerList.add(offer);
            }
            mBinding.setOffersList(offersRetailerList);
            notifyPropertyChanged(BR.offersList);

            data.close();
        }
        else {
            mBinding.setOffersList(null);
            notifyPropertyChanged(BR.offersList);
        }
    }
}
