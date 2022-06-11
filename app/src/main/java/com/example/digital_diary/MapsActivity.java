package com.example.digital_diary;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.digital_diary.databinding.ActivityMapsBinding;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    ArrayList<Entry> entries;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if(getIntent().hasExtra("entries")){
            entries = (ArrayList<Entry>) getIntent().getSerializableExtra("entries");
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        for(int i=0;i<entries.size();i++){
            mMap.addMarker(new MarkerOptions().position(new LatLng(entries.get(i).getLatidude(),entries.get(i).getLongtidude())).title(entries.get(i).getLocation()));
        }
        Log.i("TAG", "onMapReady: "+entries.get(0).getLatidude());
        // Add a marker in Sydney and move the camera
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.setMinZoomPreference(2.0f);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(4.0f));
        mMap.setMaxZoomPreference(14.0f);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(entries.get(0).getLatidude(),entries.get(0).getLongtidude())));
    }
}