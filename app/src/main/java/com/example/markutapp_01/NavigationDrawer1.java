package com.example.markutapp_01;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
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

public class NavigationDrawer1 extends AppCompatActivity
{
    DatabaseReference firebaseAuth;

    String searchValue;


    private AppBarConfiguration mAppBarConfiguration;
    private Globals global = Globals.getInstance();



    //widgets
    RecyclerView recyclerView;
    //firebase:
    private DatabaseReference myRef;
    //variables:
    private ArrayList<Messages> messagesList;
    private RecyclerAdapter recyclerAdapter;

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
        String searchValue = searchBar.getQuery().toString();


        navigationView.getMenu().findItem(R.id.logout).setOnMenuItemClickListener(menuItem -> {
            global.clearUser();
            Toast.makeText(NavigationDrawer1.this, "Logout Successful", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(NavigationDrawer1.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        });

        recyclerView=findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        myRef=FirebaseDatabase.getInstance().getReference("Advertisements");
        //Arraylist
        messagesList=new ArrayList<>();
        ClearAll();
        GetDataFromFirebase();
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




    private void GetDataFromFirebase() {
        //Query query=myRef.child();
        System.out.println(myRef);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override





           // searchValue = ""

            //Divya Krishna will fetch values based on search value

            // based on the date advertisement should display (order by date) //


            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                ClearAll();
                for(DataSnapshot snapshot:datasnapshot.getChildren()){
                    Messages messages=new Messages();
                    messages.setImageUrl(snapshot.child("image_path").getValue().toString());
                    messages.setImageTitle(snapshot.child("title").getValue().toString());
                    messages.setPrice(snapshot.child("price").getValue().toString());
                    System.out.println("heyyyyyyyyy"+snapshot.child("image_path").getValue().toString() );
                    messagesList.add(messages);
                }
                recyclerAdapter = new RecyclerAdapter(getApplicationContext(),messagesList);
                recyclerView.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void ClearAll(){
        if(messagesList !=null)
        {
            messagesList.clear();
            if(recyclerAdapter !=null)
            {
                recyclerAdapter.notifyDataSetChanged();
            }
        }
        messagesList=new ArrayList<>();
    }


//    public void getAdvertisements()
//    {
//        firebaseAuth.orderByChild("date_created").limitToLast(20).addValueEventListener(new ValueEventListener()
//        {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot)
//            {
//                int i = 0;
//
//                for (DataSnapshot datas : dataSnapshot.getChildren())
//                {
//                    adTitle.add(datas.child("title").getValue().toString());
//                    adPrice.add(datas.child("price").getValue().toString());
//                    adImage.add(R.drawable.dowload_1);
//                }
//
//                Collections.reverse(adTitle);
//                Collections.reverse(adPrice);
//                Collections.reverse(adImage);
//
//                // Add empty advertisements to beginning of the list for formatting.
//                adTitle.add(0, "");
//                adPrice.add(0, "");
//                adImage.add(0, R.drawable.dowload_1);
//
//                MyListAdapter adapter=new MyListAdapter(
//                        NavigationDrawer1.this,
//                        adPrice.toArray(new String[adPrice.size()]),
//                        adTitle.toArray(new String[adTitle.size()]),
//                        adImage.toArray(new Integer[adImage.size()]));
//                list=(ListView)findViewById(R.id.list);
//                list.setAdapter(adapter);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError)
//            {
//                return;
//            }
//        });
//    }

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