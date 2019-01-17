package io.github.troblecodings.ctf_app;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener{

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

        for(View view : getAll()){
            view.setEnabled(false);
            view.setOnClickListener(MainActivity.INSTANCE);
            view.setOnTouchListener(MainActivity.INSTANCE);
        }
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
            case R.id.end_blue: networking.sendData("match_end blue_win"); break;
            case R.id.end_red: networking.sendData("match_end red_win"); break;
        }
    }

    private void sendTo(int id, String str){
        switch (id){
            case R.id.blue_team_player_1: networking.sendData(str + "blue:1"); break;
            case R.id.blue_team_player_2: networking.sendData(str + "blue:2"); break;
            case R.id.blue_team_player_3: networking.sendData(str + "blue:3"); break;
            case R.id.blue_team_player_4: networking.sendData(str + "blue:4"); break;
            case R.id.red_team_player_1: networking.sendData(str + "red:1"); break;
            case R.id.red_team_player_2: networking.sendData(str + "red:2"); break;
            case R.id.red_team_player_3: networking.sendData(str + "red:3"); break;
            case R.id.red_team_player_4: networking.sendData(str + "red:4"); break;
        }
    }

    private ArrayList<View> views_list;

    public List<View> getAll(){
        if(views_list == null) {
            views_list = new ArrayList<>();
            views_list.add(findViewById(R.id.blue_team_player_1));
            views_list.add(findViewById(R.id.blue_team_player_2));
            views_list.add(findViewById(R.id.blue_team_player_3));
            views_list.add(findViewById(R.id.blue_team_player_4));
            views_list.add(findViewById(R.id.red_team_player_1));
            views_list.add(findViewById(R.id.red_team_player_2));
            views_list.add(findViewById(R.id.red_team_player_3));
            views_list.add(findViewById(R.id.red_team_player_4));
            views_list.add(findViewById(R.id.end_blue));
            views_list.add(findViewById(R.id.end_red));
        }
        return views_list;
    }

    @Override
    protected void onDestroy() {
        networking.close();
        super.onDestroy();
    }

    private float x = -1;

    @Override
    public boolean onTouch(final View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (getAll().contains(v)) {
                    x = event.getH;
                } else {
                    x = -1;
                }
                LOGGER.info("Drag started " + x);
                break;
            case MotionEvent.ACTION_BUTTON_RELEASE:
                LOGGER.info("Drag ended " + event.getX() + " " + x);
                if (getAll().contains(v) && x > event.getX() - 10) {
                    AlertDialog.Builder bld = new AlertDialog.Builder(this);
                    bld.setMessage("Report foul?");
                    bld.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sendTo(v.getId(), "ban ");
                        }
                    });
                    bld.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    bld.show();
                }
        }
        return true;
    }
}
