package base.zc.com.project.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import base.zc.com.project.R;

public class ProgressBar extends View {
    public ProgressBar(Context context) {
        super(context);
        initView();
    }

    public ProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    Paint paint;
    float width, height;
    float percent;
    float deltaX;
    int color = getResources().getColor(R.color.colorPrimary);

    private void initView(){
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(height == 0){
            height = getHeight();
            deltaX = height / 2;
            width = getWidth() - deltaX * 2;
            paint.setStrokeWidth(height);
        }

        canvas.drawLine(deltaX, height / 2, deltaX + width * percent, height / 2, paint);

    }

    public void setProgress(float percent){
        this.percent = percent;
        postInvalidate();
    }

    public void setColor(int color) {
        this.color = color;
        paint.setColor(color);
    }
}
