package com.tauruslab.app.popularmovies;

import java.util.Date;

/**
 * Criado por Fernando em 12/09/2017.
 * Classe para os filmes
 */

public class Movie {
    int id;
    String title;
    String posterPath;
    String overview;
    Date releaseDate;
    int voteCount;

    public Movie(int id, String title, String posterPath, String overview, Date releaseDate, int voteCount) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.voteCount = voteCount;
    }

    public String getPosterURL() {
        return "http://image.tmdb.org/t/p/w185/" + posterPath;
    }
}
