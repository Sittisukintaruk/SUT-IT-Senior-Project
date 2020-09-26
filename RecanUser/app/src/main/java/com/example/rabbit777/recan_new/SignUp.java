package com.example.rabbit777.recan_new;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SignUp extends AppCompatActivity {
    private EditText inputEmail, inputPassword, tel, Name, Supname, password;
    private Button btnSignup;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private String Barcode,point,vail ="";
    private  double pot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        btnSignup = (Button) findViewById(R.id.button_sigup);
        inputEmail = (EditText) findViewById(R.id.edil_email);
        inputPassword = (EditText) findViewById(R.id.password);
        password = (EditText) findViewById(R.id.edil_username);
        tel = (EditText) findViewById(R.id.edil_tel);
        Name = (EditText) findViewById(R.id.edit_user);
        Supname = (EditText) findViewById(R.id.edit_subname);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
        pot = 0.0;

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = Name.getText().toString().trim();
                final String Tel = tel.getText().toString().trim();
                final String email = inputEmail.getText().toString().trim();
                final String subname = Supname.getText().toString().trim();
                final String passwords = password.getText().toString().trim();
                final String re_password = inputPassword.getText().toString().trim();
                point = String.format("%.1f",pot);

                char[] chars1 = "ABCDEF012GHIJKL345MNOPQR678STUVWXYZ9".toCharArray();
                StringBuilder sb1 = new StringBuilder();
                Random random1 = new Random();
                for (int i = 0; i < 10; i++)
                {
                    char c1 = chars1[random1.nextInt(chars1.length)];
                    sb1.append(c1);
                }
                Barcode = sb1.toString();


                if(name.isEmpty())
                {
                    Name.setError("Name required");
                    Name.requestFocus();
                    return;
                }
                if(subname.isEmpty())
                {
                    Supname.setError("subname required");
                    Supname.requestFocus();

                    return;
                }

                if(email.isEmpty())
                {
                    inputEmail.setError("email required");
                    inputEmail.requestFocus();

                    return;
                }

                if(Tel.isEmpty())
                {
                    tel.setError("tel required");
                    tel.requestFocus();

                    return;
                }
                if (tel.length() != 10)
                {
                    tel.setError("คุณกรอกเบอร์มือถือไม่ครบถ้วน");
                    tel.requestFocus();
                    return;
                }

                if(passwords.isEmpty())
                {
                    password.setError("Password required");
                    password.requestFocus();

                    return;
                }
                if(passwords.length() < 6)
                {
                    password.setError("รหัสผ่านต้องมากกว่า 6 ตัวขึ้นไป");
                    password.requestFocus();

                    return;
                }

                if(re_password.isEmpty())
                {
                    inputPassword.setError("Re-Password required");
                    inputPassword.requestFocus();

                    return;
                }
                if(!re_password.equals(passwords))
                {

                    inputPassword.setError("Re-Password is not matched");
                    inputPassword.requestFocus();

                    return;
                }




                mAuth.createUserWithEmailAndPassword(email, passwords).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.VISIBLE);

                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);

                                User user = new User(name, subname, Tel, email, Barcode, passwords, point,vail);
                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            alertDisplayerregister("Register Success ", "ยินดีต้อนรับคุณ " + name + " " + subname);


                                        } else {

                                            alertDisplayer("Register Fail ", task.getException().getMessage());

                                        }
                                    }
                                });


                            } else {
                                progressBar.setVisibility(View.GONE);

                                Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                }


        });

    }



    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null)
        {


        }
    }
    void alertDisplayerregister(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        final Timer timer2 = new Timer();
                        timer2.schedule(new TimerTask()
                        {
                            public void run() {
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();
                            }
                        },500); // the timer will count 5 seconds....

                    }
                });

        AlertDialog ok = builder.create();
        ok.show();
    }
    void alertDisplayer(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this)
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
}







