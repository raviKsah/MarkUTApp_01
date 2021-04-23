package com.example.markutapp_01;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import java.util.Dictionary;
import java.util.Hashtable;

public class ValidateSecurityQuestions extends AppCompatActivity {

    DatabaseReference firebaseAuth;
    Session session;
    EditText answer;
    Button proceed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_security_questions);
        session = new Session(this);
        firebaseAuth= FirebaseDatabase.getInstance().getReference("User_Details");
        answer = (EditText) findViewById(R.id.question1);
        proceed= (Button) findViewById(R.id.verifyAndProceed);
        displaySecurityQuestions();
        proceed.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           validateSecurityAnswer();
                                       }
                                   }
        );
    }

   /** public String[] getSecurityQuestions()
    {
        String[] questions = { "What is your favorite book?", "What is your favorite band?", "What is your favorite food?" };

        return questions;
    }

    public Dictionary getAnswers()
    {
        String[] questions = getSecurityQuestions();

        Dictionary answers = new Hashtable<String, String>();

        answers.put(questions[0], "Paradise Lost");
        answers.put(questions[1], "Red Hot Chili Peppers");
        answers.put(questions[2], "Sushi");

        return answers;
    }**/

    public void displaySecurityQuestions()
    {
        String id=session.getSecUserEmail();
        firebaseAuth.orderByChild("email_id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String getQuestion = "";
                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                    getQuestion = datas.child("security_question").getValue().toString();
                }
                TextView q1 = (TextView)findViewById(R.id.question1);

                q1.setHint(getQuestion);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ValidateSecurityQuestions.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });



    }




    public void validateSecurityAnswer(){
        String id=session.getSecUserEmail();
        firebaseAuth.orderByChild("email_id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String getAnswer = "";
                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                    getAnswer = datas.child("security_answer").getValue().toString();
                }
                if(getAnswer.equals(answer.getText().toString())){
                    Intent intent = new Intent(getApplicationContext(), ChangePassword.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(ValidateSecurityQuestions.this, "Incorrect Answer", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ValidateSecurityQuestions.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}