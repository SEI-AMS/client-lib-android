package edu.cmu.sei.ams.cloudlet.android;

import android.content.Context;
import edu.cmu.sei.ams.cloudlet.Service;
import edu.cmu.sei.ams.cloudlet.ServiceVM;

/**
 * User: jdroot
 * Date: 4/25/14
 * Time: 3:06 PM
 */
public class StartServiceAsyncTask extends CloudletAsyncTask<ServiceVM>
{
    private static final String TITLE = "Cloudlet";
    private static final String MESSAGE = "Searching for Cloudlets...";

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
        return mService.startService();
    }
}
