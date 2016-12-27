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

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.HandlerThread;
import android.util.Log;
import android.os.Handler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;

import edu.cmu.sei.ams.cloudlet.Cloudlet;
import edu.cmu.sei.ams.cloudlet.ICurrentCloudlerHolder;
import edu.cmu.sei.ams.cloudlet.IDeviceMessageHandler;
import edu.cmu.sei.ams.cloudlet.MessageException;
import edu.cmu.sei.ams.cloudlet.android.DeviceIdManager;
import edu.cmu.sei.ams.cloudlet.android.security.AndroidCredentialsManager;
import edu.cmu.sei.ams.cloudlet.impl.CloudletImpl;

/**
 * Created by Sebastian on 2016-05-27.
 */
public class MoveToNewCloudletHandler implements IDeviceMessageHandler, IMessagePollingThreadMover {
    private static final String LOG_TAG = "MoveToNewCloudlet";
    private static final String GENERIC_CLOUDLET_FQDN = "cloudlet.svm.cloudlet.local.";

    private Context context;
    private ICurrentCloudlerHolder _cloudletHolder;
    private CloudletDataBundle _cloudletData;
    private APConnectedBroadcastReceiver _broadcastReceiver;

    /**
     *
     */
    public MoveToNewCloudletHandler(Context context) {
        this.context = context;
    }

    /**
     * Stores an incoming paired data bundle, and creates the associated profile.
     */
    public void handleData(HashMap<String, String> data, ICurrentCloudlerHolder currentCloudlerHolder)
            throws MessageException {
        Log.v(LOG_TAG, "Params:" + data);

        CloudletDataBundle cloudletData = new CloudletDataBundle(data);

        // Set broadcast receiver to move thread.
        setThreadMoverBroadcastReceiver(cloudletData, currentCloudlerHolder);

        // Try to connect to the new AP.
        connectToExistingNetwork(cloudletData);
    }

    /**
     * Connects to the given existing network.
     */
    private void connectToExistingNetwork(CloudletDataBundle cloudletData)
    {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        boolean isFound = false;
        List<WifiConfiguration> existingNetworks = wifiManager.getConfiguredNetworks();
        for(WifiConfiguration currentConfig : existingNetworks)
        {
            String currentSSID = currentConfig.SSID.replaceAll("^\"(.*)\"$", "$1");
            if(currentSSID.equals(cloudletData.getCloudletSSID()))
            {
                Log.w(LOG_TAG, "Connecting to SSID " + cloudletData.getCloudletSSID());
                int networkId = currentConfig.networkId;
                wifiManager.enableNetwork(networkId, true);
                isFound = true;
            }
        }

        if(!isFound)
        {
            Log.w(LOG_TAG, "Existing network with SSID " + cloudletData.getCloudletSSID() + " was not found.");
        }
    }

    /**
     * Registers this object as a listener to a broadcast receiver, to move the thread when appropriate.
     */
    private void setThreadMoverBroadcastReceiver(CloudletDataBundle cloudletData, ICurrentCloudlerHolder currentCloudlerHolder) {
        _cloudletData = cloudletData;
        _cloudletHolder = currentCloudlerHolder;

        _broadcastReceiver = new APConnectedBroadcastReceiver();
        _broadcastReceiver.setExpectedSSID(cloudletData.getCloudletSSID());
        _broadcastReceiver.setThreadMover(this);

        // Create a thread to process the notification when we have connected to a network.
        HandlerThread handlerThread = new HandlerThread("APBroadcastReceiver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        context.registerReceiver(_broadcastReceiver, intentFilter, null, handler);
    }

    /**
     * Moves the thread polling for new messages to start doing it to a new cloudlet.
     */
    @Override
    public void moveMessagePollingThreadToNewCloudlet() throws MessageException {
        try {
            context.unregisterReceiver(_broadcastReceiver);
            Log.v(LOG_TAG, "Changing cloudlet to poll to: " + _cloudletData.getCloudletIP());

            // Convert the IP of the new cloudlet.
            InetAddress cloudletInetAddress = InetAddress.getByName(_cloudletData.getCloudletIP());

            AndroidCredentialsManager credentialsManager = new AndroidCredentialsManager();
            String deviceId = DeviceIdManager.getDeviceId(context);

            // Change the cloudlet the message thread is pinging to.
            Cloudlet newCloudlet = new CloudletImpl(_cloudletData.getCloudletName(),
                    cloudletInetAddress,
                    _cloudletData.getCloudletPort(),
                    _cloudletData.isCloudletEncryptionEnabled(),
                    deviceId,
                    credentialsManager);
            _cloudletHolder.setCurrentCloudlet(newCloudlet);
        } catch (UnknownHostException e) {
            throw new MessageException("Can't resolve cloudlet with IP: " + _cloudletData.getCloudletIP());
        } catch (IOException e) {
            throw new MessageException("Can't find password for cloudlet with: " + _cloudletData.getCloudletFqdn());
        }
    }
}
