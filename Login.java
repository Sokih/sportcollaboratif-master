package com.example.mo.shareyousport;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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

public class Login extends AppCompatActivity {
    private static final String TAG = "Login";
    private static final int REQUEST_SIGNUP = 0;
    private String result_login = "false";
    private SharedPreferences sharedpreferences;

    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.btn_login)
    Button _loginButton;
    @Bind(R.id.link_signup)
    TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), Signup.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void login() {


        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

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
        requeteHttp.execute(email, password);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        _loginButton.setEnabled(true);
    }


    // Classe qui contient 3 méthodes pour pouvoir effectuer une requete http
    // Ces requêtes nécessitent d'être effectuer dans un  thread
    private class PostClass extends AsyncTask<String, Void, JSONObject> {

        final ProgressDialog progressDialog = new ProgressDialog(Login.this, R.style.AppTheme_Dark_Dialog);

        @Override //Cette méthode s'execute en premier, elle ouvre une simple boite de dialogue
        protected void onPreExecute() {
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();
        }

        @Override//Cette méthode s'execute en deuxième
        protected JSONObject doInBackground(String... params) {

            ConnectivityManager check = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] info = check.getAllNetworkInfo();
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    String result;

                    try {
                        /////////////////////////////// REQUETE HTTP /////////////////////
                        URL url = new URL("http://10.0.2.2:3000/login");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setConnectTimeout(3000);
                        connection.setRequestMethod("POST");
                        connection.setDoInput(true);
                        connection.setDoOutput(true);

                        /// Mise en place des differents parametre necessaire ////

                        Uri.Builder builder = new Uri.Builder()
                                .appendQueryParameter("OBJET", "login")
                                .appendQueryParameter("EMAIL", params[0]) // params[0] entree en parametre dans la method login (cf:  requeteHttp.execute(email, password);)
                                .appendQueryParameter("PASSWORD", params[1]); //idem
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
                        reader.read(buffer);  /// On recupere ce que nous a envoyés le fichier php
                        result = new String(buffer);
                        reader.close();


                        //////////////////////JSON////////////////////////////////////
                        try {

                            JSONObject object = new JSONObject(result);
                            connection.disconnect();
                            return object; // On retourne true ou false


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

            return null;

        }

        @Override // La troisème méthode qui s'execute en dernier
        // String th, est la valeur que nous a retournee doInBackground
        protected void onPostExecute(JSONObject th) {
            progressDialog.dismiss();

            if (th != null) {
                String thValue = "false";
                try {
                    thValue = th.getString("value");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (!thValue.equals("false")) {
                    sharedpreferences = getSharedPreferences("id_utilisateur", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();

                    try {
                        editor.putString("id", th.getString("value"));
                        //editor.putString("total", th.getString("total"));  INUTILISER
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    editor.commit();
                    Toast.makeText(getBaseContext(), "Connexion réussie", Toast.LENGTH_LONG).show();
                    Intent myIntent = new Intent(Login.this, Interface.class);
                    startActivity(myIntent);


                } else {
                    Toast.makeText(getBaseContext(), "Erreur de connexion ", Toast.LENGTH_LONG).show();
                }

            }
            else {
                Toast.makeText(getBaseContext(), "Erreur de connexion ", Toast.LENGTH_LONG).show();
            }

        }

    }


}
