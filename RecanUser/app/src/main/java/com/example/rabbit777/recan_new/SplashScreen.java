package com.example.rabbit777.recan_new;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import static com.example.rabbit777.recan_new.App.channleID;

public class SplashScreen extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
   private String uid, tittel, Content;
    DatabaseReference notification;
    private NotificationManagerCompat notificationManager_new;
    FirebaseDatabase database;
    private static Object mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        notification = database.getReference("Notification");
        mContext = getApplicationContext();
        notificationManager_new = NotificationManagerCompat.from(this);
        Content = "55";

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null)
                {
                    Thread mythread_Home = new Thread(){
                        @Override
                        public void run() {
                            try {
                                sleep(3000);
                                Intent intent = new Intent(getApplicationContext(),Homepage.class);
                                startActivity(intent);
                                finish();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    mythread_Home.start();
                }
                else
                {
                    Thread mythread_login = new Thread(){
                        @Override
                        public void run() {
                            try {
                                sleep(3000);
                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent);
                                finish();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    mythread_login.start();
                }



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

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);
    }


    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(mAuthListener);
    }




    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(mAuthListener);
    }


}
