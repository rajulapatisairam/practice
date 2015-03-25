package com.practice.utils.sms;

/**
 *
 * @author anantha
 */
public interface SMS {
    
    
    boolean login(String userName,String password);
    boolean isAuthenticated();
    public void send(String mobileNo,String msg)throws NotAuthenticatedException;
    void setDebug(boolean debug);
}