/*
KVM-based Discoverable Cloudlet (KD-Cloudlet) 
Copyright (c) 2015 Carnegie Mellon University.
All Rights Reserved.

THIS SOFTWARE IS PROVIDED "AS IS," WITH NO WARRANTIES WHATSOEVER. CARNEGIE MELLON UNIVERSITY EXPRESSLY DISCLAIMS TO THE FULLEST EXTENT PERMITTEDBY LAW ALL EXPRESS, IMPLIED, AND STATUTORY WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, AND NON-INFRINGEMENT OF PROPRIETARY RIGHTS.

Released under a modified BSD license, please see license.txt for full terms.
DM-0002138

KD-Cloudlet includes and/or makes use of the following Third-Party Software subject to their own licenses:
MiniMongo
Copyright (c) 2010-2014, Steve Lacy 
All rights reserved. Released under BSD license.
https://github.com/MiniMongo/minimongo/blob/master/LICENSE

Bootstrap
Copyright (c) 2011-2015 Twitter, Inc.
Released under the MIT License
https://github.com/twbs/bootstrap/blob/master/LICENSE

jQuery JavaScript Library v1.11.0
http://jquery.com/
Includes Sizzle.js
http://sizzlejs.com/
Copyright 2005, 2014 jQuery Foundation, Inc. and other contributors
Released under the MIT license
http://jquery.org/license
*/
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
        this.setPortNumber(Integer.valueOf(prefs.getString(portPrefString, "0")));
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
     * @param intent the intent to get the IP and port from.
     * @return true if the IP and port were loaded from the Intent, false otherwise.
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
