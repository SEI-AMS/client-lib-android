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

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import edu.cmu.sei.ams.cloudlet.android.utils.FileHandler;

/**
 * Assumes device can store information of only 1 cloudlet at one time.
 * Created by Sebastian on 2015-05-27.
 */
public class CredentialsManager {
    private static final String TAG = "CredentialsManager";

    public static final String CREDENTIALS_FOLDER_PATH = "/sdcard/cloudlet/credentials/";

    /**
     * Returns the ID for the device.
     * @return a String representing a unique id for the device.
     */
    public static String getDeviceId(Context context) {
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }

    /**
     * Stores an IBC related file.
     * @param fileContents
     */
    public static void storeFile(byte[] fileContents, String fileId) {
        Log.v(TAG, "File contents for file " + fileId + ": " + new String(fileContents));
        FileHandler.writeToFile(CREDENTIALS_FOLDER_PATH + fileId, fileContents);
    }

    public static String loadDataFromFile(String fileId) {
        byte[] data = FileHandler.readFromFile(CREDENTIALS_FOLDER_PATH + fileId);
        String stringData = new String(data);
        Log.v(TAG, "File contents from file " + fileId + ": " + stringData);
        return stringData;
    }
}
