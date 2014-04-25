package edu.cmu.sei.ams.cloudlet.android;

import android.content.Context;
import edu.cmu.sei.ams.cloudlet.Cloudlet;
import edu.cmu.sei.ams.cloudlet.CloudletFinder;

import java.util.List;

/**
 * User: jdroot
 * Date: 4/25/14
 * Time: 10:31 AM
 */
public class FindCloudletsAsyncTask extends CloudletAsyncTask<List<Cloudlet>>
{
    private static final String TITLE = "Cloudlet";
    private static final String MESSAGE = "Searching for Cloudlets...";

    public FindCloudletsAsyncTask(CloudletCallback<List<Cloudlet>> callback)
    {
        super(callback);
    }

    public FindCloudletsAsyncTask(Context context, CloudletCallback<List<Cloudlet>> callback)
    {
        super(callback, context, TITLE, MESSAGE);
    }

    @Override
    protected List<Cloudlet> doInBackground(Void... params)
    {
        return CloudletFinder.findCloudlets();
    }
}
