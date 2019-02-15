package io.github.troblecodings.ctf_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class PlayerDialog extends DialogFragment implements View.OnClickListener{

    private String name;
    private int team;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final View view = getActivity().getLayoutInflater().inflate(R.layout.player_menu_layout, null);
        ((TextView)view.findViewById(R.id.player_name)).setText(name);
        TextView view1 = ((TextView)view.findViewById(R.id.player1));
        view1.setText(MainActivity.reserve_names[team][0]);
        view1.setOnClickListener(this);
        TextView view2 = ((TextView)view.findViewById(R.id.player2));
        view2.setText(MainActivity.reserve_names[team][1]);
        view2.setOnClickListener(this);
        view.findViewById(R.id.runner).setOnClickListener(this);
        builder.setView(view);

        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        name = args.getString("name");
        team = args.getInt("team");
    }

    private void send(String cmd, String name){
        switch (MainActivity.INSTANCE.last_view.getId()){
            case R.id.blue_team_player_1: MainActivity.networking.sendData(cmd + " blue:1:" + name); break;
            case R.id.blue_team_player_2: MainActivity.networking.sendData(cmd + " blue:2:" + name); break;
            case R.id.blue_team_player_3: MainActivity.networking.sendData(cmd + " blue:3:" + name); break;
            case R.id.blue_team_player_4: MainActivity.networking.sendData(cmd + " blue:4:" + name); break;
            case R.id.red_team_player_1: MainActivity.networking.sendData(cmd + " red:1:" + name); break;
            case R.id.red_team_player_2: MainActivity.networking.sendData(cmd + " red:2:" + name); break;
            case R.id.red_team_player_3: MainActivity.networking.sendData(cmd + " red:3:" + name); break;
            case R.id.red_team_player_4: MainActivity.networking.sendData(cmd + " red:4:" + name); break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.player1:
                send("change", ((TextView)v).getText().toString() + ":5:" + this.name);
                break;
            case R.id.player2:
                send("change", ((TextView)v).getText().toString() + ":6:" + this.name);
                break;
            case R.id.runner:
                if(this.name.contains(" (R)")){
                    send("unrunner", this.name);
                } else {
                    send("runner", this.name);
                }
                break;
        }
        this.dismiss();
    }
}
