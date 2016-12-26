package be.nmct.unitycard.binders;

import android.databinding.BindingAdapter;
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import be.nmct.unitycard.adapters.AddRetailerAdminRecyclerViewAdapter;
import be.nmct.unitycard.models.Retailer;

/**
 * Created by lorenzvercoutere on 25/12/16.
 */

public class AddRetailerAdminBinder {
    @BindingAdapter("itemsAddRetailerAdmin")
    public static void setItems(RecyclerView recyclerView, ObservableList<Retailer> retailers){
        if(retailers != null){
            AddRetailerAdminRecyclerViewAdapter adapter= new AddRetailerAdminRecyclerViewAdapter(recyclerView.getContext(), retailers);
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
