package io.github.troblecodings.ctf_app;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static Logger LOGGER;
    public static MainActivity  INSTANCE;

    public static StartDialog dialog;
    public static Networking networking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LOGGER = Logger.getLogger("CTF");
        INSTANCE = this;
        setContentView(R.layout.activity_main);

        dialog = new StartDialog();
        dialog.show(getSupportFragmentManager(), "start");

        for(View view : getAll()){
            view.setEnabled(false);
            view.setOnClickListener(MainActivity.INSTANCE);
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
            case R.id.pause: networking.sendData("match_pause "); break;
            default:
                foul(v.getId());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult res = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(res != null && res.getContents() != null){
            Fragment frag = getSupportFragmentManager().findFragmentByTag("start");
            if (frag == null) frag = getSupportFragmentManager().findFragmentByTag("start2");
            getSupportFragmentManager().beginTransaction().remove(frag).commit();
            String[] rs = res.getContents().split("\n");
            MainActivity.networking = new Networking(rs[0], Integer.valueOf(rs[1]), rs[2], Integer.valueOf(rs[3]));
            MainActivity.networking.start();

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

    private void foul(int id){
        switch (id){
            case R.id.blue_team_player_1_foul: networking.sendData("foul blue:1:" + ((TextView)findViewById(R.id.blue_team_player_1)).getText()); break;
            case R.id.blue_team_player_2_foul: networking.sendData("foul blue:2:" + ((TextView)findViewById(R.id.blue_team_player_2)).getText()); break;
            case R.id.blue_team_player_3_foul: networking.sendData("foul blue:3:" + ((TextView)findViewById(R.id.blue_team_player_3)).getText()); break;
            case R.id.blue_team_player_4_foul: networking.sendData("foul blue:4:" + ((TextView)findViewById(R.id.blue_team_player_4)).getText()); break;
            case R.id.red_team_player_1_foul: networking.sendData("foul red:1:" + ((TextView)findViewById(R.id.red_team_player_1)).getText()); break;
            case R.id.red_team_player_2_foul: networking.sendData("foul red:2:" + ((TextView)findViewById(R.id.red_team_player_2)).getText()); break;
            case R.id.red_team_player_3_foul: networking.sendData("foul red:3:" + ((TextView)findViewById(R.id.red_team_player_3)).getText()); break;
            case R.id.red_team_player_4_foul: networking.sendData("foul red:4:" + ((TextView)findViewById(R.id.red_team_player_4)).getText()); break;
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
            views_list.add(findViewById(R.id.blue_team_player_1_foul));
            views_list.add(findViewById(R.id.blue_team_player_2_foul));
            views_list.add(findViewById(R.id.blue_team_player_3_foul));
            views_list.add(findViewById(R.id.blue_team_player_4_foul));
            views_list.add(findViewById(R.id.red_team_player_1_foul));
            views_list.add(findViewById(R.id.red_team_player_2_foul));
            views_list.add(findViewById(R.id.red_team_player_3_foul));
            views_list.add(findViewById(R.id.red_team_player_4_foul));
            views_list.add(findViewById(R.id.pause));
        }
        return views_list;
    }

    @Override
    protected void onDestroy() {
        networking.close();
        super.onDestroy();
    }

}
