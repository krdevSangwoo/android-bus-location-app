package com.example.lgpc.mygooglemap1;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class StationGpsValue {

    private ArrayList<Double> stationLatitude = new ArrayList<Double>();
    private ArrayList<Double> stationLongitude = new ArrayList<Double>();
    private ArrayList<Double> stationDistance = new ArrayList<Double>();
    private ArrayList<String> stationName = new ArrayList<String>();
    private ArrayList<LatLng> stationLatLng = new ArrayList<LatLng>();

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

    public ArrayList getLatLng() {
        for (int i = 0; i < stationLatitude.size(); i++) {
            stationLatLng.add(new LatLng(stationLatitude.get(i), stationLongitude.get(i)));
        }
        return stationLatLng;
    }

    public ArrayList getStationDistance(){
        for(int i = 0; i < stationLatitude.size() - 1; i++){
            stationDistance.add(getDistance(new LatLng(stationLatitude.get(i),
                    stationLongitude.get(i)), new LatLng(stationLatitude.get(i+1),
                    stationLongitude.get(i+1))));
        }
        return stationDistance;
    }

    public double getDistance(LatLng bus, LatLng station){
        final int RADIUS = 6371;
        double lat1 = bus.latitude;
        double lat2 = station.latitude;
        double lon1 = bus.longitude;
        double lon2 = station.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double value = RADIUS * c * 1000; // m단위 기준, *1000빼면 km

        return value;
    }
}
