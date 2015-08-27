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
import edu.cmu.sei.ams.cloudlet.CloudletException;

import java.io.File;
import java.util.List;

/**
 * User: jdroot
 * Date: 6/17/14
 * Time: 12:55 PM
 */
public class DownloadAppAsyncTask extends CloudletAsyncTask<File>
{
    private static final String TITLE = "Cloudlet";
    private static final String MESSAGE = "Downloading app...";

    private final App mApp;
    private final File mOutDir;

    public DownloadAppAsyncTask(Context context, App app, File outDir, CloudletCallback<File> callback)
    {
        super(context, callback, TITLE, MESSAGE);
        this.mApp = app;
        this.mOutDir = outDir;
    }

    @Override
    protected File doInBackground(Void... params)
    {
        try
        {
            return this.mApp.downloadApp(this.mOutDir);
        }
        catch (Exception e)
        {
            Log.e("DownloadAppAsyncTask", "Error downloading file", e);
            this.mException = e;
            return null;
        }
    }
}
