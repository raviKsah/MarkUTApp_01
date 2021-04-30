package com.example.markutapp_01;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditAdvertisement extends AppCompatActivity
{
	DatabaseReference firebaseAuth;
	Intent intent = null;

	EditText title = null;
	EditText desc = null;
	EditText price = null;
	TextView PostCategorySpinner = null;
	Switch deactivate = null;

	String category = "";

	public static final String [] PostCategory =
	{
		"Electronics", "Furniture", "Books", "Clothing", "Vehicle"
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_advertisement);

		PostCategorySpinner = findViewById(R.id.editPostCategorySpinner);
		deactivate = (Switch)findViewById(R.id.deactivate);

		firebaseAuth = FirebaseDatabase.getInstance().getReference("Advertisements");

		intent = getIntent();

		getAdDetails();

		PostCategorySpinner.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				category = showCategoryDialogue();
			}
		});

		deactivate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if(isChecked)
				{
					title.setEnabled(false);
					desc.setEnabled(false);
					PostCategorySpinner.setEnabled(false);
					price.setEnabled(false);

					displayMessage(
							"WARNING",
							"If you submit the post with this switch on, the post will be deactivated. It will not appear on the dashboard, and you will not be able to edit it.");
				}

				else
				{
					title.setEnabled(true);
					desc.setEnabled(true);
					PostCategorySpinner.setEnabled(true);
					price.setEnabled(true);
				}

			}
		});
	}

	public void getAdDetails()
    {
        firebaseAuth.orderByChild("date_created").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                int i = 0;

                for (DataSnapshot datas : dataSnapshot.getChildren())
                {
                    if(datas.child("ad_id").getValue().toString().equals(intent.getStringExtra("adID")))
					{
						title = (EditText)findViewById(R.id.editPostTitle);
						desc = (EditText)findViewById(R.id.editPostDescription);
						price = (EditText)findViewById(R.id.editProductPrice);

						title.setText(datas.child("title").getValue().toString());
						desc.setText(datas.child("description").getValue().toString());
//						category = datas.child("category").getValue().toString();
						price.setText(datas.child("price").getValue().toString());

						break;
					}
                }
            }

			@Override
			public void onCancelled(@NonNull DatabaseError error)
			{
				return;
			}
        });
    }

	public void displayMessage(String title, String message)
	{
		android.app.AlertDialog alert = new android.app.AlertDialog.Builder(EditAdvertisement.this).create();
		alert.setTitle(title);
		alert.setMessage(message);
		alert.setButton(android.app.AlertDialog.BUTTON_NEUTRAL, "OK",
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				});

		alert.show();
	}

	private String showCategoryDialogue()
	{
		final String[] cat = new String[1];
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Product Category").setItems(PostCategory, new DialogInterface.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				String category = PostCategory[which];

				PostCategorySpinner.setText(category);
				cat[0] =PostCategorySpinner.getText().toString();

			}
		}).show();
		return cat[0];
	}
}