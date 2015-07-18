package com.arbuleac.movieapp.sync;

import com.arbuleac.movieapp.model.Movie;
import com.arbuleac.movieapp.model.response.MovieResult;
import com.arbuleac.movieapp.model.response.ReviewResult;
import com.arbuleac.movieapp.model.response.VideoResult;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface MovieApi {

    @GET("/discover/movie")
    void discoverMovie(@Query("page") int page, @Query("sort_by") String sortBy, Callback<MovieResult> callback);

    @GET("/movie/{id}/videos")
    void trailers(@Path("id") int movieId, Callback<VideoResult> callback);

    @GET("/movie/{id}/reviews")
    void reviews(@Path("id") int movieId, Callback<ReviewResult> callback);
}
