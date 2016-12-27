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
import be.nmct.unitycard.models.Retailer;
import be.nmct.unitycard.models.postmodels.AwardLoyaltyPointsBody;
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
    private ApiRepository apiRepository;
    public Retailer retailer;
    public int mLoyaltyCardId = 0;

    public RetailerAdminFragmentVM(final FragmentRetailerAdminBinding binding, final Context context, final RetailerAdminFragment.RetailerAdminFragmentListener listener) {
        this.mBinding = binding;
        this.mContext = context;
        this.mListener = listener;

        apiRepository = new ApiRepository(mContext);

        mBinding.setViewmodel(this);

        if(retailer != null){
            mBinding.txtRetailer.setText(retailer.getName());
        }

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
                final int loyaltyPointsIncrementAmount;
                if(mBinding.txtLoyaltypointCount.getText().toString() != "") {
                    loyaltyPointsIncrementAmount = Integer.parseInt(mBinding.txtLoyaltypointCount.getText().toString());
                } else {
                    loyaltyPointsIncrementAmount = 0;
                    Log.d("Geef punten op", "");
                }

                final AwardLoyaltyPointsBody awardLoyaltyPointsBody = new AwardLoyaltyPointsBody(loyaltyPointsIncrementAmount);

                AuthHelper.getAccessToken(AuthHelper.getUser(mContext), mContext, new AuthHelper.GetAccessTokenListener() {
                    @Override
                    public void tokenReceived(final String accessToken) {
                        if(mLoyaltyCardId != 0 && loyaltyPointsIncrementAmount != 0){
                            apiRepository.getUserIdByLoyaltyCardId(accessToken, mLoyaltyCardId, new ApiRepository.GetResultListener<String>() {
                                @Override
                                public void resultReceived(String userIdCustomer) {
                                    apiRepository.awardLoyaltyPoints(accessToken, userIdCustomer, retailer.getId(), awardLoyaltyPointsBody, new ApiRepository.GetResultListener<Void>() {
                                        @Override
                                        public void resultReceived(Void result) {
                                            Log.d("punten toegevoegd","");
                                        }

                                        @Override
                                        public void requestError(String error) {
                                            Log.d("punten niet toegevoegd","");
                                        }
                                    });
                                }

                                @Override
                                public void requestError(String error) {
                                    Log.d("Geen token gekregen","");
                                }
                            });
                        }
                        else {
                            Log.d("Geen LoyaltyCardId", "");
                        }
                    }

                    @Override
                    public void requestNewLogin() {
                        Log.d("nieuwe login nodig","");
                    }
                });

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

                        if(mLoyaltyCardId != 0){
                            final ApiRepository apiRepo = new ApiRepository(mContext);

                            apiRepo.pushAdvertisementNotification(accessToken, mLoyaltyCardId, body, // todo: momenteel nog hard coded retailerid
                                    new ApiRepository.GetResultListener<Void>() {
                                        @Override
                                        public void resultReceived(Void result) {

                                        }

                                        @Override
                                        public void requestError(String error) {

                                        }
                                    }
                            );
                        } else {
                            Log.d("Geen LoyaltyCardId", "");
                        }
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
