package be.nmct.unitycard.fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import be.nmct.unitycard.R;
import be.nmct.unitycard.activities.MainActivity;
import be.nmct.unitycard.auth.AuthHelper;
import be.nmct.unitycard.contracts.LoyaltyCardContract;
import be.nmct.unitycard.models.LoyaltyCard;
import be.nmct.unitycard.repositories.ApiRepository;
import butterknife.Bind;
import butterknife.ButterKnife;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class MyLoyaltyCardFragment extends Fragment {

    @Bind(R.id.imageViewQR) ImageView imageViewQR;

    private final String LOG_TAG = this.getClass().getSimpleName();
    private MyLoyaltyCardFragmentListener mListener;

    public MyLoyaltyCardFragment() {
        // Required empty public constructor
    }

    public static MyLoyaltyCardFragment newInstance() {
        return new MyLoyaltyCardFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_loyalty_card, container, false);
        ButterKnife.bind(this, view);

        loadQRcode();

        return view;
    }

    // http://stackoverflow.com/questions/28232116/android-using-zxing-generate-qr-code
    Bitmap encodeAsBitmap(String content, BarcodeFormat format, int width, int height) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(content, format, width, height, null);
        } catch (IllegalArgumentException e) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, w, h);
        return bitmap;
    }

    private void loadQRcode() {
        // Show loading indication
        mListener.swipeRefreshLayoutAddTask(MainActivity.TASK_LOAD_MY_LOYALTY_CARD);

        // Get access token
        AuthHelper.getAccessToken(AuthHelper.getUser(getActivity()), getActivity(), new AuthHelper.GetAccessTokenListener() {
            @Override
            public void tokenReceived(final String accessToken) {
                Log.d(LOG_TAG, "Using access token: " + accessToken);

                final ApiRepository apiRepo = new ApiRepository(getActivity());
                apiRepo.getLoyaltyCard(accessToken, AuthHelper.getUserId(getActivity()), new ApiRepository.GetResultListener<LoyaltyCard>() {
                    @Override
                    public void resultReceived(LoyaltyCard loyaltyCard) {
                        Log.d(LOG_TAG, "Received loyalty card: " + loyaltyCard);

                        // Hide loading indication
                        mListener.swipeRefreshLayoutRemoveTask(MainActivity.TASK_LOAD_MY_LOYALTY_CARD);

                        try {
                            // Genereer een QR code
                            Bitmap bm = encodeAsBitmap(
                                    LoyaltyCardContract.getQRcodeContent(loyaltyCard.getId()),
                                    BarcodeFormat.QR_CODE,
                                    imageViewQR.getWidth(),
                                    imageViewQR.getHeight()
                            );

                            if (bm != null) {
                                imageViewQR.setImageBitmap(bm);
                            }
                        } catch (WriterException e) {
                            // todo
                        }
                    }

                    @Override
                    public void requestError(String error) {
                        // Invalideer het gebruikte access token, het is niet meer geldig (anders zou er geen error zijn)
                        AuthHelper.invalidateAccessToken(accessToken, getActivity());

                        // Hide loading indication
                        mListener.swipeRefreshLayoutRemoveTask(MainActivity.TASK_LOAD_MY_LOYALTY_CARD);

                        // Try again
                        loadQRcode();
                    }
                });
            }

            @Override
            public void requestNewLogin() {
                // Hide loading indication
                mListener.swipeRefreshLayoutRemoveTask(MainActivity.TASK_LOAD_MY_LOYALTY_CARD);

                // Something went wrong, toon login scherm
                mListener.requestNewLogin();
            }
        });
    }

    public interface MyLoyaltyCardFragmentListener {
        void swipeRefreshLayoutAddTask(String task);
        void swipeRefreshLayoutRemoveTask(String task);
        void requestNewLogin();
        void handleError(String error);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // zeker zijn dat de container activity de callback geimplementeerd heeft
        if (context instanceof MyLoyaltyCardFragmentListener) {
            mListener = (MyLoyaltyCardFragmentListener)context;
        } else {
            throw new ClassCastException(context.toString() + "must implement " + MyLoyaltyCardFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
