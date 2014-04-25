package edu.cmu.sei.ams.cloudlet.android;

import android.content.Context;
import edu.cmu.sei.ams.cloudlet.Cloudlet;
import edu.cmu.sei.ams.cloudlet.CloudletFinder;
import edu.cmu.sei.ams.cloudlet.rank.CloudletRanker;

/**
 * User: jdroot
 * Date: 4/25/14
 * Time: 2:23 PM
 */
public class FindCloudletByRankAsyncTask extends CloudletAsyncTask<Cloudlet>
{
    private static final String TITLE = "Cloudlet";
    private static final String MESSAGE = "Searching for Cloudlets...";

    private String mServiceId;
    private CloudletRanker mRanker;

    public FindCloudletByRankAsyncTask(String serviceId, CloudletRanker ranker, CloudletCallback<Cloudlet> callback)
    {
        super(callback);
        this.mServiceId = serviceId;
        this.mRanker = ranker;
    }

    public FindCloudletByRankAsyncTask(Context context, String serviceId, CloudletRanker ranker, CloudletCallback<Cloudlet> callback)
    {
        super(context, callback, TITLE, MESSAGE);
        this.mServiceId = serviceId;
        this.mRanker = ranker;

    }

    @Override
    protected Cloudlet doInBackground(Void... params)
    {
        return CloudletFinder.findCloudletForService(mServiceId, mRanker);
    }
}
