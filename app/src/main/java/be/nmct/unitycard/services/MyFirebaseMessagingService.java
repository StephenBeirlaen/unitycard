package be.nmct.unitycard.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import be.nmct.unitycard.R;
import be.nmct.unitycard.activities.customer.MainActivity;

/**
 * Created by Stephen on 28/11/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String LOG_TAG = this.getClass().getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage message){
        String from = message.getFrom();
        Map data = message.getData();

        if (data.size() > 0) {
            Log.d(LOG_TAG, "Message data payload: " + data);

            if (data.containsKey("message") && data.containsKey("retailerName") && data.containsKey("title")) {
                String msg = (String)data.get("message");
                String posterUsername = (String)data.get("retailerName");
                String title = (String)data.get("title");

                // intent om de juiste activity te openen bij het klikken op de notification
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // todo: met de intent naar de pagina van de mRetailer gaan
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);

                if (msg != null && msg.equals("AdvertisementUploaded")) {
                    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_credit_card_black_24dp)
                            .setContentTitle(posterUsername + getString(R.string.retailer_placed_new_offer))
                            .setContentText(title)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);
                    //NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                    notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
                }
            }
        }
    }
}
