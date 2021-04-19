package com.example.markutapp_01;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

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
        Spinner questionList = (Spinner) findViewById(R.id.securityQuestions);

        String[] questions = getSecurityQuestions();
        List<String> spinnerQuestions = new ArrayList<String>();

        for(int i = 0; i < questions.length; i++)
        {
            spinnerQuestions.add(questions[i]);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, questions);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        questionList.setAdapter(dataAdapter);
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

        Spinner uiQuestions = (Spinner)findViewById(R.id.securityQuestions);
        Dictionary answers = getAnswers();

        int questionID = uiQuestions.getSelectedItemPosition();
        String[] questions = getSecurityQuestions();
        String question = questions[questionID];
        String answer = (String)answers.get(question);

        EditText submittedAnswer = findViewById(R.id.question1);

        if(submittedAnswer.getText().toString().equals(answer))
            isCorrectAnswer = true;

        return isCorrectAnswer;
    }
}