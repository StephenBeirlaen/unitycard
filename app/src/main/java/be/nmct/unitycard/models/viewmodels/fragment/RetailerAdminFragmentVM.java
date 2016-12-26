package be.nmct.unitycard.models.viewmodels.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;

import com.google.zxing.integration.android.IntentIntegrator;

import be.nmct.unitycard.activities.retailer.RetailerAdminAddRetailerActivity;
import be.nmct.unitycard.auth.AuthHelper;
import be.nmct.unitycard.databinding.FragmentRetailerAdminBinding;
import be.nmct.unitycard.fragments.retailer.RetailerAdminFragment;
import be.nmct.unitycard.models.postmodels.PushAdvertisementNotificationBody;
import be.nmct.unitycard.repositories.ApiRepository;

/**
 * Created by Stephen on 9/11/2016.
 */

public class RetailerAdminFragmentVM extends BaseObservable {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private FragmentRetailerAdminBinding mBinding;
    private RetailerAdminFragment.RetailerAdminFragmentListener mListener;
    private Context mContext;

    public RetailerAdminFragmentVM(final FragmentRetailerAdminBinding binding, final Context context, RetailerAdminFragment.RetailerAdminFragmentListener listener) {
        this.mBinding = binding;
        this.mContext = context;
        this.mListener = listener;

        mBinding.setViewmodel(this);

        mBinding.btnChooseRetailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, RetailerAdminAddRetailerActivity.class);
                mContext.startActivity(intent);
            }
        });

        mBinding.btnScanLoyaltycard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Scanning via intent
                // https://github.com/zxing/zxing/wiki/Scanning-Via-Intent
                IntentIntegrator integrator = new IntentIntegrator((Activity) mContext);
                integrator.initiateScan();
            }
        });

        mBinding.btnAwardLoyaltypoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.txtLoyaltypointCount.setText("");
            }
        });

        mBinding.btnSendAdvertisement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mBinding.txtAdvertisementDescription.getText().toString();
                final PushAdvertisementNotificationBody body =
                        new PushAdvertisementNotificationBody(title);

                AuthHelper.getAccessToken(AuthHelper.getUser(mContext), mContext, new AuthHelper.GetAccessTokenListener() {
                    @Override
                    public void tokenReceived(final String accessToken) {
                        Log.d(LOG_TAG, "Using access token: " + accessToken);

                        final ApiRepository apiRepo = new ApiRepository(mContext);

                        apiRepo.pushAdvertisementNotification(accessToken, 2, body, // todo: momenteel nog hard coded retailerid
                                new ApiRepository.GetResultListener<Void>() {
                                    @Override
                                    public void resultReceived(Void result) {

                                    }

                                    @Override
                                    public void requestError(String error) {

                                    }
                                }
                        );
                    }

                    @Override
                    public void requestNewLogin() {
                        mListener.requestLogOut();
                    }
                });

                mBinding.txtAdvertisementDescription.setText("");

            }
        });

        mBinding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.requestLogOut();
            }
        });
    }


}
