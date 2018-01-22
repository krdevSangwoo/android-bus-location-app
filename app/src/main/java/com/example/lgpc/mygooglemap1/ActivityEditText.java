package com.example.lgpc.mygooglemap1;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ActivityEditText extends Activity {

    Button btn;
    double gps1, gps2;
    String busNum, currentTime;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edt_text);

        Log.i("OnCreate", "OnCreateStart");

        btn = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("OnCreate", "ButtonListener Start");
                new ActivityEditText.JSONDataLoadTask().execute();
                Log.i("OnCreate", "Server Data In?");

            }
        });
        Log.i("Oncreate", "OnCreate End");
    }

    private class JSONDataLoadTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... strs){
            Log.i("On Json Data Load", "doInBackground");
            return GpsDataUpdate();
        }
        protected void onPostExecute(String result){
            Log.i("On Json Data Load", "onPostExecute");
        }
    }

    public String GpsDataUpdate(){
        StringBuffer sb = new StringBuffer();
        try{
            Log.i("On getJsonText", "Function Start");
            String jsonPage = new HttpServerConnect().
                    getData("http://13.124.201.205/k2m.php");

            Log.i("On getJsonText", "Parsing Start");
            JSONObject json = new JSONObject(jsonPage);
            JSONArray jArr = json.getJSONArray("MyData");

            for(int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(0);
                gps1 = Double.parseDouble(json.getString("위도"));
                gps2 = Double.parseDouble(json.getString("경도"));
                currentTime = json.getString("시간");
                busNum = json.getString("번호");

                Log.i("On getJsonText", "Parsing End, Next is Intent");

                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("gpsX", gps1);
                intent.putExtra("gpsY", gps2);
                intent.putExtra("bus", busNum);
                intent.putExtra("time", currentTime);
                startActivity(intent);

                sb.append("\n");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return sb.toString();
    }
}