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

import edu.cmu.sei.ams.cloudlet.Cloudlet;
import edu.cmu.sei.ams.cloudlet.CloudletFinder;
import edu.cmu.sei.ams.cloudlet.rank.CloudletRanker;

/**
 * User: jdroot
 * Date: 4/25/14
 * Time: 2:23 PM
 */
public class FindCloudletByRankAsyncTask extends CloudletAsyncTask<Cloudlet>
{
    private static final String TITLE = "Cloudlet";
    private static final String MESSAGE = "Searching for Cloudlets...";

    private String mServiceId;
    private CloudletRanker mRanker;

    public FindCloudletByRankAsyncTask(Context context, String serviceId, CloudletRanker ranker,
                                       CloudletCallback<Cloudlet> callback)
    {
        super(context, callback, TITLE, MESSAGE);
        this.mServiceId = serviceId;
        this.mRanker = ranker;
    }

    @Override
    protected Cloudlet doInBackground(Void... params)
    {
        try
        {
            CloudletFinder finder = new CloudletFinder();
            boolean encryptionEnabled = CloudletPreferences.isEncryptionEnabled(this.mContext);
            if(encryptionEnabled)
                finder.enableEncryption(CredentialsManager.getDeviceId(this.mContext), CredentialsManager.loadDataFromFile("password"));
            return finder.findCloudletForService(mServiceId, mRanker);
        }
        catch(Exception e)
        {
            Log.e("FindCloudletByRankAsyncTask", "Error finding cloudlet: ", e);
            this.mException = e;
            return null;
        }
    }
}
