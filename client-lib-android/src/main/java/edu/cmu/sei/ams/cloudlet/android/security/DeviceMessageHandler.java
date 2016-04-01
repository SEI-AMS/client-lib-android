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
import android.util.Log;

import java.io.FileNotFoundException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.List;

import edu.cmu.sei.ams.cloudlet.Cloudlet;
import edu.cmu.sei.ams.cloudlet.CloudletException;
import edu.cmu.sei.ams.cloudlet.DeviceMessage;

/**
 * Created by Sebastian on 2016-03-28.
 */
public class DeviceMessageHandler {

    private boolean stopped = false;

    public void stop(){
        stopped = true;
    }

    // TODO: improve error handling.
    public void execute(Context context, Cloudlet currentCloudlet) {
        while(!stopped) {
            try {
                List<DeviceMessage> messages = currentCloudlet.getMessages();

                for (DeviceMessage message: messages) {
                    String messageText = message.getMessage();
                    switch(messageText)
                    {
                        case "add-trusted-cloudlet":
                            HashMap<String, String> params = message.getParams();
                            Log.v("DevMesHandler", "Params:" + params);
                            PairedDataBundleHandler pairedDataHandler = new PairedDataBundleHandler();
                            try {
                                pairedDataHandler.handleData(context, params.get("cloudlet_name"),
                                        params.get("ssid"), params.get("auth_password"), params.get("server_radius_cert"));
                                Log.v("DeviceMessageHandler", "Wi-fi Profile generated");
                            } catch (CertificateException e) {
                                e.printStackTrace();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (RuntimeException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                }

                int pollTimeInMs = 10 * 1000;
                Thread.sleep(pollTimeInMs);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (CloudletException e) {
                e.printStackTrace();
            }
        }
    }
}
