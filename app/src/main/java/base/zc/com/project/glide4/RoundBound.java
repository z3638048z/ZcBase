package base.zc.com.project.glide4;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import com.bumptech.glide.BuildConfig;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.bumptech.glide.util.Util;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;

public class RoundBound extends BitmapTransformation {
    private static final String ID = BuildConfig.APPLICATION_ID + "roundBound";
    public float radius;
    public int boundWidth;
    public int color;

    public RoundBound(float radius, int color){
        this(radius, 0, color);
    }

    public RoundBound(float radius){
        this(radius, 0, 0);
    }

    public RoundBound(float radius, int boundWidth, int color){
        this.radius = radius;
        this.boundWidth = boundWidth;
        this.color = color;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

        Charset charset = Charset.forName(STRING_CHARSET_NAME);

        messageDigest.update(ID.getBytes(charset));

        byte[] radiusData = ByteBuffer.allocate(4).putFloat(radius).array();
        messageDigest.update(radiusData);

        byte[] boundWidthData = ByteBuffer.allocate(4).putInt(boundWidth).array();
        messageDigest.update(boundWidthData);

        byte[] colorData = ByteBuffer.allocate(4).putInt(color).array();
        messageDigest.update(colorData);

    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof RoundBound){
            RoundBound other = (RoundBound) o;
            return other.radius == radius && other.boundWidth == boundWidth && other.color == color;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Util.hashCode(ID.hashCode(),
                Util.hashCode(radius, Util.hashCode(boundWidth, Util.hashCode(color))));
    }

    private Bitmap.Config getNonNullConfig(@NonNull Bitmap bitmap) {
        return bitmap.getConfig() != null ? bitmap.getConfig() : Bitmap.Config.ARGB_8888;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {

        int bmWidth = toTransform.getWidth();
        int bmHeight = toTransform.getHeight();
//                float transX = Math.abs(outWidth - bmWidth) / 2;
//                float transY = Math.abs(outHeight - bmHeight) / 2;

        Bitmap transBm = pool.get(bmWidth, bmHeight, getNonNullConfig(toTransform));
        // We don't add or remove alpha, so keep the alpha setting of the Bitmap we were given.
        TransformationUtils.setAlpha(toTransform, transBm);

        Canvas canvas = new Canvas(transBm);

        if(boundWidth > 0){
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(color);
            canvas.drawRoundRect(new RectF(0, 0, bmWidth, bmHeight), radius, radius, paint);
        }

        Matrix mm = new Matrix();
        float scaleX = (transBm.getWidth() - boundWidth * 2) * 1f / bmWidth;
        float scaleY = (transBm.getHeight() - boundWidth * 2) * 1f / bmHeight;
        mm.setScale(scaleX, scaleY);
        mm.preTranslate(-transBm.getWidth() / 2, -bmWidth / 2);
        mm.postTranslate(transBm.getWidth() / 2, bmHeight / 2);
//                mm.preTranslate(transX, transY);
        canvas.drawBitmap(toTransform, mm, new Paint());
        return transBm;
    }
}
