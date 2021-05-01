package com.example.markutapp_01;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PostAd extends AppCompatActivity {

    // views for button
    private ImageView btnUpload;
    private ImageView btnSelect;
    private TextView PostCategorySpinner;
    Button postAdd;
    String url;
    String adID = "";

    // view for image view
    private ImageView imageView;
    String Storage_Path = "Images/";


    String Database_Path = "Advertisements";

    // Uri indicates, where the image will be picked from


    // request code
    private final int PICK_IMAGE_REQUEST = 22;

    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;

    Uri FilePathUri;
    Session session;
    EditText title;
    EditText description,price;
    DatabaseReference databaseReference;
    String category = "";
    ProgressDialog progressDialog ;

    Switch deactivate = null;

    Intent intent = null;
    boolean isEditPage = false;

    public static final String [] PostCategory =
            {
                    "Apparel","Appliances","Books","Computers", "Electronics", "Home & Garden","Pet Supplies","Sports","Toys & Games", "Vehicle","Other"
            };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        intent = getIntent();
        adID = intent.getStringExtra("adID");

        isEditPage = intent.getBooleanExtra("editAd", false);

        setContentView(R.layout.activity_post_ad1);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);
        progressDialog = new ProgressDialog(PostAd.this);

        btnSelect = findViewById(R.id.SelectImage);
        //btnUpload = findViewById(R.id.SelectImage);
        imageView = findViewById(R.id.SelectImage);
        PostCategorySpinner = findViewById(R.id.PostCategorySpinner);
        title=findViewById(R.id.inputPostTitle);
        description=findViewById(R.id.inputPostDescription);
        price=findViewById(R.id.inputProductPrice);
        postAdd=findViewById(R.id.btnAddProduct);
        session = new Session(this);
        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        deactivate = (Switch)findViewById(R.id.deactivate);

        // on pressing btnSelect SelectImage() is called
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SelectImage();
            }
        });

        PostCategorySpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoryDialogue();


            }
        });

        deactivate.setVisibility(View.GONE);

        deactivate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    title.setEnabled(false);
                    description.setEnabled(false);
                    PostCategorySpinner.setEnabled(false);
                    price.setEnabled(false);

                    displayMessage(
                            "WARNING",
                            "If you submit the post with this switch on, the post will be deactivated. It will not appear on the dashboard, and you will not be able to edit it.");
                }

                else
                {
                    title.setEnabled(true);
                    description.setEnabled(true);
                    PostCategorySpinner.setEnabled(true);
                    price.setEnabled(true);
                }

            }
        });

        // on pressing btnUpload uploadImage() is called
//        btnUpload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {+
//                uploadImage();
//            }
//        });

        postAdd.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(isEditPage)
                {
                    editAd();

                    Intent intent = new Intent(getApplicationContext(), NavigationDrawer1.class);
                    startActivity(intent);
                }

                else
                {
                    if(uploadImage())
                    {
                        Intent intent = new Intent(getApplicationContext(), NavigationDrawer1.class);
                        startActivity(intent);
                    }
                }
            }
        });

        if(isEditPage)
        {
            getAdDetails();
            postAdd.setText("Edit Advertisement");
            deactivate.setVisibility(View.VISIBLE);
        }
    }

    public void getAdDetails()
    {
        databaseReference.orderByChild("date_created").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for (DataSnapshot datas : dataSnapshot.getChildren())
                {
                    if(datas.child("ad_id").getValue().toString().equals(adID))
                    {
                        title.setText(datas.child("title").getValue().toString());
                        description.setText(datas.child("description").getValue().toString());
                        PostCategorySpinner.setText(datas.child("category").getValue().toString());
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
        android.app.AlertDialog alert = new android.app.AlertDialog.Builder(PostAd.this).create();
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


    // Select Image method
    private void SelectImage()
    {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Image from here..."),PICK_IMAGE_REQUEST);
    }

    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            // Get the Uri of data
            FilePathUri = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),FilePathUri);
                imageView.setImageBitmap(bitmap);

            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    private void editAd()
    {
        Globals global = Globals.getInstance();
        User_Details user = global.getUser();

//        url = uri.toString();

        databaseReference.orderByChild("ad_id").equalTo(adID).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for(DataSnapshot datas : snapshot.getChildren())
                {
                    /*
                    if(deactivate.isChecked())
                    {
                        datas.child("is_complete").getRef().setValue(true);
                        datas.child("date_completed").getRef().setValue(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
                        datas.child("edit_date").getRef().setValue(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
                        datas.child("was_edited").getRef().setValue(true);
                    }

                    else
                    {
                        datas.child("category").getRef().setValue(PostCategorySpinner.getText().toString());
                        datas.child("description").getRef().setValue(description.getText().toString());
                        datas.child("edit_date").getRef().setValue(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
                        datas.child("editor").getRef().setValue(user.email_id);
                        datas.child("price").getRef().setValue(price.getText().toString());
                        datas.child("title").getRef().setValue(title.getText().toString());
                        datas.child("was_edited").getRef().setValue(true);
                    }*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }


    // UploadImage method
    private boolean uploadImage()
    {
        // Checking whether FilePathUri Is empty or not.
        if (FilePathUri != null) {


            // Setting progressDialog Title.
            progressDialog.setTitle("Image is Uploading...");

            // Showing progressDialog.
            progressDialog.show();

            // Creating second StorageReference.
            StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            storageReference2nd.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    url = uri.toString();


                                    // Getting image name from EditText and store into string variable.
                                    String postTitle = title.getText().toString().trim();
                                    String postDesc = description.getText().toString().trim();
                                    String cost = price.getText().toString().trim();
                                    String createdDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                                    String creator = session.getusename();
                                    String editedDate = "";
                                    Boolean isComplete = false;
                                    Boolean underReport = false;
                                    Boolean wasEdited = false;
                                    String editor = "";
                                    String dateCompleted = "";


                                    // Hiding the progressDialog after done uploading.
                                    progressDialog.dismiss();

                                    // Showing toast message after done uploading.
                                    Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                                    String ImageUploadId = databaseReference.push().getKey();
                                    @SuppressWarnings("VisibleForTests")
                                    PostDetails imageUploadInfo = new PostDetails(ImageUploadId, creator, category, dateCompleted, createdDate, postDesc, editedDate, editor, url , cost, postTitle, isComplete, underReport, wasEdited);
                                    //String ad_id, String advertiser, String category, String date_completed, String date_created, String description, String edited_date, String editor, String image_path, String price, String title, Boolean is_complete, Boolean under_report, Boolean was_edited
                                    // Getting image upload ID.


                                    // Adding image upload id s child element into databaseReference.
                                    databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
                                }
                            });
                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Hiding the progressDialog.
                            progressDialog.dismiss();

                            // Showing exception erro message.
                            Toast.makeText(PostAd.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            // Setting progressDialog Title.
                            progressDialog.setTitle("Image is Uploading...");

                        }
                    });

            return true;
        }
        else {
            Toast.makeText(PostAd.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }


    private void showCategoryDialogue()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Product Category").setItems(PostCategory, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) {
                category = PostCategory[which];

                PostCategorySpinner.setText(category);


            }
        }).show();

    }
}