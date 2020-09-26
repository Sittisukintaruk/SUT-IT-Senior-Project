package com.example.rabbit777.recan_new;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private EditText Editusername,Editpassword ;
    DatabaseReference user;
    FirebaseDatabase database;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        user = database.getReference("Users");
        TextView register = (TextView) findViewById(R.id.Register);
        Editusername = (EditText)findViewById(R.id.edit_user);
        Editpassword = (EditText)findViewById(R.id.edit_password) ;
        saveLoginCheckBox = (CheckBox)findViewById(R.id.saveLoginCheckBox);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            Editusername.setText(loginPreferences.getString("username", ""));
            saveLoginCheckBox.setChecked(true);
        }

        register.setPaintFlags(register.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
        Button login = (Button) findViewById(R.id.login);
        mAuth = FirebaseAuth.getInstance();
    login.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            final String Username_email = Editusername.getText().toString().trim();
            final String Password = Editpassword.getText().toString().trim();

            if (saveLoginCheckBox.isChecked()) {
                loginPrefsEditor.putBoolean("saveLogin", true);
                loginPrefsEditor.putString("username", Username_email);
                loginPrefsEditor.commit();
            } else {
                loginPrefsEditor.clear();
                loginPrefsEditor.commit();
            }

            if(Username_email.isEmpty())
            {
                Editusername.setError("กรุณาระบุอีเมล์");
                Editusername.requestFocus();
                return;
            }
            if(Password.isEmpty())
            {
                Editpassword.setError("กรุณาระบุรหัสผ่าน");
                Editpassword.requestFocus();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);


            mAuth.signInWithEmailAndPassword(Username_email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    progressBar.setVisibility(View.GONE);
                    if(task.isSuccessful())
                    {

                        alertDisplayerregister("Login Success ", "เข้าสู่ระบบเรียบร้อย ");

                    }else
                    {

                        alertDisplayer("Login Fail ", "อีเมล์หรือรหัสผ่านของคุณไม่ถูกต้องกรุณาตรวจสอบแล้วลองอีกครั้ง");

                    }
                }
            });



        }
    });

        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SignUp.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.nothing);

            }
        });



    }

   // private void signIn(final String username,final String password)
    //{
        //user.child("gsZmqEVBD1WIx2IXgeC6NAL6k8b2").addListenerForSingleValueEvent(new ValueEventListener() {
         //   @Override
          //  public void onDataChange(@NonNull DataSnapshot dataSnapshot)
         //   {

              //      User login = dataSnapshot.getValue(User.class);
             //   Toast.makeText(MainActivity.this,"Username: " + login.getUsername() +"Password : " + login.getPassword(), Toast.LENGTH_SHORT).show();

               // if(!username.isEmpty())
                    //{


                        // if (login.getPassword().equals(password) && login.getUsername().equals(username) )
                       // {
                       //     Intent intent = new Intent(getApplicationContext(),Homepage.class);
                       //     startActivity(intent);
                      //      overridePendingTransition(R.anim.fade_in,R.anim.nothing);
                      //  }else
                      //  {
                      //      Toast.makeText(MainActivity.this,"Password is wrong", Toast.LENGTH_SHORT).show();
                      //  }
                   // }
                  //  else
                   // {

                       // Toast.makeText(MainActivity.this,"Username is Wrong", Toast.LENGTH_SHORT).show();


                   // }

              //  }



           // @Override
          //  public void onCancelled(@NonNull DatabaseError databaseError)
          //  {

          //  }
       // });
  //  }

 /*   @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
    }*/
    void alertDisplayerregister(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(),Homepage.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in,R.anim.nothing);
                    }
                });

        AlertDialog ok = builder.create();
        ok.show();
    }

    void alertDisplayer(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                        finish();
                        overridePendingTransition(0,0);
                        startActivity(getIntent());
                        overridePendingTransition(0,0);



                    }
                });

        AlertDialog ok = builder.create();
        ok.show();
    }

    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

}