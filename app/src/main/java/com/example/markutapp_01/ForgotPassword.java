package com.example.markutapp_01;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class ForgotPassword extends AppCompatActivity
{
    EditText emailID;
    TextInputLayout emailError;
    Button proceed;
    Session session;
    Context cntx;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        session = new Session(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        emailID=(EditText)findViewById(R.id.email);
        emailError=(TextInputLayout)findViewById(R.id.emailError);
        proceed =(Button) findViewById(R.id.forgotpwdproceed);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sec_email=emailID.getText().toString();
                System.out.println("heloooooooo" +sec_email);
                session.setSecUserEmail(sec_email);
                if(isEmailCorrect()) {
                    Intent intent = new Intent(getApplicationContext(), ValidateSecurityQuestions.class);
                    startActivity(intent);
                }
            }
        });
    }



    public boolean isEmailCorrect()
    {
        boolean isEmailValid = false;



        // Check for a valid email address.
        if (emailID.getText().toString().isEmpty())
        {
            emailError.setError(getResources().getString(R.string.email_error));
        }

        else if (!Patterns.EMAIL_ADDRESS.matcher(emailID.getText().toString()).matches())
        {
            emailError.setError(getResources().getString(R.string.error_invalid_email));
        }

        else
        {
            isEmailValid = true;
            emailError.setErrorEnabled(false);
        }

        return isEmailValid;
    }
}