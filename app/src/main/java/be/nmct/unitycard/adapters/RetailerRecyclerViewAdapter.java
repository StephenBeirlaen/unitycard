package be.nmct.unitycard.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.nmct.unitycard.R;
import be.nmct.unitycard.databinding.RowAddRetailerBinding;
import be.nmct.unitycard.databinding.RowRetailerBinding;
import be.nmct.unitycard.models.Retailer;

/**
 * Created by lorenzvercoutere on 22/11/16.
 */

public class RetailerRecyclerViewAdapter
        extends RecyclerView.Adapter<RetailerRecyclerViewAdapter.RetailerViewHolder> {

    private ObservableList<Retailer> mRetailers;
    private Context mContext;

    public RetailerRecyclerViewAdapter(Context context, ObservableList<Retailer> retailers){
        this.mContext = context;
        this.mRetailers = retailers;
    }

    class RetailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final RowRetailerBinding binding;

        public RetailerViewHolder(RowRetailerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // // TODO: Click on Retailer
        }

        public RowRetailerBinding getBinding() {
            return binding;
        }
    }

    @Override
    public RetailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowRetailerBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_retailer, parent, false);
        RetailerViewHolder retailerViewHolder = new RetailerViewHolder(binding);
        return retailerViewHolder;
    }

    @Override
    public void onBindViewHolder(RetailerViewHolder holder, int position) {
        Retailer retailer = mRetailers.get(position);
        holder.getBinding().setRetailer(retailer);
    }

    @Override
    public int getItemCount() {
        return mRetailers.size();
    }
}
