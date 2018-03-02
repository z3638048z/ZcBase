package base.zc.com.project.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager {

    private boolean canPage = true;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(!canPage){
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void setCanPage(boolean canPage) {
        this.canPage = canPage;
    }

}
