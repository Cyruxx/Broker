package com.raizlabs.android.broker.volley;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.Volley;
import com.raizlabs.android.broker.Request;
import com.raizlabs.android.broker.RequestCallback;
import com.raizlabs.android.broker.RequestConfig;
import com.raizlabs.android.broker.RequestExecutor;
import com.raizlabs.android.broker.core.Method;
import com.raizlabs.android.broker.core.Priority;

/**
 * Description: Provides the default implementation for a volley request.
 */
public class VolleyExecutor implements RequestExecutor<Object> {

    /**
     * Default timeout set to 15 seconds
     */
    static final int sSOCKET_TIMEOUT_MS = 15000;

    private static VolleyExecutor sharedExecutor;

    public static VolleyExecutor getSharedExecutor() {
        if (sharedExecutor == null) {
            sharedExecutor = new VolleyExecutor();
        }
        return sharedExecutor;
    }

    private RequestQueue mQueue;

    private HttpClientStack mStack;

    /**
     * This defines the retry policy for all requests on this executor. This is a default retry policy with
     * a timeout of 15 seconds (since 2.5 seconds for default is pretty quick).
     */
    private RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
            sSOCKET_TIMEOUT_MS,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    /**
     * Sets a custom stack for us to use in order to manage cookies.
     *
     * @param stack
     */
    public void setStack(HttpClientStack stack) {
        mStack = stack;
    }

    /**
     * Defines a custom {@link com.android.volley.RetryPolicy} for volley.
     *
     * @param retryPolicy
     */
    public void setRetryPolicy(RetryPolicy retryPolicy) {
        mRetryPolicy = retryPolicy;
    }

    @Override
    public void execute(final Request request) {
        if (mQueue == null) {
            if (mStack == null) {
                mQueue = Volley.newRequestQueue(RequestConfig.getContext());
            } else {
                mQueue = Volley.newRequestQueue(RequestConfig.getContext(), mStack);
            }
        }

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String errStr = "";
                try {
                    errStr = new String(volleyError.networkResponse.data);
                } catch (Throwable ignored) {
                    ignored.printStackTrace();
                }
                RequestCallback callback = request.getCallback();
                if (callback != null) {
                    callback.onRequestError(volleyError, errStr);
                }
            }
        };

        String url = request.getUrl();
        if (request.getMethod() == Method.GET) {
            url = request.getFullUrl();
        }

        com.android.volley.Request volleyRequest = new BrokerVolleyRequest(request, url, errorListener);
        volleyRequest.setRetryPolicy(mRetryPolicy);
        mQueue.add(volleyRequest);
    }

    @Override
    public void cancelRequest(Object tag, final Request request) {
        if (mQueue != null) {
            if (tag != null && !tag.equals("")) {
                mQueue.cancelAll(tag);
            } else {
                mQueue.cancelAll(new RequestQueue.RequestFilter() {
                    @Override
                    public boolean apply(com.android.volley.Request<?> volleyRequest) {
                        String url = request.getFullUrl();
                        String url2 = volleyRequest.getUrl();
                        return url.equalsIgnoreCase(url2);
                    }
                });
            }
        }
    }

    @Override
    public void cancelAllRequests() {
        if (mQueue != null) {
            mQueue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(com.android.volley.Request<?> request) {
                    return true;
                }
            });
        }
    }

    @Override
    public com.android.volley.Request.Priority convertPriority(Priority priority) {
        switch (priority) {
            case IMMEDIATE:
                return com.android.volley.Request.Priority.IMMEDIATE;
            case HIGH:
                return com.android.volley.Request.Priority.HIGH;
            case NORMAL:
                return com.android.volley.Request.Priority.NORMAL;
            case LOW:
                return com.android.volley.Request.Priority.LOW;
        }
        return null;
    }
}