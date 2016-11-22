package be.nmct.unitycard.binders;

import android.databinding.BindingAdapter;
import android.databinding.ObservableList;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import be.nmct.unitycard.adapters.RetailerRecyclerViewAdapter;
import be.nmct.unitycard.models.Retailer;

/**
 * Created by lorenzvercoutere on 22/11/16.
 */

public class RetailerFragmentBinder {
    @BindingAdapter("items")
    public static void setItems(RecyclerView recyclerView, ObservableList<Retailer> retailers){
        if(retailers != null){
            RetailerRecyclerViewAdapter adapter = new RetailerRecyclerViewAdapter(recyclerView.getContext(), retailers);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @BindingAdapter("app:imageUrl")
    public static void setImage(ImageView view, String logoUrl){
        if (!TextUtils.isEmpty(logoUrl)){
            Picasso.with(view.getContext())
                    .load(logoUrl)
                    .into(view);
        }
    }

}
