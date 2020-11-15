package com.yq.imageviewer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.yq.imageviewer.utils.DeviceUtils;

public class MovieView extends View {

    private Movie mMovie;
    private long mMovieStart;
    private float mScaleSize;

    public MovieView(Context context) {
        super(context);
    }

    public MovieView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setMovieFile(String path) {
        mMovie = Movie.decodeFile(path);
        mScaleSize = DeviceUtils.getScreenWidth(getContext()) / (float) mMovie.width();
        mScaleSize = (mScaleSize != 0 ? mScaleSize : 1);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        long now = android.os.SystemClock.uptimeMillis();
        if (mMovieStart == 0) {
            mMovieStart = now;
        }
        int d = mMovie.duration();
        if (d <= 0) {
            d = 1500;
        }
        canvas.scale(mScaleSize, mScaleSize);
        if (mMovie != null) {
            int relTime = (int) ((now - mMovieStart) % d);
            mMovie.setTime(relTime);
            mMovie.draw(canvas, 0, (DeviceUtils.getScreenHeight(getContext()) / mScaleSize - mMovie.height()) / 2);
            this.invalidate();
        }
    }
}
