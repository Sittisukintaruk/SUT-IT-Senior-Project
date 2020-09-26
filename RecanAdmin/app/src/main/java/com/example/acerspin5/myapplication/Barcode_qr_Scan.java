package com.example.acerspin5.myapplication;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Barcode_qr_Scan extends AppCompatActivity {
    Button Button_submit;
    ImageView img;
    private RadioGroup radioGroup;
    DatabaseReference Weights;
    FirebaseDatabase database;
    TextView Textview_gum,Textview_pointe_user_get;
    Integer WiehtChose = 0,Result,Wiehtpoint = 0;
    String point_user_fomat,point_get = "0.0";
    double Exchange_rate = 0.0,points,number_wieht,number_format;
    private RadioButton Smail_bt, Big_bt, Conet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_code_scan);


        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_qrcode);

        database = FirebaseDatabase.getInstance();
        Weights = database.getReference("Weight");
        Textview_gum = (TextView)findViewById(R.id.gumla) ;
        Textview_pointe_user_get = (TextView)findViewById(R.id.point_user);
        Big_bt = (RadioButton)findViewById(R.id.Big_bottel);
        Smail_bt = (RadioButton)findViewById(R.id.Smail_bottle) ;
        Conet = (RadioButton)findViewById(R.id.canned);
        img = (ImageView)findViewById(R.id.back_hr1);
        radioGroup = (RadioGroup) findViewById(R.id.myRadioGroup);
        Button_submit = (Button)findViewById(R.id.but);

        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                if(checkedId == R.id.Smail_bottle) {

                    Exchange_rate = 1.0;
                        WiehtChose = 20;



                } else if(checkedId == R.id.Big_bottel) {

                    Exchange_rate = 1.5;
                    WiehtChose = 30;
                } else {

                    Exchange_rate = 2.0;
                    WiehtChose = 10;




                }
                 int selectedId = radioGroup.getCheckedRadioButtonId();

                if (selectedId > 0) {
                    Result = Wiehtpoint / WiehtChose;
                    points = Result * Exchange_rate;
                    point_get = String.format("%.1f",points);



                }


                Textview_pointe_user_get.setText(point_get);


            }
        });



        img.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
                finish();
            }
        });




        Button_submit.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v)
          {
              int selectedId = radioGroup.getCheckedRadioButtonId();

              if(selectedId > 0) {
                  Intent intent = new Intent(getApplicationContext(), ScanCodeActivity.class);
                  intent.putExtra("point", point_get);

                  startActivity(intent);
                    finish();

              }
              else
              {
                  Toast.makeText(Barcode_qr_Scan.this, "กรุณาเลือกประเภทขวด", Toast.LENGTH_SHORT).show();

                    return;
              }



          }
      });

    Weights.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


            Weigt_real weight_realtime = dataSnapshot.getValue(Weigt_real.class);
            number_wieht = Double.parseDouble(weight_realtime.weight);
            if(number_wieht > 0.00)
            {
                number_format = number_wieht * 1000;
                       Wiehtpoint = (int) number_format;
            }
            else
            {
               Wiehtpoint = 0;

            }

            point_user_fomat = String.format("%d",Wiehtpoint);
            Textview_gum.setText(point_user_fomat);

            int selectedId = radioGroup.getCheckedRadioButtonId();

            if (selectedId > 0) {
                Result = Wiehtpoint / WiehtChose;
                points = Result * Exchange_rate;
                point_get = String.format("%.1f",points);


            } else {
                point_get = "0.0";

            }
            Textview_pointe_user_get.setText(point_get);


        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(Barcode_qr_Scan.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });





















    }



}
