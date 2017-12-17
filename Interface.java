package com.example.mo.shareyousport;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class Interface extends AppCompatActivity {
    private ImageView logo;
    private MyTimerTask taskService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interface);

        // Pas encore implémentée
        ImageView event = (ImageView) findViewById(R.id.event);
        ImageView imageView5 = (ImageView) findViewById(R.id.imageView5);
        event.setVisibility(View.INVISIBLE);
        imageView5.setVisibility(View.INVISIBLE);

        ImageView img = (ImageView) findViewById(R.id.create);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Interface.this, Creation.class);
                startActivity(myIntent);
            }
        });


        ImageView parametreUtilisateur = (ImageView) findViewById(R.id.paraUtil);
        parametreUtilisateur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Interface.this, parametre_utilisateur.class);
                startActivity(myIntent);
            }
        });


        ImageView imgJoin = (ImageView) findViewById(R.id.join);
        imgJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Interface.this, MapsActivity.class);
                startActivity(myIntent);
            }
        });
    }

    class MyTimerTask extends TimerTask {
        public void run() {
            Intent myIntent = new Intent(Interface.this,EvenementService.class);
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_interface, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
