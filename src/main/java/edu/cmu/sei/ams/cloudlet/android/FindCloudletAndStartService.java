package edu.cmu.sei.ams.cloudlet.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import edu.cmu.sei.ams.cloudlet.Cloudlet;
import edu.cmu.sei.ams.cloudlet.Service;
import edu.cmu.sei.ams.cloudlet.ServiceVM;
import edu.cmu.sei.ams.cloudlet.rank.CloudletRanker;
import edu.cmu.sei.ams.cloudlet.rank.CpuBasedRanker;

/**
 * User: jdroot
 * Date: 4/29/14
 * Time: 2:55 PM
 */
public class FindCloudletAndStartService
{
    private Context mContext;
    private String mServiceId;
    private CloudletRanker mRanker;
    private CloudletCallback<ServiceVM> mCallback;


    public FindCloudletAndStartService(Context context, String serviceId, CloudletRanker ranker, CloudletCallback<ServiceVM> callback)
    {
        this.mContext = context;
        this.mServiceId = serviceId;
        this.mRanker = ranker;
        this.mCallback = callback;
    }

    public FindCloudletAndStartService(Context context, String serviceId, final ConnectionInfo connectionInfo)
    {
        this.mContext = context;
        this.mServiceId = serviceId;
        this.mRanker = new CpuBasedRanker();    // Default ranker.
        this.mCallback = new CloudletCallback<ServiceVM>()
        {
            @Override
            public void handle(ServiceVM result)
            {
                Context context = FindCloudletAndStartService.this.mContext;
                Log.v("FindCloudletAndStartService", "Got service results: " + result.getInstanceId());

                if (result == null)
                {
                    Toast.makeText(context, "Failed to locate a cloudlet for this app", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(context, "Located a cloudlet to use!", Toast.LENGTH_LONG).show();
                connectionInfo.storeIntoPreferences(result.getAddress().getHostAddress(), result.getPort());
            }
        };
    }

    public void execute()
    {
        new FindCloudletByRankAsyncTask(mContext, mServiceId, mRanker, new CloudletCallback<Cloudlet>()
        {
            public void handle(Cloudlet result)
            {
                if (result == null)
                {
                    mCallback.handle(null);
                    return;
                }
                try
                {
                    Service service = result.getServiceById(mServiceId); //This *should* never fail
                    new StartServiceAsyncTask(service, mContext, mCallback).execute();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).execute();
    }
}
