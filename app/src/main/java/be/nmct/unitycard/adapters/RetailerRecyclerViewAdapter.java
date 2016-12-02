package be.nmct.unitycard.adapters;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.nmct.unitycard.R;
import be.nmct.unitycard.activities.customer.RetailerActivity;
import be.nmct.unitycard.databinding.RowRetailerBinding;
import be.nmct.unitycard.models.Retailer;

/**
 * Created by lorenzvercoutere on 22/11/16.
 */

public class RetailerRecyclerViewAdapter
        extends RecyclerView.Adapter<RetailerRecyclerViewAdapter.RetailerViewHolder> {

    private ObservableList<Retailer> mRetailers;
    private Context mContext;
    public final static String EXTRA_RETAILER_ID = "RetailerID";

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
            // TODO: Click on Retailer
            Intent retailerActivityIntent = new Intent(mContext, RetailerActivity.class);
            retailerActivityIntent.putExtra(EXTRA_RETAILER_ID, binding.getRetailer().getId());
            mContext.startActivity(retailerActivityIntent);
            //mListener.showRetailerInfo(binding.getRetailer());
            /*apiRepo = new ApiRepository(view.getContext());
            view.getContext();
            AuthHelper.getAccessToken(AuthHelper.getUser(view.getContext()), view.getContext(), new AuthHelper.GetAccessTokenListener() {
                @Override
                public void tokenReceived(String accessToken) {
                    apiRepo.getRetailer(accessToken, "2", new ApiRepository.GetResultListener<Retailer>() {
                        @Override
                        public void resultReceived(Retailer result) {
                            //TODO: ga naar retailerinfo en geef result mee
                            //kan niet rechtstreeks naar de fragment retailer info gaan

                        }

                        @Override
                        public void requestError(String error) {
                            Log.d("", error);
                        }
                    });
                }

                @Override
                public void requestNewLogin() {

                }
            });*/
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
