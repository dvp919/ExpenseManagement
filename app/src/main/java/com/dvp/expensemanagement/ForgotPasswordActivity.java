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

public class ForgotPasswordActivity extends AppCompatActivity {

    private Button btnForgot;
    private EditText mEmail;
    private TextView mLogin;

    private ProgressDialog mDialog;

    //Firebase...
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();

        mDialog = new ProgressDialog(this);

        ForgotPassword();

    }

    private void ForgotPassword()
    {
        btnForgot = findViewById(R.id.btn_forgot);
        mEmail = findViewById(R.id.email_forgot_password);
        mLogin = findViewById(R.id.login_forgot);

        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email Required..");
                    return;
                }

                mDialog.setMessage("Sending Email for reset Password...");
                mDialog.show();

                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            mDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Email send for Reset Password",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        else
                        {
                            mDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Unable to send Email, Please try again..",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

    }
}