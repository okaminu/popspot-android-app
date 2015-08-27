package com.jaguarsoft.mac.favouriteplaces;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private boolean commentEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        setupButtons();



    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }
//lots of comments
    //dsad1123w1ed
    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true));

        if (location != null)
        {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 17));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(15)                   // Sets the zoom
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }

    }


    protected void setupButtons(){
        final Context context = this;
        final Button buttonLike = (Button) findViewById(R.id.buttonLike);
        buttonLike.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(commentEnabled){
                    Intent intent = new Intent(context, CommentActivity.class);

                    startActivity(intent);

                }
                else{

                }
            }
        });

        final Button buttonDislike = (Button) findViewById(R.id.buttonDislike);

        buttonDislike.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(commentEnabled){
                    Intent intent = new Intent(context, CommentActivity.class);

                    startActivity(intent);
                }
                else{

                }
            }
        });

        final ImageButton buttonComment = (ImageButton) findViewById(R.id.buttonComment);
        buttonComment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(commentEnabled) {
                    commentEnabled = false;
                    buttonComment.setImageResource(R.drawable.ic_mode_comment_gray_36dp);
                }
                else {
                    commentEnabled = true;
                    buttonComment.setImageResource(R.drawable.ic_mode_comment_36dp);
                }
            }
        });
    }










}
