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

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Criado por Fernando em 12/09/2017.
 * Adapter para Movie
 */

public class MovieAdapter extends ArrayAdapter<Movie> {

    private final Picasso mPicasso;

    public MovieAdapter(Activity context, List<Movie> movies) {
        super(context, 0, movies);

        mPicasso = new Picasso.Builder(context)
                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        Log.e("PICASSO_FAILED", "URI: " + uri, exception);
                    }
                })
                .build();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Movie movie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_movie, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.list_item_poster);

        if(movie != null) {
            String url = movie.getPosterURL();

            mPicasso.load(url)
                    .placeholder(R.drawable.progress)
                    .error(R.drawable.warning)
                    .resize(800, 800)
                    .centerCrop()
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.warning);
        }

        return super.getView(position, convertView, parent);
    }
}
