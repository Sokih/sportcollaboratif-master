package com.example.mo.shareyousport;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import android.util.Log;
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
import com.google.android.gms.maps.model.MarkerOptions;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ImageButton;


import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;









import java.io.InputStream;

//import org.json.simple.JSONValue; Trouve run moyen de résoudre le soucis de dépendance
//import org.json.simple.JSONObject;


/**
 * VOIR LES COMMENTAIRES DE MapsActivity SI BESOIN
 */


public class MapsActivityCreation extends FragmentActivity implements OnMapReadyCallback, OnInfoWindowClickListener, LocationListener,OnMarkerClickListener {
    private SportFieldGroup userFields = new SportFieldGroup();
    private SportField markerField = new SportField();
    //mettre en place en methode pour récupérer tous les évenements en bdd ici

    private GoogleMap mMap;

    private MyTimerTask taskService;

    private LocationManager locationManager;

    private static final long MIN_TIME = 400;

    private static final float MIN_DISTANCE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER
    }


    class MyTimerTask extends TimerTask {
        public void run() {
            Intent myIntent = new Intent(MapsActivityCreation.this,EvenementService.class);
            startService(myIntent);
        }
    }
    public void displayNotification() {
        int delay = 0;
        taskService = new MyTimerTask();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(taskService, delay, 10000);
    }


    @Override
    protected void onPause( ) {
        super.onPause();
        taskService.cancel();

    }

    @Override
    protected void onResume( ) {
        super.onResume();
        displayNotification();

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        SportField intermUserField;
        mMap = googleMap;






        googleMap.setInfoWindowAdapter(new InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker arg0) {

                // Getting view from the layout file info_window_layout
                View v = getLayoutInflater().inflate(R.layout.info_window_inter_crea, null);

                // Getting the position from the marker
                LatLng latLng = arg0.getPosition();

                SportField markerField = userFields.findFieldByCoord(latLng);


                TextView tvName = ((TextView)v.findViewById(R.id.localName_field));
                tvName.setText(markerField.getName());


                TextView tvAdress = ((TextView)v.findViewById(R.id.address_field));
                tvAdress.setText(markerField.getAdress());

                TextView tvCity = ((TextView)v.findViewById(R.id.city_field));
                tvCity.setText(markerField.getCity());


                // Returning the view containing InfoWindow contents
                return v;

            }
        });





        PostClassCreate requetteHttp = new PostClassCreate();
        requetteHttp.execute(userFields);


        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerClickListener(this);

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, marker.getTitle(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(MapsActivityCreation.this, Creation.class);
        intent.putExtra("donnée", markerField.getName());
        intent.putExtra("latitude", markerField.getCoord().latitude);
        intent.putExtra("longitude", markerField.getCoord().longitude);
        intent.putExtra("adresse", markerField.getAdress());
        intent.putExtra("ville", markerField.getCity());
        startActivity(intent);
    }

    private void addMarker(GoogleMap map, double lat, double lon,
                           String adress) {
        map.addMarker(new MarkerOptions().title(adress).position(new LatLng(lat, lon)).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        mMap.animateCamera(cameraUpdate);
        //locationManager.removeUpdates(this);
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }


    //Methode utilisé pour changer d'un activité tout en passant des information à la nouvelle activité lorsque l'utilisateur clique sur un marker
    @Override
    public boolean onMarkerClick(Marker marker) {

        markerField = userFields.findFieldByCoord(marker.getPosition());

        return false;
    }


    private class PostClassCreate extends AsyncTask<SportFieldGroup, SportFieldGroup, SportFieldGroup> {
        final ProgressDialog progressDialog = new ProgressDialog(MapsActivityCreation.this, R.style.AppTheme_Dark_Dialog);
        SportFieldGroup myFields;
        @Override //Cette méthode s'execute en premier, elle ouvre une simple boite de dialogue
        protected void onPreExecute() {
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        protected SportFieldGroup doInBackground(SportFieldGroup... params) {
            myFields = params[0];
            ConnectivityManager check = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] info = check.getAllNetworkInfo();
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {

                    SportFieldGroup testField = new SportFieldGroup();
                    String result;
                    try {


                        URL url = new URL("http://10.0.2.2:3000/fields");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setConnectTimeout(3000);
                        connection.setRequestMethod("POST");
                        connection.setDoInput(true);
                        connection.setDoOutput(true);

                        /// Mise en place des differents parametre necessaire ////

                        Uri.Builder builder = new Uri.Builder()
                                .appendQueryParameter("OBJET", "tousTerrain");

                        String query = builder.build().getEncodedQuery();

                        OutputStream os = connection.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(
                                new OutputStreamWriter(os, "UTF-8"));
                        writer.write(query);
                        writer.flush();
                        writer.close();
                        os.close();

                        connection.connect();


                       InputStream inputStream = connection.getInputStream();

                        // InputStreamOperations est une classe complémentaire:
                        //Elle contient une méthode InputStreamToString.

                        result = InputStreamOperations.InputStreamToString(inputStream);



                        try{





                        // On récupère le JSON complet
                        //JSONObject jsonObject = new JSONObject(result);
                        //System.out.println(jsonObject.toString());
                        // On récupère le tableau d'objets qui nous concernent
                            JSONArray array = new JSONArray(result);
                       //JSONArray array = new JSONArray(jsonObject.getString("terrain"));


                        LatLng coordInter;
                        // Pour tous les objets on récupère les infos
                        for (int j = 0; j < array.length(); j++) {

                            // On récupère un objet JSON du tableau
                            JSONObject obj = new JSONObject(array.getString(j));
                            SportField sports = new SportField();
                            // On fait le lien Terrain - Objet JSON

                            sports.setId(obj.getString("_id"));
                            sports.setName(obj.getString("name"));
                            sports.setAdress(obj.getString("adress"));
                            sports.setCity(obj.getString("city"));
                            coordInter = new LatLng(obj.getDouble("latitude"), obj.getDouble("longitude"));
                            sports.setCoord(coordInter);
                            // On ajoute la personne à la liste
                            myFields.addSports(sports);



                        }


                    } catch (JSONException e) {
                        Log.e("log_tag", "Error parsing data " + e.toString());

                    }
                    }
                    catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            return myFields;
        }

        protected void onPostExecute(SportFieldGroup th) {
            progressDialog.dismiss();
            SportField intermUserField;
            Toast.makeText(getBaseContext(), "The Job is done", Toast.LENGTH_LONG).show();
            Iterator<SportField> it = th.iterator();
            while (it.hasNext()) {

                intermUserField = it.next();

                addMarker(mMap, intermUserField.getCoord().latitude, intermUserField.getCoord().longitude, intermUserField.getName());

            }
        }


    }
}
