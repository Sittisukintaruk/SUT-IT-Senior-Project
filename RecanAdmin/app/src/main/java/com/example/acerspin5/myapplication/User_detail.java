package com.example.acerspin5.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class User_detail extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference user,notification,History_dataabase;
    String key,point_format,sum_point_user ,point_get_new;
    TextView name_users,point_user_haves,point_get;
    ImageView succes_bt ,close_bt;
    Double test,points,sum_point;
    Context context;
    String date_notime,Content,Content_history,Negative,date;
    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_user);

        database = FirebaseDatabase.getInstance();
        user = database.getReference("Users");
        History_dataabase = database.getReference("History");
        notification = database.getReference("Notification");
        Content_history = "ได้รับ แต้มสะสม";
        Negative = "+";
        context = this;
        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        points = intent.getDoubleExtra("point",0.0);
        name_users = (TextView)findViewById(R.id.name_user_detail);
        point_user_haves = (TextView)findViewById(R.id.point_user_have);
        point_get = (TextView)findViewById(R.id.point_user_get);
        succes_bt = (ImageView) findViewById(R.id.Success_button);
        close_bt = (ImageView) findViewById(R.id.Close_button);


        succes_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                date = String.valueOf(android.text.format.DateFormat.format("dd/MM/yyyy HH:mm:ss", new java.util.Date()));
                date_notime = String.valueOf(android.text.format.DateFormat.format("dd/MM/yyyy HH:mm", new java.util.Date()));

                final String key_noti = notification.child(key).push().getKey();
                final String  key_history = History_dataabase.child(key).push().getKey();
                String text = String.format("%.0f",points);
                String text2 = String.format("%.2f",points);

                Content = "ได้รับแต้มสะสมเป็นจำนวนทั้งสิน "+ text +" พอทย์";

                final Notification noti = new Notification(date,Content,key_noti);
                final History history = new History(text2,date_notime,Content_history,Negative,key_history);



                History_dataabase.child(key).child(key_history).setValue(history);
                notification.child(key).child(key_noti).setValue(noti);
                user.child(key).child("point").setValue(point_get_new);

               SweetAlertDialog bt =  new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
                        bt.setTitleText("เสร็จสิ้น!")

                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog)
                                    {

                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                });




                final SweetAlertDialog closedialog= bt;

                closedialog.show();

                final Timer timer2 = new Timer();
                timer2.schedule(new TimerTask() {
                    public void run() {
                        closedialog.dismiss();
                        timer2.cancel(); //this will cancel the timer of the system
                        Intent intent = new Intent(getApplicationContext(), Barcode_qr_Scan.class);
                        startActivity(intent);
                        finish();
                    }
                }, 1500); // the timer will count 5 seconds....


            }
        });

        close_bt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), Barcode_qr_Scan.class);
                startActivity(intent);
                finish();
            }
        });

        user.child(key).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                User login = dataSnapshot.getValue(User.class);

                name_users.setText("คุณ "+login.name+" "+ login.subname);

                test = Double.parseDouble(login.point);
                sum_point_user = String.format("%.1f",test);
                point_format = String.format("%.1f",points);
                point_user_haves.setText(sum_point_user);
                point_get.setText(point_format);

                sum_point = test + points;

                point_get_new = String.format("%.1f",sum_point);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}
