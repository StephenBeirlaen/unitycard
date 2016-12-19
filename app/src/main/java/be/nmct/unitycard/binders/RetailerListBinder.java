package be.nmct.unitycard.binders;

import android.databinding.BindingAdapter;
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import be.nmct.unitycard.adapters.RetailerRecyclerViewAdapter;
import be.nmct.unitycard.models.Retailer;
import be.nmct.unitycard.models.viewmodels.RetailerLoyaltyPointVM;

/**
 * Created by lorenzvercoutere on 22/11/16.
 */

public class RetailerListBinder {
    @BindingAdapter("itemsRetailerLoyaltyPointVM")
    public static void setItems(RecyclerView recyclerView, ObservableList<RetailerLoyaltyPointVM> RetailerLoyaltyPointVM) {
        if (RetailerLoyaltyPointVM != null) {
            RetailerRecyclerViewAdapter adapter = new RetailerRecyclerViewAdapter(recyclerView.getContext(), RetailerLoyaltyPointVM);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @BindingAdapter("app:imageUrl")
    public static void setImage(ImageView view, String logoUrl) {
        if (!TextUtils.isEmpty(logoUrl)) {
            Picasso.with(view.getContext())
                    .load(logoUrl)
                    .into(view);
        }
    }
}
