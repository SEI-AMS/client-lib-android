package edu.cmu.sei.ams.cloudlet.android;

import android.content.Context;
import edu.cmu.sei.ams.cloudlet.App;
import edu.cmu.sei.ams.cloudlet.Cloudlet;
import edu.cmu.sei.ams.cloudlet.Service;

import java.util.List;

/**
 * User: jdroot
 * Date: 6/17/14
 * Time: 11:39 AM
 */
public class GetAppsAsyncTask extends CloudletAsyncTask<List<App>>
{
    private static final String TITLE = "Cloudlet";
    private static final String MESSAGE = "Loading app list...";

    private final Cloudlet mCloudlet;

    public GetAppsAsyncTask(Cloudlet cloudlet, CloudletCallback<List<App>> callback)
    {
        super(callback);
        this.mCloudlet = cloudlet;
    }

    public GetAppsAsyncTask(Context context, Cloudlet cloudlet, CloudletCallback<List<App>> callback)
    {
        super(context, callback, TITLE, MESSAGE);
        this.mCloudlet = cloudlet;
    }

    @Override
    protected List<App> doInBackground(Void... params)
    {
        try
        {
            return this.mCloudlet.getApps();
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
