package com.example.markutapp_01;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ViewAdvertisement extends AppCompatActivity
{
    DatabaseReference advertisementRef;
    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User_Details");

    ImageView image = null;

    Intent intent = null;

    String adID = "";

    TextView title = null;
    TextView price = null;
    TextView category = null;
    TextView listedDate = null;
    TextView description = null;
    TextView sellerName = null;
    TextView sellerEmail = null;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_advertisement);

        advertisementRef = FirebaseDatabase.getInstance().getReference("Advertisements");

        image = (ImageView)findViewById(R.id.view_ad_image);

        title = (TextView)findViewById(R.id.view_ad_title);
        price = (TextView)findViewById(R.id.view_ad_price);
        category = (TextView)findViewById(R.id.view_ad_category);
        listedDate = (TextView)findViewById(R.id.view_ad_date);
        description = (TextView)findViewById(R.id.view_ad_description);
        sellerName = (TextView)findViewById(R.id.view_ad_seller_name);
        sellerEmail = (TextView)findViewById(R.id.view_ad_seller_email);

        intent = getIntent();

        adID = intent.getStringExtra("adID");

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getAdDetails();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getAdDetails()
    {
        advertisementRef.orderByChild("ad_id").equalTo(adID).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                String email = "";
                for (DataSnapshot datas : dataSnapshot.getChildren())
                {
                    title.setText(datas.child("title").getValue().toString());
                    price.setText("$" + datas.child("price").getValue().toString());
                    category.setText(datas.child("category").getValue().toString());
                    // Can this be changed to MM/dd/yyyy?
                    try
                    {
                        listedDate.setText(convertDateFormat(datas.child("date_created").getValue().toString()));
                    } catch(ParseException e)
                    {
                        e.printStackTrace();
                    }
                    description.setText(datas.child("description").getValue().toString());
                    email = datas.child("advertiser").getValue().toString();
                    sellerEmail.setText(email);

                    String stringURL = intent.getStringExtra("imageURL");

                    Uri uri = Uri.parse(stringURL);

                    Glide.with(ViewAdvertisement.this)
                            .asBitmap()
                            .load(uri)
                            .into(image);
                }

                userRef.orderByChild("email_id").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        for (DataSnapshot datas : snapshot.getChildren())
                        {
                            sellerName.setText(datas.child("first_name").getValue().toString() + " " + datas.child("last_name").getValue().toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                return;
            }
        });
    }

    public String convertDateFormat(String date) throws ParseException
    {
        String newDate = "";

        DateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);

        Date inputDate = inputFormat.parse(date);

        newDate = outputFormat.format(inputDate);

        return newDate;
    }
}