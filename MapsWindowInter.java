package com.example.mo.shareyousport;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import android.widget.Toast;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowCloseListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

/**
 * FINALEMENT INUTILISER
 */

class MapsWindowInter implements InfoWindowAdapter{



    LayoutInflater inflater = null;


    MapsWindowInter(LayoutInflater inflater){
        this.inflater = inflater;

    }

    @Override
    public View getInfoContents(Marker marker) {
        View myContentsView = inflater.inflate(R.layout.info_window_inter, null);

        LatLng latLng = marker.getPosition();



        TextView tvParticipants = ((TextView)myContentsView.findViewById(R.id.participants_number));
        tvParticipants.setText("Participants à récupérer via script");
        TextView tvEquipments = ((TextView)myContentsView.findViewById(R.id.equipments));
        tvEquipments.setText("Equipements à déterminer via script");
        TextView tvGps = ((TextView)myContentsView.findViewById(R.id.gps_coordonate));
        tvGps.setText("Coordonnée GPS: "+latLng.latitude+","+latLng.longitude);
        TextView tvAddress = ((TextView)myContentsView.findViewById(R.id.address));
        tvAddress.setText("Addresse à récupérer via script ");


        return myContentsView;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        // TODO Auto-generated method stub
        return null;
    }

}
