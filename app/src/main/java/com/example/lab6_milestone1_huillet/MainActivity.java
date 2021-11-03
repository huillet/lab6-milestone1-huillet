package com.example.lab6_milestone1_huillet;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends FragmentActivity {

    // Bascom Hall
    private final LatLng mDestinationLatLng = new LatLng(43.0757378,-89.4061951);
    private GoogleMap mMap;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_map);

        // Obtain a FusedLocationProviderClient
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;
            // code to display marker
            mMap.addMarker(new MarkerOptions()
                    .position(mDestinationLatLng)
                    .title("Bascom Hall"));
            displayMyLocation();
        });
    }

    private void displayMyLocation() {
        //Check if permission is granted
        int permission = ActivityCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission == PackageManager.PERMISSION_DENIED) {
            // If not, ask for it
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        else
        {
            // If permission granted, display marker at current location
            mFusedLocationProviderClient.getLastLocation()
                    .addOnCompleteListener(this, task -> {
                                Location mLastKnownLocation = task.getResult();
                                if (task.isSuccessful() && mLastKnownLocation != null)
                                {
                                    Log.i("Test", "found location");
                                    LatLng currentPosition = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                                    mMap.addPolyline(new PolylineOptions().add(
                                            currentPosition,
                                            mDestinationLatLng));
                                    mMap.addMarker(new MarkerOptions()
                                            .position(currentPosition)
                                            .title("You"));
                                }
                                else
                                {
                                    Log.i("Test", "location null");
                                }
                            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            // If request is cancelled, the result arrays are empty
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                displayMyLocation();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}