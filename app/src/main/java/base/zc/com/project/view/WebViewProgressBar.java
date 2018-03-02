package base.zc.com.project.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import base.zc.com.project.R;


/**
 * Created by Darren on 2017/12/5.
 */

public class WebViewProgressBar extends View {
    private Paint paintLeft;
    private Paint paint;
    private Paint paintBack;
    private float progress;
    private float currentProgress;
    private ValueAnimator valueAnimator;
    public WebViewProgressBar(Context context) {
        super(context);
        init();
    }

    private void init(){
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        paint.setAntiAlias(true);
        paintLeft = new Paint();
        paintLeft.setColor(0x66F18e00);
        paintLeft.setAntiAlias(true);
        paintBack = new Paint();
        paintBack.setColor(Color.WHITE);
        paintBack.setAntiAlias(true);


    }
    public WebViewProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(currentProgress != 1){
            canvas.drawRect(new RectF(0,0,getWidth() * currentProgress,getHeight()),paintBack);
            if(currentProgress <0.1){
                RectF rect = new RectF(0,0,getWidth() * currentProgress,getHeight());
                LinearGradient linearGradient = new LinearGradient(0,0,getWidth() * (currentProgress),0,paintLeft.getColor(),paint.getColor(), Shader.TileMode.CLAMP);
                paint.setShader(linearGradient);
                canvas.drawRect(rect,paint);
            }else{
                RectF rect1 = new RectF(0,0,getWidth() * (currentProgress-0.1f),getHeight());
                canvas.drawRect(rect1,paintLeft);

                RectF rect2 = new RectF(getWidth() * (currentProgress-0.1f),0,getWidth() * (currentProgress),getHeight());
                LinearGradient linearGradient = new LinearGradient(getWidth() * (currentProgress-0.1f),0,getWidth() * (currentProgress),0,paintLeft.getColor(),paint.getColor(), Shader.TileMode.CLAMP);
                paint.setShader(linearGradient);
                canvas.drawRect(rect2,paint);
            }
        }else{
            canvas.drawColor(Color.TRANSPARENT);
            currentProgress = 0;
        }


    }

    public void setProgress(float progress){
        this.progress = progress;
        if(valueAnimator != null){
            valueAnimator.cancel();
        }

        if(progress <currentProgress){
            currentProgress = 0;
        }
        valueAnimator = ObjectAnimator.ofFloat(currentProgress,progress).setDuration((long) ((progress-currentProgress)*2000));
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentProgress = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.start();

    }
}
