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

import java.util.HashMap;

import edu.cmu.sei.ams.cloudlet.DeviceMessageManager;
import edu.cmu.sei.ams.cloudlet.IDeviceMessageHandler;
import edu.cmu.sei.ams.cloudlet.Service;
import edu.cmu.sei.ams.cloudlet.android.security.messages.AddTrustedCloudletHandler;
import edu.cmu.sei.ams.cloudlet.android.security.messages.MoveToNewCloudletHandler;

/**
 * Created by Sebastian on 2016-05-31.
 */
public class MessagePollingThreadManager
{
    private Thread messageCheckerThread;
    private DeviceMessageManager messageManager;

    /**
     *
     */
    public void startMessagePollingThread(final Service service, final Context context)
    {
        final HashMap<String, IDeviceMessageHandler> handlers = new HashMap<>();
        handlers.put("add-trusted-cloudlet", new AddTrustedCloudletHandler(context));
        handlers.put("move-to-new-cloudlet-network", new MoveToNewCloudletHandler(context));

        if(service.getServiceVM() != null) {
            // Create a thread that will check for messages associated to this service and device.
            stopMessagePollingThread();
            messageCheckerThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (handlers.size() > 0) {
                        messageManager = new DeviceMessageManager();
                        for (String message : handlers.keySet()) {
                            messageManager.registerHandler(message, handlers.get(message));
                        }

                        messageManager.execute(service.getCloudlet(), service.getServiceId());
                    }
                }
            });
            messageCheckerThread.start();
        }
    }

    /**
     *
     */
    public void stopMessagePollingThread()
    {
        if(messageCheckerThread != null)
        {
            messageCheckerThread.interrupt();
            messageCheckerThread = null;
        }
    }
}
