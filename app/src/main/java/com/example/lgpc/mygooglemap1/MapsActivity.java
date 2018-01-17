package com.example.lgpc.mygooglemap1;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double xGps, yGps;
    TextView number, gps1, gps2, currentTime;
    MarkerOptions markerOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        number = (TextView)findViewById(R.id.number);
        gps1 = (TextView)findViewById(R.id.gps1);
        gps2 = (TextView)findViewById(R.id.gps2);
        currentTime = (TextView)findViewById(R.id.currentTime);

        Intent intent = getIntent();
        xGps = intent.getDoubleExtra("gpsX", 0);
        yGps = intent.getDoubleExtra("gpsY", 0);

        gps1.setText(Double.toString(xGps));
        gps2.setText(Double.toString(yGps));
        number.setText(intent.getStringExtra("bus"));
        currentTime.setText(intent.getStringExtra("time"));

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

        LatLng location = new LatLng(xGps, yGps);
        CameraPosition position = new CameraPosition.Builder().target(location)
                .zoom(15f).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));

        /*
        markerOptions = new MarkerOptions();
        markerOptions.position(location).title(Geocoding()).snippet(
                "(" + xGps + ", " + yGps + ")");
        mMap.addMarker(markerOptions);
        */

        mMap.addMarker(new MarkerOptions().position(location).title(Geocoding())
        .snippet("(" + xGps + ", " + yGps + ")"));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) { // 맵 클릭시 기존의 마커를 지우고 새로운 마커를 표기

                mMap.clear();

                xGps = latLng.latitude;
                yGps = latLng.longitude;

                MarkerOptions marker = new MarkerOptions();
                marker.position(latLng).title(Geocoding()).snippet(
                        "(" + xGps + ", " + yGps + ")");
                mMap.addMarker(marker);
            }
        });
    }

    public String Geocoding() { // 입력한 위도 경도를 주소 문자열로 변환해줌
        Geocoder geocoder = new Geocoder(getApplicationContext());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(xGps, yGps, 1);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "지오코더 서비스 사용불가",
                    Toast.LENGTH_SHORT);
            return null;
        } catch (IllegalArgumentException IllegalArgument){
            Toast.makeText(getApplicationContext(), "잘못 된 위도 경도 입력",
                    Toast.LENGTH_SHORT);
            return null;
        }

        if(addresses == null || addresses.size() == 0){
            Toast.makeText(getApplicationContext(), "주소 미발견",
                    Toast.LENGTH_SHORT);
            return null;
        }
        else{
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }
    }
}