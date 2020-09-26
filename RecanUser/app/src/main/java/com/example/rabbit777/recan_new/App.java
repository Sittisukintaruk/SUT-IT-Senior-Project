package com.example.rabbit777.recan_new;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.Date;

public class App extends Application {
    public static final String channleID = "NotificationID";
    public static final String channleID_2 = "NotificationID_Admin";
    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference notification;
    FirebaseDatabase database;
    String uid, tittel, Content;
    private NotificationManagerCompat notificationManager_new;
    private static Object mContext;
    private FirebaseAuth firebaseAuth;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        notification = database.getReference("Notification");
        tittel = "เเจ้งเตือน";
        createNotificationChannles();
        mContext = getApplicationContext();
        notificationManager_new = NotificationManagerCompat.from(this);
        Content = "55";


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    notification.child(uid).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            if (dataSnapshot.hasChildren()) {
                                Notification_data noti = dataSnapshot.getValue(Notification_data.class);
                                Content = noti.Content;
                                sendOnchannel(tittel, Content);
                            }

                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            Notification_data noti = dataSnapshot.getValue(Notification_data.class);
                            Content = noti.Content;
                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                } else {
                    uid = null;
                    // User is signed out
                }
                // ...
            }
        };


    }




    private void createNotificationChannles()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel1 = new NotificationChannel(
                    channleID,
                    "Notification",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("this Notification");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);

            NotificationChannel channel2 = new NotificationChannel(
                    channleID_2,
                    "Notification",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel1.setDescription("this Notification");

            NotificationManager manager1 = getSystemService(NotificationManager.class);
            manager1.createNotificationChannel(channel2);
        }
    }


    public void sendOnchannel(String tittel ,String Massage)
    {
        Intent activityIntent = new Intent(this,Notification_page.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0,activityIntent,0);

        Intent broadcastIntent = new Intent(this,NotificationReceiver.class);
        broadcastIntent.putExtra("toastMessage",Massage);
        PendingIntent actionIntent = PendingIntent.getBroadcast(this,
                0,broadcastIntent,PendingIntent.FLAG_UPDATE_CURRENT);



        Notification notification = new NotificationCompat.Builder(this,channleID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(tittel)
                .setContentText(Massage)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.BLUE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .addAction(R.mipmap.ic_launcher,"Toast",actionIntent)
                .build();


        notificationManager_new.notify(1,notification);


    }


}
