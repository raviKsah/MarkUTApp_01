package com.example.markutapp_01;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Dictionary;
import java.util.Hashtable;

public class ValidateSecurityQuestions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_security_questions);
        displaySecurityQuestions();
    }

    public String[] getSecurityQuestions()
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
    }

    public void displaySecurityQuestions()
    {
        String[] questions = getSecurityQuestions();

        TextView q1 = (TextView)findViewById(R.id.question1);

        q1.setHint(questions[0]);
    }

    public void submitAnswers(View view)
    {
        TextInputLayout questionError = (TextInputLayout)findViewById(R.id.questionError);

        if(!validateSecurityQuestions())
        {
            questionError.setError("Your answer to the security question is incorrect.");
            return;
        }

        //startActivity(new Intent(com.elevenzon.MarkUTApp.ValidateSecurityQuestions.this, ChangePassword.class));

        Intent intent = new Intent(getApplicationContext(), ChangePassword.class);
        startActivity(intent);

    }

    public boolean validateSecurityQuestions()
    {
        boolean isCorrectAnswer = false;

        String[] questions = getSecurityQuestions();
        Dictionary answers = getAnswers();

        String question = questions[0];
        String answer = (String)answers.get(question);

        EditText submittedAnswer = findViewById(R.id.question1);

        if(submittedAnswer.getText().toString().equals(answer))
            isCorrectAnswer = true;

        return isCorrectAnswer;
    }
}