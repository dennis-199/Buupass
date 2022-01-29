package com.example.buupass;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class fragment1 extends Fragment {
    private RecyclerView recyclerView;
    private DatabaseReference likesRef,commentsRef, databaseReference, databaseReference2;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    Boolean likeChecker = false;
    private FirebaseRecyclerAdapter adapter;
    String currentUserID = null;
    Spinner spinner, spinner2;
    List<String> froms;
    List<String> tos;
    ArrayAdapter<String> arrayAdapter;
    ArrayAdapter<String> arrayAdapter2;

    AlertDialog.Builder comment_alert;
    LayoutInflater inflater;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_fragment1, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        //Reverse the layout so as to display the most recent post at the top
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


        spinner = view.findViewById(R.id.spinnerFrom);
        spinner2 = view.findViewById(R.id.spinnerTo);
        froms = new ArrayList<>();
        tos = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot chilsnapshot:snapshot.getChildren()) {
                    String spinnerName = chilsnapshot.child("From").getValue(String.class);
                    froms.add(spinnerName);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, froms);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(arrayAdapter);



            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        databaseReference2.child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot childsnapshot:snapshot.getChildren()) {
                    String spinnerName1 = childsnapshot.child("To").getValue(String.class);
                    tos.add(spinnerName1);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, tos);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(arrayAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //get the database reference where you will fetch posts
        // mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
        // Initialize the database reference where you will store likes


        likesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        // Initialize the database reference where you will store comments
        commentsRef = FirebaseDatabase.getInstance().getReference().child("Comments");
        //get an instance of firebase authentication
        mAuth = FirebaseAuth.getInstance();
        //get currently logged in user
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // if user is not logged in refer him/he ro the register activity
            Intent loginIntent = new Intent(getActivity(), RegisterUser.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
        }



        return view;
    }




    @Override
    public void onStart() { //
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //check to see if the user is logged in
        if (currentUser != null) {
            //if user is logged in populate the Ui With card views
            updateUI(currentUser);
            // Listen to the events on the adapter
            adapter.startListening();
        }
    }
    private void updateUI(final FirebaseUser currentUser) {
        //create and initialize an instance of Query that retrieves all posts uploaded
        Query query = FirebaseDatabase.getInstance().getReference().child("Posts");
        // Create and initialize an instance of Recycler Options passing in your model class
        FirebaseRecyclerOptions<Attic> options = new FirebaseRecyclerOptions.Builder<Attic>().setQuery(query, new SnapshotParser<Attic>() {
            @NonNull
            @Override
            //Create a snapshot of your model
            public Attic parseSnapshot(@NonNull DataSnapshot snapshot) {
                return new Attic(snapshot.child("title").getValue().toString(),
                        snapshot.child("desc").getValue().toString(),
                        snapshot.child("postImage").getValue().toString(),


                        snapshot.child("time").getValue().toString(),
                        snapshot.child("date").getValue().toString());
            }
        }).build();
        // crate a fire base adapter passing in the model, and a View holder
        // Create a new ViewHolder as a public inner class that extends RecyclerView.Holder, outside the create , start and update the Ui methods.
        //Then implement the methods onCreateViewHolder and onBindViewHolder
        // Complete all the steps in the AtticViewHolder before proceeding to the methods onCreateViewHolder, and onBindViewHolder
        adapter = new FirebaseRecyclerAdapter<Attic, AtticViewHolder>(options) {
            @NonNull
            @Override
            public AtticViewHolder onCreateViewHolder(
                    @NonNull ViewGroup parent, int viewType) {
                //inflate the layout where you have the card view items
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_items, parent, false);
                return new AtticViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull AtticViewHolder holder, int position, @NonNull Attic model) {
                // very important for you to get the post key since we will use this to set likes and delete a particular post
                final String post_key = getRef(position).getKey();
                //populate the card views with data
                holder.setTitle(model.getTitle());
                holder.setDesc(model.getDesc());
                holder.setPostImage(getContext(), model.getPostImage());

                holder.setTime(model.getTime());
                holder.setDate(model.getDate());
                //set a like on a particular post
                holder.setLikeButtonStatus(post_key);
                //add on click listener on the a particular post to allow opening this post on a different screen
                holder.post_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //launch the screen single post activity on clicking a particular cardview item
                        // create this activity using the empty activity template
                        Intent singleActivity = new Intent(getActivity(), SinglePostActivity.class);
                        singleActivity.putExtra("PostID", post_key);
                        startActivity(singleActivity);
                    }
                });
                // set the onclick listener on the button for liking a post
                holder.likePostButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // initialize the like checker to true, we are using this boolean variable to determine if a post has been liked or dislike
                        // we declared this variable on to of our activity class
                        likeChecker = true;
                        //check the currently logged in user using his/her ID
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            currentUserID = user.getUid();
                        } else {
                            Toast.makeText(getActivity(), "please login", Toast.LENGTH_SHORT).show();
                        }
                        //Listen to changes in the likes database reference
                        likesRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (likeChecker.equals(true)) {
                                    // if the current post has a like, associated to the current logged and the user clicks on it again, remove the like, basically this means the user is disliking the post
                                    if (dataSnapshot.child(post_key).hasChild(currentUserID)) {
                                        likesRef.child(post_key).child(currentUserID).removeValue();
                                        likeChecker = false;
                                    } else {
                                        //here the user is liking, set value on the like
                                        likesRef.child(post_key).child(currentUserID).setValue(true);
                                        likeChecker = false;
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                });
                //set the onclick listener on the button for commenting on a post
                holder.commentPostButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View view = inflater.inflate(R.layout.comment_pop, null);
                        //start alert dialog
                        comment_alert.setTitle("Add comment").setMessage("Please add a comment").setPositiveButton("Comment", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //check the currently logged in user using his/her ID
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user != null) {
                                    currentUserID = user.getUid();
                                } else {
                                    Toast.makeText(getActivity(), "please login", Toast.LENGTH_SHORT).show();
                                }
                                //Listen to changes in the comments database reference
                                commentsRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        //get the value of the comment
                                        EditText commentEntered = view.findViewById(R.id.comment);
                                        String comment = commentEntered.getText().toString().trim();
                                        //get the date and time of the post
                                        java.util.Calendar calendar = Calendar.getInstance();
                                        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
                                        final String saveCurrentDate = currentDate.format(calendar.getTime());
                                        java.util.Calendar calendar1 = Calendar.getInstance();
                                        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
                                        final String saveCurrentTime = currentTime.format(calendar1.getTime());

                                        commentsRef.child(post_key).child(currentUserID).child("comment").setValue(comment);
                                        commentsRef.child(post_key).child(currentUserID).child("date").setValue(saveCurrentDate);
                                        commentsRef.child(post_key).child(currentUserID).child("time").setValue(saveCurrentTime);
                                        commentsRef.child(post_key).child(currentUserID).child("uid").setValue(currentUser.getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(getActivity(),"Comment successfully added",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });
                            }
                        }).setNegativeButton("Cancel",null).setView(view).create().show();
                    }
                });
                //share button on click
                holder.sharePostButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this post" + post_key);
                        Intent chooser = Intent.createChooser(shareIntent, "Share via");
                        if(shareIntent.resolveActivity(getActivity().getPackageManager())!=null){
                            startActivity(chooser);
                        }
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onStop() {
        super.onStop();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            adapter.stopListening();
        }
    }
    public class AtticViewHolder extends RecyclerView.ViewHolder {
        //Declare the view objects in the card view
        public TextView post_title;
        public TextView post_desc;
        public ImageView post_image;
        public TextView postUserName;
        public ImageView user_image;
        public TextView postTime;
        public TextView postDate;
        public LinearLayout post_layout;
        public ImageButton likePostButton, commentPostButton, sharePostButton;
        public TextView displayLikes;
        //Declare an int variable to hold the count of likes
        int countLikes;
        //Declare a string variable to hold the user ID of currently logged in user
        String currentUserID;
        //Declare an instance of firebase authentication
        FirebaseAuth mAuth;
        //Declare a database reference where you are saving the likes
        DatabaseReference likesRef;

        //create constructor matching super
        public AtticViewHolder(@NonNull View itemView) {
            super(itemView);
            //Initialize the card view item objects
            post_title = itemView.findViewById(R.id.post_title_txtview);
            post_desc = itemView.findViewById(R.id.post_desc_txtview);
            post_image = itemView.findViewById(R.id.post_image);
            postUserName = itemView.findViewById(R.id.post_user);
            user_image = itemView.findViewById(R.id.userImage);
            postTime = itemView.findViewById(R.id.time);
            postDate = itemView.findViewById(R.id.date);
            post_layout = itemView.findViewById(R.id.linear_layout_post);
            likePostButton = itemView.findViewById(R.id.like_button);
            commentPostButton = itemView.findViewById(R.id.comment);
            sharePostButton = itemView.findViewById(R.id.share);
            displayLikes = itemView.findViewById(R.id.likes_display);
            //Initialize a database reference where you will store the likes
            likesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        }

        // create yos setters, you will use this setter in you onBindViewHolder method
        public void setTitle(String title) {
            post_title.setText(title);
        }

        public void setDesc(String desc) {
            post_desc.setText(desc);
        }

        public void setPostImage(Context ctx, String postImage) {
            Picasso.with(ctx).load(postImage).into(post_image);
        }

        public void setUserName(String userName) {
            postUserName.setText(userName);
        }

        public void setProfilePhoto(Context context, String profilePhoto) {
            Picasso.with(context).load(profilePhoto).into(user_image);
        }

        public void setTime(String time) {
            postTime.setText(time);
        }

        public void setDate(String date) {
            postDate.setText(date);
        }

        public void setLikeButtonStatus(final String post_key) {
            //we want to know who has like a particular post, so let's get the user using their user_ID
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                currentUserID = user.getUid();
            } else {
                Toast.makeText(getActivity(), "please login", Toast.LENGTH_SHORT).show();
            }
// Listen to changes in the database reference of Likes
            likesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //define post_key in the in the onBindViewHolder method
                    // check if a particular post has been liked
                    if (dataSnapshot.child(post_key).hasChild(currentUserID)) {
                        //if liked get the number of likes
                        countLikes = (int) dataSnapshot.child(post_key).getChildrenCount();
                        //check the image from initial dislike to like
                        likePostButton.setImageResource(R.drawable.like);
                        // count the like and display them in the textView for likes
                        displayLikes.setText(Integer.toString(countLikes));
                    } else {
                        //If disliked, get the current number of likes
                        countLikes = (int) dataSnapshot.child(post_key).getChildrenCount();
                        // set the image resource as disliked
                        likePostButton.setImageResource(R.drawable.dislike);
                        //display the current number of likes
                        displayLikes.setText(Integer.toString(countLikes));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }





}