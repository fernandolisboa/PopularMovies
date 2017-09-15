package com.tauruslab.app.popularmovies.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.tauruslab.app.popularmovies.R;

/**
 * Criado por Fernando em 13/09/2017.
 */

public class RectangleImageView extends AppCompatImageView {

    private static final int FIXED_DIMENSION_WIDTH = 0;
    private static final int FIXED_DIMENSION_HEIGHT = 1;

    private int mFixedDimension = -1;
    private int mWidthRatio = 1;
    private int mHeightRatio = 1;

    public RectangleImageView(Context context) {
        super(context);
    }

    public RectangleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.RectangleImageView, 0, 0);

        try {
            mFixedDimension = a.getInteger(
                    R.styleable.RectangleImageView_fixedDimension, FIXED_DIMENSION_WIDTH);
            mWidthRatio = a.getInteger(R.styleable.RectangleImageView_widthRatio, 10);
            mHeightRatio = a.getInteger(R.styleable.RectangleImageView_heightRatio, 15);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mFixedDimension == FIXED_DIMENSION_WIDTH) {
            int height = getMeasuredWidth() / mWidthRatio * mHeightRatio;
            setMeasuredDimension(getMeasuredWidth(), height);
        } else if (mFixedDimension == FIXED_DIMENSION_HEIGHT ) {
            int width = getMeasuredHeight() / mHeightRatio * mWidthRatio;
            setMeasuredDimension(width, getMeasuredHeight());
        }
    }
}
