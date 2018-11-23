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

    private Networking networking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LOGGER = Logger.getLogger("CTF");
        INSTANCE = this;
        setContentView(R.layout.activity_main);
        this.networking = new Networking("10.0.2.2");
        this.networking.start();

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
            case R.id.blue_team_player_1: this.networking.sendData("disable blue:1"); break;
            case R.id.blue_team_player_2: this.networking.sendData("disable blue:2"); break;
            case R.id.blue_team_player_3: this.networking.sendData("disable blue:3"); break;
            case R.id.blue_team_player_4: this.networking.sendData("disable blue:4"); break;
            case R.id.red_team_player_1: this.networking.sendData("disable red:1"); break;
            case R.id.red_team_player_2: this.networking.sendData("disable red:2"); break;
            case R.id.red_team_player_3: this.networking.sendData("disable red:3"); break;
            case R.id.red_team_player_4: this.networking.sendData("disable red:4"); break;
        }
    }

    @Override
    protected void onDestroy() {
        this.networking.close();
        super.onDestroy();
    }
}
