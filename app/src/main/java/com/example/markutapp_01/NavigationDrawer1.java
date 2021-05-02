package com.example.markutapp_01;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NavigationDrawer1 extends AppCompatActivity
{
    DatabaseReference firebaseAuth;

    String searchValue;
    ImageButton report_btn_logo;
    String items;
    private AppBarConfiguration mAppBarConfiguration;

    String category;
    private Globals global = Globals.getInstance();

    //widgets
    RecyclerView recyclerView;
    //firebase:
    private DatabaseReference myRef;
    //variables:
    private ArrayList<Messages> messagesList;
    private RecyclerAdapter recyclerAdapter;
    private RecyclerAdapter recyclerAdapter1;
    Spinner categoryList;
    private MyListingsAdapter myListingAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigation_drawer1);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        categoryList = (Spinner) findViewById(R.id.searchCategories);

//        firebaseAuth = FirebaseDatabase.getInstance().getReference("Advertisements");

        FloatingActionButton fab = findViewById(R.id.sell);
        ImageButton report_btn = findViewById(R.id.report_btn_logo);

/*
        report_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reportChecker("");

            }
        });
*/

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

        View headerView = navigationView.getHeaderView(0);
        TextView headerUser = (TextView) headerView.findViewById(R.id.textView100);
        TextView headerEmail = (TextView) headerView.findViewById(R.id.textView101);
        headerUser.setText(global.getUser().getFirst_name());
        headerEmail.setText(global.getUser().getEmail_id());

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




       /** searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBar.setIconified(false);
                searchValue = searchBar.getQuery().toString();
                System.out.println("Search value"+searchValue);
                GetDataFromFirebase(searchValue);
            }
        });**/


        navigationView.getMenu().findItem(R.id.logout).setOnMenuItemClickListener(menuItem -> {
            global.clearUser();
            Toast.makeText(NavigationDrawer1.this, "Logout Successful", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(NavigationDrawer1.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        });

        navigationView.getMenu().findItem(R.id.sell).setOnMenuItemClickListener(menuItem ->
        {
            Intent intent = new Intent(getApplicationContext(), PostAd.class);
            startActivity(intent);
            return true;
        });



        recyclerView=findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

       /** @Override
        public void setOnQueryTextListener(OnQueryTextListener listener) {
            super.setOnQueryTextListener(listener);
            this.listener = listener;
            mSearchSrcTextView = this.findViewById(android.support.v7.appcompat.R.id.search_src_text);
            mSearchSrcTextView.setOnEditorActionListener((textView, i, keyEvent) -> {
                if (listener != null) {
                    listener.onQueryTextSubmit(getQuery().toString());
                }
                return true;
            });
        }**/

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchValue = searchBar.getQuery().toString();
                System.out.println("Search value"+searchValue);
                category = categoryList.getSelectedItem().toString();
                System.out.println("Spinner value" +category);
                if(!(searchValue.isEmpty())) {
                    GetDataFromFirebase(searchValue,category);
                }
                else{
                    System.out.println("asdfghjkl");
                    category = categoryList.getSelectedItem().toString();
                    GetDataFromFirebase(category);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if(!(newText.isEmpty())) {
                    category = categoryList.getSelectedItem().toString();
                    GetDataFromFirebase(newText,category);
                }
                else{
                    System.out.println("asdfghjkl");
                    category = categoryList.getSelectedItem().toString();
                    GetDataFromFirebase(category);
                }
                return false;
            }
        });


       /* categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                items=parent.getItemAtPosition(position).toString();
                GetDataFromFirebase(items);
            }
        });*/
        categoryList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                items=parent.getItemAtPosition(position).toString();
                System.out.println("categoryyyyyyyyyyyyyyyyyyyy"+items);
                GetDataFromFirebase(items);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                GetDataFromFirebase(false);
            }
        });

        myRef=FirebaseDatabase.getInstance().getReference("Advertisements");
        //Arraylist
        messagesList=new ArrayList<>();
        ClearAll();
        GetDataFromFirebase(false);

        navigationView.getMenu().findItem(R.id.nav_gallery).setOnMenuItemClickListener(menuItem ->
        {
            fab.setVisibility(View.GONE);
            GetDataFromFirebase(true);
            return true;
        });

        navigationView.getMenu().findItem(R.id.viewDashboard).setOnMenuItemClickListener(menuItem ->
        {
            fab.setVisibility(View.VISIBLE);
            GetDataFromFirebase(false);
            return true;
        });
    }


/*    public void editAd(View view)
    {
        LinearLayout ll = (LinearLayout)findViewById(R.id.adInfoEdit);

        TextView v = (TextView)ll.getChildAt(1);

        Intent intent = new Intent(NavigationDrawer1.this, EditAdvertisement.class);
        intent.putExtra("adID", view.getId());
        startActivity(intent);
    }
*/

    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
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
        User_Details user = global.getUser();

        if(user.type.toLowerCase().equals("admin"))
        {
            NavigationView navigationView = findViewById(R.id.nav_view);

            MenuItem sell = navigationView.getMenu().findItem(R.id.sell);
            MenuItem myListings = navigationView.getMenu().findItem(R.id.nav_gallery);

            sell.setVisible(false);
            myListings.setVisible(false);
        }
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }



    private void GetDataFromFirebase(String categorySelected) {
        //Query query=myRef.child();
        System.out.println(categorySelected);


        // searchValue = ""

        //Divya Krishna will fetch values based on search value

        // based on the date advertisement should display (order by date) //
        if(categorySelected.equals("All")) {
            myRef.addValueEventListener(new ValueEventListener() {
                @Override

                public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                    ClearAll();

                    for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                        Messages messages = new Messages();
                        messages.setImageUrl(snapshot.child("image_path").getValue().toString());
                        messages.setImageTitle(snapshot.child("title").getValue().toString());
                        messages.setPrice(snapshot.child("price").getValue().toString());
                        //  System.out.println("heyyyyyyyyy" + snapshot.child("image_path").getValue().toString());
                        messagesList.add(messages);
                    }
                    recyclerAdapter = new RecyclerAdapter(getApplicationContext(), messagesList);
                    recyclerView.setAdapter(recyclerAdapter);
                    recyclerAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else{
            myRef.orderByChild("category").equalTo(categorySelected).addValueEventListener(new ValueEventListener() {
                @Override

                public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                    ClearAll();

                    for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                        Messages messages = new Messages();
                        messages.setImageUrl(snapshot.child("image_path").getValue().toString());
                        messages.setImageTitle(snapshot.child("title").getValue().toString());
                        messages.setPrice(snapshot.child("price").getValue().toString());
                        //  System.out.println("heyyyyyyyyy" + snapshot.child("image_path").getValue().toString());
                        messagesList.add(messages);
                    }
                    recyclerAdapter = new RecyclerAdapter(getApplicationContext(), messagesList);
                    recyclerView.setAdapter(recyclerAdapter);
                    recyclerAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }

    private void GetDataFromFirebase(boolean myListing)
    {
            myRef.addValueEventListener(new ValueEventListener()
            {

                public void onDataChange(@NonNull DataSnapshot datasnapshot)
                {
                    ClearAll();

                    User_Details user = global.getUser();

                    for (DataSnapshot snapshot : datasnapshot.getChildren())
                    {
                        Messages messages = new Messages();
                        messages.setAdID(snapshot.child("ad_id").getValue().toString());
                        messages.setImageUrl(snapshot.child("image_path").getValue().toString());
                        messages.setImageTitle(snapshot.child("title").getValue().toString());
                        messages.setPrice(snapshot.child("price").getValue().toString());
                        System.out.println("heyyyyyyyyy" + snapshot.child("image_path").getValue().toString());

                        if (myListing && !user.email_id.equals(snapshot.child("advertiser").getValue().toString()))
                        {
                            continue;
                        }

                        messagesList.add(messages);
                    }

                    if (myListing)
                    {
                        myListingAdapter = new MyListingsAdapter(getApplicationContext(), messagesList);
                        recyclerView.setAdapter(myListingAdapter);
                        myListingAdapter.notifyDataSetChanged();
                    }

                    else
                     {
                        recyclerAdapter = new RecyclerAdapter(getApplicationContext(), messagesList);
                        recyclerView.setAdapter(recyclerAdapter);
                        recyclerAdapter.notifyDataSetChanged();
                     }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error)
                {

                }
            });
        }

    private void GetDataFromFirebase(String text, String categoryItem) {
        System.out.println("emptyyyyyyyyyyyy"+text);
         if(!text.isEmpty()) {
            //Query query=myRef.child();
            System.out.println("searchhhhhhhhhhhhhhhhh" + categoryItem);
            myRef.orderByChild("category").equalTo(categoryItem).addValueEventListener(new ValueEventListener() {

                @Override

                public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                    ClearAll();

                    for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                        String t = snapshot.child("title").getValue().toString();
                        String patternString = ".*" + text + ".*";

                        Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);

                        Matcher matcher = pattern.matcher(t);


                        if (matcher.matches()) {
                            Messages messages = new Messages();
                            messages.setImageUrl(snapshot.child("image_path").getValue().toString());
                            messages.setImageTitle(snapshot.child("title").getValue().toString());
                            messages.setPrice(snapshot.child("price").getValue().toString());
                            // System.out.println("heyyyyyyyyy" + snapshot.child("image_path").getValue().toString());
                            messagesList.add(messages);
                            // System.out.println("sizeeeeeeeeee"+messages.getImageUrl());
                        }
                        recyclerAdapter1 = new RecyclerAdapter(getApplicationContext(), messagesList);
                        recyclerView.setAdapter(recyclerAdapter1);
                        recyclerAdapter1.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });





        }
        else{

            myRef.addValueEventListener(new ValueEventListener() {
                @Override

                public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                    ClearAll();

                    for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                        Messages messages = new Messages();
                        messages.setImageUrl(snapshot.child("image_path").getValue().toString());
                        messages.setImageTitle(snapshot.child("title").getValue().toString());
                        messages.setPrice(snapshot.child("price").getValue().toString());
                        //  System.out.println("heyyyyyyyyy" + snapshot.child("image_path").getValue().toString());
                        messagesList.add(messages);
                    }
                    recyclerAdapter = new RecyclerAdapter(getApplicationContext(), messagesList);
                    recyclerView.setAdapter(recyclerAdapter);
                    recyclerAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
    }




    public void reportChecker(final String postkey) {

//        myRef = database.getReference("under_report");
//        myRef.orderByChild("under_report");
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
     //      final String uid = user.getUid();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(postkey).hasChild("")){
                    report_btn_logo.setImageResource(R.drawable.ic_baseline_flag_24);
                }else {
                    report_btn_logo.setImageResource(R.drawable.ic_baseline_outlined_flag_24);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



    private void ClearAll () {
            if (messagesList != null) {
                messagesList.clear();
                if (recyclerAdapter != null) {
                    recyclerAdapter.notifyDataSetChanged();
                }
            }
            messagesList = new ArrayList<>();
        }


    public void displayAdvertisementCategories()
    {


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