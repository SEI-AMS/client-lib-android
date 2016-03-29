package edu.cmu.sei.ams.cloudlet.android;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Sebastian on 2015-10-01.
 */
public class CloudletPreferences
{
    private static final String PREFS_FILE_NAME = "cloudlet_preferences";

    private static final String PREF_ENCRYPTION_STATE = "encryptionState";

    /**
     * Returne whether encryption is enabled or not for Cloudlet API requests.
     * @param context
     * @return
     */
    public static boolean isEncryptionEnabled(Context context)
    {
        SharedPreferences settings = context.getSharedPreferences(PREFS_FILE_NAME, 0);
        boolean encryptionEnabled = settings.getBoolean(PREF_ENCRYPTION_STATE, false);
        return encryptionEnabled;
    }

    /**
     * Changes the encryption setting.
     * @param context
     * @param encryptionState
     */
    public static void setEncryptionState(Context context, boolean encryptionState)
    {
        SharedPreferences settings = context.getSharedPreferences(PREFS_FILE_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(PREF_ENCRYPTION_STATE, encryptionState);
        editor.commit();

    }
}
