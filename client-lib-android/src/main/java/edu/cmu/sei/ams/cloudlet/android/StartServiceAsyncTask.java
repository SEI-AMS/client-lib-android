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
import android.util.Log;

import java.util.HashMap;

import edu.cmu.sei.ams.cloudlet.IDeviceMessageHandler;
import edu.cmu.sei.ams.cloudlet.Service;
import edu.cmu.sei.ams.cloudlet.ServiceVM;
import edu.cmu.sei.ams.cloudlet.android.security.messages.MoveToNewCloudletHandler;
import edu.cmu.sei.ams.cloudlet.android.security.messages.AddTrustedCloudletHandler;

/**
 * User: jdroot
 * Date: 4/25/14
 * Time: 3:06 PM
 */
public class StartServiceAsyncTask extends CloudletAsyncTask<ServiceVM>
{
    private static final String TITLE = "Cloudlet";
    private static final String MESSAGE = "Starting service";

    private Service mService;

    public StartServiceAsyncTask(Service service, CloudletCallback<ServiceVM> callback)
    {
        super(callback);
        this.mService = service;
    }

    public StartServiceAsyncTask(Service service, Context context, CloudletCallback<ServiceVM> callback)
    {
        super(context, callback, TITLE, MESSAGE);
        this.mService = service;
    }

    @Override
    protected ServiceVM doInBackground(Void... params)
    {
        try
        {
            HashMap<String, IDeviceMessageHandler> handlers = new HashMap<>();
            handlers.put("add-trusted-cloudlet", new AddTrustedCloudletHandler(mContext));
            handlers.put("move-to-new-cloudlet-network", new MoveToNewCloudletHandler(mContext));
            ServiceVM serviceVM = mService.startService(handlers);
            return serviceVM;
        }
        catch(Exception e)
        {
            Log.e("StartServiceAsyncTask", "Error getting starting service: ", e);
            this.mException = e;
            return null;
        }
    }
}
