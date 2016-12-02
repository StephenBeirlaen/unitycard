package be.nmct.unitycard.helpers;

import android.content.Context;
import android.util.Log;

import be.nmct.unitycard.auth.AuthHelper;
import be.nmct.unitycard.models.postmodels.ChangeFcmTokenBody;
import be.nmct.unitycard.repositories.AuthRepository;

/**
 * Created by Stephen on 28/11/2016.
 */

public class FcmTokenHelper {
    private final static String LOG_TAG = FcmTokenHelper.class.getSimpleName();

    public static void sendRegistrationToServer(final String fcmToken, final Context context) {
        // Get access token
        AuthHelper.getAccessToken(AuthHelper.getUser(context), context, new AuthHelper.GetAccessTokenListener() {
            @Override
            public void tokenReceived(String accessToken) {
                sendToServer(fcmToken, context, accessToken);
            }

            @Override
            public void requestNewLogin() {
                Log.e(LOG_TAG, "Token error!");
            }
        });
    }

    // voor het verwijderen
    private static void sendToServer(String fcmToken, final Context context, final String accessToken) {
        Log.d(LOG_TAG, "Using access token: " + accessToken);

        final AuthRepository authRepo = new AuthRepository(context);

        ChangeFcmTokenBody changeFcmTokenBody = new ChangeFcmTokenBody(fcmToken);

        authRepo.changeUserFcmToken(accessToken, changeFcmTokenBody, new AuthRepository.ChangeFcmTokenResponseListener() {
            @Override
            public void fcmTokenChanged() {
                Log.d(LOG_TAG, "Updated FCM token");
            }

            @Override
            public void fcmTokenChangeRequestError(String error) {
                // Invalideer het gebruikte access token, het is niet meer geldig (anders zou er geen error zijn)
                AuthHelper.invalidateAccessToken(accessToken, context);

                Log.e(LOG_TAG, "Token error!");
            }
        });
    }

    public static void removeRegistrationToken(Context context, String accessToken) {
        sendToServer(null, context, accessToken);
    }
}
