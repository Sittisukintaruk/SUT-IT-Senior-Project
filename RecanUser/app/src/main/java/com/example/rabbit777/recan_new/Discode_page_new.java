package com.example.rabbit777.recan_new;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import cn.pedant.SweetAlert.SweetAlertDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import org.w3c.dom.Text;

import java.util.Random;

import static com.example.rabbit777.recan_new.App.channleID;

public class Discode_page_new extends AppCompatActivity
{

    private FirebaseAuth mAuth;
    DatabaseReference user,notification,History_dataabase;
    FirebaseDatabase database;
    String uid,pointe,point_des,date_notime,Status ;
    TextView point,discode_text;
    ImageView Free01,Free02,Free03;
    Double number = 0.0,text = 0.0;
    Context context;
    Double sum,result,point_new_dis = 0.0;
    private String tittel;
    private ImageView back_dis;
    String Content,date,results,Barcode,Content_history,Negative;
    private NotificationManagerCompat notificationManager_new;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discode_cafee_new);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_discode);


        database = FirebaseDatabase.getInstance();
        context = this;
        user = database.getReference("Users");
        notification = database.getReference("Notification");
        History_dataabase = database.getReference("History");
        notificationManager_new = NotificationManagerCompat.from(this);
        mAuth = FirebaseAuth.getInstance();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        Free01 = (ImageView)findViewById(R.id.imageView_free1);
        Free02 = (ImageView)findViewById(R.id.imageView_free2);
        Free03 = (ImageView)findViewById(R.id.imageView_free3);
        back_dis = (ImageView)findViewById(R.id.back_discode) ;
        point = (TextView)findViewById(R.id.point_new_discode);
        discode_text = (TextView)findViewById(R.id.discode_text);
        Negative = "-";
        Content_history = "แลก แต้มสะสม";
        tittel = "แจ้งเตือนการแลกเเต้ม";

        back_dis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        user.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                User login = dataSnapshot.getValue(User.class);

                text = Double.parseDouble(login.point);
                String number_point = String.format("%.1f",text);
                point.setText(number_point);
                point.setTextSize(30);
                pointe = point.getText().toString();

                if(text < 499.0 )
                {
                    Status = "";

                    setLocked(Free01);
                    setLocked(Free02);
                    setLocked(Free03);

                }
                else if(text >= 499.0 && text <500.0 )
                {

                    setLocked(Free02);
                    setLocked(Free01);


                    setUnlocked(Free03);





                }
                else  if(text >= 500.0 && text < 1000.0)
                {
                    setLocked(Free02);
                    setUnlocked(Free01);
                    setUnlocked(Free03);
                }
                else
                {

                    Free01.setEnabled(true);
                    Free02.setEnabled(true);
                    Free03.setEnabled(true);
                    setUnlocked(Free01);
                    setUnlocked(Free02);
                    setUnlocked(Free03);

                }
            }

            @Override
            public void onCancelled( DatabaseError databaseError)
            {

            }
        });


        Free01.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(text >= 500.0) {

                    setUnlocked(Free01);
                    setLocked(Free02);
                    setLocked(Free03);
                    Status = "A";
                    point_new_dis = 500.0;
                    discode_text.setTextColor(Color.WHITE);
                }
                else
                {
                    Toast.makeText(context, "แต้มของคุณไม่พอที่จะเลือกรายการนี้", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        Free02.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(text >= 1000.0) {
                    setUnlocked(Free02);
                    setLocked(Free01);
                    setLocked(Free03);
                    Status = "B";
                    point_new_dis = 1000.0;
                    discode_text.setTextColor(Color.WHITE);
                }
                else
                {
                    Toast.makeText(context, "แต้มของคุณไม่พอที่จะเลือกรายการนี้", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });

        Free03.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(text >= 499.0)
                {

                    setUnlocked(Free03);
                    setLocked(Free01);
                    setLocked(Free02);
                    Status = "C";
                    point_new_dis = 499.0;
                    discode_text.setTextColor(Color.WHITE);
                }
                else
                {
                    Toast.makeText(context, "แต้มของคุณไม่พอที่จะเลือกรายการนี้", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });







        discode_text.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {

                check_chose(Status,point_new_dis);

            }
        });

    }

    public static void  setLocked(ImageView v)
    {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);  //0 means grayscale
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
        v.setColorFilter(cf);
        v.setImageAlpha(128);   // 128 = 0.5
    }

    public static void  setUnlocked(ImageView v)
    {
        v.setColorFilter(null);
        v.setImageAlpha(255);
    }



   private void check_chose(final String select,final Double point_chose)
   {
       final String key = notification.child(uid).push().getKey();
       final String  key_history = History_dataabase.child(uid).push().getKey();
       final Double[] numberpoi = new Double[1];

       if(text <= 0.0)
       {
           Toast.makeText(context, "ไม่สามารถดำเนินการอได้อันเนื่องมาจากแต้มของคุณไม่เพียงพอ", Toast.LENGTH_SHORT).show();
           return;
       }
        if(select==null || select.trim().equals(""))
       {
           Toast.makeText(context, "กรุณาเลือกรายการที่ต้องการ", Toast.LENGTH_SHORT).show();
           return;
       }
           char[] chars1 = "ABCDEF012GHIJKL345MNOPQR678STUVWXYZ9".toCharArray();
           StringBuilder sb1 = new StringBuilder();
           Random random1 = new Random();
           for (int i = 0; i < 6; i++) {
               char c1 = chars1[random1.nextInt(chars1.length)];
               sb1.append(c1);
           }
           Barcode = sb1.toString();
           date_notime = String.valueOf(android.text.format.DateFormat.format("dd/MM/yyyy HH:mm", new java.util.Date()));
           date = String.valueOf(android.text.format.DateFormat.format("dd/MM/yyyy HH:mm:ss", new java.util.Date()));


           new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                   .setTitleText("คุณต้องการที่จะดำเนินการใช่หรือไม่ ?")
                   .setContentText("เมื่อยืนยันเเต้มพอทย์ของคุณจะถูกหักออกจากระบบ")
                   .setConfirmText("ยืนยัน")
                   .setCancelText("ยกเลิก")
                   .showCancelButton(true)
                   .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                       @Override
                       public void onClick(SweetAlertDialog sweetAlertDialog) {

                           sweetAlertDialog.dismissWithAnimation();
                           return;


                       }
                   })
                   .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                       @Override
                       public void onClick(SweetAlertDialog sweetAlertDialog)
                       {

                           switch (select)
                           {
                               case "A":
                                   Content = "ใช้ 500 พอทย์แลกรับส่วนลดมูลค่า 50 บาท สำหรับเครื่องดื่ม Inthanin รหัสของคุณคือ " + Barcode;
                                    break;

                               case "B":
                                   Content = "ใช้ 1000 พอทย์แลกรับส่วนลดมูลค่า 100 บาท สำหรับเไอศครีม  BB baskin fobbins รหัสของคุณคือ " + Barcode;
                                   break;

                               case "C":
                                   Content = "ใช้ 499 พอทย์แลกรับเครื่องดื่มมูลค่า  65 บาท  รหัสของคุณคือ "+ Barcode;
                                   break;

                           }
                            point_des = String.format("%.2f",point_chose);
                           final Notification_data noti = new Notification_data(date,Content,key);
                           final History history = new History(point_des,date_notime,Content_history,Negative,key_history);


                           user.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                               {
                                   User login = dataSnapshot.getValue(User.class);
                                   numberpoi[0] = Double.parseDouble(login.point) ;
                                   result = numberpoi[0] - point_chose;
                                   results = String.format("%.1f",result);
                                   discode_text.setTextColor(Color.parseColor("#838B8B"));


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
                                                       SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
                                                       pDialog.setTitleText("สำเร็จ!");
                                                       pDialog.setContentText("ระบบได้ทำการตัดแต้มคะแนนของท่านเรียบร้อยแล้ว! รหัสของคุณคือ " + Barcode);                                                        pDialog.setConfirmText("OK");
                                                       pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                           @Override
                                                           public void onClick(SweetAlertDialog sweetAlertDialog)
                                                           {
                                                               Status ="";
                                                               sweetAlertDialog.dismissWithAnimation();


                                                           }
                                                       });
                                                       pDialog.show();

                                                   } else
                                                   {


                                                   }
                                               }
                                           });
                                           History_dataabase.child(uid).child(key_history).setValue(history).addOnCompleteListener(new OnCompleteListener<Void>() {
                                               @Override
                                               public void onComplete(@NonNull Task<Void> task)
                                               {

                                               }
                                           });



                                       }
                                   });

                               }

                               @Override
                               public void onCancelled(@NonNull DatabaseError databaseError) {

                               }
                           });
                           sweetAlertDialog.dismissWithAnimation();

                       }
                   })
                   .show();
   }
}
