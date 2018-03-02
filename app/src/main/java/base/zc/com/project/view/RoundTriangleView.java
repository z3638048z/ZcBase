package base.zc.com.project.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import base.zc.com.project.R;
import base.zc.com.project.util.DisplayUtil;

/**
 * TODO: document your custom view class.
 */
public class RoundTriangleView extends View {

    private Paint solidPaint;
    private Paint strokePaint;
    public RoundTriangleView(Context context) {
        super(context);
        init(null, 0);
    }

    public RoundTriangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public RoundTriangleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        solidPaint = new Paint();
        solidPaint.setColor(0xfffff5e8);
        solidPaint.setAntiAlias(true);
        solidPaint.setStyle(Paint.Style.FILL);
        strokePaint = new Paint();
        strokePaint.setColor(getResources().getColor(R.color.colorPrimary));
        strokePaint.setAntiAlias(true);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(DisplayUtil.dip2px(getContext(),1));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        float gap = strokePaint.getStrokeWidth()/2;
        Path path = new Path();
        int trangle_width = DisplayUtil.dip2px(getContext(),12);
        int trangle_height = DisplayUtil.dip2px(getContext(),8);
        int radius = DisplayUtil.dip2px(getContext(),5);
        path.moveTo((width+trangle_width)/2,height- trangle_height);
        path.lineTo((width)/2,height);
        path.lineTo((width-trangle_width)/2,height- trangle_height);
        path.lineTo(radius+gap,height- trangle_height);
        path.arcTo(new RectF(gap,height-trangle_height-2*radius,2*radius+gap,height-trangle_height),90,90);
        path.lineTo(gap,radius+gap);
        path.arcTo(new RectF(gap,gap,2*radius+gap,2*radius+gap),180,90);
        path.lineTo(width -radius-gap,gap);
        path.arcTo(new RectF(width-2*radius-gap,gap,width-gap,2*radius+gap),270,90);
        path.lineTo(width-gap,height-trangle_height-radius);
        path.arcTo(new RectF(width-2*radius-gap,height-trangle_height-2*radius,width-gap,height-trangle_height),0,90);
        path.lineTo((width+trangle_width)/2,height- trangle_height);
        canvas.drawPath(path,solidPaint);
        canvas.drawPath(path,strokePaint);
    }


}
