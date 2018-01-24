package com.example.lgpc.mygooglemap1;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class StationGpsValue {

    ArrayList<Double> stationLatitude = new ArrayList<Double>();
    ArrayList<Double> stationLongitude = new ArrayList<Double>();
    ArrayList<String> stationName = new ArrayList<String>();
    ArrayList<LatLng> stationGps = new ArrayList<LatLng>();

    public ArrayList getLatitude(){
        stationLatitude.add(37.274689);
        stationLatitude.add(37.270816);
        stationLatitude.add(37.271096);
        stationLatitude.add(37.274938);
        stationLatitude.add(37.276684);
        return stationLatitude;
    }

    public ArrayList getLongitude(){
        stationLongitude.add(127.115757);
        stationLongitude.add(127.125391);
        stationLongitude.add(127.126952);
        stationLongitude.add(127.130702);
        stationLongitude.add(127.134326);
        return stationLongitude;
    }

    public ArrayList getName(){
        stationName.add("기흥역");
        stationName.add("강남대 정류장");
        stationName.add("씨유 앞");
        stationName.add("인사관");
        stationName.add("이공관");
        return stationName;
    }

    public ArrayList getGps() {
        for (int i = 0; i < stationLatitude.size(); i++) {
            stationGps.add(new LatLng(stationLatitude.get(i), stationLongitude.get(i)));
        }
        return stationGps;
    }
}
