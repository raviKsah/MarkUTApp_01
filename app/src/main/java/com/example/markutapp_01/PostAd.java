package com.example.markutapp_01;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.markutapp_01.R;
import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class PostAd extends AppCompatActivity {

    // views for button
    private ImageView btnUpload;
    private ImageView btnSelect;
    private TextView PostCategorySpinner;
    Button postAdd;

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
    String category;
    ProgressDialog progressDialog ;

    public static final String [] PostCategory =
            {
                  "Electronics", "Furniture", "Books", "Clothing", "Vehicle"
            };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_ad1);
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
                category=showCategoryDialogue();
                System.out.println("heyyyyyyyyyyyy"+category);

            }
        });



        // on pressing btnUpload uploadImage() is called
//        btnUpload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                uploadImage();
//            }
//        });

        postAdd.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
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

    // UploadImage method
    private void uploadImage()
    {
//        if (filePath != null) {
//
//            // Code for showing progressDialog while uploading
//            ProgressDialog progressDialog
//                    = new ProgressDialog(this);
//            progressDialog.setTitle("Uploading...");
//            progressDialog.show();
//
//            // Defining the child of storageReference
//            StorageReference ref
//                    = storageReference
//                    .child(
//                            "images/"
//                                    + UUID.randomUUID().toString());
//
//            // adding listeners on upload
//            // or failure of image
//            ref.putFile(filePath)
//                    .addOnSuccessListener(
//                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
//
//                                @Override
//                                public void onSuccess(
//                                        UploadTask.TaskSnapshot taskSnapshot)
//                                {
//
//                                    // Image uploaded successfully
//                                    // Dismiss dialog
//                                    progressDialog.dismiss();
//                                    Toast
//                                            .makeText(PostAd.this,
//                                                    "Image Uploaded!!",
//                                                    Toast.LENGTH_SHORT)
//                                            .show();
//
//                                    ///////   Here need to store file path to user table database
//                                }
//                            })
//
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e)
//                        {
//
//                            // Error, Image not uploaded
//                            progressDialog.dismiss();
//                            Toast
//                                    .makeText(PostAd.this,
//                                            "Failed " + e.getMessage(),
//                                            Toast.LENGTH_SHORT)
//                                    .show();
//                        }
//                    })
//                    .addOnProgressListener(
//                            new OnProgressListener<UploadTask.TaskSnapshot>() {
//
//                                // Progress Listener for loading
//                                // percentage on the dialog box
//                                @Override
//                                public void onProgress(
//                                        UploadTask.TaskSnapshot taskSnapshot)
//                                {
//                                    double progress
//                                            = (100.0
//                                            * taskSnapshot.getBytesTransferred()
//                                            / taskSnapshot.getTotalByteCount());
//                                    progressDialog.setMessage(
//                                            "Uploaded "
//                                                    + (int)progress + "%");
//                                }
//                            });
//        }
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

                            // Getting image name from EditText and store into string variable.
                            String postTitle = title.getText().toString().trim();
                            String postDesc = description.getText().toString().trim();
                            String cost = price.getText().toString().trim();
                            String createdDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                            String creator= session.getusename();
                            String editedDate="";
                            Boolean isComplete=false;
                            Boolean underReport=false;
                            Boolean wasEdited=false;
                            String editor="";
                            String dateCompleted="";


                            // Hiding the progressDialog after done uploading.
                            progressDialog.dismiss();

                            // Showing toast message after done uploading.
                            Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                            String ImageUploadId = databaseReference.push().getKey();
                            @SuppressWarnings("VisibleForTests")
                             PostDetails imageUploadInfo = new PostDetails(ImageUploadId,creator,category,dateCompleted,createdDate, postDesc, editedDate, editor, taskSnapshot.getUploadSessionUri().toString(),cost, postTitle,isComplete,underReport,wasEdited);
                            //String ad_id, String advertiser, String category, String date_completed, String date_created, String description, String edited_date, String editor, String image_path, String price, String title, Boolean is_complete, Boolean under_report, Boolean was_edited
                            // Getting image upload ID.


                            // Adding image upload id s child element into databaseReference.
                            databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
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
        }
        else {

            Toast.makeText(PostAd.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }

    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }


    private String showCategoryDialogue()
    {
        final String[] cat = new String[1];
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Product Category").setItems(PostCategory, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) {
                String category = PostCategory[which];

                PostCategorySpinner.setText(category);
                cat[0] =PostCategorySpinner.getText().toString();

            }
        }).show();
        return cat[0];
    }
}