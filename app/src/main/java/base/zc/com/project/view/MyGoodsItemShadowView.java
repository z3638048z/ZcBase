package base.zc.com.project.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class MyGoodsItemShadowView extends View {
    public MyGoodsItemShadowView(Context context) {
        super(context);
    }

    public MyGoodsItemShadowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGoodsItemShadowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Paint paintShadow, paintWhole;
    private Bitmap shadowBm;
    private Canvas shadowCanvas;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(shadowBm == null){
            shadowBm = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            shadowCanvas = new Canvas(shadowBm);
            paintShadow = new Paint();
            paintShadow.setAntiAlias(true);
            paintWhole = new Paint();
            paintWhole.setAntiAlias(true);
            LinearGradient lg = new LinearGradient(0, 0, getWidth(), 0, new int[]{0x06000000, 0x00000000}, new float[]{0f, 1f}, Shader.TileMode.CLAMP);
            paintShadow.setShader(lg);
            shadowCanvas.drawRect(0, 0, getWidth(), getHeight(), paintShadow);
        }

        canvas.drawBitmap(shadowBm, 0, 0, paintWhole);

    }
}
