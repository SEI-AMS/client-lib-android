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
import android.widget.Toast;

import edu.cmu.sei.ams.cloudlet.Cloudlet;
import edu.cmu.sei.ams.cloudlet.Service;
import edu.cmu.sei.ams.cloudlet.ServiceVM;
import edu.cmu.sei.ams.cloudlet.rank.CloudletRanker;
import edu.cmu.sei.ams.cloudlet.rank.CpuBasedRanker;

/**
 * User: jdroot
 * Date: 4/29/14
 * Time: 2:55 PM
 */
public class FindCloudletAndStartService
{
    private Context mContext;
    private String mServiceId;
    private CloudletRanker mRanker;
    private CloudletCallback<ServiceVM> mCallback;


    public FindCloudletAndStartService(Context context, String serviceId, CloudletRanker ranker, CloudletCallback<ServiceVM> callback)
    {
        this.mContext = context;
        this.mServiceId = serviceId;
        this.mRanker = ranker;
        this.mCallback = callback;
    }

    /**
     * Constructor that uses default CpuBasedRanker.
     */
    public FindCloudletAndStartService(Context context, String serviceId, CloudletCallback<ServiceVM> callback)
    {
        this.mContext = context;
        this.mServiceId = serviceId;
        this.mRanker = new CpuBasedRanker();
        this.mCallback = callback;
    }

    public void execute()
    {
        new FindCloudletByRankAsyncTask(mContext, mServiceId, mRanker, new CloudletCallback<Cloudlet>()
        {
            public void handle(Cloudlet result)
            {
                if (result == null)
                {
                    mCallback.handle(null);
                    return;
                }
                try
                {
                    Service service = result.getServiceById(mServiceId); //This *should* never fail
                    new StartServiceAsyncTask(service, mContext, mCallback).execute();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).execute();
    }
}
