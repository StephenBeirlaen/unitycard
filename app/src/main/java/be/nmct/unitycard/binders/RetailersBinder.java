package be.nmct.unitycard.binders;

import android.databinding.BindingAdapter;
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;

import be.nmct.unitycard.adapters.AddRetailerRecyclerViewAdapter;
import be.nmct.unitycard.models.Retailer;

/**
 * Created by Stephen on 8/11/2016.
 */

public class RetailersBinder {
    @BindingAdapter("items")
    public static void setItems(RecyclerView recyclerView, ObservableList<Retailer> retailers) {
        if (retailers != null) {
            AddRetailerRecyclerViewAdapter adapter = new AddRetailerRecyclerViewAdapter(recyclerView.getContext(), retailers);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}
