package com.quantumtech.vido.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

public class MovieItemImageView extends androidx.appcompat.widget.AppCompatImageView {
    public static float radius = 10.0f;
    Path clipPath;
    RectF rect;
//    Paint paint;
    Bitmap bitmap;

    public MovieItemImageView(Context context) {
        super(context);
    }

    public MovieItemImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MovieItemImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        clipPath = new Path();
        rect = new RectF(0,0,this.getWidth(),this.getHeight());
//        paint = new Paint();
//        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.img);

//        paint.setAntiAlias(true);
//        paint.setShadowLayer(10f,0f,2f, Color.BLACK);
//        setLayerType(LAYER_TYPE_SOFTWARE,paint);

        clipPath.addRoundRect(rect,radius,radius, Path.Direction.CW);
//        float[] a = {
//                0,0,
//                0,0,
//                this.getWidth()/2f,this.getHeight()/8f,
//                this.getWidth()/2f,this.getHeight()/8f,};
//
//        clipPath.addRoundRect(rect,a, Path.Direction.CW);

        canvas.clipPath(clipPath);
//        canvas.drawColor(Color.GRAY);
//        canvas.drawRect(rect,paint);
//        canvas.drawBitmap(bitmap,0,0,paint);
        super.onDraw(canvas);
    }
}
