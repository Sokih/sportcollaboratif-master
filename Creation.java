package com.example.mo.shareyousport;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;

public class Creation extends AppCompatActivity {

    private MyTimerTask taskService;
    private Button choixDusport,heuredebut,heurefin,dateevenement;
    private ImageView _send;//boutton de validation du formulaire

    private String textPosition="" ;// Nom du lieu ou le sport sera pratique
    //Latitude et Longitude
    private double latField=0 ;
    private double longField=0 ;
    private String cityField = null;
    private  String adressField = null;

     @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent thisIntent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation);

        _send= (ImageView) findViewById(R.id.send);
        _send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                creerEvenement(textPosition,latField,longField);
            }
        });

        ////////////////////  Boutton liste des sports ///////////////////////////////////
        choixDusport= (Button) findViewById(R.id.Liste_sport);

        choixDusport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAlertDialogWithListview();

            }
        });
        ////////////////////  Fin Boutton liste des sports ///////////////////////////////////

        ////////////////////  Choix du nombre de personnes ///////////////////////////////////

        ImageView augmenter = (ImageView) findViewById(R.id.plus);
        ImageView diminuer = (ImageView) findViewById(R.id.minus);
        final EditText nombrepersonne = (EditText) findViewById(R.id.nombrepersonne);

        augmenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String nombre= nombrepersonne.getText().toString();
                int nbr=Integer.parseInt(nombre);
                nbr++;
                nombre=Integer.toString(nbr);
                nombrepersonne.setText(nombre);
            }
        });

        diminuer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nombre= nombrepersonne.getText().toString();
                int nbr=Integer.parseInt(nombre);
                if(nbr>0) {
                    nbr--;
                }
                nombre=Integer.toString(nbr);
                nombrepersonne.setText(nombre);
            }
        });

        ////////////////////  fin Choix du nombre de personnes ///////////////////////////////////


        ////////////////////  Géolocation ///////////////////////////////////
         textPosition = thisIntent.getStringExtra("donnée");

        //Latitude et Longitude
         latField = thisIntent.getDoubleExtra("latitude",0);
         longField = thisIntent.getDoubleExtra("longitude",0);
         cityField = thisIntent.getStringExtra("ville");
         adressField = thisIntent.getStringExtra("adresse");


        EditText txt = (EditText) findViewById(R.id.editText);

        if(textPosition != null){
            txt.setText(textPosition);
        }

        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Creation.this, MapsActivityCreation.class);
                startActivity(myIntent);
            }
        });
        //////////////////// Fin Géolocation ///////////////////////////////////



        //////////////////// Choix de la date et de l'heure  ///////////////////////////////////
        heuredebut=(Button) findViewById(R.id.heuredebut);
        heurefin=(Button) findViewById(R.id.heurefin);
        dateevenement=(Button) findViewById(R.id.dateevenement);

        heuredebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(Creation.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if(selectedHour<10 && selectedMinute<10)
                        {
                            heuredebut.setText("0"+selectedHour + "h0" + selectedMinute);
                        }
                        else if(selectedHour<10 && selectedMinute>=10)
                        {
                            heuredebut.setText("0"+selectedHour + "h" + selectedMinute);
                        }

                        else if(selectedHour>=10 && selectedMinute<10)
                        {
                            heuredebut.setText(selectedHour + "h" + selectedMinute);
                        }
                        else if(selectedHour>=10 && selectedMinute>=10)
                        {
                            heuredebut.setText(selectedHour + "h" + selectedMinute);
                        }

                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        heurefin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(Creation.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if(selectedHour<10 && selectedMinute<10)
                        {
                            heurefin.setText("0"+selectedHour + "h0" + selectedMinute);
                        }
                        else if(selectedHour<10 && selectedMinute>=10)
                        {
                            heurefin.setText("0"+selectedHour + "h" + selectedMinute);
                        }

                        else if(selectedHour>=10 && selectedMinute<10)
                        {
                            heurefin.setText(selectedHour + "h" + selectedMinute);
                        }
                        else if(selectedHour>=10 && selectedMinute>=10)
                        {
                            heurefin.setText(selectedHour + "h" + selectedMinute);
                        }

                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        dateevenement.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //To show current date in the datepicker
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(Creation.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                        selectedmonth = selectedmonth + 1;

                        if(selectedday<10 && selectedmonth<10)
                        {
                            dateevenement.setText("0" + selectedday + "/0" + selectedmonth + "/" + selectedyear);
                        }
                        else if(selectedday<10 && selectedmonth>=10)
                        {
                            dateevenement.setText("0" + selectedday + "/" + selectedmonth + "/" + selectedyear);
                        }

                        else if(selectedday>=10 && selectedmonth<10)
                        {
                            dateevenement.setText("" + selectedday + "/0" + selectedmonth + "/" + selectedyear);
                        }
                        else if(selectedday>=10 && selectedmonth>=10)
                        {
                            dateevenement.setText("" + selectedday + "/" + selectedmonth + "/" + selectedyear);
                        }
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();
            }
        });

        //////////////////// Fin Choix de la date et de l'heure ///////////////////////////////////



        ImageView img = (ImageView) findViewById(R.id.back);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Creation.this, Interface.class);
                startActivity(myIntent);
            }
        });

    }

    class MyTimerTask extends TimerTask {
        public void run() {
            Intent myIntent = new Intent(Creation.this,EvenementService.class);
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


    public void creerEvenement(String _textPosition, Double _latField, Double _longField)
    {

        String sport= ((Button) findViewById(R.id.Liste_sport)).getText().toString();
        String lieuPratique= "";
        lieuPratique= _textPosition;
        String latitude=Double.toString(_latField);
        String longitude=Double.toString(_longField);
        String date=((Button) findViewById(R.id.dateevenement)).getText().toString();
        String heuredebut=((Button) findViewById(R.id.heuredebut)).getText().toString();
        String heurefin= ((Button) findViewById(R.id.heurefin)).getText().toString();
        String nbrpersonne=((EditText) findViewById(R.id.nombrepersonne)).getText().toString();;
        String checkbox;
       if(((CheckBox) findViewById(R.id.checkBox)).isEnabled())
       {
           checkbox="true";
       }
        else
       {
           checkbox="false";
       }



        if (sport.isEmpty() || sport.equals("Choix du sport")) {
            ((Button) findViewById(R.id.Liste_sport)).setError("Choisissez un sport");
            return;
        } else {
            ((Button) findViewById(R.id.Liste_sport)).setError(null);
        }


        if (lieuPratique==null) {
            ((EditText) findViewById(R.id.editText)).setError("Choisissez un lieu");
            return;
        }
        else
        {
            if (lieuPratique.isEmpty() || lieuPratique.equals("Votre lieu de pratique")) {
                ((EditText) findViewById(R.id.editText)).setError("Choisissez un lieu");
                return;
            } else {
                ((EditText) findViewById(R.id.editText)).setError(null);
            }
        }



        if (date.isEmpty() || date.equals("Date de l'évènement")) {
            ((Button) findViewById(R.id.dateevenement)).setError("Choisissez ");
            return;
        } else {
            ((Button) findViewById(R.id.dateevenement)).setError(null);
        }

        if (heuredebut.isEmpty() || heuredebut.equals("Heure de debut")) {
            ((Button) findViewById(R.id.heuredebut)).setError("Choisissez l'heure du début ");
            return;
        } else {
            ((Button) findViewById(R.id.heuredebut)).setError(null);
        }

        if (heurefin.isEmpty() || heurefin.equals("Heure de fin")) {
            ((Button) findViewById(R.id.heurefin)).setError("Choisissez l'heure du début ");
            return;
        } else {
            ((Button) findViewById(R.id.heurefin)).setError(null);
        }

        if (nbrpersonne.isEmpty() || nbrpersonne.equals("0")) {
            ((EditText) findViewById(R.id.nombrepersonne)).setError("Combien de personne souhaitez-vous ? ");
            return;
        } else {
            ((EditText) findViewById(R.id.nombrepersonne)).setError(null);
        }



        // Envoie de la requête http avec la method post à la base de données
        PostClass requeteHttp = new PostClass();

        requeteHttp.execute(sport, lieuPratique, latitude, longitude, date, heuredebut, heurefin, nbrpersonne, checkbox,adressField,cityField);


    }
    public void ShowAlertDialogWithListview()
    {

        ArrayList<String> listeSport = new ArrayList<>();
        listeSport.add(getString(R.string.Football));
        listeSport.add(getString(R.string.Rugby));
        listeSport.add(getString(R.string.Basketball));
        listeSport.add(getString(R.string.Tennis));
        listeSport.add(getString(R.string.Badminton));
        listeSport.add(getString(R.string.Volley_ball));
        listeSport.add(getString(R.string.Handball));
        listeSport.add(getString(R.string.Course));
        listeSport.add(getString(R.string.Cyclisme));

        final CharSequence[] dialogSport = listeSport.toArray(new String[listeSport.size()]);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Sport");
        dialogBuilder.setItems(dialogSport, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                String selectedText = dialogSport[item].toString();  //Selected item in listview
                choixDusport.setText(selectedText);
            }
        });
        //Create alert dialog object via builder
        AlertDialog alertDialogObject = dialogBuilder.create();
        //Show the dialog
        alertDialogObject.show();
    }

    private class PostClass extends AsyncTask<String, Void, String> {

        final ProgressDialog progressDialog = new ProgressDialog(Creation.this, R.style.AppTheme_Dark_Dialog);

        @Override //Cette méthode s'execute en premier, elle ouvre une simple boite de dialogue
        protected void onPreExecute() {
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();
        }

        @Override//Cette méthode s'execute en deuxième
        protected String doInBackground(String... params) {

            ConnectivityManager check = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] info = check.getAllNetworkInfo();
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    String result;

                    try {
                        /////////////////////////////// REQUETE HTTP /////////////////////
                        URL url = new URL("http://10.0.2.2:3000/addevents");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setConnectTimeout(3000);
                        connection.setRequestMethod("POST");
                        connection.setDoInput(true);
                        connection.setDoOutput(true);

                        /// Mise en place des differents parametre necessaire ////

                        Uri.Builder builder = new Uri.Builder()
                                .appendQueryParameter("OBJET", "evenement")
                                .appendQueryParameter("SPORT", params[0])
                                .appendQueryParameter("LIEUPRATIQUE", params[1])
                                .appendQueryParameter("LATITUDE", params[2])
                                .appendQueryParameter("LONGITUDE", params[3])
                                .appendQueryParameter("DATE", params[4])
                                .appendQueryParameter("HEUREDEBUT", params[5])
                                .appendQueryParameter("HEUREFIN", params[6])
                                .appendQueryParameter("NBRPERSONNE", params[7])
                                .appendQueryParameter("CHECKBOX", params[8])
                                .appendQueryParameter("ADRESSE", params[9])
                                 .appendQueryParameter("VILLE", params[10]);
                        String query = builder.build().getEncodedQuery();

                        OutputStream os = connection.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(
                                new OutputStreamWriter(os, "UTF-8"));
                        writer.write(query);
                        writer.flush();
                        writer.close();
                        os.close();

                        connection.connect();

                        ///////////////////////////////BUFFERREADER/////////////////////

                        Reader reader =new InputStreamReader(connection.getInputStream(), "UTF-8");
                        char[] buffer = new char[50];
                        reader.read(buffer);  /// On recupere ce que nous a envoyés le fichier php
                        result = new String(buffer);
                        reader.close();


                        //////////////////////JSON////////////////////////////////////
                        try {

                            JSONObject object = new JSONObject(result);
                            connection.disconnect();
                            return object.getString("value"); // On retourne true ou false


                        } catch (JSONException e) {
                            Log.e("log_tag", "Error parsing data " + e.toString());

                        }

                        ///////////////// Code permettant de vérifier la connexion avecle server////////////////
                  /*      if (connection.getResponseCode() == 200) {
                            return   String.valueOf(connection.getResponseCode()) + " "+ connection.getResponseMessage();
                        }

                        return    String.valueOf(connection.getResponseCode()) + " "+ connection.getResponseMessage();
                    */
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            return "false";

        }

        @Override // La troisème méthode qui s'execute en dernier
        // String th, est la valeur que nous a retournee doInBackground
        protected void onPostExecute(String th) {
            progressDialog.dismiss();


            if (Boolean.parseBoolean(th)) {
                /*Intent myIntent = new Intent(Creation.this, Interface.class);
                startActivity(myIntent);*/
                finish();


            } else {
                Toast.makeText(getBaseContext(), "Erreur de connexion ", Toast.LENGTH_LONG).show();
            }



        }

    }



}
