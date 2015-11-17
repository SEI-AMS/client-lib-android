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

import android.util.Log;

import java.io.File;

import edu.cmu.sei.ams.cloudlet.ICredentialsManager;
import edu.cmu.sei.ams.cloudlet.android.utils.FileHandler;

/**
 * Created by Sebastian on 2015-11-13.
 */
public class AndroidCredentialsManager implements ICredentialsManager {
    private static final String TAG = "CredentialsManager";
    private static final String CREDENTIALS_FOLDER_PATH = "/sdcard/cloudlet/credentials/";
    private static final String ENCRYPTION_PASSWORD_FILE_NAME = "encryption_password.txt";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFullPath(String cloudletName, String fileName) {
        return CREDENTIALS_FOLDER_PATH + "/" + cloudletName + "/" + fileName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEncryptionPassword(String cloudletName) {
        return loadDataFromFile(cloudletName, ENCRYPTION_PASSWORD_FILE_NAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void storeFile(String cloudletName, byte[] fileContents, String fileId) {
        Log.d(TAG, "File contents for file " + fileId + ": " + new String(fileContents));
        FileHandler.writeToFile(getFullPath(cloudletName, fileId), fileContents);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String loadDataFromFile(String cloudletName, String fileId) {
        byte[] data = FileHandler.readFromFile(getFullPath(cloudletName, fileId));
        String stringData = "";
        if(data == null)
            Log.e(TAG, "File not found or empty! " + fileId);
        else
            stringData = new String(data);
        Log.d(TAG, "File contents from file " + fileId + ": " + stringData);
        return stringData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean clearCredentials() {
        File rootFolder = new File(CREDENTIALS_FOLDER_PATH);
        return FileHandler.deleteDir(rootFolder);
    }
}
