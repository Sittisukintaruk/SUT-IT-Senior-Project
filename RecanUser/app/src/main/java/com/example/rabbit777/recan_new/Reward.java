package com.example.rabbit777.recan_new;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class Reward extends AppCompatActivity {

    private FirebaseAuth mAuth;
    DatabaseReference user;
    FirebaseDatabase database;
    LinearLayout Hr,money,discode;
    String uid;
    TextView name,phone,email,point;
    private ImageView back_n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reward_ativity);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_reward);

        database = FirebaseDatabase.getInstance();
        user = database.getReference("Users");
        point = (TextView)findViewById(R.id.point);
        mAuth = FirebaseAuth.getInstance();
        money = (LinearLayout)findViewById(R.id.money_cilk);
        discode = (LinearLayout)findViewById(R.id.textdiscode);
        back_n = (ImageView)findViewById(R.id.back_reward_back) ;


        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Hr = (LinearLayout)findViewById(R.id.texthr);
        user.child(uid).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                User login = dataSnapshot.getValue(User.class);

                point.setText(login.point);
                point.setTextSize(30);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Hr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),Hr_page.class);
                startActivity(intent);
            }
        });

        money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),Money_page.class);
                startActivity(intent);
            }
        });

        discode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),Discode_page_new.class);
                startActivity(intent);
            }
        });

        back_n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }
}
