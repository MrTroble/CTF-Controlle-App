package io.github.troblecodings.ctf_app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import java.net.Socket;

public class Networking extends Thread {

    private Socket socket;
    private PrintWriter writer;
    private Scanner scanner;
    private volatile ArrayList<String> msg_queue = new ArrayList<>();
    private volatile boolean close_request = false;
    private final String local_ip, pw;
    private final int port, matchid;

    public Networking(String local_ip, int port, String pw,int matchid) {
        this.local_ip = local_ip;
        this.port = port;
        this.pw = pw;
        this.matchid = matchid;
    }

    @Override
    public void run() {
        try {
            startNetworking();
        } catch (Exception e) {
            MainActivity.LOGGER.info("Networking error!");
            MainActivity.LOGGER.info(e.toString());
            e.printStackTrace();
        }
    }

    private void startNetworking() throws Exception{
        MainActivity.LOGGER.info("Start networking on");
        this.socket = new Socket(this.local_ip, this.port);
        MainActivity.LOGGER.info("Initialized networking");
        this.writer = new PrintWriter( this.socket.getOutputStream(), true);
        this.scanner = new Scanner(this.socket.getInputStream());
        MainActivity.LOGGER.info("Initialized networking");
        MainActivity.LOGGER.info("Start message queue");
        this.writer.println(this.pw);
        Reader reader = new Reader(this.writer, this.scanner);
        reader.start();
        while (true){
            if (msg_queue.size() > 0) {
                String[] cpy = msg_queue.toArray(new String[msg_queue.size()]);
                for(String str : cpy){
                    MainActivity.LOGGER.info("Trying to send message: " + str);
                    this.writer.println(str);
                    this.msg_queue.remove(str);
                }
            }
            if(close_request){
                break;
            }
        }
        this.socket.close();
    }

    public void sendData(String str){
        MainActivity.LOGGER.info("Queuing message: " + str);
        this.msg_queue.add(str + ":" + matchid);
    }

    public void close() {
        this.close_request = true;
        this.stop();
    }

    private class Reader extends Thread{

        private PrintWriter writer;
        private Scanner scanner;
        private boolean active = false;

        public Reader(PrintWriter writer, Scanner scanner) {
            this.writer = writer;
            this.scanner = scanner;
        }

        @Override
        public void run() {
            MainActivity.LOGGER.info("Start server command listener!");
            while(scanner.hasNextLine()){
                String input = scanner.nextLine();
                if(input.isEmpty())continue;
                MainActivity.LOGGER.info(socket + " send data " + input);
                String command = input.split(" ")[0];
                String arg = input.replaceFirst(command + " ", "");
                processData(command, arg.split(":"));
            }
            MainActivity.LOGGER.info("No further input! Networking error?");
            MainActivity.INSTANCE.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.INSTANCE);
                    builder.setMessage("No further input! \n This seams to be an error! \n Please report back to an admin!");
                    builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }});
                    builder.show();
                }
            });
        }

        private void processData(String command, final String[] args) {
            if(command.equals("motd")) {
                MainActivity.INSTANCE.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.INSTANCE);
                        builder.setMessage(args[0].replace("%n", "\n"));
                        builder.setTitle("MOTD");
                        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.show();
                    }
                });
                return;
            }
            if(Integer.valueOf(args[args.length - 1]) != matchid)return;
            if(command.equals("lock")){
                MainActivity.INSTANCE.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        get(args).setEnabled(false);
                    }
                });
            }
            else if(command.equals("unlock") && active) {
                MainActivity.INSTANCE.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        get(args).setEnabled(true);
                    }
                });
            } else if(command.equals("set_name")) {
                MainActivity.INSTANCE.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        get(args).setText(args[2]);
                    }
                });
            } else if(command.equals("match_start")){
                MainActivity.INSTANCE.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for(View view : MainActivity.INSTANCE.getAll()){
                            if(view instanceof TextView && ((TextView) view).getText().equals("BANNED")) continue;
                            view.setEnabled(true);
                        }
                    }
                });
                active = true;
            } else if(command.equals("match_end")) {
                active = false;
                MainActivity.INSTANCE.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for(View view : MainActivity.INSTANCE.getAll()){
                            view.setEnabled(false);
                        }
                        if(args[0].equals("time")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.INSTANCE);
                            builder.setMessage("Match has ended! Time has run out!");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {@Override public void onClick(DialogInterface dialog, int which) { }});
                            builder.create().show();
                        }
                        if(args[0].equals("blue_win")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.INSTANCE);
                            builder.setMessage("Match has ended! Blue team won!");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {@Override public void onClick(DialogInterface dialog, int which) { }});
                            builder.create().show();
                        }
                        if(args[0].equals("red_win")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.INSTANCE);
                            builder.setMessage("Match has ended! Red team won!");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {@Override public void onClick(DialogInterface dialog, int which) { }});
                            builder.create().show();
                        }
                    }});
            } else if(command.equals("match_pause")) {
                MainActivity.INSTANCE.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for(View v : MainActivity.INSTANCE.getAll()){
                            v.setEnabled(false);
                        }
                    }
                });
            } else if(command.equals("match_unpause")) {
                MainActivity.INSTANCE.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for(View v : MainActivity.INSTANCE.getAll()){
                            if(v instanceof TextView && ((TextView) v).getText().equals("BANNED")) continue;
                            v.setEnabled(true);
                        }
                    }
                });
            } else if(command.equals("ban")) {
                MainActivity.INSTANCE.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        get(args).setText("BANNED");
                        get(args).setEnabled(false);
                    }
                });
            }
        }

        private TextView get(String[] str){
            if(str[0].equals("red")){
                switch (Integer.valueOf(str[1])){
                    case 1: return MainActivity.INSTANCE.findViewById(R.id.red_team_player_1);
                    case 2: return MainActivity.INSTANCE.findViewById(R.id.red_team_player_2);
                    case 3: return MainActivity.INSTANCE.findViewById(R.id.red_team_player_3);
                    case 4: return MainActivity.INSTANCE.findViewById(R.id.red_team_player_4);
                }
            } else {
                switch (Integer.valueOf(str[1])){
                    case 1: return MainActivity.INSTANCE.findViewById(R.id.blue_team_player_1);
                    case 2: return MainActivity.INSTANCE.findViewById(R.id.blue_team_player_2);
                    case 3: return MainActivity.INSTANCE.findViewById(R.id.blue_team_player_3);
                    case 4: return MainActivity.INSTANCE.findViewById(R.id.blue_team_player_4);
                }
            }
            return null;
        }
    }

}
