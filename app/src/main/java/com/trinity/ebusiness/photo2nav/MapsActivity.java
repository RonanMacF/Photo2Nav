package com.trinity.ebusiness.photo2nav;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private static final String TAG = "ViewDatabase";
    private  String userID;
    private TextView text;
    UserLocationPoints uInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        userID = mAuth.getCurrentUser().getUid();
        Log.i(TAG, userID);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                uInfo = dataSnapshot.child(userID).getValue(UserLocationPoints.class);
                LatLng coor = null;
                if (!uInfo.isEmpty()) {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            LatLng coor = null;
                            for (int i = 0; i < Math.min(uInfo.getLatitude().size(), uInfo.getLongitude().size()); i++) {
                                 coor = new LatLng(uInfo.getLatitude().get(i), uInfo.getLongitude().get(i));
                                mMap.addMarker(new MarkerOptions().position(coor));
                            }
                              mMap.moveCamera(CameraUpdateFactory.newLatLng(coor));

                        }
                    }, 500);


                }else{
                    Toast.makeText(getApplicationContext(), "no coordinates found", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;




       // for(int i = 0; i < uInfo.getLatitude().size(); i++){
        //    coor = new LatLng(uInfo.getLatitude().get(i), uInfo.getLongitude().get(i));
        //    mMap.addMarker(new MarkerOptions().position(coor));
        //}

    }
}
