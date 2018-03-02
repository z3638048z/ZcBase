package base.zc.com.project.view;

import android.content.Context;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class FixedSpeedScroller extends Scroller {

    private int mDuration = 1500; // 默认滑动速度 1500ms
    private int screenWidth;

    public FixedSpeedScroller(Context context) {
        this(context, new AccelerateInterpolator());
    }

    public FixedSpeedScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, (int) (mDuration * (Math.abs(dx) * 1f / screenWidth)));
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, (int) (mDuration * (Math.abs(dx) * 1f / screenWidth)));
    }

    /**
     * set animation time
     *
     * @param time
     */
    public void setmDuration(int time) {
        mDuration = time;
    }

    /**
     * get current animation time
     *
     * @return
     */
    public int getmDuration() {
        return mDuration;
    }
}
