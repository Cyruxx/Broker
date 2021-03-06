package com.raizlabs.android.broker.tests;

import com.raizlabs.android.broker.Request;
import com.raizlabs.android.broker.RequestCallback;
import com.raizlabs.android.broker.RequestCallbackAdapter;
import com.raizlabs.android.broker.core.Body;
import com.raizlabs.android.broker.core.Endpoint;
import com.raizlabs.android.broker.core.Method;
import com.raizlabs.android.broker.core.Param;
import com.raizlabs.android.broker.core.Part;
import com.raizlabs.android.broker.core.RequestExecutor;
import com.raizlabs.android.broker.core.ResponseHandler;
import com.raizlabs.android.broker.core.RestService;
import com.raizlabs.android.broker.responsehandler.SimpleJsonArrayResponseHandler;
import com.raizlabs.android.broker.responsehandler.SimpleJsonResponseHandler;
import com.raizlabs.android.broker.volley.VolleyExecutor;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Description:
 */
@RestService(baseUrl = "http://jsonplaceholder.typicode.com")
@ResponseHandler(SimpleJsonArrayResponseHandler.class)
@RequestExecutor(VolleyExecutor.class)
public interface TestRestInterface  {

    public static final String POSTS = "posts";

    public static final String COMMENTS = "comments";

    @Method(url = POSTS)
    public void fetchPostsByUserId(@Param("userId") long userID,
                                   RequestCallback<JSONArray> requestCallback);

    @Method(url = POSTS)
    public void fetchAllPosts(JsonArrayCallback requestCallback);

    @Method(url = COMMENTS)
    public void fetchAllComments(JsonArrayCallback callback);

    @Method(url = "/{firstLevel}/{secondLevel}/{thirdLevel}")
    public void fetchData(@Endpoint String firstLevel, @Endpoint String secondLevel, @Endpoint String thirdLevel,
                          RequestCallback<JSONArray> jsonArrayRequestCallback);

    @Method(url = POSTS + "/{userId}", method = Method.PUT)
    @ResponseHandler(SimpleJsonResponseHandler.class)
    public Request<JSONObject> updateCommentsWithUserId(@Body String putData, @Endpoint String userId, RequestCallbackAdapter<JSONObject> requestCallback);

    @Method(url = "/{firstLevel}/{secondLevel}/{thirdLevel}")
    @ResponseHandler(SimpleJsonResponseHandler.class)
    public Request<JSONObject> getFetchDataRequest(@Endpoint String firstLevel, @Endpoint String secondLevel, @Endpoint String thirdLevel);

    @Method(url = COMMENTS)
    public Request.Builder<JSONObject> getCommentsRequestBuilder();

    @Method(url = COMMENTS)
    public Request<JSONArray> getPostsByUserIdParamRequest(@Param("userId") long userId, @Param("id") long id);

    @Method(url = COMMENTS)
    public void postCommentData(@Part(value = "image", isFile = true) String imageFilePath, @Part("caption") String caption);

}
