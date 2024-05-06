package com.example.accidentsos;

import static android.content.Context.MODE_PRIVATE;
import static com.example.accidentsos.AppConfig.TAG;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.accidentsos.ServerResponses.CommonResponse;
import com.example.accidentsos.api.RestClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Home extends Fragment {

    private Button addLocationBtn, alertsbtn;
    private RadioGroup radioGroup;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;

    RadioButton radioButton;
    private String id;

    Location location;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        addLocationBtn = view.findViewById(R.id.addlocationbtn);
        alertsbtn = view.findViewById(R.id.alerts);

        // Initialize fused location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        // Initialize location request
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000); // 100 seconds

        radioGroup = view.findViewById(R.id.radiogrp);
        SharedPreferences sign = getActivity().getSharedPreferences("sign", MODE_PRIVATE);
        String status = sign.getString("status", null);


        Log.d(TAG, "onCreate: " + status);
        if (status != null) {
            if (status.equals("signedout")) {
                getActivity().finish();
            }
        }

        SharedPreferences sf =getActivity().getSharedPreferences("usersf",MODE_PRIVATE);
        id= sf.getString("id",null);

        // on below line we are adding check change listener for our radio group.
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // on below line we are getting radio button from our group.
                 radioButton = view.findViewById(checkedId);

                SharedPreferences sf = getActivity().getSharedPreferences("usersf", MODE_PRIVATE);
                SharedPreferences.Editor editor = sf.edit();
                editor.putString("vehicle", radioButton.getText().toString());
                editor.apply();

            }
        });


        addLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check if GPS is enabled
                LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
                if (locationManager != null && !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    // GPS is not enabled, show dialog to enable it
                    new AlertDialog.Builder(requireContext())
                            .setTitle("GPS is not enabled")
                            .setMessage("Would you like to enable GPS?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Open location settings page
                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                } else {

                    // Check location permissions
                    if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // Permission is not granted, request it
                        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                    }
                    // GPS is enabled, proceed with your logic
                    new AlertDialog.Builder(getContext())
                            .setTitle("Location")
                            .setIcon(R.drawable.currenticon)
                            .setMessage("GPS Turned On")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Handle logic when user clicks "Yes"
                                }
                            })
                            .show();
                }


            }
        });


        alertsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (radioGroup.getCheckedRadioButtonId() == -1) {
                    // No radio button is selected, show a message to select one
                    new AlertDialog.Builder(getContext())
                            .setTitle("Select Vehicle")
                            .setMessage("Please select a vehicle option.")
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                    return;
                }

                startLocationUpdates();

                if(location!=null) {
                    // Proceed if a radio button is selected
                    new AlertDialog.Builder(getContext())
                            .setTitle("Alert")
                            .setIcon(R.drawable.img_10)
                            .setMessage("Alert has been sent")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                    startActivity(new Intent(getActivity(), Map.class));
                                    showNotification();
                                }
                            })
                            .show();
                }else {
                    Toast.makeText(getContext(), "Click Again To Alert", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void showNotification() {
        if (getContext() == null) {
            return;
        }

        Uri sound = Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.raw.track);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext(), "default_notification_channel_id")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Alert")
                .setSound(sound)
                .setContentText("Emergency Alert");
        NotificationManager mNotificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel("NOTIFICATION_CHANNEL_ID", "Alerts", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationChannel.setSound(sound, audioAttributes);
            mBuilder.setChannelId("NOTIFICATION_CHANNEL_ID");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        mNotificationManager.notify((int) System.currentTimeMillis(), mBuilder.build());
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Check for location permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                return;
            }
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);

            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // Handle location updates here
                 location = locationResult.getLastLocation();
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    Call<CommonResponse> responseCall = RestClient.makeApi().alert(id,addresses.get(0).getAddressLine(0), String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()),radioButton.getText().toString());
                    responseCall.enqueue(new Callback<CommonResponse>() {
                        @Override
                        public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                            if(response.isSuccessful()){
                                if(response.body().getStatus().equals("200")){
                                    Toast.makeText(getContext(), "location send", Toast.LENGTH_SHORT).show();
                                }

                                if (response.body().getStatus().equals("404")){
                                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<CommonResponse> call, Throwable t) {
                            Toast.makeText(getContext(), "check internet"+t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }, null);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop location updates when activity is paused
        fusedLocationClient.removeLocationUpdates(new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
            }
        });
    }
}
