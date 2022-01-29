package com.example.buupass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SinglePostActivity extends AppCompatActivity {
    private ImageView singleImage;
    private TextView singleTitle, singleDesc, singleFROM, singleTO, singleTerminal, singleDepartureTime,singleDepartute_Date;
    String post_key = null;
    private DatabaseReference mDatabase;
    private Button deleteBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);
        singleImage = findViewById(R.id.singleImageview);
        singleTitle = findViewById(R.id.singleTitle);
        singleDesc = findViewById(R.id.singleDesc);
        singleFROM= findViewById(R.id.singleFrom);
        singleTO=findViewById(R.id.singleTo);
        singleDepartureTime=findViewById(R.id.singledeparture);
        singleDepartute_Date = findViewById(R.id.singledepartureDate);

        singleTerminal=findViewById(R.id.singleTerminal);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
        post_key = getIntent().getExtras().getString("PostID");
        deleteBtn = findViewById(R.id.deleteBtn);
        mAuth = FirebaseAuth.getInstance();
        deleteBtn.setVisibility(View.INVISIBLE);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child(post_key).removeValue();
                Intent mainIntent = new Intent(SinglePostActivity.this,
                        MainActivity.class);
                startActivity(mainIntent);
            }
        });
        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_title = (String) dataSnapshot.child("title").getValue();
                String post_desc = (String) dataSnapshot.child("desc").getValue();
                String post_image = (String) dataSnapshot.child("postImage").getValue();
                String post_uid = (String) dataSnapshot.child("uid").getValue();
                String Terminal_point = (String) dataSnapshot.child("From").getValue();
                String Terminal = (String) dataSnapshot.child("Terminal").getValue();
                String To_origin = (String) dataSnapshot.child("To").getValue();
                String depatureTime = (String) dataSnapshot.child("Departure time").getValue();
                String depatureDate = (String) dataSnapshot.child("Departure Date").getValue();
                singleTitle.setText(post_title);
                singleDesc.setText(post_desc);
                singleFROM.setText(Terminal_point);
                singleTO.setText(To_origin);
                singleDepartureTime.setText(depatureTime);
                singleDepartute_Date.setText(depatureDate);
                singleTerminal.setText(Terminal);
                Picasso.with(SinglePostActivity.this).load(post_image).into(singleImage);
                if (mAuth.getCurrentUser().getUid().equals(post_uid)) {
                    deleteBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void startMpesaservice(View view) {

        String faretopay = singleDesc.getText().toString();

        Intent intent = new Intent(SinglePostActivity.this, MpesaActivity.class);
        intent.putExtra("keyname",faretopay);
        startActivity(intent);
    }
}