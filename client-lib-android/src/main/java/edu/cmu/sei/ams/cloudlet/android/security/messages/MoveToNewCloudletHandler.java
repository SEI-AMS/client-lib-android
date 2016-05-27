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
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

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

        // Get cloudlet data, and set broadcast receiver to move thread.
        CloudletDataBundle cloudletData = new CloudletDataBundle(data);
        setThreadMoverBroadcastReceiver(cloudletData, currentCloudlerHolder);
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

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        context.registerReceiver(_broadcastReceiver, intentFilter);
    }

    /**
     * Moves the thread polling for new messags to start doing it to a new cloudlet.
     */
    @Override
    public void moveMessagePollingThreadToNewCloudlet() throws MessageException {
        try {
            context.unregisterReceiver(_broadcastReceiver);

            // Try to get the IP of the new cloudlet.
            // Cloudlet FQDN is useless here, since it is only resolvable from inside the cloudlet.
            // We will use the standard cloudlet domain understood by the DNS server.
            InetAddress cloudletInetAddress = InetAddress.getByName(GENERIC_CLOUDLET_FQDN);

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
            throw new MessageException("Can't resolve cloudlet with FQDN: " + _cloudletData.getCloudletFqdn());
        } catch (IOException e) {
            throw new MessageException("Can't find password for cloudlet with: " + _cloudletData.getCloudletFqdn());
        }
    }
}
