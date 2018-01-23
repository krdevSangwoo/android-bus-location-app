package com.example.lgpc.mygooglemap1;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double xGps, yGps, busGps1, busGps2;
    TextView number, gps1, gps2, currentTime;
    String latitude, longitude, code, time;
    boolean loopFlag = true;
    int num = 1;
    MarkerOptions markerOptions;

    Handler handler = new Handler(new Handler.Callback(){
        public boolean handleMessage(Message message){
            Log.i("CheckOnLog", "onHandler");

            Bundle bundle = message.getData();
            gps1.setText(latitude);
            gps2.setText(longitude);
            number.setText(bundle.getString("bcode"));
            currentTime.setText(num + ""); // 테스트용

            LatLng busLocation = new LatLng(busGps1, busGps2);
            LatLng stationLocation = new LatLng(xGps, yGps);

            mMap.clear();

            markerOptions = new MarkerOptions();
            markerOptions.position(busLocation).title(Geocoding()).snippet(
                    "(" + busGps1 + ", " + busGps2 + ")").draggable(true);
            mMap.addMarker(markerOptions);

            mMap.addMarker(new MarkerOptions().position(stationLocation).title(Geocoding())
                    .snippet("(" + xGps + ", " + yGps + ")")).setDraggable(true);

            return true;
        }
    });

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

        number.setText(intent.getStringExtra("bus"));
        currentTime.setText(intent.getStringExtra("time"));

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void onBackPressed(){ // 뒤로가기 버튼
        super.onBackPressed();
        loopFlag = false;
    }

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

        new GpsDataUpdate().start();

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

    class GpsDataUpdate extends Thread{

        public void run(){

            try {
                StringBuffer sb = new StringBuffer();

                while(loopFlag) {

                    HttpServerConnect http = new HttpServerConnect();
                    String jsonPage = http.getData(
                            "http://13.124.201.205/k2m.php");

                    JSONObject json = new JSONObject(jsonPage);
                    JSONArray jArr = json.getJSONArray("MyData");
                    json = jArr.getJSONObject(0);

                    Log.i("CheckOnLog", "onThreadRun");

                    code = json.getString("번호");
                    latitude = json.getString("위도");
                    longitude = json.getString("경도");
                    time = json.getString("시간");

                    busGps1 = Double.parseDouble(latitude);
                    busGps2 = Double.parseDouble(longitude);

                    Message message = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("bcode", code);
                    bundle.putInt("bnum", num); // 테스트용
                    message.setData(bundle);
                    handler.sendMessage(message);
                    num += 1;

                    Thread.sleep(5000);
                }
                sb.append("\n");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
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