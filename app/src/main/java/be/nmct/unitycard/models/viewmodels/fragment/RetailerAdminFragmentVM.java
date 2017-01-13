package be.nmct.unitycard.models.viewmodels.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.util.Log;
import android.view.View;

import com.google.zxing.common.StringUtils;
import com.google.zxing.integration.android.IntentIntegrator;

import org.abego.treelayout.internal.util.java.lang.string.StringUtil;

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
    public Retailer mRetailer;

    public RetailerAdminFragmentVM(final FragmentRetailerAdminBinding binding, final Context context, final RetailerAdminFragment.RetailerAdminFragmentListener listener) {
        this.mBinding = binding;
        this.mContext = context;
        this.mListener = listener;

        apiRepository = new ApiRepository(mContext);

        mBinding.setViewmodel(this);

        if(mRetailer != null){
            mBinding.txtRetailer.setText(mRetailer.getName());
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
                String loyaltyPointCountInput = mBinding.txtLoyaltypointCount.getText().toString();
                final int loyaltyPointsIncrementAmount;
                if (!loyaltyPointCountInput.equals("")) {
                    try {
                        loyaltyPointsIncrementAmount = Integer.parseInt(loyaltyPointCountInput);
                    }
                    catch (NumberFormatException e) {
                        //Log.d(LOG_TAG, "Geef een geldig aantal punten op");
                        mListener.handleError("Geef een geldig aantal punten op");
                        return;
                    }
                } else {
                    //Log.d(LOG_TAG, "Geef aantal punten op");
                    mListener.handleError("Geef aantal punten op");
                    return;
                }

                String loyaltyCardIdInput = mBinding.txtSelectedCustomerId.getText().toString();
                final int loyaltyCardId;
                if (!loyaltyCardIdInput.equals("")) {
                    try {
                        loyaltyCardId = Integer.parseInt(loyaltyCardIdInput);
                    }
                    catch (NumberFormatException e) {
                        //Log.d(LOG_TAG, "Geef een geldige loyaltycard op");
                        mListener.handleError("Geef een geldige loyaltycard op");
                        return;
                    }
                } else {
                    //Log.d(LOG_TAG, "Geef een loyaltycard op");
                    mListener.handleError("Geef een loyaltycard op");
                    return;
                }

                if (mRetailer == null) {
                    //Log.d(LOG_TAG, "Kies een mRetailer");
                    mListener.handleError("Kies een mRetailer");
                    return;
                }

                final AwardLoyaltyPointsBody awardLoyaltyPointsBody = new AwardLoyaltyPointsBody(loyaltyPointsIncrementAmount);

                AuthHelper.getAccessToken(AuthHelper.getUser(mContext), mContext, new AuthHelper.GetAccessTokenListener() {
                    @Override
                    public void tokenReceived(final String accessToken) {
                        if (loyaltyCardId != 0 && loyaltyPointsIncrementAmount != 0) {
                            apiRepository.getUserIdByLoyaltyCardId(accessToken, loyaltyCardId, new ApiRepository.GetResultListener<String>() {
                                @Override
                                public void resultReceived(String userIdCustomer) {
                                    apiRepository.awardLoyaltyPoints(accessToken, userIdCustomer, mRetailer.getId(), awardLoyaltyPointsBody, new ApiRepository.GetResultListener<Void>() {
                                        @Override
                                        public void resultReceived(Void result) {
                                            //Log.d(LOG_TAG, "Punten toegevoegd");
                                            mListener.handleError("Punten toegevoegd");
                                        }

                                        @Override
                                        public void requestError(String error) {
                                            //Log.d(LOG_TAG, "Punten niet toegevoegd");
                                            mListener.handleError("Punten niet toegevoegd");
                                        }
                                    });
                                }

                                @Override
                                public void requestError(String error) {
                                    //Log.d(LOG_TAG, "Geen token gekregen");
                                    mListener.handleError("Geen token gekregen");
                                }
                            });
                        }
                        else {
                            //Log.d(LOG_TAG, "Geen LoyaltyCardId gekozen");
                            mListener.handleError("Geen LoyaltyCardId gekozen");
                        }
                    }

                    @Override
                    public void requestNewLogin() {
                        //Log.d(LOG_TAG, "Nieuwe login nodig");
                        mListener.handleError("Nieuwe login nodig");
                    }
                });

                mBinding.txtLoyaltypointCount.setText("");
            }
        });

        mBinding.btnSendAdvertisement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mBinding.txtAdvertisementDescription.getText().toString();
                if (title.equals("")) {
                    //Log.d(LOG_TAG, "Geef een advertentie op");
                    mListener.handleError("Geef een advertentie op");
                    return;
                }

                if (mRetailer == null) {
                    //Log.d(LOG_TAG, "Kies een mRetailer");
                    mListener.handleError("Kies een mRetailer");
                    return;
                }

                final PushAdvertisementNotificationBody body =
                        new PushAdvertisementNotificationBody(title);

                AuthHelper.getAccessToken(AuthHelper.getUser(mContext), mContext, new AuthHelper.GetAccessTokenListener() {
                    @Override
                    public void tokenReceived(final String accessToken) {
                        //Log.d(LOG_TAG, "Using access token: " + accessToken);

                        final ApiRepository apiRepo = new ApiRepository(mContext);

                        apiRepo.pushAdvertisementNotification(accessToken, mRetailer.getId(), body,
                                new ApiRepository.GetResultListener<Void>() {
                                    @Override
                                    public void resultReceived(Void result) {
                                        //Log.d(LOG_TAG, "Advertentie verzonden");
                                        mListener.handleError("Advertentie verzonden");
                                    }

                                    @Override
                                    public void requestError(String error) {
                                        //Log.d(LOG_TAG, "Advertentie niet verzonden");
                                        mListener.handleError("Advertentie niet verzonden");
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

    public void loyaltyCardIdParsed(int loyaltyCardId) {
        mBinding.txtSelectedCustomerId.setText(Integer.toString(loyaltyCardId));
    }
}
