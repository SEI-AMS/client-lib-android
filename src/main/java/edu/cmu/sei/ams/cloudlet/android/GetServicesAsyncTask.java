package edu.cmu.sei.ams.cloudlet.android;

import android.content.Context;
import edu.cmu.sei.ams.cloudlet.Cloudlet;
import edu.cmu.sei.ams.cloudlet.Service;

import java.util.List;

/**
 * User: jdroot
 * Date: 6/17/14
 * Time: 11:29 AM
 */
public class GetServicesAsyncTask extends CloudletAsyncTask<List<Service>>
{
    private static final String TITLE = "Cloudlet";
    private static final String MESSAGE = "Loading services list...";

    private final Cloudlet mCloudlet;

    public GetServicesAsyncTask(Cloudlet cloudlet, CloudletCallback<List<Service>> callback)
    {
        super(callback);
        this.mCloudlet = cloudlet;
    }

    public GetServicesAsyncTask(Context context, Cloudlet cloudlet, CloudletCallback<List<Service>> callback)
    {
        super(context, callback, TITLE, MESSAGE);
        this.mCloudlet = cloudlet;
    }

    @Override
    protected List<Service> doInBackground(Void... params)
    {
        try
        {
            return this.mCloudlet.getServices();
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
