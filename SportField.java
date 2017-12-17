package com.example.mo.shareyousport;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**
 * CLASS REPRESENTANT UN EVENEMENT
 */
public class SportField {
    private String id;
    private String name;
    private String adress;
    private String city;
    private LatLng coord;

    public SportField(){
        id = "Undefined ID";
        name ="Undefined name";
        adress = "Undefined adress";
        city = "Undefined city";
        coord = null;
    }

    public SportField(String _id, String _name, String _adress, String _city, LatLng _coord){
        id = _id;
        name = _name;
        adress = _adress;
        city = _city;
        coord = _coord;
    }




    public String getId() {
        return id;
    }

    public String getAdress() {
        return adress;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public LatLng getCoord() {
        return coord;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCoord(LatLng coord) {
        this.coord = coord;
    }

    @Override
    public String toString() {
        return "SportField{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", adress='" + adress + '\'' +
                ", city='" + city + '\'' +
                ", coord=" + coord +
                '}';
    }
}
