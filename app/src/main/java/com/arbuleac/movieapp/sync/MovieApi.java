package com.arbuleac.movieapp.sync;

import com.arbuleac.movieapp.model.Movie;
import com.arbuleac.movieapp.model.response.MovieResult;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

public interface MovieApi {

    @GET("/discover/movie")
    void discoverMovie(@Query("page") int page, @Query("sort_by") String sortBy, Callback<MovieResult> callback);
}
