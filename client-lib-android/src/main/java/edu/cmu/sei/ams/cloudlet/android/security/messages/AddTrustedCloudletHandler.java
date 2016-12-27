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
import android.util.Log;

import java.io.FileNotFoundException;
import java.security.cert.CertificateException;
import java.util.HashMap;

import edu.cmu.sei.ams.cloudlet.ICurrentCloudlerHolder;
import edu.cmu.sei.ams.cloudlet.MessageException;
import edu.cmu.sei.ams.cloudlet.android.DeviceIdManager;
import edu.cmu.sei.ams.cloudlet.IDeviceMessageHandler;
import edu.cmu.sei.ams.cloudlet.android.security.AndroidCredentialsManager;
import edu.cmu.sei.ams.cloudlet.android.security.WifiProfileManager;

/**
 */
public class AddTrustedCloudletHandler implements IDeviceMessageHandler {
    private static final String LOG_TAG = "AddTrustedCloudlet";
    private static final String RADIUS_CERT_NAME = "radius.pem";

    private Context context;

    /**
     *
     */
    public AddTrustedCloudletHandler(Context context) {
        this.context = context;
    }

    /**
     * Stores an incoming paired data bundle, and creates the associated profile.
     */
    public void handleData(HashMap<String, String> data, ICurrentCloudlerHolder currentCloudlerHolder)
            throws MessageException {
        Log.v(LOG_TAG, "Params:" + data);

        // Get credentials data.
        CredentialsDataBundle credentialsData = new CredentialsDataBundle(data);

        // Store credentials.
        storeCredentials(credentialsData);

        // Create profile.
        createWifiProfile(credentialsData);

        // Handle moving to new cloudlet network.
        // (This has to be done here and not in parallel as it needs the wifi profile to be ready first).
        MoveToNewCloudletHandler mover = new MoveToNewCloudletHandler(context);
        mover.handleData(data, currentCloudlerHolder);
    }

    /**
     * Stores the credentials in a local file, in a folder for this specific cloudlet.
     */
    private void storeCredentials(CredentialsDataBundle credentialsData) {
        AndroidCredentialsManager credentialsManager = new AndroidCredentialsManager();
        String cloudletName = credentialsData.getCloudletName();

        // Store certificate.
        credentialsManager.storeFile(cloudletName, credentialsData.getRadiusServeCertData().getBytes(), AddTrustedCloudletHandler.RADIUS_CERT_NAME);
        String serverCertificatePath = credentialsManager.getFullPath(cloudletName, AddTrustedCloudletHandler.RADIUS_CERT_NAME);

        // Store device private key.
        credentialsManager.storeFile(cloudletName, credentialsData.getDevicePrivateKey().getBytes(), AndroidCredentialsManager.PRIVATE_KEY_FILE_NAME);

        // Store other profile info in case we want to create the profile again.
        credentialsManager.storeFile(cloudletName, credentialsData.getCloudletSSID().getBytes(), WifiProfileManager.SSID_FILE_NAME);
        credentialsManager.storeFile(cloudletName, credentialsData.getAuthPassword().getBytes(), WifiProfileManager.AUTH_PASSWORD_FILE_NAME);
        credentialsManager.storeFile(cloudletName, serverCertificatePath.getBytes(), WifiProfileManager.SERVER_CERT_PATH_FILE_NAME);
    }

    /**
     * Creates a Wi-Fi profile for the AP of the given cloudlet.
     */
    private void createWifiProfile(CredentialsDataBundle credentialsData)
            throws MessageException {
        AndroidCredentialsManager credentialsManager = new AndroidCredentialsManager();
        String serverCertificatePath = credentialsManager.getFullPath(credentialsData.getCloudletName(), AddTrustedCloudletHandler.RADIUS_CERT_NAME);

        String deviceId = DeviceIdManager.getDeviceId(context);
        try {
            WifiProfileManager.setupWPA2WifiProfile(credentialsData.getCloudletSSID(), serverCertificatePath,
                                                    deviceId, credentialsData.getAuthPassword(), context);
            Log.v(LOG_TAG, "Wi-fi profile with SSID " + credentialsData.getCloudletSSID() + " generated");
        } catch (CertificateException e) {
            throw new MessageException(e);
        } catch (FileNotFoundException e) {
            throw new MessageException(e);
        }
    }
}
