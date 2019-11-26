package com.app.shakealertla.Listener;

/**
 * Created by Kamran Ahmed on 4/30/2015.
 */
public interface ServiceListener<T,E> {

    public void success(T SuccessListener);
    public void error(E ErrorListener);
}
