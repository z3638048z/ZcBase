package base.zc.com.project.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import base.zc.com.project.R;

/**
 * Created by Darren on 2018/1/16.
 */

public class WithdrawProgressView extends View {
    public static final int  STATE_FINISHED=0;
    public static final int  STATE_HANDLING=1;
    private int state = STATE_FINISHED;
    private Paint paint;
    public WithdrawProgressView(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    public WithdrawProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        if(state == STATE_FINISHED){
            canvas.drawRect(new RectF(0,0,getWidth(),getHeight()),paint);
        }else{
            canvas.drawRect(new RectF(0,0,getWidth(),getHeight()/2),paint);
            paint.setColor(ContextCompat.getColor(getContext(), R.color.colorLightGray4));
            canvas.drawRect(new RectF(0,getHeight()/2,getWidth(),getHeight()),paint);
        }
    }

    public void setState(int state) {
        this.state = state;
        invalidate();
    }
}
