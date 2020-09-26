package com.example.rabbit777.recan_new;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.constant.Unit;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class Location_new extends FragmentActivity implements OnMapReadyCallback ,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    private LocationRequest locationRequest;
    private Location lastlocation;
    private Marker mCurrLocationMarker;
    private static final int Request_User_Location_Code = 99;
    LocationManager locationManager;
    LatLng userLocation, galoy_location, lind_locaiton;
    ImageView Mylocation, galoy_img, lind_imgs;
    LinearLayout lind, galoy;
    TextView galoy_name, galoy_detaill, galoy_times, Lind_name, Lind_detail, Lind_time;
    private GoogleApiClient googleApiClient;
    private static final String TAG = "Location_new";
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_location);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checakUserLocationPermision();
        }

        context = this;
        galoy = (LinearLayout) findViewById(R.id.text_galoy);
        lind = (LinearLayout) findViewById(R.id.textfide_lend);
        galoy_name = (TextView) findViewById(R.id.Location_name);
        galoy_detaill = (TextView) findViewById(R.id.Location_detail);
        galoy_times = (TextView) findViewById(R.id.Location_time);
        Lind_name = (TextView) findViewById(R.id.Lend_name);
        Lind_detail = (TextView) findViewById(R.id.Lend_detail);
        Lind_time = (TextView) findViewById(R.id.Lend_time);
        galoy_location = new LatLng(14.896790, 102.012700);
        lind_locaiton = new LatLng(14.881123, 102.017221);
        galoy_img = (ImageView) findViewById(R.id.galoy_narrow);
        lind_imgs = (ImageView)findViewById(R.id.Lind_narrow) ;

        galoy_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lind_imgs.setVisibility(View.VISIBLE);
                mMap.clear();

// Getting URL to the Google Directions API
                String serverKey = "AIzaSyBPAkbyggtkKBbOIV6snsbhRDIIlhu-wRY";
                GoogleDirection.withServerKey(serverKey)
                        .from(userLocation)
                        .to(galoy_location)
                        .unit(Unit.METRIC)
                        .transportMode(TransportMode.DRIVING)
                        .execute(new DirectionCallback() {
                            @Override
                            public void onDirectionSuccess(Direction direction, String rawBody) {
                                if (direction.isOK()) {
                                    Route route = direction.getRouteList().get(0);
                                    int legCount = route.getLegList().size();
                                    for (int index = 0; index < legCount; index++) {
                                        Leg leg = route.getLegList().get(index);
                                        mMap.addMarker(new MarkerOptions().position(leg.getStartLocation().getCoordination()));
                                        if (index == legCount - 1) {
                                            mMap.addMarker(new MarkerOptions().position(leg.getEndLocation().getCoordination()));
                                        }
                                        List<Step> stepList = leg.getStepList();
                                        ArrayList<PolylineOptions> polylineOptionList = DirectionConverter.createTransitPolyline(context, stepList, 5, Color.RED, 3, Color.BLUE);
                                        for (PolylineOptions polylineOption : polylineOptionList) {
                                            mMap.addPolyline(polylineOption);
                                        }
                                    }
                                    setCameraWithCoordinationBounds(route);
                                    galoy_img.setVisibility(View.GONE);
                                    mMap.addMarker(new MarkerOptions().position(userLocation).title("Me"));
                                    mMap.addMarker(new MarkerOptions().position(galoy_location).title("กาสลอง"));
                                    mMap.addMarker(new MarkerOptions().position(lind_locaiton).title("อาคารเรียนรวมหนึ่ง"));

                                }
                            }

                            @Override
                            public void onDirectionFailure(Throwable t) {
//                                Snackbar.make(galoy_img, t.getMessage(), Snackbar.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        lind_imgs.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                galoy_img.setVisibility(View.VISIBLE);
                String serverKey = "AIzaSyBPAkbyggtkKBbOIV6snsbhRDIIlhu-wRY";
                mMap.clear();
                GoogleDirection.withServerKey(serverKey)
                        .from(userLocation)
                        .to(lind_locaiton)
                        .unit(Unit.METRIC)
                        .transportMode(TransportMode.DRIVING)
                        .execute(new DirectionCallback() {
                            @Override
                            public void onDirectionSuccess(Direction direction, String rawBody) {
//                                Snackbar.make(lind_imgs, "Success with status : " + direction.getStatus(), Snackbar.LENGTH_SHORT).show();
                                if (direction.isOK()) {
                                    Route route = direction.getRouteList().get(0);
                                    int legCount = route.getLegList().size();
                                    for (int index = 0; index < legCount; index++) {
                                        Leg leg = route.getLegList().get(index);
                                        mMap.addMarker(new MarkerOptions().position(leg.getStartLocation().getCoordination()));
                                        if (index == legCount - 1) {
                                            mMap.addMarker(new MarkerOptions().position(leg.getEndLocation().getCoordination()));
                                        }
                                        List<Step> stepList = leg.getStepList();
                                        ArrayList<PolylineOptions> polylineOptionList = DirectionConverter.createTransitPolyline(context, stepList, 5, Color.RED, 3, Color.BLUE);
                                        for (PolylineOptions polylineOption : polylineOptionList) {
                                            mMap.addPolyline(polylineOption);
                                        }
                                    }
                                    setCameraWithCoordinationBounds(route);
                                    lind_imgs.setVisibility(View.GONE);
                                    mMap.addMarker(new MarkerOptions().position(userLocation).title("Me"));
                                    mMap.addMarker(new MarkerOptions().position(galoy_location).title("กาสลอง"));
                                    mMap.addMarker(new MarkerOptions().position(lind_locaiton).title("อาคารเรียนรวมหนึ่ง"));

                                }
                            }

                            @Override
                            public void onDirectionFailure(Throwable t) {
//                                Snackbar.make(lind_imgs, t.getMessage(), Snackbar.LENGTH_SHORT).show();
                            }
                        });



            }
        });
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
        final float zoomLevel = 16.0f;
        // Add a marker in Sydney and move the camera


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            builGoogleApiCilent();

            mMap.setMyLocationEnabled(true);


        }
        mMap.addMarker(new MarkerOptions().position(galoy_location).title("กาสลอง"));
        mMap.addMarker(new MarkerOptions().position(lind_locaiton).title("อาคารเรียนรวมหนึ่ง"));


        //Getting current location


        lind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lind.setBackgroundColor(Color.parseColor("#007896"));
                galoy.setBackgroundColor(Color.WHITE);

                Lind_name.setTextColor(Color.WHITE);
                Lind_detail.setTextColor(Color.WHITE);
                Lind_time.setTextColor(Color.WHITE);

                galoy_name.setTextColor(Color.parseColor("#3F51B5"));
                galoy_detaill.setTextColor(Color.parseColor("#707070"));
                galoy_times.setTextColor(Color.parseColor("#707070"));

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lind_locaiton, zoomLevel));

            }
        });


        galoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                galoy.setBackgroundColor(Color.parseColor("#007896"));
                lind.setBackgroundColor(Color.WHITE);

                galoy_name.setTextColor(Color.WHITE);
                galoy_detaill.setTextColor(Color.WHITE);
                galoy_times.setTextColor(Color.WHITE);

                Lind_name.setTextColor(Color.parseColor("#3F51B5"));
                Lind_detail.setTextColor(Color.parseColor("#707070"));
                Lind_time.setTextColor(Color.parseColor("#707070"));

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(galoy_location, zoomLevel));


            }
        });


    }

    private void setCameraWithCoordinationBounds(Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }

    public boolean checakUserLocationPermision() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);

            }
            return false;
        } else {
            return true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Request_User_Location_Code:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (googleApiClient == null) {
                            builGoogleApiCilent();
                        }
                        mMap.setMyLocationEnabled(true);
                    }


                } else {
                    Toast.makeText(context, "Permistion Denind...", Toast.LENGTH_SHORT).show();
                }
                return;

        }
    }



    protected synchronized void builGoogleApiCilent()

    {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }


    @Override
    public void onLocationChanged(Location location) {

        lastlocation = location;

        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        userLocation = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(userLocation);
        markerOptions.title("User curren location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        mCurrLocationMarker = mMap.addMarker(markerOptions);


        mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(16));


        if (googleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }

    }




    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }





}
