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

import java.util.ArrayList;
import java.util.List;

import edu.cmu.sei.ams.cloudlet.Cloudlet;
import edu.cmu.sei.ams.cloudlet.CloudletFinder;
import edu.cmu.sei.ams.cloudlet.android.security.AndroidCredentialsManager;
import edu.cmu.sei.ams.cloudlet.android.wifi.CloudletNetworkSelector;

/**
 * User: jdroot
 * Date: 4/25/14
 * Time: 10:31 AM
 */
public class FindCloudletsAsyncTask extends CloudletAsyncTask<List<Cloudlet>>
{
    private static final String TITLE = "Cloudlet";
    private static final String MESSAGE = "Searching for Cloudlets...";

    public FindCloudletsAsyncTask(Context context, CloudletCallback<List<Cloudlet>> callback)
    {
        super(context, callback, TITLE, MESSAGE);
    }

    @Override
    protected List<Cloudlet> doInBackground(Void... params)
    {
        try
        {
            // First ensure we are in a valid cloudlet network.
            boolean success = CloudletNetworkSelector.connectToRandomValidNetwork(mContext);
            if(!success)
            {
                Log.e("FindCloudletsAsyncTask", "No recognized cloudlet networks available, will attempt to use current network anyway. ");
            }

            Log.i("FindCloudletsAsyncTask", "Finding cloudlets");
            CloudletFinder finder = new CloudletFinder(DeviceIdManager.getDeviceId(this.mContext), new AndroidCredentialsManager());
            return finder.findCloudlets();
        }
        catch(Exception e)
        {
            Log.e("FindCloudletsAsyncTask", "Error finding cloudlets: ", e);
            this.mException = e;
            return new ArrayList<Cloudlet>();
        }
    }
}
