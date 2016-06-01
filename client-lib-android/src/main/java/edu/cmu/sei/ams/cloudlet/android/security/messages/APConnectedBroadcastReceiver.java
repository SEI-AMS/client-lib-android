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
package edu.cmu.sei.ams.cloudlet.android.security.messages;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Created by Sebastian on 2016-05-26.
 */
public class APConnectedBroadcastReceiver extends BroadcastReceiver

{
    private static final String LOG_TAG = "APConnectedBR";

    private String _expectedSSID;
    private IMessagePollingThreadMover _threadMover;

    /**
     * Public setter to set the SSID we are waiting for.
     */
    public void setExpectedSSID(String expectedSSID)
    {
        _expectedSSID = expectedSSID;
    }

    /**
     * Public setter to set handler to be called.
     */
    public void setThreadMover(IMessagePollingThreadMover threadMover)
    {
        _threadMover = threadMover;
    }

    /**
     * Called when the intent is received.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION.equals(action)) {
            SupplicantState state = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
            if(SupplicantState.isValidState(state) && state == SupplicantState.COMPLETED) {
                // If we are here, it means we just finished connecting to an Access Point.

                // Check if we connected to the new AP we were expecting.
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiNetworkInfo = wifiManager.getConnectionInfo();
                if (wifiNetworkInfo == null) {
                    Log.e(LOG_TAG, "Associated to AP, but got no network info...");
                    return;
                }

                // We have information about the new network we connected to.
                String connectedSSID = wifiNetworkInfo.getSSID().replaceAll("^\"(.*)\"$", "$1");
                if(!connectedSSID.equals(_expectedSSID)) {
                    Log.w(LOG_TAG, "Did not connect to expected SSID " + _expectedSSID + ", connected to SSID " + connectedSSID + " instead.");
                    return;
                }

                // We connected to the expected SSID, tell thread to move to poll new cloudlet.
                Log.w(LOG_TAG, "Connected to SSID " + connectedSSID);
                try {
                    // Wait a few seconds just in case we have not obtained the DHCP info yet.
                    // TODO: This would need another broadcast receiver to be done properly.
                    int waitTimeInMS = 3 * 1000;
                    Thread.sleep(waitTimeInMS);

                    _threadMover.moveMessagePollingThreadToNewCloudlet();
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Error moving polling thread to new thread: " + e.toString());
                    e.printStackTrace();
                }
            }
        }
    }
}
