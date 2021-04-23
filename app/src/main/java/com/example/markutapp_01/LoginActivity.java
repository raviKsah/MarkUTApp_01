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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button login;
    TextView register, forgotpassword;
    boolean isEmailValid, isPasswordValid;
    TextInputLayout emailError, passError;
    DatabaseReference firebaseAuth;

    private Session session;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        session = new Session(this);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        register = (TextView) findViewById(R.id.register);
        emailError = (TextInputLayout) findViewById(R.id.emailError);
        passError = (TextInputLayout) findViewById(R.id.passError);
        forgotpassword = (TextView) findViewById(R.id.forgotpasssword);

//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SetValidation();
//            }
//        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // redirect to View Dashboard
                Intent intent = new Intent(getApplicationContext(), NavigationDrawer1.class);
                startActivity(intent);

                firebaseAuth = FirebaseDatabase.getInstance().getReference("User_Details");
                SetValidation();

            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // redirect to RegisterActivity
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // redirect to RegisterActivity
                Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
                startActivity(intent);
            }
        });
    }

    public void SetValidation() {
        // Check for a valid email address.
        if (email.getText().toString().isEmpty()) {
            emailError.setError(getResources().getString(R.string.email_error));
            isEmailValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            emailError.setError(getResources().getString(R.string.error_invalid_email));
            isEmailValid = false;
        } else {
            isEmailValid = true;
            emailError.setErrorEnabled(false);
        }

        // Check for a valid password.
        if (password.getText().toString().isEmpty()) {
            passError.setError(getResources().getString(R.string.password_error));
            isPasswordValid = false;
        } else {
            isPasswordValid = true;
            passError.setErrorEnabled(false);
        }

        if (isEmailValid && isPasswordValid) {
            //Toast.makeText(getApplicationContext(), "Successfully", Toast.LENGTH_SHORT).show();
            verifyDetailsInFb();
        }

    }

    public void verifyDetailsInFb() {
        String id = email.getText().toString();
        String user_password = password.getText().toString();


        firebaseAuth.orderByChild("email_id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String getPassword = "";
                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                    getPassword = datas.child("password").getValue().toString();

                }
                if (getPassword.equals(user_password)) {

                    session.setusename(id);


                    Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, ViewDashboard.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}