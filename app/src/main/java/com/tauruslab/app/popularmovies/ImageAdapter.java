package com.tauruslab.app.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Criado por Fernando em 10/09/2017.
 * Adapter para as imagens da grid inicial.
 */

class ImageAdapter extends BaseAdapter {
    private final Context mContext;
    private final Picasso mPicasso;

    public ImageAdapter(Context c) {
        mContext = c;

        mPicasso = new Picasso.Builder(c)
                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        Log.e("PICASSO_FAILED", "URI: " + uri, exception);
                    }
                })
                .build();
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public String getItem(int position) {
        return "http://i.imgur.com/DvpvklR.png";
    }

    public long getItemId(int position) {
        return mThumbIds[position];
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = (ImageView) convertView;
        if (imageView == null) {
            imageView = new ImageView(mContext);
        }

        String url = getItem(position);

        mPicasso.load(url)
                .placeholder(R.drawable.progress)
                .error(R.drawable.warning)
                .resize(800, 800)
                .centerCrop()
                .into(imageView);

        return imageView;
    }

    // references to our images
    private final Integer[] mThumbIds = {
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7
    };
}