package com.example.mo.shareyousport;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
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
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class EvenementService extends IntentService {
    private SharedPreferences sharedpreferences;
    private Object context=this;


    public EvenementService() {
        super("EvenementService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

    execute();

    }
    private void execute()
    {
        PostClass totalRequete=new PostClass();
        totalRequete.execute("");
    }

    private class PostClass extends AsyncTask<String, Void, String> {

        @Override//Cette méthode s'execute en deuxième
        protected String doInBackground(String... params) {

            ConnectivityManager check = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] info = check.getAllNetworkInfo();
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    String result;

                    try {
                        /////////////////////////////// REQUETE HTTP /////////////////////
                        //URL url = new URL("http://humanapp.assos.efrei.fr/shareyoursport/script/shareyoursportcontroller.php");
                        URL url = new URL("http://10.0.2.2:3000/services");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setConnectTimeout(3000);
                        connection.setRequestMethod("POST");
                        connection.setDoInput(true);
                        connection.setDoOutput(true);

                        /// Mise en place des differents parametre necessaire ////

                        Uri.Builder builder = new Uri.Builder()
                                .appendQueryParameter("OBJET", "total");
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

                        Reader reader = new InputStreamReader(connection.getInputStream(), "UTF-8");
                        char[] buffer = new char[50];
                        System.out.println("\n\n\n\n\n\n\n\n\n\n\n CHAAAAR : "+buffer +"\n\n\n\n\n ");
                        reader.read(buffer);  /// On recupere ce que nous a envoyés le fichier php
                        result = new String(buffer);
                        reader.close();


                        //////////////////////JSON////////////////////////////////////
                        try {
                            System.out.println("\n\n\n\n\n\n\n\n\n\n\n TEEESSSSTTTT \n\n\n\n\n ");

                            JSONObject object = new JSONObject(result);
                            System.out.println("\n\n\n\n\n\n\n\n\n\n\n TEEESSSSTTTT  2222222 : "+object.getString("total")+"\n\n\n\n\n ");
                            connection.disconnect();
                            return object.getString("total"); // On retourne true ou false


                        } catch (JSONException e) {
                            Log.e("log_tag", "Error parsing data " + e.toString());

                        }


                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            return null;

        }


        protected void onPostExecute(String th) {

            if (!th.equals("false")) {

                String total="";
                sharedpreferences = getSharedPreferences("id_utilisateur", Context.MODE_PRIVATE);
                total = sharedpreferences.getString("total", "");

                int ancienTotal=0;
                if(!total.equals(""))
                {
                    ancienTotal=Integer.parseInt(total);
                }

                int  nouveauTotal=Integer.parseInt(th);

                if(nouveauTotal>ancienTotal)
                {

                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder((Context)context)
                                    .setSmallIcon(R.drawable.common_plus_signin_btn_icon_dark)
                                    .setContentTitle("ShareYourSport")
                                    .setContentText("Un nouvel évènement a été crée " );

                    NotificationManager mNotificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    int truc = NotificationID.getID();
                    mBuilder.setAutoCancel(true);
                    mNotificationManager.notify(truc, mBuilder.build());

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("total",th);
                    editor.commit();
                }
                else if(nouveauTotal<ancienTotal)
                {

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("total",th);
                    editor.commit();

                }


            }

        }

    }


}
