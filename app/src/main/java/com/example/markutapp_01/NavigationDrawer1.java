package com.example.markutapp_01;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NavigationDrawer1 extends AppCompatActivity
{
    DatabaseReference firebaseAuth;
    ListView list;

    // Add empty value to display 5 ads.
    String[] maintitle ={
            "",
            "$130","$70",
            "$20","$180",
            "$3000",
    };

    // Add empty value to display 5 ads.
    String[] subtitle ={
            "",
            "Blue Chair","Office Chair",
            "Fictional Books","Queen size bed",
            "2018 Honda Civic",
    };

    // Add dummy value to display 5 ads.
    Integer[] imgid={
            R.drawable.dowload_1,
            R.drawable.dowload_1,R.drawable.dowload_2,
            R.drawable.dowload_3,R.drawable.dowload_4,
            R.drawable.dowload_5,};

    List<String> adTitle = new ArrayList<String>();
    List<String> adPrice = new ArrayList<String>();
    List<Integer> adImage = new ArrayList<Integer>();

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer1);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseDatabase.getInstance().getReference("Advertisements");

        FloatingActionButton fab = findViewById(R.id.sell);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), PostAd.class);
                startActivity(intent);


//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        SearchView searchBar = (SearchView)findViewById(R.id.search);

        getAdvertisements();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer1, menu);
        displayAdvertisementCategories();
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void getAdvertisements()
    {
        firebaseAuth.orderByChild("date_created").limitToLast(20).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                int i = 0;

                for (DataSnapshot datas : dataSnapshot.getChildren())
                {
                    adTitle.add(datas.child("title").getValue().toString());
                    adPrice.add(datas.child("price").getValue().toString());
                    adImage.add(R.drawable.dowload_1);
                }

                Collections.reverse(adTitle);
                Collections.reverse(adPrice);
                Collections.reverse(adImage);

                // Add empty advertisements to beginning of the list for formatting.
                adTitle.add(0, "");
                adPrice.add(0, "");
                adImage.add(0, R.drawable.dowload_1);

                MyListAdapter adapter=new MyListAdapter(
                        NavigationDrawer1.this,
                        adPrice.toArray(new String[adPrice.size()]),
                        adTitle.toArray(new String[adTitle.size()]),
                        adImage.toArray(new Integer[adImage.size()]));
                list=(ListView)findViewById(R.id.list);
                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                return;
            }
        });
    }

/*    public Integer[] convertToArray(List<int> list)
    {
        int[] convertedArray = new int[list.size()];

        for(int i = 0; i < list.size(); i++)
        {
            convertedArray[i] = list.get(i);
        }

        return convertedArray;
    }
*/
    public void displayAdvertisementCategories()
    {
        Spinner categoryList = (Spinner) findViewById(R.id.searchCategories);

        List<String> categories = new ArrayList<String>();

        categories.add("All");
        categories.add("Apparel");
        categories.add("Appliances");
        categories.add("Books");
        categories.add("Computers");
        categories.add("Electronics");
        categories.add("Home & Garden");
        categories.add("Pet Supplies");
        categories.add("Sports");
        categories.add("Toys & Games");
        categories.add("Vehicles");
        categories.add("Other");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        categoryList.setAdapter(dataAdapter);
    }
}