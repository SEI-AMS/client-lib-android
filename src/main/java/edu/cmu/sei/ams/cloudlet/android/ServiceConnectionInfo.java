package edu.cmu.sei.ams.cloudlet.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by Sebastian on 2015-03-19.
 */
public class ServiceConnectionInfo
{
    private static final String LOG_TAG = "ServiceConnectionInfo";

    private String ipAddress;
    private int portNumber;

    public static final String INTENT_EXTRA_APP_SERVER_IP_ADDRESS = "edu.cmu.sei.cloudlet.appServerIp";
    public static final String INTENT_EXTRA_APP_SERVER_PORT ="edu.cmu.sei.cloudlet.appServerPort";

    /**
     * Loads from shared preferences.
     */
    public void loadFromPreferences(Context context, String ipPrefString, String portPrefString)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.setIpAddress(prefs.getString(ipPrefString, "127.0.0.1"));
        this.setPortNumber(prefs.getInt(portPrefString, 0));
    }

    /**
     * Stores into shared preferences.
     */
    public void storeIntoPreferences(Context context, String ipPrefString, String portPrefString)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(ipPrefString, this.getIpAddress());
        prefsEditor.putInt(portPrefString, this.getPortNumber());
        prefsEditor.commit();
    }

    /**
     * Loads IP and port from the given Intent, if any.
     * @param intent
     * @return
     */
    public boolean loadFromIntent(Intent intent)
    {
        // Check if we have a valid intent.
        if(intent == null)
        {
            Log.v(LOG_TAG, "No info loaded since Intent was invalid.");
            return false;
        }

        // Check if the intent has information.
        Bundle extras = intent.getExtras();
        if(extras == null)
        {
            Log.v(LOG_TAG, "No info loaded since Intent contained no extras.");
            return false;
        }

        // Get the values from the intent.
        String serverIPAddress = extras.getString(INTENT_EXTRA_APP_SERVER_IP_ADDRESS);
        int serverPort = extras.getInt(INTENT_EXTRA_APP_SERVER_PORT);

        // Check if the intent has valid information.
        boolean validExtras = (serverIPAddress != null) && (serverPort != 0);
        if (!validExtras)
        {
            Log.v(LOG_TAG, "No info loaded since Intent contained invalid values for IP address or port.");
            return false;
        }

        Log.v(LOG_TAG, "Old IP and port:");
        Log.v(LOG_TAG, "IP:   " + this.getIpAddress());
        Log.v(LOG_TAG, "Port: " + this.getPortNumber());

        // Load the data internally.
        this.setIpAddress(serverIPAddress);
        this.setPortNumber(serverPort);

        Log.v(LOG_TAG, "Stored new IP and port.");
        Log.v(LOG_TAG, "IP:   " + serverIPAddress);
        Log.v(LOG_TAG, "Port: " + serverPort);

        // Remove the extras so that they won't be loaded again if we jump back to this activity.
        intent.removeExtra(INTENT_EXTRA_APP_SERVER_IP_ADDRESS);
        intent.removeExtra(INTENT_EXTRA_APP_SERVER_PORT);

        return true;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }
}
