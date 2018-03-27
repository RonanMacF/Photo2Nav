package com.trinity.ebusiness.photo2nav;

import java.util.ArrayList;

/**
 * Created by ronan on 20/03/2018.
 * This is the class which will be saved in the Firbase database, there is 2 arrayLists, one containing latitude and the other containing logitude
 */

public class UserLocationPoints {

    public ArrayList<Float> latitude;
    public ArrayList<Float> longitude;

    public UserLocationPoints() {
        this.latitude = new ArrayList<>();
        this.longitude = new ArrayList<>();
    }

    public UserLocationPoints(float lat, float lon) {
        this.latitude = new ArrayList<>();
        this.longitude = new ArrayList<>();
    }

    public ArrayList<Float> getLatitude() {
        return latitude;
    }

    public void setLatitude(ArrayList<Float> latitude) {
        this.latitude = latitude;
    }

    public ArrayList<Float> getLongitude() {
        return longitude;
    }

    public void setLongitude(ArrayList<Float> longitude) {
        this.longitude = longitude;
    }

    public void updateLatitude(float newLat){
        latitude.add(newLat);
    }

    public void updateLongitude(float newLon){
        longitude.add(newLon);
    }
}
