package edu.cmu.sei.ams.cloudlet.android;

import android.content.Context;
import edu.cmu.sei.ams.cloudlet.App;
import edu.cmu.sei.ams.cloudlet.AppFilter;
import edu.cmu.sei.ams.cloudlet.AppFinder;

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

    public AppFinderAsyncTask(CloudletCallback<List<App>> callback)
    {
        super(callback);
    }

    public AppFinderAsyncTask(AppFilter filter, CloudletCallback<List<App>> callback)
    {
        super(callback);
        this.filter = filter;
    }

    public AppFinderAsyncTask(Context context, CloudletCallback<List<App>> callback)
    {
        super(context, callback, TITLE, MESSAGE);
    }

    public AppFinderAsyncTask(Context context, AppFilter filter, CloudletCallback<List<App>> callback)
    {
        super(context, callback, TITLE, MESSAGE);
        this.filter = filter;
    }

    @Override
    protected List<App> doInBackground(Void... params)
    {
        return AppFinder.findApps(filter);
    }
}
