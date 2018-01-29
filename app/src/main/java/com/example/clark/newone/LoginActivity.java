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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnLogin;
    EditText input_email, input_password;
    TextView btnSignup, btnForgotPass;

    RelativeLayout loginLayout;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //View
        btnLogin = (Button) findViewById(R.id.buttonLogin);
        input_email = (EditText) findViewById(R.id.editTextEmail);
        input_password = (EditText) findViewById(R.id.editTextPassword);
        btnSignup = (TextView) findViewById(R.id.textViewSignUp);
        btnForgotPass = (TextView) findViewById(R.id.textViewForgotPass);
        loginLayout = (RelativeLayout) findViewById(R.id.loginLayout);

        btnSignup.setOnClickListener(this);
        btnForgotPass.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        //init FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Check already session
        if(mAuth.getCurrentUser() != null)
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.textViewForgotPass) {
            startActivity(new Intent(LoginActivity.this, ForgotPassActivity.class));
            finish();

        }else if(view.getId() == R.id.textViewSignUp) {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            finish();
        }else if(view.getId() == R.id.buttonLogin) {
            loginUser(input_email.getText().toString(), input_password.getText().toString());
        }
    }

    private void loginUser(String email, final String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            if(password.length() < 8){
                                Snackbar snackBar = Snackbar.make(loginLayout, "Password lenght must be over 8", Snackbar.LENGTH_SHORT);
                                snackBar.show();
                            }
                        }
                        else {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }
                    }
                });
    }
}
