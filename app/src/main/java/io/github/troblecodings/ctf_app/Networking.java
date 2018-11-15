package io.github.troblecodings.ctf_app;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class Networking extends AsyncTask<String, Void, Void> {

    private SSLSocket socket;
    private PrintWriter writer;
    private Scanner scanner;
    private ArrayList<String> msg_queue = new ArrayList<>();

    @Override
    protected Void doInBackground(String... local_ip) {
        try {
            startNetworking(local_ip[0]);
        } catch (Exception e) {
            MainActivity.LOGGER.log(Level.WARNING,"Networking error!");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    private void startNetworking(String local_ip) throws Exception{
        this.socket = (SSLSocket) SSLSocketFactory.getDefault().createSocket(local_ip, 555);
        this.writer = new PrintWriter(this.socket.getOutputStream());
        this.scanner = new Scanner(this.socket.getInputStream());
        while (true){
            if(msg_queue.size() > 0) {
                for (String msg : msg_queue) {
                    this.writer.println(msg);
                    MainActivity.LOGGER.info("Message: " + msg);
                    if (this.scanner.hasNextLine()) {
                        String rsp = this.scanner.nextLine();
                        MainActivity.LOGGER.info("Response: " + rsp);
                    }
                }
            }
        }
    }

    public void sendData(String str){
        msg_queue.add(str);
    }

    public void close() {
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
