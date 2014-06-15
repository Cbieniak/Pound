package com.bienprogramming.pound.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.bienprogramming.pound.app.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**The Activity that hold the google map. Allows the user to choose a location on the map where the pet was lost
 *
 */
public class PetLocationActivity extends FragmentActivity  implements LocationListener{
    private LocationManager locationManager;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    private Marker markedLocation;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private boolean useLocation; // A reference for whether or not the center the map on the users location

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_location);
        setUpMapIfNeeded();
        useLocation = getIntent().getExtras().getBoolean("location");
        mMap.setMyLocationEnabled(useLocation);
        if(useLocation) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
        }else {
            //If the user doesn't want to use location focus on melbourne
            LatLng melbourneLatLng = new LatLng(-37.81319, 144.96298);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(melbourneLatLng, 15));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.map_menu, menu);
            return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_done) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
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

    private void setUpMap() {

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(markedLocation != null) markedLocation.remove();
                markedLocation = mMap.addMarker(new MarkerOptions().position(latLng).title("Pet"));
            }
        });
    }

    @Override
    public void onLocationChanged(Location location)
    {
        LatLng loc = new LatLng(location.getLatitude(),location.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    @Override
    public void finish() {
        // Prepare data intent
        Intent data = new Intent();
        if(markedLocation !=null) {
            data.putExtra("Latitude", markedLocation.getPosition().latitude);
            data.putExtra("Longitude", markedLocation.getPosition().longitude);
            // Activity finished ok, return the data
            setResult(RESULT_OK, data);
        } else {
            setResult(RESULT_CANCELED);
        }
        super.finish();
    }
}
