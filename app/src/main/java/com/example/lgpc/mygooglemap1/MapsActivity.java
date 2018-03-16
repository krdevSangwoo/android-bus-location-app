package com.example.lgpc.mygooglemap1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double xGps, yGps, busGps1, busGps2; // 정류장 위치와 버스 위치
    TextView number, gps1, gps2, currentTime, stationHead, stationInform;
    String latitude, longitude, code, time, name;
    boolean loopFlag = true;
    Bitmap bus, busStation;
    ArrayList<Double> stationLatitude, stationLongitude;
    ArrayList<String> stationName;
    ArrayList<LatLng> stationLatLng;

    // 실시간 처리
    Handler handler = new Handler(new Handler.Callback(){
        public boolean handleMessage(Message message){
            Log.i("CheckOnLog", "onHandler_Map");

            Bundle bundle = message.getData();
            gps1.setText(latitude);
            gps2.setText(longitude);
            number.setText(bundle.getString("bcode"));
            currentTime.setText(bundle.getString("btime"));

            LatLng busLocation = new LatLng(busGps1, busGps2);

            mMap.clear();

            // 비트맵 변수에 이미지 받아와서 사이즈 조절
            bus = BitmapFactory.decodeResource(getResources(), R.drawable.bus);
            bus = Bitmap.createScaledBitmap(bus, 45, 38, true);

            busStation = BitmapFactory.decodeResource(getResources(), R.drawable.bus_station);
            busStation = Bitmap.createScaledBitmap(busStation,
                    35, 35, true);

            stationMarking(mMap);

            mMap.addMarker(new MarkerOptions().position(busLocation).
                    icon(BitmapDescriptorFactory.fromBitmap(bus)).
                    title(Geocoding(busLocation)).
                    snippet("(" + busGps1 + ", " + busGps2 + ")").draggable(true));
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
        stationHead = (TextView)findViewById(R.id.stationHead);
        stationInform = (TextView)findViewById(R.id.stationInform);

        Intent intent = getIntent(); // 클릭하여 넘어온 정류장의 데이터 받기
        xGps = intent.getDoubleExtra("gpsX", 0);
        yGps = intent.getDoubleExtra("gpsY", 0);
        name = intent.getStringExtra("station");

        stationHead.setText(name);
        stationInform.setText(Geocoding(new LatLng(xGps, yGps)) + "\n"
        + "(" + xGps + ", " + yGps + ")");
        loopFlag = true;

        StationGpsValue gps = new StationGpsValue();
        stationLatitude = gps.getLatitude();
        stationLongitude = gps.getLongitude();
        stationName = gps.getName();
        stationLatLng = gps.getLatLng();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    // 마지막에 요부분만 추가함!
    @Override
    protected void onStop() {
        loopFlag = false; // 스레드-핸들러 무한루프 중지
        super.onStop();
    }

    public void onBackPressed(){ // 뒤로가기 버튼
        super.onBackPressed();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng location = new LatLng(xGps, yGps);
        CameraPosition position = new CameraPosition.Builder().target(location)
                .zoom(17f).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));

        /*
        markerOptions = new MarkerOptions();
        markerOptions.position(location).title(Geocoding()).snippet(
                "(" + xGps + ", " + yGps + ")");
        mMap.addMarker(markerOptions);
        */
        new GpsDataUpdate().start();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                stationHead.setText(marker.getTitle());
                stationInform.setText(marker.getSnippet());
                xGps = marker.getPosition().latitude;
                yGps = marker.getPosition().longitude;

                return false;
            }
        });
        /*
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
        */
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

                    Log.i("CheckOnLog", "onThreadRun_Map");

                    code = json.getString("번호");
                    latitude = json.getString("위도");
                    longitude = json.getString("경도");
                    time = json.getString("시간");

                    busGps1 = Double.parseDouble(latitude);
                    busGps2 = Double.parseDouble(longitude);

                    Message message = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("bcode", code);
                    bundle.putString("btime", time);
                    message.setData(bundle);
                    handler.sendMessage(message);

                    Thread.sleep(5000);
                }
                sb.append("\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stationMarking(GoogleMap map){

        for(int i = 0; i < stationLatitude.size(); i++){
            map.addMarker(new MarkerOptions().position(stationLatLng.get(i)).
                    icon(BitmapDescriptorFactory.fromBitmap(busStation)).
                    title(stationName.get(i) + "").
                    snippet(Geocoding(stationLatLng.get(i)) + "\n" +
                            "(" + stationLatitude.get(i) + ", " +
                            stationLongitude.get(i) + ")").draggable(true));
        }
    }

    public String Geocoding(LatLng lat) { // 입력한 위도 경도를 주소 문자열로 변환해줌
        Geocoder geocoder = new Geocoder(getApplicationContext());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(lat.latitude, lat.longitude, 1);

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