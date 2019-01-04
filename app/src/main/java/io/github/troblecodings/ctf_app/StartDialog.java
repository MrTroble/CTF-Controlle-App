package io.github.troblecodings.ctf_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class StartDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setCancelable(false);
        builder.setTitle("Setup admin");
        final View view = getActivity().getLayoutInflater().inflate(R.layout.start_diag_view, null);
        view.findViewById(R.id.apply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.networking = new Networking(((TextView)view.findViewById(R.id.ip)).getText().toString(),
                        Integer.valueOf(((TextView)view.findViewById(R.id.port_input)).getText().toString()),
                        ((TextView)view.findViewById(R.id.pw_input)).getText().toString(),
                        Integer.valueOf(((TextView)view.findViewById(R.id.matchid)).getText().toString()));
                MainActivity.networking.start();
                StartDialog.this.dismiss();
            }
        });
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        return dialog;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        MainActivity.LOGGER.info("Cancel");
        if(getFragmentManager().findFragmentByTag("start") != null){
            new StartDialog().show(getFragmentManager(), "start2");
        } else {
            new StartDialog().show(getFragmentManager(), "start");
        }
    }

}
