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

public class PlayerDialog extends DialogFragment {

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
        TextView view2 = ((TextView)view.findViewById(R.id.player2));
        view2.setText(MainActivity.reserve_names[team][1]);
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
}
