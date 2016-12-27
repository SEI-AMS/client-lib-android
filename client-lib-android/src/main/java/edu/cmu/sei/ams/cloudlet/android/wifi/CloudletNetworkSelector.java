package edu.cmu.sei.ams.cloudlet.android.wifi;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.List;

/**
 * Created by Sebastian on 2015-12-24.
 */
public class CloudletNetworkSelector
{
    private static final String LOG_TAG = "CloudletNetworkSelector";

    /**
     * Checks if the current network is a valid cloudlet network.
     * @param context the Android context.
     * @return True if it is valid, false if not or if it is not connected.
     */
    public static boolean isConnectedToValidNetwork(Context context)
    {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo connection = wifiManager.getConnectionInfo();
        if(connection != null)
        {
            if(CloudletNetwork.isValidNetwork(connection.getSSID().replaceAll("\"", "")))
            {
                Log.i(LOG_TAG, "Already connected to a valid network.");
                return true;
            }
            else
                return false;
        }
        else
            return false;
    }

    /**
     * Attempts to connect to any valid cloudlet network, it not connected.
     * @param context the Android context.
     * @return true if we could connect or were connected to a valid network.
     */
    public static boolean connectToRandomValidNetwork(Context context)
    {
        if(isConnectedToValidNetwork(context))
        {
            // If we are already connected to a valid network, do nothing.
            return true;
        }
        else
        {
            // Find the valid networks.
            CloudletNetworkFinder finder = new CloudletNetworkFinder(context);
            List<CloudletNetwork> networks = finder.findNetworks();

            // Connect to first one in list.
            if (networks.size() > 0)
            {
                return networks.get(0).connect(context);
            }
        }

        return false;
    }
}
