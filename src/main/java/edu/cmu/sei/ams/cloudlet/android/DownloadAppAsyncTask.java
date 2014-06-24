package edu.cmu.sei.ams.cloudlet.android;

import android.content.Context;
import edu.cmu.sei.ams.cloudlet.App;

import java.io.File;

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

    public DownloadAppAsyncTask(App app, File outDir, CloudletCallback<File> callback)
    {
        super(callback);
        this.mApp = app;
        this.mOutDir = outDir;
    }

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
            return null;
        }
    }
}
