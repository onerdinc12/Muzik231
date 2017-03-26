package com.example.jean.jcplayer;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.jean.jcplayer.JcPlayerExceptions.AudioListNullPointerException;

public class JcPlayerNotificationReceiver extends BroadcastReceiver {
    public JcPlayerNotificationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        JcAudioPlayer jcAudioPlayer = JcAudioPlayer.getInstance();
        String action = "";
        String action2 = "";

        if(intent.hasExtra(JcNotificationPlayer.ACTION))
            action = intent.getStringExtra(JcNotificationPlayer.ACTION);

        switch (action){


            case JcNotificationPlayer.KAPAT:

                NotificationManager notifManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notifManager.cancelAll();
                jcAudioPlayer.kill();
                break;

            case JcNotificationPlayer.PLAY:
                try {
                    jcAudioPlayer.continueAudio();
                    jcAudioPlayer.updateNotification();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case JcNotificationPlayer.PAUSE:
                jcAudioPlayer.pauseAudio();
                jcAudioPlayer.updateNotification();
                break;

            case JcNotificationPlayer.NEXT:
                try {
                    jcAudioPlayer.nextAudio();
                } catch (AudioListNullPointerException e) {
                    try {
                        jcAudioPlayer.continueAudio();
                    } catch (AudioListNullPointerException e1) {
                        e1.printStackTrace();
                    }
                }
                break;

            case JcNotificationPlayer.PREVIOUS:
                try {
                    jcAudioPlayer.previousAudio();
                } catch (Exception e) {
                    try {
                        jcAudioPlayer.continueAudio();
                    } catch (AudioListNullPointerException e1) {
                        e1.printStackTrace();
                    }
                }

                break;




        }
    }


}
