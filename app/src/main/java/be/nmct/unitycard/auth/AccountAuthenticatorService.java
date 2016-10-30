package be.nmct.unitycard.auth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Stephen on 30/10/2016.
 */

public class AccountAuthenticatorService extends Service {

    private AccountAuthenticator mAuthenticator;

    public AccountAuthenticatorService() {
    }

    //The system calls this method when the service is first created, to perform one-time setup procedures
    @Override
    public void onCreate() {
        super.onCreate();

        // Dit is de service die met AccountManager communiceert.
        // Maak een nieuwe AccountAuthenticator aan (AbstractAccountAuthenticator)
        // Die opent de AccountActivity
        mAuthenticator = new AccountAuthenticator(this);
    }

    //The system calls this method when another component wants to bind with the service
    //IBinder: you must provide an interface that clients use to communicate with the service, by returning an IBinder
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
