package be.nmct.unitycard.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.nmct.unitycard.R;
import be.nmct.unitycard.databinding.RowAdvertisingBinding;
import be.nmct.unitycard.models.Offer;

/**
 * Created by lorenzvercoutere on 30/11/16.
 */

public class OffersRecyclerViewAdapter extends RecyclerView.Adapter<OffersRecyclerViewAdapter.OfferViewHolder> {

    private ObservableList<Offer> mOffers;
    private Context mContext;

    public OffersRecyclerViewAdapter(Context context, ObservableList<Offer> offers){
        this.mContext = context;
        this.mOffers = offers;

    }

    class OfferViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final RowAdvertisingBinding binding;

        public OfferViewHolder(RowAdvertisingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }

        public RowAdvertisingBinding getBinding(){
            return binding;
        }
    }

    @Override
    public OfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowAdvertisingBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_advertising, parent, false);
        OfferViewHolder offerViewHolder = new OfferViewHolder(binding);
        return offerViewHolder;
    }

    @Override
    public void onBindViewHolder(OfferViewHolder holder, int position) {
        Offer offer = mOffers.get(position);
        holder.getBinding().setOffer(offer);
    }

    @Override
    public int getItemCount() {
        return mOffers.size();
    }



}
