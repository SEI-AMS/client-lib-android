package edu.cmu.sei.ams.cloudlet.android;

/**
 * User: jdroot
 * Date: 4/25/14
 * Time: 10:10 AM
 */
public interface CloudletCallback<T>
{
    public void handle(T result);
}
