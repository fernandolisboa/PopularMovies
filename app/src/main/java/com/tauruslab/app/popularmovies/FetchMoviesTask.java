package com.tauruslab.app.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

/**
 * Criado por Fernando em 10/09/2017.
 * Tarefa para recuperar os filmes da API
 */

public class FetchMoviesTask extends AsyncTask<String, Void, Movie[]> {

    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

    private final Context mContext;

    private MovieAdapter mMovieAdapter;

    public FetchMoviesTask(Context context, MovieAdapter movieAdapter) {
        mContext = context;
        mMovieAdapter = movieAdapter;
    }

    private Movie[] getMoviesDataFromJson(String moviesJsonStr) throws JSONException {

        final String OWM_RESULTS = "results";

        final String OWM_ID = "id";
        final String OWM_TITLE = "title";
        final String OWM_POSTER_PATH = "poster_path";
        final String OWM_OVERVIEW = "overview";
        final String OWM_RELEASE_DATE = "release_date";
        final String OWM_VOTE_COUNT = "vote_count";

        try {
            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray(OWM_RESULTS);

            Movie[] movies = new Movie[moviesArray.length()];
            for (int i = 0; i < movies.length; i++) {
                int id;
                String title;
                String posterPath;
                String overview;
                Date releaseDate;
                int voteCount;

                JSONObject movieJson = moviesArray.getJSONObject(i);

                id = movieJson.getInt(OWM_ID);
                title = movieJson.getString(OWM_TITLE);
                posterPath = movieJson.getString(OWM_POSTER_PATH);
                overview = movieJson.getString(OWM_OVERVIEW);
                releaseDate = Utility.strToDate(movieJson.getString(OWM_RELEASE_DATE));
                voteCount = movieJson.getInt(OWM_VOTE_COUNT);

                Movie movie = new Movie(id, title, posterPath, overview, releaseDate, voteCount);

                movies[i] = movie;
            }

            return movies;
        } catch (JSONException|ParseException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected Movie[] doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String moviesJsonStr;

        try {
            // https://api.themoviedb.org/3/movie/popular?
            // https://api.themoviedb.org/3/movie/top_rated?
            final String MOVIES_BASE_URL = "https://api.themoviedb.org/3/movie";
            final String MOVIES_ORDER = params[0];
            final String APIKEY_PARAM = "api_key";

            Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    .appendPath(MOVIES_ORDER)
                    .appendQueryParameter(APIKEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder builder = new StringBuilder();
            if (inputStream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }

            if (builder.length() == 0) {
                return null;
            }

            moviesJsonStr = builder.toString();
            return getMoviesDataFromJson(moviesJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error", e);

            return null;
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Movie[] movies) {
        if (movies != null && mMovieAdapter != null) {
            mMovieAdapter.clear();
            for(Movie movie: movies) {
                mMovieAdapter.add(movie);
            }
        }
    }
}
