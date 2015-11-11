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

import edu.cmu.sei.ams.cloudlet.App;
import edu.cmu.sei.ams.cloudlet.AppFilter;
import edu.cmu.sei.ams.cloudlet.AppFinder;
import edu.cmu.sei.ams.cloudlet.CloudletException;
import edu.cmu.sei.ams.cloudlet.CloudletFinder;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jdroot
 * Date: 9/12/14
 * Time: 2:30 PM
 */
public class AppFinderAsyncTask extends CloudletAsyncTask<List<App>>
{
    private static final String TITLE = "App Finder";
    private static final String MESSAGE = "Searching for apps...";

    private AppFilter filter;

    public AppFinderAsyncTask(Context context, CloudletCallback<List<App>> callback)
    {
        super(context, callback, TITLE, MESSAGE);
    }

    public AppFinderAsyncTask(Context context, AppFilter filter,
                              CloudletCallback<List<App>> callback)
    {
        super(context, callback, TITLE, MESSAGE);
        this.filter = filter;
    }

    @Override
    protected List<App> doInBackground(Void... params)
    {
        try
        {
            CloudletFinder finder = new CloudletFinder();
            finder.setEncryptionCredentials(CredentialsManager.getDeviceId(this.mContext), CredentialsManager.loadDataFromFile("password"));
            AppFinder appFinder = new AppFinder(finder);
            return appFinder.findApps(filter);
        }
        catch(Exception except)
        {
            this.mException = except;
            this.mException.printStackTrace();
            return new ArrayList<App>();
        }
    }
}
