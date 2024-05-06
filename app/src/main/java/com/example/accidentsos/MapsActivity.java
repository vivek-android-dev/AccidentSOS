package com.example.accidentsos;

import static com.example.accidentsos.AppConfig.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.accidentsos.DistanceResponse.GDistanceResponse;
import com.example.accidentsos.MapResponse.NearByHospitalResponse;
import com.example.accidentsos.ServerResponses.CommonResponse;
import com.example.accidentsos.api.DistanceClient;
import com.example.accidentsos.api.MapClient;
import com.example.accidentsos.api.RestClient;
import com.example.accidentsos.databinding.ActivityMapsBinding;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, RoutingListener {

    private static final int FINE_PERMISSION_CODE = 1;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 2;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private String pid, pname, page, pdob, paddress, pgender, platitude, plongitude, pmob, pblood;
    private PlacesClient placesClient;
    private Polyline polyline;
    private Marker personMarker;
    protected LatLng start = null;
    protected LatLng end = null;
    private List<Polyline> polylines = null;
    private List<GooglePlaceModel> googlePlaceModelList = new ArrayList<>();
    private ArrayList<String> userSavedLocationId = new ArrayList<>();
    Location myLocation = null;
    ImageView imageView, imageView1;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    Marker userLocationMarker;
    Circle userLocationAccuracyCircle;
    CardView cardView, accept, decline, distancecard, complete;

    TextView distance;

    boolean b = false;
    String string;
    LatLng platlon;
    LatLng currentLocation;
    private ArrayList<NearByHospitalResponse> data = new ArrayList<>();

    @SuppressLint("PotentialBehaviorOverride")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imageView = findViewById(R.id.imageView5);
        cardView = findViewById(R.id.cardview2);
        distancecard = findViewById(R.id.cardview);
        accept = findViewById(R.id.acceptbtn);
        decline = findViewById(R.id.rejectbtn);
        distance = findViewById(R.id.text);
        complete = findViewById(R.id.complete);
        SharedPreferences sf = getSharedPreferences("notificationsf", Context.MODE_PRIVATE);

        pid = sf.getString("id", null);
        pname = sf.getString("name", null);
        page = sf.getString("age", null);
        pdob = sf.getString("dob", null);
        paddress = sf.getString("address", null);
        pgender = sf.getString("gender", null);
        platitude = sf.getString("latitude", null);
        plongitude = sf.getString("longitude", null);
        pmob = sf.getString("mobile", null);
        pblood = sf.getString("blood", null);

        SharedPreferences usf = getSharedPreferences("usersf", MODE_PRIVATE);
        String uid = usf.getString("id", null);
        String uname = usf.getString("name", null);

        Places.initialize(getApplicationContext(), getString(R.string.Api_key));
        placesClient = Places.createClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, Notifications.class));
            }
        });
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (end != null) {
                    Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(end.latitude, end.longitude, 1);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    if (addresses != null && !addresses.isEmpty()) {
                        Address returnedAddress = addresses.get(0);
                        String address = returnedAddress.getAddressLine(0); // Complete address
                        String city = returnedAddress.getLocality(); // City
                        String state = returnedAddress.getAdminArea(); // State
                        String country = returnedAddress.getCountryName(); // Country
                        String postalCode = returnedAddress.getPostalCode(); // Postal code
                        String knownName = returnedAddress.getFeatureName(); // Feature name (if available)

                        Log.d(TAG, "onClick: " + address + knownName);
                    }


                }
                startActivity(new Intent(MapsActivity.this, HospitalInfo.class));
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNearbyHospitals();
                addMarkerForPerson();
                distancecard.setVisibility(View.INVISIBLE);
                if (b) {
                    cardView.setVisibility(View.VISIBLE);
                }

                mMap.resetMinMaxZoomPreference();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(platlon, 12), 1500, null);

            }
        });


        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (end == null) {
                    Toast.makeText(MapsActivity.this, "Select Marker to Directions", Toast.LENGTH_SHORT).show();
                } else {

//                    mMap.clear();
                    Findroutes(start, end);
                    distancecard.setVisibility(View.INVISIBLE);
                    cardView.setVisibility(View.INVISIBLE);
                    mMap.getProjection();
                    mMap.getCameraPosition();
                    complete.setVisibility(View.VISIBLE);
                    if (start != null) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(start, 20), 1500, null);
                    }

                }


            }


        });


        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accept.setVisibility(View.GONE);
                decline.setVisibility(View.GONE);
                cardView.setVisibility(View.VISIBLE);
//               mMap.clear();
                addMarkerForPerson();

                b = true;

                if (start != null) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(start, 15), 1250, null);

                    Call<CommonResponse> res = RestClient.makeApi().accept(pid, uid);
                    Log.d(TAG, "onClick: " + pid + uid);
                    res.enqueue(new Callback<CommonResponse>() {
                        @Override
                        public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                            if (response.isSuccessful()) {
                                if (response.body().getStatus().equals("200")) {
                                    Toast.makeText(MapsActivity.this, "Request Accepted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<CommonResponse> call, Throwable t) {
                            Log.e(TAG, "onFailure: " + t.getMessage());
                            Toast.makeText(MapsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

    }

    private void getLocationDistance() {
        String url = null;
        if (start != null && end != null) {
            String origin = start.latitude + "," + start.longitude;
            String destination = end.latitude + "," + end.longitude;

            url = "https://maps.googleapis.com/maps/api/distancematrix/json?destinations=" + destination
                    + "&origins=" + origin + "&units=imperial&key=" + getString(R.string.Api_key);


        }
        if (url != null) {

            DistanceClient.makeApi().getDistance(url).enqueue(new Callback<GDistanceResponse>() {
                @Override
                public void onResponse(Call<GDistanceResponse> call, Response<GDistanceResponse> response) {

                    Gson gson = new Gson();
                    String res = gson.toJson(response.body());
                    Log.d("Distance", "onResponse: " + res);
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equals("OK") && response.body().getRows().get(0).getElements().get(0) != null) {

                            GDistanceResponse.Row.Element element = response.body().getRows().get(0).getElements().get(0);
//                        Log.d(TAG, "onResponse: "+element.getDistance().getText()+element.getDuration().getText());
                            distance.setText(element.getDuration().getText() + "(" + element.getDistance().getText() + ")");
                            distancecard.setVisibility(View.VISIBLE);
                        }
                    }

                }

                @Override
                public void onFailure(Call<GDistanceResponse> call, Throwable t) {

                }
            });
        }
    }

    @SuppressLint({"MissingPermission", "PotentialBehaviorOverride"})
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        addMarkerForPerson();
        findNearbyHospitals();
        addMarkerForCurrentLocation();
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                myLocation = location;
                if (myLocation != null) {
                    start = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());

                    if (mMap != null) {
                        setUserLocationMarker(myLocation);
                    }

                }
            }
        });
        mMap.setMyLocationEnabled(true);


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {

                SharedPreferences sf = getSharedPreferences("Hospitalsf", MODE_PRIVATE);
                SharedPreferences.Editor editor = sf.edit();
                editor.putString("address", marker.getSnippet());
                editor.putString("hName", marker.getTitle());

                Log.d(TAG, "onMarkerClick: " + marker.getTitle());
                editor.apply();
                end = marker.getPosition();
                Findroutes(start, end);
                getLocationDistance();
                return false;
            }
        });


        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(platlon, 14));
//        mMap.setMinZoomPreference(20);
    }


    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
//            Log.d(TAG, "onLocationResult: " + locationResult.getLastLocation());
//            if (mMap != null) {
//                setUserLocationMarker(locationResult.getLastLocation());
//            }
        }
    };

    private void setUserLocationMarker(Location location) {

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        if (userLocationMarker == null) {
            //Create a new marker
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.walk));
            markerOptions.rotation(location.getBearing());
            markerOptions.anchor((float) 0.5, (float) 0.5);
            userLocationMarker = mMap.addMarker(markerOptions);
        } else {
            //use the previously created marker
            userLocationMarker.setPosition(latLng);
            userLocationMarker.setRotation(location.getBearing());
        }


    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        } else {
            // you need to request permissions...
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    private void addMarkerForCurrentLocation() {
        // Check for location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permission if not granted
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);

            }

            return;
        }

        // Get the last known location
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

    }


    private void addMarkerForPerson() {
        // Add a marker in Sydney and move the camera
        platlon = new LatLng(Double.parseDouble(platitude), Double.parseDouble(plongitude));

        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(resizeBitmap(R.drawable.sos, 120, 120));
        mMap.addMarker(new MarkerOptions().position(platlon)
                .icon(icon)
                .title(pname));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(platlon, 17), 1500, null);
    }

    private Bitmap resizeBitmap(int resId, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), resId);
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
    }

    private void findNearbyHospitals() {

        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                + platitude + "," + plongitude
                + "&radius=" + 5000 + "&type=" + "hospital" + "&key=" +
                getResources().getString(R.string.Api_key);

        MapClient.makeApi().getNearByPlaces(url).enqueue(new Callback<GoogleResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<GoogleResponseModel> call, @NonNull Response<GoogleResponseModel> response) {
                Gson gson = new Gson();
                String res = gson.toJson(response.body());
//                Log.d("TAG", "onResponse: " + res);
                if (response.errorBody() == null) {
                    if (response.body() != null) {
                        if (response.body().getGooglePlaceModelList() != null && response.body().getGooglePlaceModelList().size() > 0) {

                            googlePlaceModelList.clear();
                            for (int i = 0; i < response.body().getGooglePlaceModelList().size(); i++) {

                                if (userSavedLocationId.contains(response.body().getGooglePlaceModelList().get(i).getPlaceId())) {
                                    response.body().getGooglePlaceModelList().get(i).setSaved(true);
                                }
                                googlePlaceModelList.add(response.body().getGooglePlaceModelList().get(i));
                                addMarker(response.body().getGooglePlaceModelList().get(i), i);
                            }

                        } else if (response.body().getError() != null) {
                            Snackbar.make(binding.getRoot(),
                                    response.body().getError(),
                                    Snackbar.LENGTH_LONG).show();
                        } else {

                            googlePlaceModelList.clear();

                        }
                    }

                } else {
                    Log.d("TAG", "onResponse: " + response.errorBody());
                    Toast.makeText(MapsActivity.this, "Error : " + response.errorBody(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GoogleResponseModel> call, Throwable t) {

                Log.d("TAG", "onFailure: " + t);

            }
        });

    }

    private void addMarker(GooglePlaceModel googlePlaceModel, int position) {

        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(googlePlaceModel.getGeometry().getLocation().getLat(),
                        googlePlaceModel.getGeometry().getLocation().getLng()))
                .title(googlePlaceModel.getName())
                .snippet(googlePlaceModel.getVicinity());
        markerOptions.icon(getCustomIcon());
        mMap.addMarker(markerOptions).setTag(position);
    }

    private BitmapDescriptor getCustomIcon() {

        Drawable background = ContextCompat.getDrawable(MapsActivity.this, R.drawable.hospital);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public void Findroutes(LatLng Start, LatLng End) {
        if (Start == null || End == null) {
            Toast.makeText(MapsActivity.this, "Unable to get Route", Toast.LENGTH_LONG).show();
        } else {

            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.WALKING)
                    .withListener((RoutingListener) MapsActivity.this)
                    .alternativeRoutes(true)
                    .waypoints(Start, End)
                    .key(getString(R.string.Api_key))  //also define your api key here.
                    .build();
            routing.execute();
        }
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(parentLayout, e.toString(), Snackbar.LENGTH_LONG);
        snackbar.show();
//        Findroutes(start,end);
    }

    @Override
    public void onRoutingStart() {
        Toast.makeText(MapsActivity.this, "Finding Route...", Toast.LENGTH_LONG).show();
    }

    //If Route finding success..
    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(start,17),1500,null);
        if (polylines != null) {
            polylines.clear();
        }

        PolylineOptions polyOptions = new PolylineOptions();
        LatLng polylineStartLatLng = null;
        LatLng polylineEndLatLng = null;


        polylines = new ArrayList<>();
        //add route(s) to the map using polyline
        for (int i = 0; i < route.size(); i++) {

            if (i == shortestRouteIndex) {

                polyOptions.color(getResources().getColor(R.color.colorPrimary));
                polyOptions.width(15);
                polyOptions.addAll(route.get(shortestRouteIndex).getPoints());
                if (polyline != null) {
                    polyline.remove();
                }
                polyline = mMap.addPolyline(polyOptions);
                polylineStartLatLng = polyline.getPoints().get(0);
                int k = polyline.getPoints().size();
                polylineEndLatLng = polyline.getPoints().get(k - 1);
                polylines.add(polyline);

            } else {

            }

        }

//        //Add Marker on route starting position
//        MarkerOptions startMarker = new MarkerOptions();
//        startMarker.position(polylineStartLatLng);
//        startMarker.title("My Location");
//        mMap.addMarker(startMarker);

//        //Add Marker on route ending position
//        MarkerOptions endMarker = new MarkerOptions();
//        endMarker.position(polylineEndLatLng);
//        endMarker.title("Destination");
//        mMap.addMarker(endMarker);
    }

    @Override
    public void onRoutingCancelled() {
        Findroutes(start, end);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Findroutes(start, end);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}