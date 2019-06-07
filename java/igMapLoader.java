package com.example.sankba.infogroup;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class igMapLoader extends Fragment
        implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public static final String migPref = "igroupPref";
    public static final String mlatkey = "Geolat";
    public static final String mlngkey = "Geolong";

    SharedPreferences migPreferences;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference myRef1 = db.getReference();
    List<igGeoWrapper> locationWrapper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.locationfragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        migPreferences = getActivity().getSharedPreferences(migPref, Context.MODE_PRIVATE);

        SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);
        RegisterEventListener();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(true);

            } else {
                Toast.makeText(getActivity().getApplicationContext(), "No Permission", Toast.LENGTH_LONG).show();
            }

        }else{
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
        }
    }

    private void buildGoogleApiClient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity().getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location)
    {
        DrawMarkers(location);

    }

    private void RegisterEventListener()
    {
        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(getActivity().getApplicationContext(), "onDataChange", Toast.LENGTH_SHORT).show();
                ReadLocationInfo(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void DrawMarkers(Location location)
    {
        mLastLocation = location;

        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        storeLocationInfo(latLng.latitude,latLng.longitude);

    }

    private void ReadLocationInfo(DataSnapshot dataSnapshot)
    {
        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {

            //double mylat = (double)snapshot.child("geoLatPos").getValue();
            //double mylon = (double)snapshot.child("geoLongPos").getValue();

            LatLng newLocation = new LatLng(
                    snapshot.child("geoLatPos").getValue(double.class),
                    snapshot.child("geoLongPos").getValue(double.class));


            mMap.addMarker(new MarkerOptions()
                    .position(newLocation)
                    .title(dataSnapshot.getKey())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

        }

//        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

            //String location = postSnapshot.getValue().toString();
       //     Toast.makeText(getActivity().getApplicationContext(), "befire line", Toast.LENGTH_SHORT).show();


          //  LatLng newLocation = new LatLng(
         //           dataSnapshot.child("geoLatPos").getValue(double.class),
         //           dataSnapshot.child("geoLongPos").getValue(double.class)
         //   );

          //  Toast.makeText(getActivity().getApplicationContext(), "below line", Toast.LENGTH_SHORT).show();
          //  mMap.addMarker(new MarkerOptions()
          //          .position(newLocation)
          //          .title(dataSnapshot.getKey())
          //          .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));


      //  }



    }

    private void storeLocationInfo(double Lat,double lng)
    {
        igGeoWrapper iWrapper = new igGeoWrapper("Device1",Lat ,lng);
        myRef1.child("Device1").setValue(iWrapper);

        double val1 =12.920694;
        double val2 =77.664677;

        igGeoWrapper iWrapper1 = new igGeoWrapper("Device2",val1 ,val2);
        myRef1.child("Device2").setValue(iWrapper1);


        /* To store in shared pref
        SharedPreferences.Editor editor = migPreferences.edit();
        editor.putFloat (mlatkey, (float)Lat);
        editor.putFloat (mlngkey, (float)lng);
        editor.apply();
        */
    }

    @Override
    public void onPause() {
        super.onPause();
        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(getActivity().getApplicationContext(), getString(R.string.PermissionDeny), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}