package edu.cmu.sei.ams.cloudlet.android.security;

import edu.cmu.sei.ams.cloudlet.MessageException;

/**
 * Contains method to make a thread start polling to a new cloudlet.
 */
public interface IMessagePollingThreadMover {
    void moveMessagePollingThreadToNewCloudlet() throws MessageException;
}
