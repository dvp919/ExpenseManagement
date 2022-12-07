package com.dvp.expensemanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText mEmail,mPass;
    private Button btnLogin;
    private TextView btn_reg;
    private TextView mForgotPassword;

    private ProgressDialog mDialog;

    //Firebase..
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        }

        mDialog = new ProgressDialog(this);

        loginDetails();




    }

    private void loginDetails()
    {
        mEmail = findViewById(R.id.email_login);
        mPass = findViewById(R.id.password_login);
        btnLogin = findViewById(R.id.btn_login);
        mForgotPassword = findViewById(R.id.forget_password);
        btn_reg = findViewById(R.id.create_account);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String pass = mPass.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email Required..");
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    mPass.setError("Password Required");
                    return;
                }

                mDialog.setMessage("Please Wait...");
                mDialog.show();
                mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            mDialog.dismiss();

                            Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        }
                        else
                        {
                            mDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Please check email and password...",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegistrationActivity.class);
                startActivity(intent);
            }
        });

        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}