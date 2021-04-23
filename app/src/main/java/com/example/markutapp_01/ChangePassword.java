package com.example.markutapp_01;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangePassword extends AppCompatActivity
{
	DatabaseReference firebaseAuth;
	EditText passwordFirst;
	EditText passwordSecond;
	TextInputLayout pw1Err;
	TextInputLayout pw2Err;
	Button submit;
	Session session;
	String password;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);
		session=new Session(this);
		passwordFirst = (EditText)findViewById(R.id.newPassword1);
		passwordSecond = (EditText)findViewById(R.id.newPassword2);
		pw1Err = (TextInputLayout)findViewById(R.id.pw1Error);
		pw2Err = (TextInputLayout)findViewById(R.id.pw2Error);
		submit=(Button)findViewById(R.id.submitNewPassword);
		firebaseAuth = FirebaseDatabase.getInstance().getReference("User_Details");
		submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isPasswordCorrect())
				{
					updatePasswordInFb();
				}
			}
		});

	}



	public boolean isPasswordCorrect()
	{
		boolean isPasswordValid = false;



		if (passwordFirst.getText().toString().isEmpty())
		{
			pw1Err.setError("Please enter a password.");
		}

		else if (passwordSecond.getText().toString().isEmpty())
		{
			pw2Err.setError("Please re-enter your password.");
		}

		else if (passwordFirst.getText().length() < 6)
		{
			pw1Err.setError("Please enter a minimum password of 6 characters.");
		}

		else if(!(passwordFirst.getText().toString().equals(passwordSecond.getText().toString())))
		{
			pw2Err.setError("Your passwords do not match.");
		}

		else
		{
			isPasswordValid = true;
			pw1Err.setErrorEnabled(false);
			pw2Err.setErrorEnabled(false);
		}

		return isPasswordValid;
	}

	public void displayMessage(String title, String message)
	{
		AlertDialog alert = new AlertDialog.Builder(com.example.markutapp_01.ChangePassword.this).create();
		alert.setTitle(title);
		alert.setMessage(message);
		alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				});

		alert.show();
	}
	public void updatePasswordInFb(){

	String id=session.getSecUserEmail();
	String newPassword=passwordFirst.getText().toString();
		firebaseAuth.orderByChild("email_id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {

				String getPassword = "";
				for (DataSnapshot datas : dataSnapshot.getChildren()) {
					datas.getRef().child("password").setValue(newPassword);
				}

				displayMessage("PASSWORD RESET", "Your password has successfully been reset.");
				//startActivity(new Intent(com.elevenzon.MarkUTApp.ChangePassword.this, LoginActivity.class));
				Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
				startActivity(intent);
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
				Toast.makeText(ChangePassword.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
			}
		});
	}
}