package com.example.accidentsos;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.RequestQueue;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class RouteActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, RoutingListener {

    private RequestQueue mRequestQueue;
    private static RouteActivity mInstance;
    private GoogleMap mMap;

    //current and destination location objects
    Location myLocation = null;
    Location destinationLocation = null;
    protected LatLng start = null;
    protected LatLng end = null;

    //to get location permissions.
    private final static int LOCATION_REQUEST_CODE = 23;
    private final int FINE_PERMISSION_CODE = 1;
    boolean locationPermission = false;

    //polyline object
    private List<Polyline> polylines = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        //request location permission.
        requestPermision();

        //init google map fragment to show map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.routemap);
        mapFragment.getMapAsync(this);
    }

    private void requestPermision() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},FINE_PERMISSION_CODE);
            return;
        } else {
            locationPermission = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //if permission granted.
                    locationPermission = true;
                    getMyLocation();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }

            }
        }
    }

    //to get user location
    private void getMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},FINE_PERMISSION_CODE);
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                myLocation=location;
                LatLng ltlng=new LatLng(location.getLatitude(),location.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                        ltlng, 16f);
                mMap.animateCamera(cameraUpdate);
            }
        });


        //get destination location when user click on map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                end=latLng;

                mMap.clear();

                start=new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
                //start route finding
//                Findroutes(start,end);
            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getMyLocation();



    }



    // function to find Routes.
    public void Findroutes(LatLng Start, LatLng End)
    {
        if(Start==null || End==null) {
            Toast.makeText(RouteActivity.this,"Unable to get location", Toast.LENGTH_LONG).show();
        }
        else
        {

            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener((RoutingListener) RouteActivity.this)
                    .alternativeRoutes(true)
                    .waypoints(Start, End)
                    .key(getString(R.string.Api_key))  //also define your api key here.
                    .build();
            routing.execute();
        }
    }



    //Routing call back functions.
    @Override
    public void onRoutingFailure(RouteException e) {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar snackbar= Snackbar.make(parentLayout, e.toString(), Snackbar.LENGTH_LONG);
        snackbar.show();
//        Findroutes(start,end);
    }

    @Override
    public void onRoutingStart() {
        Toast.makeText(RouteActivity.this,"Finding Route...",Toast.LENGTH_LONG).show();
    }

    //If Route finding success..
    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

        CameraUpdate center = CameraUpdateFactory.newLatLng(start);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        if(polylines!=null) {
            polylines.clear();
        }
        PolylineOptions polyOptions = new PolylineOptions();
        LatLng polylineStartLatLng=null;
        LatLng polylineEndLatLng=null;


        polylines = new ArrayList<>();
        //add route(s) to the map using polyline
        for (int i = 0; i <route.size(); i++) {

            if(i==shortestRouteIndex)
            {
                polyOptions.color(getResources().getColor(R.color.colorPrimary));
                polyOptions.width(15);
                polyOptions.addAll(route.get(shortestRouteIndex).getPoints());
                Polyline polyline = mMap.addPolyline(polyOptions);
                polylineStartLatLng=polyline.getPoints().get(0);
                int k=polyline.getPoints().size();
                polylineEndLatLng=polyline.getPoints().get(k-1);
                polylines.add(polyline);

            }
            else {

            }

        }

        //Add Marker on route starting position
        MarkerOptions startMarker = new MarkerOptions();
        startMarker.position(polylineStartLatLng);
        startMarker.title("My Location");
        mMap.addMarker(startMarker);

        //Add Marker on route ending position
        MarkerOptions endMarker = new MarkerOptions();
        endMarker.position(polylineEndLatLng);
        endMarker.title("Destination");
        mMap.addMarker(endMarker);
    }

    @Override
    public void onRoutingCancelled() {
//        Findroutes(start,end);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        Findroutes(start,end);

    }
}