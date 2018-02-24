package com.example.clark.newone.Login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clark.newone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private Button cngPass;
    private EditText forgotPassEmail;
    private RelativeLayout activity_forgot;
    private TextView bcklogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        cngPass = (Button) findViewById(R.id.buttonChangePass);
        activity_forgot = (RelativeLayout) findViewById(R.id.activity_forgot_password);
        forgotPassEmail = (EditText) findViewById(R.id.change_pass_email);

        bcklogin = (TextView) findViewById(R.id.textViewBackToLogin);

        cngPass.setOnClickListener(this);
        bcklogin.setOnClickListener(this);

        //Init Firebase
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.textViewBackToLogin) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }else if(view.getId() == R.id.buttonChangePass) {

            String email = forgotPassEmail.getText().toString();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Enter your email", Toast.LENGTH_SHORT).show();
                return;
            }

            resetPassword(forgotPassEmail.getText().toString());
        }
    }

    private void resetPassword(final String email) {

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            Snackbar snackbar = Snackbar.make(activity_forgot, "We have sent password to Email: " + email, Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }else {

                            Snackbar snackbar = Snackbar.make(activity_forgot, "Failed to send password: ", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }

                    }
                });

        final Intent i = new Intent(this, LoginActivity.class);

        Thread timer = new Thread(){
            public void run(){
                try {
                    sleep(3000);
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {

                    startActivity(i);
                    finish();

                }
            }
        };

        timer.start();



    }
}
