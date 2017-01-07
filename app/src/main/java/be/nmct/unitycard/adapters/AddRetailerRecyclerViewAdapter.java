package be.nmct.unitycard.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.nmct.unitycard.R;
import be.nmct.unitycard.auth.AuthHelper;
import be.nmct.unitycard.databinding.RowAddRetailerBinding;
import be.nmct.unitycard.helpers.SyncHelper;
import be.nmct.unitycard.models.Retailer;
import be.nmct.unitycard.models.postmodels.AddLoyaltyCardRetailerBody;
import be.nmct.unitycard.repositories.ApiRepository;

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

    class AddRetailerViewHolder extends RecyclerView.ViewHolder {
        final RowAddRetailerBinding binding;

        public AddRetailerViewHolder(final RowAddRetailerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.btnAddRetailer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    final AddLoyaltyCardRetailerBody addLoyaltyCardRetailerBody = new AddLoyaltyCardRetailerBody(binding.getRetailer().getId());
                    final ApiRepository apiRepository = new ApiRepository(mContext);
                    AuthHelper.getAccessToken(AuthHelper.getUser(mContext), mContext, new AuthHelper.GetAccessTokenListener() {
                        @Override
                        public void tokenReceived(String accessToken) {
                            apiRepository.addLoyaltyCardRetailer(accessToken, AuthHelper.getUserId(mContext), addLoyaltyCardRetailerBody, new ApiRepository.GetResultListener<Void>() {
                                @Override
                                public void resultReceived(Void result) {
                                    //SyncHelper.refreshCachedData(mContext); // we kiezen ervoor om te syncen tijdens pijltje terug
                                    Snackbar.make(view, R.string.added_retailer_to_card, Snackbar.LENGTH_LONG).show();
                                }

                                @Override
                                public void requestError(String error) {

                                }
                            });
                        }

                        @Override
                        public void requestNewLogin() {

                        }
                    });
                }
            });
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
