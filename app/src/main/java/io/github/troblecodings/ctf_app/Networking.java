package io.github.troblecodings.ctf_app;

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
    private String local_ip;

    public Networking(String local_ip) {
        this.local_ip = local_ip;
    }

    @Override
    public void run() {
        try {
            startNetworking(local_ip);
        } catch (Exception e) {
            MainActivity.LOGGER.info("Networking error!");
            MainActivity.LOGGER.info(e.toString());
            e.printStackTrace();
        }
    }

    private void startNetworking(String local_ip) throws Exception{
        MainActivity.LOGGER.info("Start networking");
        this.socket = new Socket(local_ip, 555);
        MainActivity.LOGGER.info("Initialized networking");
        this.writer = new PrintWriter( this.socket.getOutputStream(), true);
        this.scanner = new Scanner(this.socket.getInputStream());
        MainActivity.LOGGER.info("Initialized networking");
        MainActivity.LOGGER.info("Start message queue");
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

        private void processData(String command, String[] args) {

        }
    }

}
