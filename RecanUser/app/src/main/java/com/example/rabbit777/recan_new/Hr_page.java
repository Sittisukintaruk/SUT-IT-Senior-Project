package com.example.rabbit777.recan_new;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import cn.pedant.SweetAlert.SweetAlertDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import android.app.Notification;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import static com.example.rabbit777.recan_new.App.channleID;

public class Hr_page extends AppCompatActivity implements OnItemSelectedListener
{
    private FirebaseAuth mAuth;
    private int currentNotificationID = 0;

    DatabaseReference user,notification,History_dataabase;
    FirebaseDatabase database;
    TextView point;
    Spinner spinOps;
    String[] ops={"1","2","3","4","5","6","7","8","9","10"};
    Integer count = 0,item1 = 0;
    String opselected,pointe,counts,item,results,date_notime;
    String uid;
    String[] mString = new String[10];
    Context context;
    Button bt;
    Double sic,sum,result;
    String date,Content,Content_history,Negative;
    ArrayAdapter<String> adapter;
    ImageView back,img;
    private String tittel;
    private static final int MY_NOTIFICATION_ID=1;
    private NotificationManager notificationManager;
    private NotificationManagerCompat notificationManager_new;
    private NotificationCompat.Builder notificationBuilder;
    private Bitmap icon;
    private int combinedNotificationCounter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hr_activity);
        context = this;
        final AlertDialog.Builder adb = new AlertDialog.Builder(this);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        bt = (Button)findViewById(R.id.button);
        database = FirebaseDatabase.getInstance();
        user = database.getReference("Users");
        notification = database.getReference("Notification");
        History_dataabase = database.getReference("History");
        notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager_new = NotificationManagerCompat.from(this);
         tittel = "แจ้งเตือนการแลกเเต้ม";
        mAuth = FirebaseAuth.getInstance();
        final TextView title = (TextView) findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        title.setText("ชั่วโมงจิตอาสา");
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        spinOps = (Spinner)findViewById(R.id.Spinner01);
        point = (TextView)findViewById(R.id.point);
        back = (ImageView)findViewById(R.id.back_hr) ;
        img = (ImageView)findViewById(R.id.imageView3) ;
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ops);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinOps.setAdapter(adapter);
        spinOps.setOnItemSelectedListener(this);






        back.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v)
    {
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

                double text = Double.parseDouble(login.point);
                String number_point = String.format("%.1f",text);
                point.setText(number_point);
                point.setTextSize(30);
                pointe = point.getText().toString();

                sic = Double.parseDouble(pointe);

                count =  sic.intValue() / 70;

                counts = String.format("%d",count);

                if(count ==0)
                {
                    bt.setBackgroundColor(Color.GRAY);
                }
                else
                {
                    bt.setBackgroundColor(Color.parseColor("#2298FA"));
                    bt.setEnabled(true);
                    spinOps.setEnabled(true);

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
                if(count == 0)
                {
                    Toast.makeText(Hr_page.this, "แต้มคะแนนของคุณไม่เพียงพอต่อการแลกชั่วโมงจิตอาสา (ขั้นต่ำ 70 พอยท์ ต่อการแลก 1 ชั่วโมง)", Toast.LENGTH_LONG).show();
                    return;
                }
                else if(item1 > count)
                {
                    Toast.makeText(Hr_page.this, "แต้มคะแนนของคุณไม่ถึงเกรณ์ในการแลก", Toast.LENGTH_LONG).show();
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

                                int poi = Integer.parseInt(item);
                                final Double[] numberpoi = new Double[1];
                                sum = 70.0 * poi;
                                String text = String.format("%.0f",sum);
                                String text01 = String.format("%d",poi);
                                String text1 = String.format("%.2f",sum);
                                Content_history = "แลก แต้มสะสม";
                                Negative = "-";
                                Content = "นำแต้มสะสม "+ text +" แต้มแลก ชั่วโมงจิตอาสา "+text01+ " ชั่วโมง";
                                final String key = notification.child(uid).push().getKey();
                                final String  key_history = History_dataabase.child(uid).push().getKey();

                                final Notification_data noti = new Notification_data(date,Content,key);
                                final History history = new History(text1,date_notime,Content_history,Negative,key_history);
                                user.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                    {
                                        User login = dataSnapshot.getValue(User.class);
                                        numberpoi[0] = Double.parseDouble(login.point) ;
                                        result = numberpoi[0] - sum;
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

                                                        } else
                                                            {
                                                                /*Toast.makeText(Hr_page.this, "ไม่ได้", Toast.LENGTH_LONG).show();*/


                                                        }
                                                    }
                                                });

                                                History_dataabase.child(uid).child(key_history).setValue(history).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task)
                                                    {
                                                        /*Toast.makeText(Hr_page.this, "เเจ้งเตือน History", Toast.LENGTH_LONG).show();*/

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




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        opselected=ops[position];
        item = spinOps.getSelectedItem().toString();
        item1 = Integer.parseInt(item);

        //Toast.makeText(Hr_page.this, spinOps.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();

        if(item1 > count)
        {
            bt.setBackgroundColor(Color.GRAY);

        }

        else {
            bt.setBackgroundColor(Color.parseColor("#2298FA"));


        }


    }



    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
