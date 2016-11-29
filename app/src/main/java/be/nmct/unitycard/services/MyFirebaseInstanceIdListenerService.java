package be.nmct.unitycard.services;

import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import be.nmct.unitycard.auth.AuthHelper;
import be.nmct.unitycard.helpers.FcmTokenHelper;
import be.nmct.unitycard.models.postmodels.ChangeFcmTokenBody;
import be.nmct.unitycard.repositories.AuthRepository;

/**
 * Created by Stephen on 28/11/2016.
 */

public class MyFirebaseInstanceIdListenerService extends FirebaseInstanceIdService {

    private final String LOG_TAG = this.getClass().getSimpleName();

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is also called
     * when the InstanceID token is initially generated, so this is where
     * you retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(LOG_TAG, "Refreshed token: " + refreshedToken);
        // Send any registration to your app's servers.
        FcmTokenHelper.sendRegistrationToServer(refreshedToken, this);
    }
}
