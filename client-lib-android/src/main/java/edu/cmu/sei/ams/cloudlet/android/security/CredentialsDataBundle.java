package edu.cmu.sei.ams.cloudlet.android.security;

import java.util.HashMap;

import edu.cmu.sei.ams.cloudlet.MessageException;

/**
 * Created by Sebastian on 2016-05-26.
 */
public class CredentialsDataBundle {
    private String _cloudletName;
    private String _cloudletSSID;
    private String _authPassword;
    private String _devicePrivateKey;
    private String _radiusServerCertData;

    /**
     *
     */
    public CredentialsDataBundle(HashMap<String, String> data) throws MessageException {
        _cloudletName = data.get("cloudlet_name");
        _cloudletSSID = data.get("ssid");
        _authPassword = data.get("auth_password");
        _radiusServerCertData = data.get("server_radius_cert");
        _devicePrivateKey = data.get("device_private_key");

        if(_cloudletName == null)
            throw new MessageException("Invalid network SSID.");
        if(_authPassword == null)
            throw new MessageException("Invalid auth password.");
        if(_radiusServerCertData == null)
            throw new MessageException("Invalid cert data.");
        if(_devicePrivateKey == null)
            throw new MessageException("Invalid device private key.");
    }

    public String getCloudletName() {
        return _cloudletName;
    }

    public String getCloudletSSID() {
        return _cloudletSSID;
    }

    public String getAuthPassword() {
        return _authPassword;
    }

    public String getDevicePrivateKey() {
        return _devicePrivateKey;
    }

    public String getRadiusServeCertData() {
        return _radiusServerCertData;
    }
}
