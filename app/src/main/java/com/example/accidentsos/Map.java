package com.example.accidentsos;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.accidentsos.ServerResponses.CommonResponse;
import com.example.accidentsos.api.RestClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Map extends AppCompatActivity  {

    private final int FINE_PERMISSION_CODE = 1;
    String id;
    GoogleMap mymap;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    SupportMapFragment mapFragment;
    private String vehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_map);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

         mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        getLastLocation();

        SharedPreferences sf =getSharedPreferences("usersf",MODE_PRIVATE);
        id= sf.getString("id",null);
        vehicle = sf.getString("vehicle",null);

    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!= null){

                    currentLocation =location;

                    mapFragment.getMapAsync(new OnMapReadyCallback(){
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            mymap=googleMap;
                            try {
                                LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                                mymap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                                mymap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title("current location"));
                                Geocoder geocoder = new Geocoder(Map.this, Locale.getDefault());
                                List<Address> addresses = geocoder.getFromLocation(currentLocation.getLatitude(),currentLocation.getLongitude(),1);
//                                Toast.makeText(Map.this, ""+addresses.get(0).getAddressLine(0), Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "onMapReady: "+addresses.get(0).getAddressLine(0));


                                Call<CommonResponse> responseCall = RestClient.makeApi().alert(id,addresses.get(0).getAddressLine(0), String.valueOf(currentLocation.getLatitude()), String.valueOf(currentLocation.getLongitude()),vehicle);
                                responseCall.enqueue(new Callback<CommonResponse>() {
                                    @Override
                                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                                        if(response.isSuccessful()){
                                            if(response.body().getStatus().equals("200")){
                                                Toast.makeText(Map.this, "location send", Toast.LENGTH_SHORT).show();
                                            }

                                            if (response.body().getStatus().equals("404")){
                                                Toast.makeText(Map.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<CommonResponse> call, Throwable t) {
                                        Toast.makeText(Map.this, "check internet"+t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    });


                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==FINE_PERMISSION_CODE){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }
        }else {
            Toast.makeText(this, "Location Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }
}