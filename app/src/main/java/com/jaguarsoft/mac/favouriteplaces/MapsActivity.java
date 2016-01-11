package com.jaguarsoft.mac.favouriteplaces;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Looper;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jaguarsoft.mac.favouriteplaces.backend_sdk.FavoritePlacesClient;
import com.jaguarsoft.mac.favouriteplaces.backend_sdk.model.LocationVote;


public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Handler mHandler;
    //private boolean commentEnabled = false;

    private MapsActivity(){
        Handler mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                showAndroidMessage(messageToPrint);
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        setupButtons();
    }

    @Override
    protected void onStart() {
        super.onStart();
        new FavoriteLocationGetTask().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }


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

    protected void setupButtons() {
        //final Context context = this;
        //Intent intent = new Intent(context, CommentActivity.class);
        //startActivity(intent);
        final Button buttonLike = (Button) findViewById(R.id.buttonLike);

        buttonLike.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new FavoriteLocationPutTask((short) 1).execute();

            }
        });

        final Button buttonDislike = (Button) findViewById(R.id.buttonDislike);

        buttonDislike.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new FavoriteLocationPutTask((short) 0).execute();

            }
        });

        /*final ImageButton buttonComment = (ImageButton) findViewById(R.id.buttonComment);
        buttonComment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (commentEnabled) {
                    commentEnabled = false;
                    buttonComment.setImageResource(R.drawable.ic_mode_comment_gray_36dp);
                } else {
                    commentEnabled = true;
                    buttonComment.setImageResource(R.drawable.ic_mode_comment_36dp);
                }
            }
        });*/
    }


    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        Location location = location();

        if (location != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 17));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                        // Sets the center of the map to location user
                    .zoom(15)                   // Sets the zoom
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

    }

    private Location location() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        return locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true));

    }

    private class FavoriteLocationGetTask extends AsyncTask<Integer, Integer, LocationVote[]> {
        @Override
        protected LocationVote[] doInBackground(Integer... integers) {
            ApiClientFactory apiClientFactory = new ApiClientFactory();
            LocationVote[] ratings = new LocationVote[0];
            try
            {
                ratings = apiClientFactory.build(FavoritePlacesClient.class).getRatingsGet();
            }
            catch (Exception ex)
            {
                ex.getMessage();
                Message message = mHandler.obtainMessage(command, parameter);
                message.sendToTarget();
            }
            return ratings;
        }

        @Override
        protected void onPostExecute(LocationVote[] locationVotes) {
            super.onPostExecute(locationVotes);
            for (LocationVote locationVote : locationVotes) {
                float color;
                double longitude = locationVote.coordinates.longitude;
                double latitude = locationVote.coordinates.latitude;
                short rating = locationVote.feedback.rating;
                String comment = locationVote.feedback.comment;


                if (rating == 0)
                    color = BitmapDescriptorFactory.HUE_RED;
                else
                    color = BitmapDescriptorFactory.HUE_GREEN;

                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude, longitude))
                        .title(comment)
                        .icon(BitmapDescriptorFactory.defaultMarker(color)));
            }
        }
    }

    private class FavoriteLocationPutTask extends AsyncTask<Integer, Integer, LocationVote[]> {
        private short rating;

        public FavoriteLocationPutTask(short rating) {
            this.rating = rating;
        }

        @Override
        protected LocationVote[] doInBackground(Integer... integers) {
            Location location = location();
            LocationVote locationVote = new LocationVote();
            locationVote.coordinates.longitude = location.getLongitude();
            locationVote.coordinates.latitude = location.getLatitude();
            locationVote.feedback.rating = rating;
            locationVote.feedback.comment = "";
            try
            {
                com.jaguarsoft.mac.favouriteplaces.backend_sdk.model.Status status =
                        new ApiClientFactory().build(FavoritePlacesClient.class).putRatingPost(locationVote);
                return new LocationVote[]{locationVote};
            }
            catch (Exception ex)
            {
                ex.getMessage();
                //showAndroidMessage("Failed to submit vote");
            }
            return new LocationVote[]{locationVote};
        }

        @Override
        protected void onPostExecute(LocationVote[] locationVotes) {
            super.onPostExecute(locationVotes);
        }
    }


    private void showAndroidMessage(String message){
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }




}
