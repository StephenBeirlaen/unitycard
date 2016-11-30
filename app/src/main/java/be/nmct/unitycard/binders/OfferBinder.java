package be.nmct.unitycard.binders;

import android.databinding.BindingAdapter;
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import be.nmct.unitycard.adapters.OffersRecyclerViewAdapter;
import be.nmct.unitycard.models.Offer;

/**
 * Created by lorenzvercoutere on 30/11/16.
 */

public class OfferBinder {
    @BindingAdapter("offers")
    public static void setItems(RecyclerView recyclerView, ObservableList<Offer> offers) {
        if(offers != null) {
            OffersRecyclerViewAdapter adapter = new OffersRecyclerViewAdapter(recyclerView.getContext(), offers);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    /*@BindingAdapter("app:imageUrl")
    public static void setImage(ImageView view, String logoUrl) {
        if (!TextUtils.isEmpty(logoUrl)) {
            Picasso.with(view.getContext())
                    .load(logoUrl)
                    .into(view);
        }
    }*/

}
