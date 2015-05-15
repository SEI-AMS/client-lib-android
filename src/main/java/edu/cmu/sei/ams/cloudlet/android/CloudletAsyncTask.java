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

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

/**
 * User: jdroot
 * Date: 4/25/14
 * Time: 9:51 AM
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public abstract class CloudletAsyncTask<T> extends AsyncTask<Void, Void, T>
{
    private CloudletCallback<T> mCallback;
    private Context mContext;
    private String mTitle;
    private String mMessage;

    private ProgressDialog mProgressDialog = null;

    public CloudletAsyncTask(CloudletCallback<T> callback)
    {
        this.mCallback = callback;
    }

    public CloudletAsyncTask(Context context, CloudletCallback<T> callback, String title, String message)
    {
        this.mCallback = callback;
        this.mContext = context;
        this.mTitle = title;
        this.mMessage = message;
    }

    @Override
    protected void onPreExecute()
    {
        if (mProgressDialog != null)
        {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }

        if (mContext != null)
        {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setTitle(mTitle);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setMessage(mMessage);
            mProgressDialog.show();
        }
    }

    @Override
    protected void onPostExecute(T t)
    {
        if (mProgressDialog != null)
        {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }

        mCallback.handle(t);
    }
}
