package com.arbuleac.movieapp.sync;

import com.arbuleac.movieapp.util.Constants;
import com.arbuleac.movieapp.util.LogUtils;
import com.arbuleac.movieapp.util.UIUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;

import retrofit.Profiler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

public class ApiHelper {
    private static final String TAG = LogUtils.makeLogTag(ApiHelper.class);
    private static final String ENDPOINT = "http://api.themoviedb.org/3";

    private static ApiHelper sInstance;

    private MovieApi movieApi;
    private Gson mGson;

    public static ApiHelper getInstance() {
        if (sInstance == null) {
            sInstance = new ApiHelper();
        }

        return sInstance;
    }

    public static MovieApi getApi() {
        return getInstance().getMovieApi();
    }

    private ApiHelper() {
        OkHttpClient okHttpClient = new OkHttpClient();

        File cacheDir = new File(UIUtils.getAppContext().getCacheDir().getAbsolutePath(),
                UIUtils.getAppContext().getPackageName());
        Cache cache = new Cache(cacheDir, 10 * 1024 * 1024);
        okHttpClient.setCache(cache);

        createGson();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setClient(new OkClient(okHttpClient))
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addQueryParam("api_key", Constants.API_KEY);
                    }
                })
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(mGson))
                .setProfiler(new Profiler() {
                    @Override
                    public Object beforeCall() {
                        return null;
                    }

                    @Override
                    public void afterCall(RequestInformation requestInfo, long elapsedTime, int statusCode, Object beforeCallData) {
                        LogUtils.LOGD(TAG, "[Call completed] time: " + elapsedTime + " code: " + statusCode
                                + " url: " + requestInfo.getBaseUrl() + requestInfo.getRelativePath());
                    }
                })
                .build();

        movieApi = restAdapter.create(MovieApi.class);
    }

    private void createGson() {
        mGson = new GsonBuilder()
                .setExclusionStrategies(new GsonExclusionStrategy()) //will use this for stage 2 with realm.
                .setDateFormat("yyyy-MM-dd")
                .create();
    }

    private MovieApi getMovieApi() {
        return movieApi;
    }

}
