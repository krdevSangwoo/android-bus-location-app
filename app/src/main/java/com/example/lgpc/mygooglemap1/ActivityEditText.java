package com.example.lgpc.mygooglemap1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.gms.maps.model.LatLng;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class ActivityEditText extends Activity {

    // 버튼을 커스텀 버튼으로 변경 필요 -> 카카오 버스처럼
    Button btnStationA, btnStationB, btnStationC, btnStationD, btnStationE;
    ArrayList<Double> stationLatitude, stationLongitude, stationDistance;
    ArrayList<LatLng> stationLatLng;
    ArrayList<String> stationName;
    String latitude, longitude, code, time;
    double busGps1, busGps2, distanceFromStationToBus;
    int stationNearPositionNum;
    boolean loopFlag = true;
    TextView test1, test2;

    Handler handler = new Handler(new Handler.Callback(){
        public boolean handleMessage(Message message){
            Log.i("CheckOnLog", "onHandler_Bar");

            // 여기서는 현재 버스 위도 경도 값만 이용 되고 번호와 시간 데이터는 이용되지 않고 있음
            Bundle bundle = message.getData();
            LatLng busLocation = new LatLng(busGps1, busGps2);
            StationGpsValue value = new StationGpsValue();

            // 1. 버스와 가장 가까운 정류장 번호 찾기
            distanceFromStationToBus = value.getDistance(stationLatLng.get(0), busLocation);
            stationNearPositionNum = 0; // 정류장 번호는 0부터 시작하며 항상 출발지 정류장임
            for(int i = 1; i < stationLatitude.size(); i++){
                double tmp = value.getDistance(stationLatLng.get(i), busLocation); // 일회용 변수
                if(distanceFromStationToBus > tmp) { // 버스가 어느 정류장 번호와 가장 가까운가?
                    distanceFromStationToBus = tmp;
                    stationNearPositionNum = i;
                }
            }

            // 2. 버스와 가장 가까운 정류장 번호를 모든 경우에 버스 출발지 정류장 번호로 설정 시키기
            if(stationNearPositionNum == 0){ } // 첫 정류장

            else if(stationNearPositionNum == stationDistance.size()) // 끝 정류장
                stationNearPositionNum -= 1;

            else{ // 처음과 끝을 제외한 나머지 정류장
                if(value.getDistance(stationLatLng.get(stationNearPositionNum - 1),
                        busLocation) <= value.getDistance(stationLatLng.
                        get(stationNearPositionNum + 1), busLocation))
                    stationNearPositionNum -= 1; // 정류장 번호를 출발지 버스 정류장 번호로 설정
            }

            // 3. UI 수정
            // ui 수정은 기존 버튼 이미지 위에 addContentView()를 이용
            // 또는 이미지뷰 객체 생성 후 리소스를 준 뒤 setX,Y로 위치 설정시키기
            if((stationDistance.get(stationNearPositionNum) / 3)
                    >= distanceFromStationToBus){
                // 버스가 정류장 사이 거리 3분의 1지점 이하 일 때 -> ui 수정
            }
            else if ((stationDistance.get(stationNearPositionNum) / 3)
                    < distanceFromStationToBus){
                // 버스가 정류장 사이 거리 3분의 1지점 이상 일 때 -> ui 수정
            }
            else if ((stationDistance.get(stationNearPositionNum) / 3 * 2)
                    < distanceFromStationToBus){
                // 버스가 정류장 사이 거리 3분의 2지점 이상 일 때 -> ui 수정
            }
            // 정류장 번호가 출발지 정류장 번호로 되었는지 테스트
            test1.setText(stationName.get(stationNearPositionNum));
            test2.setText(stationName.get(stationNearPositionNum + 1));
            return true;
        }
    });

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edt_text);

        StationGpsValue gps = new StationGpsValue();
        stationLatitude = gps.getLatitude();
        stationLongitude = gps.getLongitude();
        stationDistance = gps.getStationDistance();
        stationLatLng = gps.getLatLng();
        stationName = gps.getName();

        test1 = (TextView)findViewById(R.id.test1);
        test2 = (TextView)findViewById(R.id.test2);


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

    @Override
    protected void onStart() { // 처음 시작 : create->start->resume
        loopFlag = true;        // 다시 시작 : pause->stop->restart->start->resume
        new GpsDataUpdateOnBar().start();

        super.onStart();
    }

    @Override
    protected void onStop() {
        loopFlag = false; // 스레드-핸들러 무한루프 중지
        super.onStop();
    }

    public void onBackPressed(){ // 뒤로가기 버튼 처리
        super.onBackPressed();
    }

    class GpsDataUpdateOnBar extends Thread{

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

                    Log.i("CheckOnLog", "onThreadRun_Bar");

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
}