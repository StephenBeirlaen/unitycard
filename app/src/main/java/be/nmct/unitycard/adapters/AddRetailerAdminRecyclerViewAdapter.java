package be.nmct.unitycard.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.databinding.tool.Binding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.nmct.unitycard.R;
import be.nmct.unitycard.databinding.RowRetailerAdminAddRetailerBinding;
import be.nmct.unitycard.fragments.retailer.RetailerAdminFragment;
import be.nmct.unitycard.models.Retailer;

/**
 * Created by lorenzvercoutere on 25/12/16.
 */

public class AddRetailerAdminRecyclerViewAdapter extends RecyclerView.Adapter<AddRetailerAdminRecyclerViewAdapter.AddRetailerAdminViewHolder> {

    private ObservableList<Retailer> mRetailers;
    private Context mContext;

    public AddRetailerAdminRecyclerViewAdapter(Context context, ObservableList<Retailer> retailers){
        this.mContext = context;
        this.mRetailers = retailers;
    }

    class AddRetailerAdminViewHolder extends RecyclerView.ViewHolder{

        final RowRetailerAdminAddRetailerBinding binding;

        public AddRetailerAdminViewHolder(final RowRetailerAdminAddRetailerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.btnAddRetailer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Retailer retailer = binding.getRetailer();
                    RetailerAdminFragment.retailer = retailer;
                }
            });
        }

        public RowRetailerAdminAddRetailerBinding getBinding() {
            return binding;
        }
    }

    @Override
    public AddRetailerAdminViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowRetailerAdminAddRetailerBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_retailer_admin_add_retailer, parent, false);
        AddRetailerAdminViewHolder addRetailerAdminViewHolder = new AddRetailerAdminViewHolder(binding);
        return addRetailerAdminViewHolder;
    }

    @Override
    public void onBindViewHolder(AddRetailerAdminViewHolder holder, int position) {
        Retailer retailer = mRetailers.get(position);
        holder.getBinding().setRetailer(retailer);
    }

    @Override
    public int getItemCount() {
        return mRetailers.size();
    }


}
