package com.example.rabbit777.recan_new;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import cn.pedant.SweetAlert.SweetAlertDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import static com.example.rabbit777.recan_new.App.channleID;

public class Money_page extends AppCompatActivity
{
    private FirebaseAuth mAuth;
    DatabaseReference user,notification,History_dataabase;
    FirebaseDatabase database;
    TextView point,Monney_max;
    Double text = 0.0;
    Spinner spinOps;
    Integer count,item1;
    String opselected,pointe,counts,item,results,Max_money;
    String uid;
    String[] mString = new String[10];
    Context context;
    Integer bath = 0;
    Double sic,sum,result,sums = 0.0;
    EditText money;
    Button bt;
    private String tittel;
    String date,Content,Content_history,Negative,date_notime;
    ArrayAdapter<String> adapter;
    ImageView back;
    private NotificationManager notificationManager;
    private NotificationManagerCompat notificationManager_new;
    private NotificationCompat.Builder notificationBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.money_discod);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_history);
        context = this;
        final AlertDialog.Builder adb = new AlertDialog.Builder(this);
        bt = (Button)findViewById(R.id.success_bt);
        database = FirebaseDatabase.getInstance();
        user = database.getReference("Users");
        notification = database.getReference("Notification");
        History_dataabase = database.getReference("History");

        notificationManager_new = NotificationManagerCompat.from(this);
        tittel = "แจ้งเตือนการแลกเเต้ม";

        mAuth = FirebaseAuth.getInstance();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        money = (EditText) findViewById(R.id.money_get);
        point = (TextView)findViewById(R.id.points);
        Monney_max = (TextView) findViewById(R.id.Money_max_point) ;
        back = (ImageView)findViewById(R.id.back_hr_money) ;

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

                user.child(uid).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                User login = dataSnapshot.getValue(User.class);
                 text = Double.parseDouble(login.point);
                String number_point = String.format("%.1f",text);
                point.setText(number_point);
                point.setTextSize(30);
                pointe = point.getText().toString();
                sic = Double.parseDouble(pointe);
                int test = text.intValue() / 30;
                if(text == 0.0 || test <= 0 )
                {
                    bt.setBackgroundColor(Color.GRAY);
                    Max_money = "จำนวนเงินสูงสุดที่แลกได้ ณ ตอนนี้ "+ test +" บาท";
                    Monney_max.setText(Max_money);
                }
                else
                {
                    bt.setBackgroundColor(Color.parseColor("#2298FA"));
                    bt.setEnabled(true);
                    Max_money = "จำนวนเงินสูงสุดที่แลกได้ ณ ตอนนี้ "+ test +" บาท";
                    Monney_max.setText(Max_money);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        bt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                final String Tel = money.getText().toString().trim();

                if(Tel.isEmpty())
                {
                    Toast.makeText(Money_page.this, "กรุณากรอกจำนวนเงินที่คุณต้องการ", Toast.LENGTH_LONG).show();
                    return;

                }
                String bt = money.getText().toString();
                bath = Integer.parseInt(bt);

                sums = bath.doubleValue() * 30.0;

                if(text == 0.0)
                {
                    Toast.makeText(Money_page.this, "แต้มคะแนนของคุณไม่เพียงพอต่อการแลกเงิน (ขั้นต่ำ 30 พอยท์ ต่อการแลก 1 บาท)", Toast.LENGTH_LONG).show();
                    return;
                }

                if(sums > text)
                {
                    Toast.makeText(Money_page.this, "แต้มคะแนนของคุณไม่ถึงเกณฑ์ในการแลก", Toast.LENGTH_LONG).show();
                    return;
                }




                adb.setTitle("ยืนยันการเลือก");

                adb.setMessage("โปรดยืนยันที่จะดำเนินการ");

                adb.setNegativeButton("ยืนยัน", new AlertDialog.OnClickListener()
                        {

                            public void onClick(DialogInterface dialog, int arg1)
                            {

                                 date = String.valueOf(android.text.format.DateFormat.format("dd/MM/yyyy HH:mm:ss", new java.util.Date()));
                                date_notime = String.valueOf(android.text.format.DateFormat.format("dd/MM/yyyy HH:mm", new java.util.Date()));

                                final Double[] numberpoi = new Double[1];
                                String text = String.format("%.0f",sums);
                                final String monney = String.format("%d",bath);
                                String text2 = String.format("%.2f",sums);

                                Content = "นำแต้มสะสม "+ text +" แต้มแลก เงิน "+ monney+ " บาท";
                                Negative = "-";
                                Content_history = "แลก แต้มสะสม";
                                money.setText("");
                                final String key = notification.child(uid).push().getKey();
                                final String  key_history = History_dataabase.child(uid).push().getKey();
                                final Notification_data noti = new Notification_data(date,Content,key);
                                final History history = new History(text2,date_notime,Content_history,Negative,key_history);
                                user.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                    {

                                        User login = dataSnapshot.getValue(User.class);
                                        numberpoi[0] = Double.parseDouble(login.point) ;
                                        result = numberpoi[0] - sums;
                                        results = String.format("%.1f",result);

                                        user.child(uid).child("point").setValue(results).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task)
                                            {
                                                notification.child(uid).child(key).setValue(noti).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task)
                                                    {
                                                        if (task.isSuccessful())
                                                        {
                                                            /*sendOnchannel();*/


                                                        } else
                                                            {
                                                               /* Toast.makeText(Money_page.this, "ไม่ได้", Toast.LENGTH_LONG).show();*/


                                                        }
                                                    }
                                                });

                                                History_dataabase.child(uid).child(key_history).setValue(history).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task)
                                                    {
                                                        /*Toast.makeText(Money_page.this, "เเจ้งเตือน History", Toast.LENGTH_LONG).show();*/

                                                    }
                                                });

                                            }
                                        });

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
                                pDialog.setTitleText("สำเร็จ!");
                                pDialog.setContentText("ระบบได้ทำการตัดแต้มคะแนนของท่านเรียบร้อยแล้ว!");
                                pDialog.setConfirmText("OK");
                                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog)
                                            {
                                                sweetAlertDialog.dismissWithAnimation();


                                            }
                                        });
                                pDialog.show();





                            }

                        });
                adb.setPositiveButton("ยกเลิก", new AlertDialog.OnClickListener() {

                    public void onClick(DialogInterface dialog, int arg1)
                    {


                       return;

                    }

                });

                adb.show();



            }

        });
    }



    public void sendOnchannel()
    {
        Intent activityIntent = new Intent(this,Notification_page.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0,activityIntent,0);

        Intent broadcastIntent = new Intent(this,NotificationReceiver.class);
        broadcastIntent.putExtra("toastMessage",Content);
        PendingIntent actionIntent = PendingIntent.getBroadcast(this,
                0,broadcastIntent,PendingIntent.FLAG_UPDATE_CURRENT);



        Notification notification = new NotificationCompat.Builder(this,channleID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(tittel)
                .setContentText(Content)
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
