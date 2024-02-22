package com.moviesbyreex.data;

import java.io.Serializable;
import java.util.ArrayList;

public class Movie implements Serializable {
    private int id;
    private int voteCount;
    private double voteAverage;
    private String title;
    private String overview;
    private String posterPath;
    private String bigPosterPath;
    private String releaseDate;

    public Movie(int id, int voteCount, double voteAverage, String title,
                 String overview, String posterPath, String bigPosterPath, String releaseDate) {
        this.id = id;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
        this.title = title;
        this.overview = overview;
        this.posterPath = posterPath;
        this.bigPosterPath = bigPosterPath;
        this.releaseDate = releaseDate;
    }

    public String getBigPosterPath() {
        return bigPosterPath;
    }

    public int getId() {
        return id;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public int getVoteCount() {
        return voteCount;
    }
}
