package com.MusicON;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientActivity extends Activity implements Runnable {

    public static final int DISCOVERY_PORT = 49155;
    public static final int VOTING_PORT = 50032;
    public static final Logger logger = Logger.getLogger(ClientActivity.class.getName());
    private List<String> songs;
    private InetAddress serverAddress;

    public ClientActivity(List<String> songs, InetAddress serverAddress) {
        this.songs = new ArrayList<>(songs);
        this.serverAddress = serverAddress;
    }

    public ClientActivity() {
    }



    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        // retrieve data and save in file
        String data = getIntent().getStringExtra("USER_PREF");
        String fileName = "musicon_user.txt";

        try {
            FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(data.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MainActivity.registered = true;

        Toast.makeText(getApplicationContext(),
                "File written. \nData: " + data + "\n", Toast.LENGTH_LONG).show();

        List<String> s = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            s.add("Generic pop song #" + i);
        }

//        Toast.makeText(getApplicationContext(),
//                s.get(1).toString(), Toast.LENGTH_LONG).show();

        List<Thread> threads = new ArrayList<>();
       InetAddress server = doDiscovery();
//        for (int i = 0; i < 10; i++) {
//            List<String> songs = new ArrayList<>();
//            songs.add(s.get(i));
//            songs.add(s.get(i + 1));
//            songs.add(s.get(i + 2));
//            threads.add(new Thread(new ClientActivity(songs, server)));
//        }
//
//
//        List<String> lol = new ArrayList<>();
//        logger.log(Level.INFO, "{0} and {1} should win.", new Object[]{s.get(5), s.get(6)});
//        lol.add(s.get(5));
//        lol.add(s.get(6));
//        threads.add(new Thread(new ClientActivity(lol, server)));
//        waitForVoting();
//
//        Toast.makeText(getApplicationContext(),
//                "Waiting for vote.\n", Toast.LENGTH_LONG).show();
//
//        for (Thread t : threads) {
//            t.start();
//        }

        setContentView(R.layout.datasaved);
    }

    public void onStartg(){
        // retrieve data and save in file
//        String data = getIntent().getStringExtra("USER_PREF");
//        String fileName = "musicon_user.txt";
//
//        try {
//            FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
//            fos.write(data.getBytes());
//            fos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        Context context = getApplicationContext();
        CharSequence text = "onStart has been Called";
        int length= Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, length);
        toast.show();

    }
    public static InetAddress doDiscovery() {
        InetAddress res = null;
        //Server sends empty packet, address can be read off from the DatagramPacket properties
        try {
            DatagramPacket packet = receiveEmptyBroadcast(DISCOVERY_PORT);
            res = packet.getAddress();
        } catch (SocketException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } //Real thing needs proper error handling of course
        return res;
    }

    /*
     * Listens for the voting broadcast
     */
    public static void waitForVoting() {
        try {
            receiveEmptyBroadcast(VOTING_PORT); //Get address from here if you're not using discovery
        } catch (SocketException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    public static DatagramPacket receiveEmptyBroadcast(int port) throws SocketException, IOException {
        DatagramSocket socket = new DatagramSocket(null);
        InetSocketAddress address = new InetSocketAddress("0.0.0.0", port);
        socket.bind(address);
        byte[] serverAck = new byte[0];
        DatagramPacket serverAckPacket = new DatagramPacket(serverAck, serverAck.length);
        socket.receive(serverAckPacket);
        socket.close();
        return serverAckPacket;
    }


    /*
     * Pack the song name into a datagrampacket and send it to the server
     */
    public void throwVotes(InetAddress serverAddress) {

        Socket socket = null;
        try {
            socket = new Socket(serverAddress, VOTING_PORT);
        } catch (IOException e) {
            Logger.getLogger(ClientActivity.class.getName()).log(Level.SEVERE, null, e);
        }

        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            for (String s : songs) {
                writer.write(s);
                writer.newLine();
            }
        } catch (IOException e) {
            Logger.getLogger(ClientActivity.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket();
            throwVotes(serverAddress);
        } catch (SocketException e) {
            logger.log(Level.SEVERE, null, e);
        }

        Toast.makeText(getApplicationContext(),
                "Threw vote.\n", Toast.LENGTH_LONG).show();
        logger.log(Level.INFO, "Threw vote");
    }

    public void returnHome(View view) {
        setContentView(R.layout.main);
    }

}
