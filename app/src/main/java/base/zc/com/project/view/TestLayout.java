package base.zc.com.project.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class TestLayout extends ViewGroup {
    public TestLayout(Context context) {
        super(context);
    }

    public TestLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);

        int widthPadding = getPaddingLeft() + getPaddingRight();

        int widthSize = 0;

        if(widthSpecMode == MeasureSpec.EXACTLY){
            widthSize = widthSpecSize;
        }else {

        }


//        View child = getChildAt(0);
//        LayoutParams lp = child.getLayoutParams();
//        resolveSize(lp.width, lp.)
//        child.measure();

    }

    private int getMeasuredLength(int length, boolean isWidth) {
        int specMode = MeasureSpec.getMode(length);
        int specSize = MeasureSpec.getSize(length);
        int size;
        int padding = isWidth ? getPaddingLeft() + getPaddingRight()
                : getPaddingTop() + getPaddingBottom();
        if (specMode == MeasureSpec.EXACTLY) {
            size = specSize;
        } else {
            size = padding;
            if (specMode == MeasureSpec.AT_MOST) {
                size = Math.min(size, specSize);
            }
        }
        return size;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layout(l, t, r, b);
    }

}
