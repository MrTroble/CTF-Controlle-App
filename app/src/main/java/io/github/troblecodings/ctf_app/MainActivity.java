package io.github.troblecodings.ctf_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static Logger LOGGER;
    public static MainActivity  INSTANCE;

    public static Networking networking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LOGGER = Logger.getLogger("CTF");
        INSTANCE = this;
        setContentView(R.layout.activity_main);

        StartDialog dialog = new StartDialog();
        dialog.show(getSupportFragmentManager(), "start");

        // This is looking bad but there is probably no better way
        findViewById(R.id.blue_team_player_1).setOnClickListener(this);
        findViewById(R.id.blue_team_player_2).setOnClickListener(this);
        findViewById(R.id.blue_team_player_3).setOnClickListener(this);
        findViewById(R.id.blue_team_player_4).setOnClickListener(this);
        findViewById(R.id.red_team_player_1).setOnClickListener(this);
        findViewById(R.id.red_team_player_2).setOnClickListener(this);
        findViewById(R.id.red_team_player_3).setOnClickListener(this);
        findViewById(R.id.red_team_player_4).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // This is looking bad but there is probably no better way
        switch (v.getId()){
            case R.id.blue_team_player_1: networking.sendData("disable blue:1"); break;
            case R.id.blue_team_player_2: networking.sendData("disable blue:2"); break;
            case R.id.blue_team_player_3: networking.sendData("disable blue:3"); break;
            case R.id.blue_team_player_4: networking.sendData("disable blue:4"); break;
            case R.id.red_team_player_1: networking.sendData("disable red:1"); break;
            case R.id.red_team_player_2: networking.sendData("disable red:2"); break;
            case R.id.red_team_player_3: networking.sendData("disable red:3"); break;
            case R.id.red_team_player_4: networking.sendData("disable red:4"); break;
        }
    }

    @Override
    protected void onDestroy() {
        networking.close();
        super.onDestroy();
    }
}
