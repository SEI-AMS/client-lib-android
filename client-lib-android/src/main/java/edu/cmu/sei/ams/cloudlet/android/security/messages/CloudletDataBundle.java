package edu.cmu.sei.ams.cloudlet.android.security.messages;

import java.util.HashMap;

import edu.cmu.sei.ams.cloudlet.MessageException;

/**
 * Contains data that describes a cloudlet.
 */
public class CloudletDataBundle {
    private String _cloudletName;
    private String _cloudletFqdn;
    private int _cloudletPort;
    private boolean _cloudletEncryptionEnabled;
    private String _cloudletSSID;

    /**
     *
     */
    public CloudletDataBundle(HashMap<String, String> data) throws MessageException {
        _cloudletName = data.get("cloudlet_name");
        _cloudletFqdn = data.get("cloudlet_fqdn");
        _cloudletPort = Integer.parseInt(data.get("cloudlet_port"));
        _cloudletEncryptionEnabled = data.get("cloudlet_encryption_enabled") == "True";
        _cloudletSSID = data.get("ssid");

        if(_cloudletName == null)
            throw new MessageException("Invalid cloudlet name.");
        if(_cloudletFqdn == null)
            throw new MessageException("Invalid auth password.");
        if(_cloudletPort == 0)
            throw new MessageException("Invalid cert data.");
        if(_cloudletSSID == null)
            throw new MessageException("Invalid cloudlet SSID.");

    }

    public String getCloudletName() {
        return _cloudletName;
    }

    public String getCloudletFqdn() {
        return _cloudletFqdn;
    }

    public int getCloudletPort() {
        return _cloudletPort;
    }

    public boolean isCloudletEncryptionEnabled() {
        return _cloudletEncryptionEnabled;
    }

    public String getCloudletSSID() {
        return _cloudletSSID;
    }
}
