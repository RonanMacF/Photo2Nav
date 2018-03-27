package com.trinity.ebusiness.photo2nav;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private static int LOAD_IMAGE_RESULTS = 1;
    private Button button;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "ViewDatabase";
    private  String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        button = (Button)findViewById(R.id.GalleryButton);
        button.setOnClickListener(this);
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOAD_IMAGE_RESULTS && resultCode == RESULT_OK && data != null) {
            Uri pickedImage = data.getData();
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            ExifInterface exifInterface = null;
            InputStream in = null;
            cursor.close();
            try {
                in = getContentResolver().openInputStream(pickedImage);
                exifInterface = new ExifInterface(in);

                float[] latlng = new float[2];
                exifInterface.getLatLong(latlng);

                if(latlng[0] != 0.0 && latlng[1] != 0.0) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    //UserLocationPoints userLocationPoints = mDatabase.child(user.getUid()).getRoot();
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?&daddr=" + latlng[0] + "," + latlng[1]));
                    startActivity(intent);
                }

            } catch (IOException e) {
                Log.e("homeActivity", "Failed exifInterface Initialise", e);
            }

        }

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void showData(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            UserLocationPoints uInfo = new UserLocationPoints();
            uInfo.setLatitude(ds.child(userID).getValue(UserLocationPoints.class).getLatitude()); //set the name
            uInfo.setLongitude(ds.child(userID).getValue(UserLocationPoints.class).getLongitude()); //set the email

            //display all the information
            Log.d(TAG, "showData: lat: " + uInfo.getLatitude());
            Log.d(TAG, "showData: lon: " + uInfo.getLongitude());
        }
    }

    @Override
    public void onClick(View view) {
        // Create the Intent for Image Gallery.
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // Start new activity with the LOAD_IMAGE_RESULTS to handle back the results when image is picked from the Image Gallery.
        startActivityForResult(i, LOAD_IMAGE_RESULTS);
    }
}
