package base.zc.com.project.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import base.zc.com.project.util.DisplayUtil;

/**
 * Created by Darren on 2018/1/12.
 */

public class MyLinearLayout extends LinearLayout {
    private float yDown,yMove;
    private float yDownTranslation;
    private OnTranslateListener onTranslateListener;
    private boolean isInterrupt;
    private ValueAnimator valueAnimator;
    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if(!isEnabled()){
            return super.dispatchTouchEvent(ev);
        }

        if(valueAnimator != null && valueAnimator.isRunning()){
            return super.dispatchTouchEvent(ev);
        }
        final float distance =  DisplayUtil.dip2px(getContext(),228);
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                yDown = ev.getRawY();
                yDownTranslation = getTranslationY();
                isInterrupt = false;
                break;
            case MotionEvent.ACTION_MOVE:
                yMove = ev.getRawY();

                if(yMove <yDown && yDownTranslation-(yDown -yMove) >0) {
                    setTranslationY(yDownTranslation-(yDown -yMove));
                    if(onTranslateListener != null){
                        onTranslateListener.onTranslate(yDownTranslation-(yDown -yMove));
                    }
                    return false;
                }else if(yMove>yDown &&yDownTranslation+(yMove -yDown)<distance){
                    setTranslationY(yDownTranslation+(yMove -yDown));
                    if(onTranslateListener != null){
                        onTranslateListener.onTranslate(yDownTranslation+(yMove -yDown));
                    }
                    return false;
                } else if(yMove <yDown && yDownTranslation-(yDown -yMove) <=0 && getTranslationY() >0) {
                    isInterrupt = true;
                    setTranslationY(0);
                    if(onTranslateListener != null){
                        onTranslateListener.onTranslate(0);
                    }
                    return false;
                }else if(yMove>yDown &&yDownTranslation+(yMove -yDown)>=distance && getTranslationY() <distance){
                    isInterrupt = true;
                    setTranslationY(distance);
                    if(onTranslateListener != null){
                        onTranslateListener.onTranslate(distance);
                    }
                    return false;
                }

                if(isInterrupt){
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                yMove = ev.getRawY();
                if(yMove <yDown && getTranslationY() >0) {
                    final float translationY = getTranslationY();
                    valueAnimator = ObjectAnimator.ofFloat(1,0);
                    valueAnimator.setDuration((long) (300.0 * translationY/distance));
                    valueAnimator.setInterpolator(new DecelerateInterpolator());
                    valueAnimator.start();
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            if(onTranslateListener != null){
                                float percent = (float) animation.getAnimatedValue();
                                setTranslationY(percent * translationY);
                                onTranslateListener.onTranslate(percent * translationY);
                            }
                        }
                    });
                    return false;
                }else if(yMove>yDown &&getTranslationY()<distance){
                    final float translationY = getTranslationY();
                    valueAnimator = ObjectAnimator.ofFloat(0,1);
                    valueAnimator.setDuration((long) (300.0 *(distance-translationY)/distance));
                    valueAnimator.setInterpolator(new DecelerateInterpolator());
                    valueAnimator.start();
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            if(onTranslateListener != null){
                                float percent = (float) animation.getAnimatedValue();
                                setTranslationY(translationY+(distance-translationY)*percent);
                                onTranslateListener.onTranslate(translationY+(distance-translationY)*percent);
                            }
                        }
                    });
                    return false;
                }
                if(isInterrupt){
                    return false;
                }
                break;

        }
        return super.dispatchTouchEvent(ev);
    }

    public void setOnTranslateListener(OnTranslateListener onTranslateListener) {
        this.onTranslateListener = onTranslateListener;
    }

    public interface OnTranslateListener{
        void onTranslate(float translationY);
    }
}
