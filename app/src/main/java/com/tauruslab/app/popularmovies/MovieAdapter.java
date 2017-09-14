package com.tauruslab.app.popularmovies;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Criado por Fernando em 12/09/2017.
 * Adapter para Movie
 */

public class MovieAdapter extends ArrayAdapter<Movie> {

    private final String LOG_TAG = MovieAdapter.class.getSimpleName();

    private final Activity mContext;
    private final List<Movie> mMovies;
    private final Picasso mPicasso;

    public MovieAdapter(Activity context, List<Movie> movies) {
        super(context, 0, movies);

        mContext = context;
        mMovies = movies;

        mPicasso = new Picasso.Builder(context)
                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        Log.e(LOG_TAG, uri.toString(), exception);
                    }
                })
                .build();
    }

    @Override
    public int getCount() {
        return mMovies.size();
    }

    @Override
    public Movie getItem(int position) {
        return mMovies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mMovies.get(position).id;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        TextView title;
        ImageView poster;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.grid_item_movie, parent, false);
            view.setTag(R.id.title, view.findViewById(R.id.title));
            view.setTag(R.id.poster, view.findViewById(R.id.poster));
        }

        title = (TextView) view.getTag(R.id.title);
        poster = (ImageView) view.getTag(R.id.poster);

        Movie movie = getItem(position);

        if(movie != null) {
            title.setText(movie.title);

            String url = movie.getPosterURL();

            mPicasso.load(url)
                    .placeholder(R.drawable.progress)
                    .error(R.drawable.warning)
                    .resize(800, 800)
                    .centerCrop()
                    .into(poster);
        } else {
            title.setText(R.string.movie_not_found);
            poster.setImageResource(R.drawable.warning);
        }

        return view;
    }
}
