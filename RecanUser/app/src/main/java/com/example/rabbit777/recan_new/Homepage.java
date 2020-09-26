package com.example.rabbit777.recan_new;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.*;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class Homepage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    DatabaseReference user;
    FirebaseDatabase database;
    ImageView imge ,barcode,noti;
    String uid,qrcode;
    TextView name;
    Context context;
    Dialog MyDialog;
    LinearLayout reward,location,notification,history,firstFragment;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        mContext = this;

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_homepage);
        database = FirebaseDatabase.getInstance();
        user = database.getReference("Users");
        imge = (ImageView) findViewById(R.id.profile_img);
        barcode =(ImageView) findViewById(R.id.barcode_img);
        reward = (LinearLayout)findViewById(R.id.Reward_img) ;
        location = (LinearLayout)findViewById(R.id.Location_new);
        notification = (LinearLayout)findViewById(R.id.line_notification);
        history = (LinearLayout)findViewById(R.id.Histor_new);
        firstFragment = (LinearLayout)findViewById(R.id.frameLayout_hom);
        mAuth = FirebaseAuth.getInstance();
        context = this;
             uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

         name = (TextView) findViewById(R.id.textName);


        imge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Intent intent = new Intent(getApplicationContext(),User_profile.class);
                startActivity(intent);
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(),History_page.class);
                startActivity(intent);
            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Intent intent = new Intent(getApplicationContext(),Location_new.class);
                startActivity(intent);
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(),Notification_page.class);
                startActivity(intent);
            }
        });

        reward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),Reward.class);
                startActivity(intent);
            }
        });
        barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                BitMatrix bitMatrix = null;
                try {
                    bitMatrix = multiFormatWriter.encode(qrcode, BarcodeFormat.QR_CODE, 600, 600);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    displaydialog(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }

            }
        });

        user.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                User login = dataSnapshot.getValue(User.class);

                name.setText(login.name+" "+login.subname);
                name.setTextSize(20);

                qrcode = uid;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        firstFragment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                MyCustomdialog();

            }
        });


    }

    private void displaydialog(Bitmap image)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Qrcode");
        ImageView img = new ImageView(mContext);
        img.setImageBitmap(image);

        builder.setView(img);
        builder.create();
        builder.show();


    }

    public  void  MyCustomdialog()
    {

        MyDialog = new Dialog(context);
        MyDialog.setContentView(R.layout.frament_botton);
        MyDialog.setTitle("");
        MyDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sample, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("คุณแน่ใจหรือไม่ว่าคุณต้องการออกจากระบบ");

            builder.setPositiveButton("ออกจากระบบ", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing but close the dialog
                    logout();
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // Do nothing
                    dialog.dismiss();
                    return;
                }
            });

            AlertDialog alert = builder.create();
            alert.show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void logout()
    {
        AuthUI.getInstance()
        .signOut(this)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                public void onComplete(@NonNull Task<Void> task) {
                    // user is now signed out
                    Intent intent = new Intent(Homepage.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            });

    }

}
