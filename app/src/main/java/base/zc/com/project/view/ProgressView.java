package base.zc.com.project.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import base.zc.com.project.util.DisplayUtil;

public class ProgressView extends View {
    public ProgressView(Context context) {
        super(context);
        initView(context);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    int color = 0x99ffa939;
    Paint paint = new Paint();
    float curPercent = 0f;
    float targetPercent = 0f;
    float radius;
    long totalTime = 1000;

    private void initView(Context context){
        paint.setAntiAlias(true);
        paint.setColor(color);
//        paint.setColor(Color.RED);
        radius = DisplayUtil.dip2px(getContext(), 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(getWidth() == 0){
            return;
        }

        int width = getWidth();
        int height = getHeight();

        canvas.drawRoundRect(new RectF(0, height * (1 - curPercent), width, height), radius, radius, paint);
    }

    private ValueAnimator animator;

    public void setCurPercent(final float targetPercent) {
        this.targetPercent = targetPercent;
        final float deltaPercent = targetPercent - curPercent;
        final float startPercent = curPercent;
        if(animator != null){
            animator.cancel();
        }
        animator = ObjectAnimator.ofFloat(0f, 1f).setDuration((long) (deltaPercent * totalTime));
        animator.setInterpolator(new DecelerateInterpolator(2));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float percent = (float) animation.getAnimatedValue();
                curPercent = startPercent + deltaPercent * percent;
                if(listener != null){
                    listener.onUpdate(curPercent);
                }
                invalidate();
            }
        });
        animator.start();
    }

    public interface onUpdateListener{
        void onUpdate(float percent);
    }

    public onUpdateListener listener;

    public void setOnUpdateListener(onUpdateListener listener) {
        this.listener = listener;
    }

    public void cancel(){
        if(animator != null){
            animator.cancel();
        }
    }

}
