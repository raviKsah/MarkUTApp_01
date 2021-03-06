package com.example.markutapp_01;

import android.content.Intent;
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
    private Globals global = Globals.getInstance();


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
                // redirect to ForgotPassword
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

                String first_name = "";
                String last_name = "";
                String email_id = "";
                String contact = "";
                String type = "";
                String security_question = "";
                String security_answer = "";

                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                    getPassword = datas.child("password").getValue().toString();

                    first_name = datas.child("first_name").getValue().toString();
                    last_name = datas.child("last_name").getValue().toString();
                    email_id = datas.child("email_id").getValue().toString();
                    contact = datas.child("contact").getValue().toString();
                    type = datas.child("type").getValue().toString();

                    if(type.toLowerCase().equals("admin"))
                    {
                        security_question = "";
                        security_answer = "";
                    }

                    else
                    {
                        security_question = datas.child("security_question").getValue().toString();
                        security_answer = datas.child("security_answer").getValue().toString();
                    }
                }
                if (getPassword.equals(user_password)) {

                    session.setusename(id);

                    User_Details user = new User_Details(first_name, last_name, email_id, user_password,
                                                         contact, type , security_question, security_answer);
                    global.setUser(user);

                    Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_LONG).show();

                        //TextView textView = (TextView) findViewById(R.id.textView101);
                        //textView.setText("text@text.txt");
                        //textView.invalidate();


                    Intent intent = new Intent(LoginActivity.this, NavigationDrawer1.class);
                        intent.putExtra("key","hello");
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