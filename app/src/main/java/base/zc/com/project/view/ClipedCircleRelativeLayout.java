package base.zc.com.project.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import base.zc.com.project.util.DisplayUtil;


public class ClipedCircleRelativeLayout extends RelativeLayout {
    public ClipedCircleRelativeLayout(Context context) {
        super(context);
    }

    public ClipedCircleRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClipedCircleRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private float radius = DisplayUtil.dip2px(getContext(), 5);

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Path path = new Path();
//        RectF rect = new RectF(0, 0, getWidth(), getHeight());
//        path.addRoundRect(rect, radius, radius, Path.Direction.CW);
        path.addCircle(radius, radius, radius, Path.Direction.CW);
        canvas.clipPath(path);
        super.dispatchDraw(canvas);
    }


}