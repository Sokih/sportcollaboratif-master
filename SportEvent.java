package com.example.mo.shareyousport;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**
 * CLASS REPRESENTANT UN EVENEMENT
 */
public class SportEvent {
    private String id;
    private String name;
    private String city;
    private String adress;
    private LatLng coord;
    private int playersNeeded;
    private int playerIn;
    private boolean equipment;
    private String typeSport;
    private Date debutHour;
    private Date endHour;

    public SportEvent(){
        id = "Undefined ID";
        name ="Undefined name";
        adress = "Undefined adress";
        city = "Undefined city";
        coord = null;
        playersNeeded = 0;
        playerIn = 0;
        equipment = false;
        typeSport = "Undefined sport";
    }

    public SportEvent(String _id, String _name, String _adress, String _city, LatLng _coord, int _playersNeeded, int _playerIn, boolean _equipment, String _typeSport){
        id = _id;
        name = _name;
        adress = _adress;
        city = _city;
        coord = _coord;
        playersNeeded = _playersNeeded;
        playerIn = _playerIn;
        equipment = _equipment;
         typeSport = _typeSport;
    }


    public String getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public String getAdress() {
        return adress;
    }

    public String getCity() {
        return city;
    }

    public LatLng getCoord() {
        return coord;
    }

    public int getPlayersNeeded() {
        return playersNeeded;
    }

    public int getPlayerIn() {
        return playerIn;
    }

    public boolean isEquipment() {
        return equipment;
    }


    public String getTypeSport() {
        return typeSport;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setTypeSport(String typeSport) {
        this.typeSport = typeSport;
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

    public void setPlayersNeeded(int playersNeeded) {
        this.playersNeeded = playersNeeded;
    }

    public void setPlayerIn(int playerIn) {
        this.playerIn = playerIn;
    }

    public void setEquipment(boolean equipment) {
        this.equipment = equipment;
    }



    public int addPlayer(){
        if (this.playerIn<this.playersNeeded){
            this.playerIn++;
        }
        else{
            System.out.println("Evenement remplis");
        }
        return playerIn;
    }

}
