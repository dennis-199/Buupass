package com.example.buupass;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.buupass.databinding.FragmentHomeBinding;
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


public class HomeFragment extends Fragment {
    // Declare the view objects
    private FragmentHomeBinding binding;
    private ImageButton imageBtn;
    private EditText textTitle;
    private EditText textDesc;
    private FirebaseUser user;
    private String userID;
    private DatabaseReference reference;
    private Button postBtn;
    private Button timeButton;
    private EditText NoOfVehicles;
    private EditText supervisorName;
    private EditText terminal;
    private EditText origin;
    private EditText destination;
    private EditText via;
    private EditText setFare;

    int hour,minute;


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button button = (Button) root.findViewById(R.id.nextButton);
        timeButton = root.findViewById(R.id.timeButton);
        NoOfVehicles = root.findViewById(R.id.NoOfvehicles);
        supervisorName = root.findViewById(R.id.supervisorname);
        terminal = root.findViewById(R.id.terminal);
        origin = root.findViewById(R.id.origin);
        destination=root.findViewById(R.id.Destination);
        via=root.findViewById(R.id.via);
        setFare=root.findViewById(R.id.farePrice);



        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("SACCOS");
        userID = user.getUid();

        final TextView greetingTextView = (TextView) root.findViewById(R.id.matatuBusID);
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Saccos userprofile = snapshot.getValue(Saccos.class);
                if(userprofile!= null){
                    String busName = userprofile.getMatatu_BusName();

                    greetingTextView.setText(busName + "!");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PostActivity.class);
                startActivity(intent);
            }
        });





        return root;


    }


}