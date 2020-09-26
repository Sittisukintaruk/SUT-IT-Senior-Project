package com.example.rabbit777.recan_new;



import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class User_profile extends AppCompatActivity {
    DatabaseReference user;
    FirebaseDatabase database;
    String uid;
    private TextView name,sub,phone,email,point,password,Fullname;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_user);
        database = FirebaseDatabase.getInstance();
        user = database.getReference("Users");

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        name = (TextView)findViewById(R.id.text_name_pr);
        sub = (TextView)findViewById(R.id.text_sub);
        email = (TextView)findViewById(R.id.text_mail);
        phone = (TextView)findViewById(R.id.text_tel);
        password = (TextView)findViewById(R.id.text_password_profile);
        Fullname = (TextView)findViewById(R.id.text_fullname);

        SetUserDetail(uid);


    }

    private void SetUserDetail(String uid)
    {
        user.child(uid).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                User user = dataSnapshot.getValue(User.class);
                name.setText(user.name);
                sub.setText(user.subname);
                email.setText(user.email);
                phone.setText(user.phone);
                password.setText(user.point);
                Fullname.setText(user.name +" "+user.subname);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

}
