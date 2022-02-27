package com.quantumtech.vido.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

public class MovieDetailImageView extends androidx.appcompat.widget.AppCompatImageView {
    public static float radius = 10.0f;
    Path clipPath;
    RectF rect;
//    Paint paint;
    Bitmap bitmap;

    public MovieDetailImageView(Context context) {
        super(context);
    }

    public MovieDetailImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MovieDetailImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        clipPath = new Path();
        rect = new RectF(0,0,this.getWidth(),this.getHeight());

//        clipPath.addRoundRect(rect,radius,radius, Path.Direction.CW);
        float[] a = {
                0,0,
                0,0,
                this.getWidth()/2f,this.getHeight()/8f,
                this.getWidth()/2f,this.getHeight()/8f,};

        clipPath.addRoundRect(rect,a, Path.Direction.CW);

        canvas.clipPath(clipPath);
        super.onDraw(canvas);
    }
}
