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
import be.nmct.unitycard.models.Retailer;

/**
 * Created by Stephen on 6/11/2016.
 */

public class AddRetailerRecyclerViewAdapter
        extends RecyclerView.Adapter<AddRetailerRecyclerViewAdapter.AddRetailerViewHolder> {

    private ObservableList<Retailer> mRetailers;
    private Context mContext;

    public AddRetailerRecyclerViewAdapter(Context context, ObservableList<Retailer> retailers) {
        this.mContext = context;
        this.mRetailers = retailers;
    }

    class AddRetailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final RowAddRetailerBinding binding;

        public AddRetailerViewHolder(RowAddRetailerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // todo
        }

        private RowAddRetailerBinding getBinding() {
            return binding;
        }
    }

    @Override
    public AddRetailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowAddRetailerBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_add_retailer, parent, false);
        AddRetailerViewHolder addRetailerViewHolder = new AddRetailerViewHolder(binding);
        return addRetailerViewHolder;
    }

    @Override
    public void onBindViewHolder(AddRetailerViewHolder holder, int position) {
        Retailer retailer = mRetailers.get(position);
        holder.getBinding().setRetailer(retailer);
    }

    @Override
    public int getItemCount() {
        return mRetailers.size();
    }
}
