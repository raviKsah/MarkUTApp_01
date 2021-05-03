package com.example.markutapp_01;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    EditText firstName,lastName, email, phone, password,answer,password2;
    Button register;
    TextView login;
    boolean isFirstNameValid,isLastNameValid, isEmailValid, isPhoneValid, isPasswordValid, isPassword2Valid, isAnswerValid;
    TextInputLayout firstNameError,lastNameError, emailError, phoneError, passError, passError2, answerError;
    String question ="";
    String type="End User";
    DatabaseReference firebaseAuth;
    Spinner dropdown;
    String items[]=new String[] {"What is your favorite book?", "What is your favorite band?", "What is your favorite food?"};

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth= FirebaseDatabase.getInstance().getReference("User_Details");
        firstName=(EditText)findViewById(R.id.FirstName);
        lastName=(EditText)findViewById(R.id.Last_name);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.phone);
        password = (EditText) findViewById(R.id.password);
        password2 = (EditText)findViewById(R.id.promptReenter_password);
        login = (TextView) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
        answer=(EditText)findViewById(R.id.SecurityAns);
//        radioGroup = (RadioGroup)findViewById(R.id.radGroup);

        firstNameError=(TextInputLayout)findViewById(R.id.firstNameError);
        lastNameError=(TextInputLayout)findViewById(R.id.lastNameError);
        emailError = (TextInputLayout) findViewById(R.id.emailError);
        phoneError = (TextInputLayout) findViewById(R.id.phoneError);
        passError = (TextInputLayout) findViewById(R.id.passError);
        passError2 = (TextInputLayout)findViewById(R.id.Re_enter_pwd);
        answerError = (TextInputLayout)findViewById(R.id.securityAnsError);

        //get the spinner from the xml.
        dropdown = findViewById(R.id.spinner);
             //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
         adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                question = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        System.out.println("question:" +question);

        register.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                SetValidation();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // redirect to LoginActivity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    public void SetValidation() {
        if(firstName.getText().toString().isEmpty()){
            firstNameError.setError("Please enter first name");
            isFirstNameValid=false;
        }else{
            isFirstNameValid=true;
            firstNameError.setErrorEnabled(false);
        }
        if(lastName.getText().toString().isEmpty()){
            lastNameError.setError("Please enter first name");
            isLastNameValid=false;
        }else{
            isLastNameValid=true;
            lastNameError.setErrorEnabled(false);
        }


        if (email.getText().toString().isEmpty()) {
            emailError.setError(getResources().getString(R.string.email_error));
            isEmailValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            emailError.setError(getResources().getString(R.string.error_invalid_email));
            isEmailValid = false;
        } else  {
            isEmailValid = true;
            emailError.setErrorEnabled(false);
        }

        // Check for a valid phone number.
        if (phone.getText().toString().isEmpty()) {
            phoneError.setError(getResources().getString(R.string.phone_error));
            isPhoneValid = false;
        } else  {
            isPhoneValid = true;
            phoneError.setErrorEnabled(false);
        }

        // Check for a valid password.
        if (password.getText().toString().isEmpty()) {
            passError.setError(getResources().getString(R.string.password_error));
            isPasswordValid = false;
        } else if (password.getText().length() < 6) {
            passError.setError(getResources().getString(R.string.error_invalid_password));
            isPasswordValid = false;
        } else  {
            isPasswordValid = true;
            passError.setErrorEnabled(false);
        }

        if(password2.getText().toString().isEmpty())
        {
            passError2.setError("Please reenter the password.");
            isPassword2Valid = false;
        }

        else if(!password2.getText().toString().equals(password.getText().toString()))
        {
            passError2.setError("The passwords you entered do not match.");
            isPassword2Valid = false;
        }

        else
        {
            isPassword2Valid = true;
            passError2.setErrorEnabled(false);
        }

        if(answer.getText().toString().isEmpty())
        {
            answerError.setError("Please provide an answer to your security question.");
            isAnswerValid = false;
        }

        else
        {
            isAnswerValid = true;
            answerError.setErrorEnabled(false);
        }

        if (isFirstNameValid && isLastNameValid && isEmailValid && isPhoneValid && isPasswordValid && isPassword2Valid && isAnswerValid) {
            verifyUser();
        }

    }
    public void insertDataToFB() {
        String first_name = firstName.getText().toString();
        String last_name = lastName.getText().toString();
        String email_id = email.getText().toString();
        String user_password = password.getText().toString();
        String contact = phone.getText().toString();
        String sec_answer=answer.getText().toString();
        //create a list of items for the spinner.


        System.out.println(question);

        if (!TextUtils.isEmpty(email_id)) {
            String id = firebaseAuth.push().getKey();
            User_Details user = new User_Details(first_name, last_name, email_id, user_password, contact, type,question,sec_answer);
            firebaseAuth.child(id).setValue(user);
            Toast.makeText(this, "User Added", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);


        }
    }
        public void verifyUser(){

            String emailId=email.getText().toString();
            firebaseAuth.orderByChild("email_id").equalTo(emailId).addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()){

                        Toast.makeText(RegisterActivity.this,"This user already exists!",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(RegisterActivity.this, RegisterActivity.class);
                        startActivity(intent);
                    }
                    else{
                        insertDataToFB();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(RegisterActivity.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
                }
            });

        }
    }
