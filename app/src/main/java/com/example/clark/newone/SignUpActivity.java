package com.example.clark.newone;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSignup;
    TextView btnLogin, btnForgotPass;
    EditText input_email, input_password;
    RelativeLayout activity_sign_up;

    private FirebaseAuth mAuth;
    Snackbar snackbar;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //View
        btnSignup = (Button) findViewById(R.id.buttonSignup);
        btnLogin = (TextView) findViewById(R.id.textViewLogIn);
        btnForgotPass = (TextView) findViewById(R.id.textViewForgotPassSignUp);
        input_email = (EditText) findViewById(R.id.editTextEmailSignUp);
        input_password = (EditText) findViewById(R.id.editTextPasswordSignUp);
        activity_sign_up = (RelativeLayout) findViewById(R.id.activity_sign_up);

        btnSignup.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnForgotPass.setOnClickListener(this);

        //Init Firebase
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.textViewLogIn){
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        }
        else if (view.getId() == R.id.textViewForgotPassSignUp) {
            startActivity(new Intent(SignUpActivity.this, ForgotPassActivity.class));
            finish();
        }
        else if (view.getId() == R.id.buttonSignup) {
            signUpUser(input_email.getText().toString(), input_password.getText().toString());

        }
    }


    private void signUpUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            snackbar = Snackbar.make(activity_sign_up, "Error"+task.getException(),Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                        else{
                            snackbar = Snackbar.make(activity_sign_up, "Registration successful : ",Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                    }
                });
    }
}
