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
package edu.cmu.sei.ams.cloudlet.android.security;

import android.content.Context;
import org.apache.commons.codec.binary.Base64;

import java.io.FileNotFoundException;
import java.security.cert.CertificateException;

import edu.cmu.sei.ams.cloudlet.android.AndroidCredentialsManager;
import edu.cmu.sei.ams.cloudlet.android.DeviceIdManager;

/**
 */
class PairedDataBundleHandler {

    private static final String RADIUS_CERT_NAME = "radius.pem";

    /**
     * Stores an incoming paired data bundle, and creates the associated profile.
     */
    public void handleData(Context context, String cloudletName, String networkId, String authPassword, String radiusServerCertData)
            throws CertificateException, FileNotFoundException {
        AndroidCredentialsManager credentialsManager = new AndroidCredentialsManager();

        if(networkId == null)
            throw new RuntimeException("Invalid network SSID.");
        if(authPassword == null)
            throw new RuntimeException("Invalid auth password.");
        if(radiusServerCertData == null)
            throw new RuntimeException("Invalid cert data.");

        // Store certificate.
        credentialsManager.storeFile(cloudletName, radiusServerCertData.getBytes(), PairedDataBundleHandler.RADIUS_CERT_NAME);
        String serverCertificatePath = credentialsManager.getFullPath(cloudletName, PairedDataBundleHandler.RADIUS_CERT_NAME);

        // Store other profile info in case we want to create the profile again.
        credentialsManager.storeFile(cloudletName, networkId.getBytes(), WifiProfileManager.SSID_FILE_NAME);
        credentialsManager.storeFile(cloudletName, authPassword.getBytes(), WifiProfileManager.AUTH_PASSWORD_FILE_NAME);
        credentialsManager.storeFile(cloudletName, serverCertificatePath.getBytes(), WifiProfileManager.SERVER_CERT_PATH_FILE_NAME);

        // Create profile.
        String deviceId = DeviceIdManager.getDeviceId(context);

        // TODO: uncomment this to actually test profile creation.
        //WifiProfileManager.setupWPA2WifiProfile(networkId, serverCertificatePath, deviceId, authPassword, context);
    }
}
