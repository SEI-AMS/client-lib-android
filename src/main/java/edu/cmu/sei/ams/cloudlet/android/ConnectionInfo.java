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
public class ConnectionInfo
{
    private Context mContext;
    private String mIpPrefString;
    private String mPortPrefString;
    private String mLogTag;

    public String ipAddress;
    public int portNumber;

    public static final String INTENT_EXTRA_APP_SERVER_IP_ADDRESS = "edu.cmu.sei.cloudlet.appServerIp";
    public static final String INTENT_EXTRA_APP_SERVER_PORT ="edu.cmu.sei.cloudlet.appServerPort";

    /**
     *
     * @param context
     * @param ipPrefString
     * @param portPrefString
     * @param logTag
     */
    public ConnectionInfo(Context context, String ipPrefString, String portPrefString, String logTag)
    {
        this.mContext = context;
        this.mIpPrefString = ipPrefString;
        this.mPortPrefString = portPrefString;
        this.mLogTag = logTag;
    }

    /**
     *
     */
    public void loadFromPreferences()
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        this.ipAddress = prefs.getString(this.mIpPrefString, "127.0.0.1");
        this.portNumber = Integer.parseInt(this.mPortPrefString, 0);
    }

    /**
     *
     * @param ipAddress
     * @param port
     * @return
     */
    public boolean storeIntoPreferences(String ipAddress, int port)
    {
        // Only store the IP and port in the preferences file if they are valid.
        boolean validExtras = ipAddress != null && port != 0;
        if(validExtras)
        {
            Log.v(mLogTag, "IP:   " + ipAddress);
            Log.v(mLogTag, "Port: " + port);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
            SharedPreferences.Editor prefsEditor = prefs.edit();
            prefsEditor.putString(this.mIpPrefString, ipAddress);
            prefsEditor.putString(this.mPortPrefString, String.valueOf(port));
            prefsEditor.commit();
            Log.w(this.mLogTag, "Server IP address and Port stored.");

            // Ensure that we have the values we just stored loaded in our instance as well.
            this.ipAddress = ipAddress;
            this.portNumber = port;
            Log.w(this.mLogTag, "Server IP address and Port loaded into instance.");

            return true;
        }
        else
        {
            Log.w(this.mLogTag,
                    "Server IP address or Port from are invalid, and will be ignored.");
            return false;
        }
    }

    /**
     *
     * @param intent
     * @return
     */
    private boolean storeConnInfoFromIntent(Intent intent)
    {
        Log.v(this.mLogTag, "storeInfoFromIntent");
        if(intent == null)
            return false;

        Bundle extras = intent.getExtras();

        if(extras != null)
        {
            // Get the values from the intent.
            String serverIPAddress = extras.getString(INTENT_EXTRA_APP_SERVER_IP_ADDRESS);
            int cloudletPort = extras.getInt(INTENT_EXTRA_APP_SERVER_PORT);

            // Store the info into the preferences.
            boolean stored = this.storeIntoPreferences(serverIPAddress, cloudletPort);
            if(stored)
            {
                // Remove the extras so that they won't be used again each time we jump back to this activity.
                intent.removeExtra(INTENT_EXTRA_APP_SERVER_IP_ADDRESS);
                intent.removeExtra(INTENT_EXTRA_APP_SERVER_PORT);
            }

            return stored;
        }
        else
        {
            Log.w(this.mLogTag,
                    "Server IP address and Port not received from Intent. Values from preferences will not be changed.");
            return false;
        }
    }
}
