package io.github.troblecodings.ctf_app;

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
    private String local_ip, pw;
    private int port;

    public Networking(String local_ip, int port, String pw) {
        this.local_ip = local_ip;
        this.port = port;
        this.pw = pw;
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
        this.msg_queue.add(str);
    }

    public void close() {
        this.close_request = true;
    }

    private class Reader extends Thread{

        private PrintWriter writer;
        private Scanner scanner;

        public Reader(PrintWriter writer, Scanner scanner) {
            this.writer = writer;
            this.scanner = scanner;
        }

        @Override
        public void run() {
            MainActivity.LOGGER.info("Start server command listener!");
            while(scanner.hasNextLine()){
                String input = scanner.nextLine();
                MainActivity.LOGGER.info(socket + " send data " + input);
                String command = input.split(" ")[0];
                String arg = input.replaceFirst(command + " ", "");
                processData(command, arg.split(":"));
            }
            MainActivity.LOGGER.info("No further input! Networking error?");
        }

        private void processData(String command, final String[] args) {
            if(command.equals("lock")){
                MainActivity.INSTANCE.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        get(args).setEnabled(false);
                    }
                });
            }
            else if(command.equals("unlock")) {
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
