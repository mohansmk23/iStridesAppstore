package com.istrides.appstore;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

/**
 * Created by Mohankumar S on 6/18/2019$.
 */
public class NotificationService extends FirebaseMessagingService {
    public static int ID_SMALL_NOTIFICATION = 235;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
         if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

             Intent i = new Intent(getApplicationContext(),AppDetails.class);
             try {
                 JSONObject json = new JSONObject(remoteMessage.getData());

                 Log.i("komali",String.valueOf(json.getInt("app-id"))+" sdfv");
                 i.putExtra("APPID",json.getString("app-id"));
                 showSmallNotification(json.getString("title"),json.getString("message"),i);
             } catch (JSONException e) {
                 e.printStackTrace();
             }




            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.

            } else {
                // Handle message within 10 seconds

            }

        }
         // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    public void showSmallNotification(String title, String message, Intent intent) {


        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        getApplicationContext(),
                        ID_SMALL_NOTIFICATION++,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        String id = null;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            id = "my_channel_01";
            CharSequence name = "new_channel";
            String description = "my  new channel";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = null;
            mChannel = new NotificationChannel(id, name,importance);
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(mChannel);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
            Notification notification;
            notification = mBuilder
                    .setSmallIcon(R.drawable.noti_icon)
                    .setTicker(title)
                    .setWhen(0)
                    .setAutoCancel(true)
                    .setContentIntent(resultPendingIntent)
                    .setContentTitle(title)
                    .setSound(uri)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(Html.fromHtml(message)))
                    .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.dasboardlogo))
                    .setContentText(Html.fromHtml(message))
                    .setChannelId(id)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200,100})
                    .setOngoing(false)
                    .build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(ID_SMALL_NOTIFICATION, notification);

        }
        else {

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
            Notification notification;
            notification = mBuilder.setSmallIcon(R.drawable.noti_icon).setTicker(title).setWhen(0)
                    .setAutoCancel(true)
                    .setContentIntent(resultPendingIntent)
                    .setContentTitle(title)
                    .setSound(uri)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200,100})
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(Html.fromHtml(message)))
                    .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.dasboardlogo))
                    .setContentText(Html.fromHtml(message))
                    .setOngoing(false)
                    .build();


            notification.flags |= Notification.FLAG_AUTO_CANCEL;

            notificationManager.notify(ID_SMALL_NOTIFICATION, notification);

        }



        /*int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel mChannel = new NotificationChannel(ID_SMALL_NOTIFICATION, name, importance);
        mChannel.setShowBadge(false);*/
        // Configure the notification channel

    }





}
