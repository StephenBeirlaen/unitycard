package be.nmct.unitycard.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.nmct.unitycard.R;
import be.nmct.unitycard.databinding.RowRetailerOfferBinding;
import be.nmct.unitycard.models.Offer;

/**
 * Created by lorenzvercoutere on 28/11/16.
 */

public class RetailerOffersRecyclerViewAdapter extends RecyclerView.Adapter<RetailerOffersRecyclerViewAdapter.RetailerOfferViewHolder> {

    private ObservableList<Offer> mOffers;
    private Context mContext;

    public RetailerOffersRecyclerViewAdapter(Context context, ObservableList<Offer> offers){
        this.mContext = context;
        this.mOffers = offers;
    }

    class RetailerOfferViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final RowRetailerOfferBinding binding;

        public RetailerOfferViewHolder(RowRetailerOfferBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }

        public RowRetailerOfferBinding getBinding(){
            return binding;
        }
    }

    @Override
    public RetailerOfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowRetailerOfferBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_retailer_offer, parent, false);
        RetailerOfferViewHolder retailerOfferViewHolder = new RetailerOfferViewHolder(binding);
        return retailerOfferViewHolder;
    }

    @Override
    public void onBindViewHolder(RetailerOfferViewHolder holder, int position) {
        Offer offer = mOffers.get(position);
        holder.getBinding().setOffer(offer);
    }

    @Override
    public int getItemCount() {
        return mOffers.size();
    }
}
