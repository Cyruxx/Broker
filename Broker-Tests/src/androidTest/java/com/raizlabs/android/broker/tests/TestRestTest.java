package com.raizlabs.android.broker.tests;

import android.test.AndroidTestCase;

import com.raizlabs.android.broker.Request;
import com.raizlabs.android.broker.RequestConfig;
import com.raizlabs.android.broker.RequestManager;
import com.raizlabs.android.broker.rest.BaseRestInterface;
import com.raizlabs.android.broker.volley.VolleyExecutor;
import com.raizlabs.synchronization.OneShotLock;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Author: andrewgrosner
 * Contributors: { }
 * Description:
 */
public class TestRestTest extends AndroidTestCase {

    private OneShotLock mRequestLock = new OneShotLock();

    @Override
    public void setUp() throws Exception {
        super.setUp();
        RequestConfig.init(getContext(), VolleyExecutor.getSharedExecutor());
    }

    public void testRestInterface() {
        TestRestInterface restInterface = RequestManager.getRestInterface(TestRestInterface.class);
        assertNotNull(restInterface);

        restInterface.fetchPostsByUserId(1, getArrayReponse());
        mRequestLock.waitUntilUnlocked();

        restInterface.fetchAllComments(getArrayReponse());
        mRequestLock.waitUntilUnlocked();

        restInterface.fetchAllPosts(getArrayReponse());
        mRequestLock.waitUntilUnlocked();

        restInterface.fetchData(TestRestInterface.POSTS, "1", TestRestInterface.COMMENTS);
        mRequestLock.waitUntilUnlocked();

        BaseRestInterface baseRestInterface = (BaseRestInterface) restInterface;

        Request<JSONObject> fetchRequest = restInterface.getFetchDataRequest(TestRestInterface.POSTS, "1", TestRestInterface.COMMENTS);
        assertNotNull(fetchRequest.getBaseUrl());
        assertNotNull(fetchRequest.getUrl());
        assertNotNull(fetchRequest.getFullUrl());
        assertEquals(fetchRequest.getFullUrl(), baseRestInterface.getFullBaseUrl() + "/posts/1/comments");
    }

    private JsonArrayCallback getArrayReponse() {
        return new JsonArrayCallback() {
            @Override
            public void onRequestDone(JSONArray jsonObject) {
                assertNotNull(jsonObject);
                mRequestLock.unlock();
            }

            @Override
            public void onRequestError(Throwable error, String stringError) {
                assertNull(error);
                assertNull(stringError);
                mRequestLock.unlock();
            }
        };
    }
}