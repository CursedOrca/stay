package ca.georgebrown.comp3074.stay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ca.georgebrown.comp3074.stay.Adapter.YourListingAdapter;

public class YourListingActivity extends AppCompatActivity {

    // private ImageView imgYourListing;
    private TextView txtYourListingTitle, txtYourListingPrice, getTxtYourListingStatus, btnDeleteYourListing, btnEditYourListing;
    private RecyclerView yourList;
    YourListingAdapter yourListingAdapter;
    List<Listing> listing;
    private ImageView imgYourListing;
    private DatabaseReference YourListingRef;
    private FirebaseAuth mAuth;

    private String currentUserId, databaseUserId, YourListingPrice, YourListingTitle, YourListingImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_listing);

        yourList = (RecyclerView) findViewById(R.id.all_your_listings);
        yourList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        yourList.setLayoutManager(linearLayoutManager);
        listing = new ArrayList<>();
        yourListingAdapter = new YourListingAdapter(getApplicationContext(), listing);
        yourList.setAdapter(yourListingAdapter);
        /*btnEditYourListing = (TextView) findViewById(R.id.txtYourListing_Edit);
        btnDeleteYourListing = (TextView) findViewById(R.id.txtYourListing_Delete);*/


        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        txtYourListingPrice = (TextView) findViewById(R.id.txtYourListing_Price);
        txtYourListingTitle = (TextView) findViewById(R.id.txtYourListing_Title);
        getTxtYourListingStatus = (TextView) findViewById(R.id.txtYourListing_Status);
        imgYourListing = (ImageView) findViewById(R.id.imgYourListing);

        yourList();

    }

    private void EditSelectedListing(String price) {
    }


    private void sendUserToMainActivity() {
        Intent mainIntent = new Intent(YourListingActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void yourList(){
        YourListingRef = FirebaseDatabase.getInstance().getReference().child("Listing");
        YourListingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Listing yourlisting = snapshot.getValue(Listing.class);
                    if(yourlisting.getUid().equals(currentUserId)){
                        listing.add(yourlisting);

                        /*btnDeleteYourListing.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                /*
                                YourListingRef.removeValue();
                                sendUserToMainActivity();
                                Toast.makeText(YourListingActivity.this, "Your Listing has been deleted", Toast.LENGTH_SHORT).show();                 }
                        });

                        btnEditYourListing.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // EditSelectedListing(YourListingPrice);
                            }
                        });*/
                    }
                }
                Collections.reverse(listing);
                yourListingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
