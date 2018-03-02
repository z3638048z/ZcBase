package base.zc.com.project.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import android.widget.Scroller;

import base.zc.com.project.R;


/**
 * 标签条移动控件
 */
public class BarMoveView extends View {

    public BarMoveView(Context context) {
        super(context);
        initView();
    }

    public BarMoveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BarMoveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private float width;
    private float height;
    private Scroller scrollerFirst, scrollerSecond;
    private int color;
    private Paint paint;
    private View fromView; //起始控件
    private View toView; //终止控件
    private float fromX = -1;
    private float toX = -1;
    private float percentFirst = 0f;
    private float percentSecond = 0f;
    private static final int DEFAULT_DURATION = 1000;
    private int duration = DEFAULT_DURATION;
    private boolean isFromLeftToRight; //是否从左至右显示动画
    public final static int TYPE_NONE = 0x01; //直接切换标签
    public final static int TYPE_SMOOTH = 0x02; //平滑移动标签
    public final static int TYPE_FRONT_BACK = 0x03; //先前面移动，后面再移动
    private int type = TYPE_FRONT_BACK;

    public void setType(int type) {
        this.type = type;
    }

    private void initView(){
        scrollerFirst = new Scroller(getContext());
        scrollerSecond = new Scroller(getContext(), new OvershootInterpolator(1f));
        color = getResources().getColor(R.color.colorPrimary);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(width == 0 && getWidth() > 0){
            width = getWidth();
            height = getHeight();
            paint.setStrokeWidth(height);
        }

        if(toView != null && toView.getWidth() > 0){
            int[] position = new int[2];
            if(fromView != null){
                fromView.getLocationInWindow(position);
                fromX = position[0];
            }
            position = new int[2];
            toView.getLocationInWindow(position);
            toX = position[0];

            if(fromX < toX){
                isFromLeftToRight = true;
            }else{
                isFromLeftToRight = false;
            }
            // 只设置了位置, 直接显示滚动条
            if(fromView == null || type == TYPE_NONE){
                if(toView != null)
                    canvas.drawLine(toX, height / 2, toX + toView.getWidth(), height / 2, paint);
            }else{
                // 二者都有此时显示动画
                if(isFromLeftToRight){
                    if(type == TYPE_SMOOTH){
                        canvas.drawLine(fromX + (toX - fromX) * percentFirst, height / 2, fromX + fromView.getWidth() + (toX + toView.getWidth() - fromX - fromView.getWidth()) * percentFirst, height / 2, paint);
                    }else if(type == TYPE_FRONT_BACK){
                        if(scrollerFirst.computeScrollOffset()){
                            canvas.drawLine(fromX, height / 2, fromX + fromView.getWidth() + (toX - fromX - fromView.getWidth() + toView.getWidth()) * percentFirst, height / 2, paint);
                        }else if(scrollerSecond.computeScrollOffset()){
                            canvas.drawLine(fromX + (toX - fromX) * percentSecond, height / 2, toX + toView.getWidth(), height / 2, paint);
                        }else{
                            canvas.drawLine(toX, height / 2, toX + toView.getWidth(), height / 2, paint);
                        }
                    }
                }else{
                    if(type == TYPE_SMOOTH){
                        canvas.drawLine(fromX - (fromX - toX) * percentFirst, height / 2, fromX + fromView.getWidth() - (fromX + fromView.getWidth() - toX - toView.getWidth()) * percentFirst, height / 2, paint);
                    }else if(type == TYPE_FRONT_BACK){
                        if(scrollerFirst.computeScrollOffset()){
                            canvas.drawLine(fromX -  (fromX - toX) * percentFirst, height / 2, fromX + fromView.getWidth(), height / 2, paint);
                        }else if(scrollerSecond.computeScrollOffset()){
                            canvas.drawLine(toX, height / 2, toX + toView.getWidth() + (fromX - toX - toView.getWidth() + fromView.getWidth()) * (1 - percentSecond), height / 2, paint);
                        }else{
                            canvas.drawLine(toX, height / 2, toX + toView.getWidth(), height / 2, paint);
                        }
                    }
                }
            }
        }

    }

    private void startScroll(){
        if(type == TYPE_FRONT_BACK){
            duration /= 2;
        }
        scrollerFirst.startScroll(0, 0, 10000, 0, duration);
        postInvalidate();
    }

    @Override
    public void computeScroll() {
        if(scrollerFirst.computeScrollOffset()){

            percentFirst = (scrollerFirst.getCurrX() + 0f) / scrollerFirst.getFinalX();

            if(scrollerFirst.isFinished()){
                scrollerSecond.startScroll(0, 0, 10000, 0, duration / 2);
            }
            postInvalidate();

        }else if(scrollerSecond.computeScrollOffset()){

            percentSecond = (scrollerSecond.getCurrX() + 0f) / scrollerSecond.getFinalX();

            if(scrollerSecond.isFinished()){

            }
            postInvalidate();

        }
    }

    /**
     * 刷新一次控件到最新的位置，位置根据上次设置控件来改变
     */
    public void setPostionByOldView(){
        postInvalidate();
    }

    public void setPosition(View toView){
        setPosition(null, toView, 0);
    }

    public void setPosition(final View fromView, final View toView){
        setPosition(fromView, toView, 0);
    }

    /**
     * 设置滚动条显示位置，根据所传控件在屏幕中的位置而定
     * @param fromView
     * @param toView
     */
    public void setPosition(final View fromView, final View toView, int duration){
        if(toView != null){
            if(duration > 0){
                this.duration = duration;
            }else{
                this.duration = DEFAULT_DURATION;
            }
            percentFirst = percentSecond = 0f;
            if(toView.getWidth() == 0){
                toView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            if(toView.getWidth() > 0)
                                toView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                        BarMoveView.this.fromView = fromView;
                        BarMoveView.this.toView = toView;
                        if(fromView != null){
                            startScroll();
                        }else{
                            postInvalidate();
                        }
                    }
                });
            }else{
                this.fromView = fromView;
                this.toView = toView;
                if(fromView != null){
                    startScroll();
                }else{
                    postInvalidate();
                }
            }
        }
    }

    public void setColor(int color) {
        this.color = color;
        paint.setColor(color);
    }
}
