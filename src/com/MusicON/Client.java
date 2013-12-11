package com.MusicON;

import android.app.Activity;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 12/8/13
 * Time: 2:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class Client extends Activity {

    public static final int DISCOVERY_PORT = 49155;
    public static final int VOTING_PORT = 50032;
    public static final Logger logger = Logger.getLogger(ClientActivity.class.getName());

    private List<String> songs;
    private InetAddress serverAddress;

    public Client(List<String> songs, InetAddress serverAddress) {
        this.songs = new ArrayList<>(songs);
        this.serverAddress = serverAddress;
    }
}
