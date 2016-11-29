package be.nmct.unitycard.binders;

import android.databinding.BindingAdapter;
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;

import be.nmct.unitycard.adapters.RetailerOffersRecyclerViewAdapter;
import be.nmct.unitycard.models.Offer;

/**
 * Created by lorenzvercoutere on 28/11/16.
 */

public class RetailerOffersBinder {
    @BindingAdapter("offersRetailer")
    public static void setItems(RecyclerView recyclerView, ObservableList<Offer> offers) {
        if (offers != null) {
            RetailerOffersRecyclerViewAdapter adapter = new RetailerOffersRecyclerViewAdapter(recyclerView.getContext(), offers);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}
