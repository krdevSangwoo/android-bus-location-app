package com.example.lgpc.mygooglemap1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ActivityEditText extends Activity {

    Button btnStationA, btnStationB, btnStationC, btnStationD, btnStationE;
    ArrayList<Double> stationLatitude, stationLongitude;
    ArrayList<String> stationName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edt_text);

        StationGpsValue gps = new StationGpsValue();
        stationLatitude = gps.getLatitude();
        stationLongitude = gps.getLongitude();
        stationName = gps.getName();

        btnStationA = (Button)findViewById(R.id.btnStationA); // 기흥역
        btnStationA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("gpsX", stationLatitude.get(0));
                intent.putExtra("gpsY", stationLongitude.get(0));
                intent.putExtra("station", stationName.get(0));
                startActivity(intent);
            }
        });
        btnStationB = (Button)findViewById(R.id.btnStationB); // 강남대 정류장
        btnStationB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("gpsX", stationLatitude.get(1));
                intent.putExtra("gpsY", stationLongitude.get(1));
                intent.putExtra("station", stationName.get(1));
                startActivity(intent);
            }
        });
        btnStationC = (Button)findViewById(R.id.btnStationC); // 씨유 앞
        btnStationC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("gpsX", stationLatitude.get(2));
                intent.putExtra("gpsY", stationLongitude.get(2));
                intent.putExtra("station", stationName.get(2));
                startActivity(intent);
            }
        });
        btnStationD = (Button)findViewById(R.id.btnStationD); // 인사관
        btnStationD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("gpsX", stationLatitude.get(3));
                intent.putExtra("gpsY", stationLongitude.get(3));
                intent.putExtra("station", stationName.get(3));
                startActivity(intent);
            }
        });
        btnStationE = (Button)findViewById(R.id.btnStationE); // 이공관
        btnStationE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("gpsX", stationLatitude.get(4));
                intent.putExtra("gpsY", stationLongitude.get(4));
                intent.putExtra("station", stationName.get(4));
                startActivity(intent);
            }
        });
    }
}