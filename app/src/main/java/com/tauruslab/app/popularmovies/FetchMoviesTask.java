package com.tauruslab.app.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Criado por Fernando em 10/09/2017.
 * Tarefa para recuperar os filmes da API
 */

public class FetchMoviesTask extends AsyncTask<Void, Void, Void> {

    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

    private final Context mContext;

    public FetchMoviesTask(Context context) {
        mContext = context;
    }

    private boolean DEBUG = true;

    private void getMoviesDataFromJson(String moviesJsonStr) throws JSONException {
        try {
            JSONObject moviesJson = new JSONObject(moviesJsonStr);

            Log.d(LOG_TAG, moviesJsonStr);
            Log.d(LOG_TAG, moviesJson.toString());
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    @Override
    protected Void doInBackground(Void... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String moviesJsonStr = null;

        try {
            final String DATA_MOCK = "2017-09-10";

            // serão usadas as urls abaixo
            // https://api.themoviedb.org/3/movie/popular?
            // https://api.themoviedb.org/3/movie/top_rated?
            final String MOVIES_BASE_URL = "https://api.themoviedb.org/3/discover/movie?";
            final String RELEASE_DATE_GTE_PARAM = "primary_release_date.gte";
            final String RELEASE_DATE_LTE_PARAM = "primary_release_date.lte";
            final String APIKEY_PARAM = "api_key";

            Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    .appendQueryParameter(RELEASE_DATE_GTE_PARAM, DATA_MOCK)
                    .appendQueryParameter(RELEASE_DATE_LTE_PARAM, DATA_MOCK)
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
            getMoviesDataFromJson(moviesJsonStr);
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
}
