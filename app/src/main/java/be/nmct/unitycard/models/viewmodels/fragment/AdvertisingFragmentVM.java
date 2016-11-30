package be.nmct.unitycard.models.viewmodels.fragment;

import android.content.Context;
import android.database.Cursor;
import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import java.text.ParseException;

import be.nmct.unitycard.BR;
import be.nmct.unitycard.contracts.DatabaseContract;
import be.nmct.unitycard.databinding.FragmentAdvertisingBinding;
import be.nmct.unitycard.helpers.TimestampHelper;
import be.nmct.unitycard.models.Offer;

import static be.nmct.unitycard.contracts.ContentProviderContract.OFFERS_URI;

/**
 * Created by Stephen on 9/11/2016.
 */

public class AdvertisingFragmentVM extends BaseObservable {

    private FragmentAdvertisingBinding mBinding;
    private Context mContext;

    public AdvertisingFragmentVM(FragmentAdvertisingBinding binding, Context context) {
        this.mBinding = binding;
        this.mContext = context;

        loadOffers();

        mBinding.setViewmodel(this);
    }

    private void loadOffers(){
        String[] columns = new String[]{
                DatabaseContract.OffersColumns.COLUMN_RETAILER_ID,
                DatabaseContract.OffersColumns.COLUMN_OFFER_DEMAND,
                DatabaseContract.OffersColumns.COLUMN_OFFER_RECEIVE,
                DatabaseContract.OffersColumns.COLUMN_CREATED_TIMESTAMP,
                DatabaseContract.OffersColumns.COLUMN_UPDATED_TIMESTAMP
        };

        Cursor data = mContext.getContentResolver().query(OFFERS_URI, columns, null, null, null);
        if(data != null){
            ObservableList<Offer> offersList = new ObservableArrayList<>();
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
                offersList.add(offer);
            }
            mBinding.setOfferList(offersList);
            notifyPropertyChanged(BR.offersList);

            data.close();
        }
        else {
            mBinding.setOfferList(null);
            notifyPropertyChanged(BR.offersList);
        }
    }
}
