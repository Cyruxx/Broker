package com.raizlabs.android.broker;

import com.raizlabs.android.broker.rest.RestAdapter;

/**
 * Description: The interface to the generated adapter class to retrieve RestInterface classes.
 */
public class RequestManager {

    private static RestAdapter restAdapter;

    public static RestAdapter getRestAdapter() {
        if(restAdapter == null) {
            try {
                restAdapter = (RestAdapter) Class.forName("com.raizlabs.android.broker.RequestManager$Adapter").newInstance();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        return restAdapter;
    }

    /**
     * Returns the specified RestInterface for the class to run requests with.
     * @param restClass
     * @param <RestClass>
     * @return
     */
    public static <RestClass> RestClass getRestInterface(Class<RestClass> restClass) {
        return getRestAdapter().getRestInterface(restClass);
    }
}
