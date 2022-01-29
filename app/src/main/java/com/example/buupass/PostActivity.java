package com.example.buupass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PostActivity extends AppCompatActivity {
    private ImageButton imageBtn;
    private EditText textTitle;
    private EditText textDesc;
    private EditText textTerminal;
    private EditText textFrom;
    private EditText textTo;

    int hour,minute;


    private Button postBtn;
    private Button timeButton;
    private EditText NoOfVehicles;
    private EditText supervisorName;
    private EditText terminal;
    private EditText origin;
    private EditText destination;
    private EditText via;
    private EditText setFare;

    //Declare an Instance of the Storage reference where we will upload the post photo
    private StorageReference mStorageRef;
    //Declare an Instance of the database reference where we will be saving the post details
    private DatabaseReference databaseRef;
    //Declare an Instance of firebase authentication
    private FirebaseAuth mAuth;
    //Declare an Instance of the database reference where we have user details
    private DatabaseReference mDatabaseUsers;
    //Declare a Instance of currently logged in user
    private FirebaseUser mCurrentUser;
    // Declare and initialize a private final static int that will serve as our request code
    private static final int GALLERY_REQUEST_CODE = 2;
    // Declare an Instance of URI for getting the image from our phone, initialize it to null
    private Uri uri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        postBtn = findViewById(R.id.postBtn);
        textDesc = findViewById(R.id.textDesc);
        textTitle = findViewById(R.id.textTitle);
        textTerminal = findViewById(R.id.terminalpoint);
        textFrom = findViewById(R.id.fromWhere);
        textTo = findViewById(R.id.toWhere);


        timeButton = findViewById(R.id.timeButton);
        NoOfVehicles = findViewById(R.id.NoOfvehicles);
        supervisorName = findViewById(R.id.supervisorname);
        terminal = findViewById(R.id.terminal);
        origin = findViewById(R.id.origin);
        destination=findViewById(R.id.Destination);
        via=findViewById(R.id.via);
        setFare=findViewById(R.id.farePrice);

        //Initialize the storage reference
        mStorageRef = FirebaseStorage.getInstance().getReference();
        //Initialize the database reference/node where you will be storing posts
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        //Initialize an instance of Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        //Initialize the instance of the firebase user
        mCurrentUser = mAuth.getCurrentUser();
        //Get currently logged in user
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        // initialize the image button
        imageBtn = findViewById(R.id.imgBtn);
        //picking image from gallery
        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
            }
        });
        // posting to Firebase
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PostActivity.this, "POSTING...", Toast.LENGTH_LONG).show();
                //get title and desc from the edit texts
                final String PostTitle = textTitle.getText().toString().trim();
                final String PostDesc = textDesc.getText().toString().trim();
                final String Terminal = textTerminal.getText().toString().trim();
                final String From = textFrom.getText().toString().trim();
                final String To = textTo.getText().toString().trim();
                final String DepatureTime = timeButton.getText().toString().trim();


                //get the date and time of the post
                java.util.Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
                final String saveCurrentDate = currentDate.format(calendar.getTime());
                java.util.Calendar calendar1 = Calendar.getInstance();
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
                final String saveCurrentTime = currentTime.format(calendar1.getTime());
                // do a check for empty fields
                if (!TextUtils.isEmpty(PostDesc) && !TextUtils.isEmpty(PostTitle)) {
                    //create Storage reference node, inside post_image storage reference where you will save the post image
                    StorageReference filepath = mStorageRef.child("post_images").child(uri.getLastPathSegment());
                    //call the putFile() method passing the post image the user set on the storage reference where you are uploading the image
                    //further call addOnSuccessListener on the reference to listen if the upload task was successful,and get a snapshot of the uploaded image
                    filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload of the post image was successful get the download url
                            if (taskSnapshot.getMetadata() != null) {
                                if (taskSnapshot.getMetadata().getReference() != null) {
                                    //get the download url from your storage use the methods getStorage() and getDownloadUrl()
                                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                    //call the method addOnSuccessListener to determine if we got the download url
                                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            //convert the uri to a string on success
                                            final String imageUrl = uri.toString();
                                            Toast.makeText(getApplicationContext(), "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                                            // call the method push() to publish the values on the database reference
                                            final DatabaseReference newPost = databaseRef.push();
                                            //adding post contents to database reference,call addValueEventListener so as to set the values
                                            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    newPost.child("title").setValue(PostTitle);
                                                    newPost.child("desc").setValue(PostDesc);
                                                    newPost.child("Terminal").setValue(Terminal);
                                                    newPost.child("From").setValue(From);
                                                    newPost.child("To").setValue(To);
                                                    newPost.child("Departure time").setValue(DepatureTime);
                                                    newPost.child("postImage").setValue(imageUrl);
                                                    newPost.child("uid").setValue(mCurrentUser.getUid());
                                                    newPost.child("time").setValue(saveCurrentTime);
                                                    newPost.child("date").setValue(saveCurrentDate);
                                                    //get the profile photo and display name of the person posting
                                                    newPost.child("profilePhoto").setValue(dataSnapshot.child("profilePhoto").getValue());
                                                    newPost.child("displayName").setValue(dataSnapshot.child("Username").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                //launch the main activity after posting
                                                                Intent intent = new Intent(PostActivity.this, HomeFragment.class);
                                                                startActivity(intent);
                                                            }
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
            }
        });
    }
    @Override
    // image from gallery result
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            //get the image selected by the user
            uri = data.getData();
            //set the image
            imageBtn.setImageURI(uri);
        }
    }

    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                timeButton.setText(String.format(Locale.getDefault(),"%02d:%02d", hour,minute));

            }
        };
        int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style,onTimeSetListener, hour, minute,true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();

    }
}