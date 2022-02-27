package com.quantumtech.vido.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import com.quantumtech.vido.activites.MainActivity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressReceiver extends BroadcastReceiver {

    private static final int DISCOVERY_PORT = 8080;
    Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
       mContext = context;
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(8080);
            socket.setBroadcast(true);
            String data = String.valueOf(getBroadcastAddress());
            DatagramPacket packet = new DatagramPacket(data.getBytes(), data.length(), getBroadcastAddress(), DISCOVERY_PORT);
            socket.receive(packet);
        } catch (IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    public InetAddress getBroadcastAddress() throws UnknownHostException {
        WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifi.getDhcpInfo();

        int broadcast = (dhcp.ipAddress & dhcp.netmask) | -dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++){
            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        }
        return InetAddress.getByAddress(quads);
    }

}