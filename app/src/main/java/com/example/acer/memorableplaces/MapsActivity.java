package com.example.acer.memorableplaces;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;

    LocationManager locationManager;
    LocationListener locationListener;
    Intent intent;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (grantResults.length > 1 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location l = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            OnCallLocation(l, "Your Location");
            }
        }else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
    }

    public void OnCallLocation(Location location, String name){
        if(location !=null){
            LatLng l = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(l).title(name));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(l,10));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
        intent = getIntent();
        int placeNumber = intent.getIntExtra("place", 0);
        if (placeNumber == 0) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    OnCallLocation(location, "Your Location");
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location lastKnownLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                OnCallLocation(lastKnownLoc, "Your Location");
            }
        }
        else{
            Location placeLoc = new Location(LocationManager.GPS_PROVIDER);
            placeLoc.setLongitude(MainActivity.location.get(intent.getIntExtra("place",0)-1).longitude);
            placeLoc.setLatitude(MainActivity.location.get(intent.getIntExtra("place",0)-1).latitude);
            OnCallLocation(placeLoc,"You have saved this location");
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().title("Your new location").position(latLng));
        Intent intent = getIntent();
        String location = " ";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if(addresses != null){
                location+= addresses.get(0).getThoroughfare() +" "+addresses.get(0).getLocality()+" "+addresses.get(0).getAdminArea();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(location.length() > 0){
            intent.putExtra("MyLocation",location);
        }
        else{
            location = "Location does not have an address";
            intent.putExtra("MyLocation",location);
        }
        MainActivity.arrayList.add(location);
        MainActivity.location.add(latLng);
        MainActivity.arrayAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Location saved", Toast.LENGTH_SHORT).show();
    }
}
