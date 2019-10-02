package com.info.manPower.Fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.info.manPower.R;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MapDialog_fragment extends DialogFragment
{
    SupportMapFragment mMap;
    TextView txaddr;
    Button ok;
    private GoogleMap googleMap;
    private Location locationC;
    String saddress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mapp,container,false);

        getDialog().setTitle("Choose Location");

        this.mMap = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.frag_map);
        txaddr = view.findViewById(R.id.tx_maddr);
        ok = view.findViewById(R.id.button_ok);

        Check_Permission();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent("StringAddr");
                in.putExtra("Addr", saddress);
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(in);
                onDismiss(getDialog());
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        assert getFragmentManager() != null;
        Fragment fragment = (getFragmentManager().findFragmentById(R.id.frag_map));
        FragmentTransaction ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }

    private void Check_Permission()
    {
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
            locationC = locationManager.getLastKnownLocation(provider);
            getMAP();
        }

        else {
            if (Build.VERSION.SDK_INT >= 23) {
                requestPermissions(new String[] {
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION },8);

              /*  ActivityCompat.requestPermissions(getActivity(), new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    8);*/
                //Toast.makeText(getActivity(),"Allow Required Permission", Toast.LENGTH_LONG).show();

            } }
    }

    private void getMAP() {
        mMap.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(final GoogleMap mMap) {

                googleMap = mMap;
                googleMap.setMyLocationEnabled(true);

                //        Geoaddress(googleMap.getMyLocation().getLatitude(),googleMap.getMyLocation().getLongitude());

                if (locationC!=null) {
                    CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(locationC.getLatitude(), locationC.getLongitude()));
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(19);
                    mMap.getUiSettings().setZoomControlsEnabled(true);
                    googleMap.moveCamera(center);
                    googleMap.animateCamera(zoom);
                }
             /*   LatLng latLng = new LatLng(locationCt.getLatitude(),
                        locationCt.getLongitude());
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,10);
                    mMap.moveCamera(cameraUpdate);
                    googleMap.moveCamera(cameraUpdate);
                    googleMap.animateCamera(cameraUpdate);
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Current Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                   Geoaddress(locationCt.getLatitude(), locationCt.getLongitude());*/

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        googleMap.clear();

                        Geoaddress(latLng.latitude, latLng.longitude);

                        //Log.e("Latitude : "+latitude,"Longitude : "+longitude);

                        googleMap.addMarker(new MarkerOptions().position(latLng).title("Custom Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                        // Toast.makeText(getActivity(), "lat long is" + latLng, Toast.LENGTH_SHORT).show();

                    }
                });
                if (googleMap != null) {
                    Toast.makeText(getActivity(), "not null", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 8: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(getActivity(), "Permission granted", Toast.LENGTH_SHORT).show();
                    mMap.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            googleMap.setMyLocationEnabled(true);
                            googleMap.getMyLocation();
                        }
                    });
                    LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
                    Criteria criteria = new Criteria();
                    String provider = locationManager.getBestProvider(criteria, true);
                    try {   locationC = locationManager.getLastKnownLocation(provider); }
                    catch (SecurityException ex)
                    {  ex.printStackTrace();     }
                    getMAP();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


    private void Geoaddress(double latitude, double longitude) {
        try {
            Geocoder geo = new Geocoder(getActivity(), Locale.ENGLISH);
            List<Address> addresses = geo.getFromLocation(latitude, longitude, 1);
            if (addresses.isEmpty()) {
                //yourtextfieldname.setText("Waiting for Location");
            } else {
                if (addresses.size() > 0) {

                    saddress = addresses.get(0).getAddressLine(0);
                    //Toast.makeText(getActivity(), ""+saddress, Toast.LENGTH_SHORT).show();
                    txaddr.setText(""+saddress);
                    Log.e("address is", "" + addresses.get(0).getAddressLine(0));
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // getFromLocation() may sometimes fail
        }
    }


}
