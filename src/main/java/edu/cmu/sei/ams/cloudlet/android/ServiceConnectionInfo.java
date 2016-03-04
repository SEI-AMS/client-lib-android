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

import java.net.UnknownHostException;

/**
 * Created by Sebastian on 2015-03-19.
 */
public class ServiceConnectionInfo
{
    private static final String LOG_TAG = "ServiceConnectionInfo";

    private String domainName;
    private int portNumber;

    /**
     * Loads from shared preferences.
     */
    public void loadFromPreferences(Context context, String domainName, String portPrefString)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.setDomainName(prefs.getString(domainName, "127.0.0.1"));
        this.setPortNumber(Integer.valueOf(prefs.getString(portPrefString, "0")));
    }

    /**
     * Stores into shared preferences.
     */
    public void storeIntoPreferences(Context context, String domainName, String portPrefString)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(domainName, this.domainName);
        prefsEditor.putString(portPrefString, Integer.toString(this.getPortNumber()));
        prefsEditor.commit();
    }

    public String getIpAddress() {
        try {
            return java.net.InetAddress.getByName(domainName).getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }
}
