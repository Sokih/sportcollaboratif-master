package com.example.mo.shareyousport;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import butterknife.ButterKnife;
import butterknife.Bind;

public class Signup extends AppCompatActivity {
    private static final String TAG = "Signup";

    @Bind(value = R.id.input_name)
     EditText _nameText;
    @Bind(value = R.id.input_email)
     EditText _emailText;
    @Bind(value = R.id.input_password)
     EditText _passwordText;
    @Bind(value = R.id.btn_signup)
    Button _signupButton;
    @Bind(value = R.id.link_login)
      TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");
        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
           return;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            return;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            return;
        } else {
            _passwordText.setError(null);
        }

        // Envoie de la requête http avec la method post à la base de données
        PostClass requeteHttp = new PostClass();
        requeteHttp.execute(name,email, password);

    }

    private class PostClass extends AsyncTask<String, Void, String> {

        final ProgressDialog progressDialog = new ProgressDialog(Signup.this, R.style.AppTheme_Dark_Dialog);

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
                        URL url = new URL("http://10.0.2.2:3000/signup");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setConnectTimeout(3000);
                        connection.setRequestMethod("POST");
                        connection.setDoInput(true);
                        connection.setDoOutput(true);

                        /// Mise en place des differents parametre necessaire ////

                        Uri.Builder builder = new Uri.Builder()
                                .appendQueryParameter("OBJET", "Signup")
                                .appendQueryParameter("PSEUDO", params[0])
                                .appendQueryParameter("EMAIL", params[1]) //
                                .appendQueryParameter("PASSWORD", params[2]); //idem
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

                            return e.toString();

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
                Toast.makeText(getBaseContext(), "Inscription effectuée, pensez à completer votre profil dans vos paramètres une fois connecté ;-)", Toast.LENGTH_LONG).show();
                Intent myIntent = new Intent(Signup.this, Login.class);
                startActivity(myIntent);
                finish();

            } else {
                Toast.makeText(getBaseContext(), "Erreur lors de l'inscription ", Toast.LENGTH_LONG).show();
            }



        }

    }




}
