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
