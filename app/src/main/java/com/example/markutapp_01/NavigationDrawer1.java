package com.example.markutapp_01;

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
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class NavigationDrawer1 extends AppCompatActivity {


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

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer1);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.sell);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

        //searchBar.setIconifiedByDefault(true);

        MyListAdapter adapter=new MyListAdapter(this, maintitle, subtitle,imgid);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
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