package com.example.mo.shareyousport;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class parametre_utilisateur extends AppCompatActivity {
    private SharedPreferences sharedpreferences;
    private String id_utilisateur;
    private PostClass requeteHttp = new PostClass();
    private PostClass requeteHttp_modifie ;
    private final String RECUPERER = "recupererdonnees";
    private final String UPDATE = "mettreajour";
    private EditText pseudo, nom, prenom, email, tel, adresse, ville;
    private Button date_de_naissance;
    private ImageView back, valider;
    private CheckBox sexe;
    private MyTimerTask taskService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametre_utilisateur);
        sharedpreferences = getSharedPreferences("id_utilisateur", Context.MODE_PRIVATE);
        id_utilisateur = sharedpreferences.getString("id", "");


        requeteHttp.execute(id_utilisateur, RECUPERER);


        ////////////////////  Choix de la date de naissance ///////////////////////////////////

        date_de_naissance = (Button) findViewById(R.id.naissance_p);
        date_de_naissance.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //To show current date in the datepicker
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(parametre_utilisateur.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                        selectedmonth = selectedmonth + 1;

                        if (selectedday < 10 && selectedmonth < 10) {
                            date_de_naissance.setText("0" + selectedday + "/0" + selectedmonth + "/" + selectedyear);
                        } else if (selectedday < 10 && selectedmonth >= 10) {
                            date_de_naissance.setText("0" + selectedday + "/" + selectedmonth + "/" + selectedyear);
                        } else if (selectedday >= 10 && selectedmonth < 10) {
                            date_de_naissance.setText("" + selectedday + "/0" + selectedmonth + "/" + selectedyear);
                        } else if (selectedday >= 10 && selectedmonth >= 10) {
                            date_de_naissance.setText("" + selectedday + "/" + selectedmonth + "/" + selectedyear);
                        }
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();
            }
        });

        //////////////////// Fin Choix de la date de naissance ///////////////////////////////////

        back = (ImageView) findViewById(R.id.back_p);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(parametre_utilisateur.this, Interface.class);
                startActivity(myIntent);
                finish();
            }
        });


        valider = (ImageView) findViewById(R.id.valider_p);
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nom = ((EditText) findViewById(R.id.lastname_p)).getText().toString();

                String prenom = ((EditText) findViewById(R.id.firstname_p)).getText().toString();

                String pseudo = ((EditText) findViewById(R.id.pseudo_p)).getText().toString();

                String adresse = ((EditText) findViewById(R.id.adresse_p)).getText().toString();

                String ville = ((EditText) findViewById(R.id.ville_p)).getText().toString();

                String password = ((EditText) findViewById(R.id.password_p)).getText().toString();

                String password_confirm = ((EditText) findViewById(R.id.password2_p)).getText().toString();
                Boolean password_modifie = false; // savoir si le mot de pass a été modifié ou pas

                if (!password.equals("") && !password_confirm.equals("")) {
                    if (!password.equals(password_confirm)) {
                        ((EditText) findViewById(R.id.password2_p)).setError("Les mots de passes ne sont pas identiques");
                        return;
                    } else if (password.equals(password_confirm)) {
                        if (password.length() <= 4) {
                            ((EditText) findViewById(R.id.password2_p)).setError("Le mot de passe est trop court");
                            return;
                        }
                        password_modifie = true;
                    }
                }

                String date_de_naissance = ((Button) findViewById(R.id.naissance_p)).getText().toString();

                String email = ((EditText) findViewById(R.id.email_p)).getText().toString();

                String tel = ((EditText) findViewById(R.id.tel_p)).getText().toString();

                String sexe = "";
                boolean val=((CheckBox) findViewById(R.id.female_p)).isChecked();
                boolean va=((CheckBox) findViewById(R.id.male_p)).isChecked();

                if (((CheckBox) findViewById(R.id.male_p)).isChecked()) {
                    sexe = "homme";
                } else if (((CheckBox) findViewById(R.id.female_p)).isChecked()) {
                    sexe = "femme";
                }else {
                    sexe = "aucun";
                }

                if (val==true && va==true) {
                    ((CheckBox) findViewById(R.id.female_p)).setError("Un seul choix possible");
                    return;
                }


                System.out.println("\n\n\n\n Valeur de val et va " +val + " "+ va+"\n\n\n\n");
                requeteHttp_modifie = new PostClass();

                if (password_modifie) {
                    requeteHttp_modifie.execute(id_utilisateur, UPDATE, nom, prenom, pseudo, adresse, ville, password_confirm, date_de_naissance, email, tel, sexe,"true");

                } else {
                    requeteHttp_modifie.execute(id_utilisateur, UPDATE, nom, prenom, pseudo, adresse, ville,password_confirm, date_de_naissance, email, tel, sexe,"false");
                }

            }
        });
    }

    class MyTimerTask extends TimerTask {
        public void run() {
            Intent myIntent = new Intent(parametre_utilisateur.this,EvenementService.class);
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

    private class PostClass extends AsyncTask<String, Void, HashMap> {


        @Override//Cette méthode s'execute en deuxième
        protected HashMap doInBackground(String... params) {

            ConnectivityManager check = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] info = check.getAllNetworkInfo();
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    String result;

                    try {

                        /////////////////////////////// REQUETE HTTP /////////////////////
                        URL url = new URL("http://10.0.2.2:3000/updateuser");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setConnectTimeout(3000);
                        connection.setRequestMethod("POST");
                        connection.setDoInput(true);
                        connection.setDoOutput(true);

                        /// Mise en place des differents parametre necessaire ////
                        if (params[1].equals(RECUPERER)) {

                            Uri.Builder builder = new Uri.Builder()
                                    .appendQueryParameter("OBJET", RECUPERER)
                                    .appendQueryParameter("ID", params[0]);
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
                            char[] buffer = new char[1024];
                            reader.read(buffer);  /// On recupere ce que nous a envoyés le fichier php
                            result = new String(buffer);
                            reader.close();


                            //////////////////////JSON////////////////////////////////////
                            try {

                                JSONObject jObject = new JSONObject(result);

                                HashMap parametresUtilisateur = new HashMap();
                                parametresUtilisateur.put("OBJET", RECUPERER);
                                parametresUtilisateur.put("nom", jObject.getString("name"));
                                parametresUtilisateur.put("prenom", jObject.getString("firstname"));
                                parametresUtilisateur.put("pseudo", jObject.getString("pseudo"));
                                parametresUtilisateur.put("adresse", jObject.getString("address"));
                                parametresUtilisateur.put("ville", jObject.getString("city"));
                                parametresUtilisateur.put("date_de_naissance", jObject.getString("born"));
                                parametresUtilisateur.put("email", jObject.getString("mail"));
                                parametresUtilisateur.put("tel", jObject.getString("tel"));
                                parametresUtilisateur.put("sexe", jObject.getString("gender"));
                                connection.disconnect();

                                return parametresUtilisateur; // On retourne true ou false


                            } catch (JSONException e) {
                                Log.e("log_tag", "Error parsing data " + e.toString());

                            }

                            ///////////////// Code permettant de vérifier la connexion avecle server////////////////
                  /*      if (connection.getResponseCode() == 200) {
                            return   String.valueOf(connection.getResponseCode()) + " "+ connection.getResponseMessage();
                        }

                        return    String.valueOf(connection.getResponseCode()) + " "+ connection.getResponseMessage();
                    */

                        } else if (params[1].equals(UPDATE)) {

                            Uri.Builder builder = new Uri.Builder()
                                    .appendQueryParameter("OBJET", UPDATE)
                                    .appendQueryParameter("ID", params[0])
                                    .appendQueryParameter("NOM", params[2])
                                    .appendQueryParameter("PRENOM", params[3])
                                    .appendQueryParameter("PSEUDO", params[4])
                                    .appendQueryParameter("ADRESSE", params[5])
                                    .appendQueryParameter("VILLE", params[6])
                                    .appendQueryParameter("PASSWORD", params[7])
                                    .appendQueryParameter("DATE_DE_NAISSANCE", params[8])
                                    .appendQueryParameter("EMAIL", params[9])
                                    .appendQueryParameter("TEL", params[10])
                                    .appendQueryParameter("SEXE", params[11])
                                    .appendQueryParameter("MDP_MODIFIE", params[12]);

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
                            char[] buffer = new char[1024];
                            reader.read(buffer);  /// On recupere ce que nous a envoyés le fichier php
                            result = new String(buffer);
                            reader.close();


                            //////////////////////JSON////////////////////////////////////
                            try {


                                JSONObject jObject = new JSONObject(result);


                                HashMap parametresUtilisateur = new HashMap();
                                parametresUtilisateur.put("OBJET", UPDATE);
                                parametresUtilisateur.put("value", jObject.getString("value"));



                                connection.disconnect();

                                return parametresUtilisateur; // On retourne true ou false


                            } catch (JSONException e) {
                                Log.e("log_tag", "Error parsing data " + e.toString());

                            }

                            ///////////////// Code permettant de vérifier la connexion avecle server////////////////
                  /*      if (connection.getResponseCode() == 200) {
                            return   String.valueOf(connection.getResponseCode()) + " "+ connection.getResponseMessage();
                        }

                        return    String.valueOf(connection.getResponseCode()) + " "+ connection.getResponseMessage();
                    */

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

        @Override // La troisème méthode qui s'execute en dernier
        // String th, est la valeur que nous a retournee doInBackground
        protected void onPostExecute(HashMap th) {

            if (th != null) {


                if (th.get("OBJET").equals(RECUPERER)) {

                    if (th.get("nom") != null || !((String) th.get("nom")).isEmpty()) {
                        nom = (EditText) findViewById(R.id.lastname_p);
                        if ((((String) th.get("nom")).equals("null")) == false) {
                            nom.setText((String) th.get("nom"));
                        }

                    }
                    if (!th.get("prenom").equals("") || th.get("prenom") != null) {
                        prenom = (EditText) findViewById(R.id.firstname_p);
                        if ((((String) th.get("prenom")).equals("null")) == false) {
                            prenom.setText((String) th.get("prenom"));
                        }

                    }
                    if (!th.get("pseudo").equals("") || th.get("pseudo") != null) {
                        pseudo = (EditText) findViewById(R.id.pseudo_p);
                        if ((((String) th.get("pseudo")).equals("null")) == false) {
                            pseudo.setText((String) th.get("pseudo"));
                        }

                    }
                    if (!th.get("adresse").equals("") || th.get("adresse") != null) {
                        adresse = (EditText) findViewById(R.id.adresse_p);
                        if ((((String) th.get("adresse")).equals("null")) == false) {
                            adresse.setText((String) th.get("adresse"));
                        }

                    }
                    if (!th.get("ville").equals("") || th.get("ville") != null) {
                        ville = (EditText) findViewById(R.id.ville_p);
                        if ((((String) th.get("ville")).equals("null")) == false) {
                            ville.setText((String) th.get("ville"));
                        }

                    }
                    if (!th.get("date_de_naissance").equals("") || th.get("date_de_naissance") != null) {
                        date_de_naissance = (Button) findViewById(R.id.naissance_p);
                        if ((((String) th.get("date_de_naissance")).equals("null")) == false) {
                            date_de_naissance.setText((String) th.get("date_de_naissance"));
                        }

                    }
                    if (!th.get("email").equals("") || th.get("email") != null) {
                        email = (EditText) findViewById(R.id.email_p);
                        if ((((String) th.get("email")).equals("null")) == false) {
                            email.setText((String) th.get("email"));
                        }

                    }
                    if (!th.get("tel").equals("") || th.get("tel") != null) {
                        tel = (EditText) findViewById(R.id.tel_p);

                        if ((((String) th.get("tel")).equals("null")) == false) {
                            tel.setText((String) th.get("tel"));
                        }
                    }
                    if (!th.get("sexe").equals("") || th.get("sexe") != null) {

                        if (th.get("sexe").equals("homme")) {
                            sexe = (CheckBox) findViewById(R.id.male_p);
                            sexe.setChecked(true);
                        } else if (th.get("sexe").equals("femme")) {
                            sexe = (CheckBox) findViewById(R.id.female_p);
                            sexe.setChecked(true);
                        }

                    }

                }
                else if(th.get("OBJET").equals(UPDATE))
                {
                    if(((String) th.get("value")).equals("true"))
                    {
                        Toast.makeText(getBaseContext(), "Mise à jour réussie !", Toast.LENGTH_LONG).show();
                        requeteHttp_modifie.cancel(true);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(getBaseContext(), "Echec de la mise à jour  !", Toast.LENGTH_LONG).show();
                    }
                    requeteHttp_modifie.cancel(true);
                }
            } else {
                Toast.makeText(getBaseContext(), "Erreur de connexion à vos données", Toast.LENGTH_LONG).show();
            }


            //


        }

    }

}
