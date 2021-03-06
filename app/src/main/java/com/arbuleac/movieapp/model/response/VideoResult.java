package com.arbuleac.movieapp.model.response;

import com.arbuleac.movieapp.model.Video;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @since 7/12/15.
 */
public class VideoResult {
    @SerializedName("id")
    private int id;
    @SerializedName("results")
    private List<Video> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Video> getResults() {
        return results;
    }

    public void setResults(List<Video> results) {
        this.results = results;
    }
}
