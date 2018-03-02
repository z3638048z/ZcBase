package base.zc.com.project.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import base.zc.com.project.R;
import base.zc.com.project.util.DisplayUtil;

import java.util.Calendar;
import java.util.Date;

public class CalendarDayView extends RelativeLayout {

    public CalendarDayView(Context context) {
        super(context);
        initView();
    }

    public CalendarDayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CalendarDayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(changed){
            createView();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }

    private CalendarMonthView monthView;
    private CalendarChooseBgView chooseBgView;
    private CalendarBottomPointView bottomPointView;
    private TextView tv_day;
    public int year, month, day;


    public void createView(){
        if(chooseBgView.getParent() == null){
            addView(chooseBgView);
            addView(tv_day);
            addView(bottomPointView);
        }
        chooseBgView.setLayoutParams(new LayoutParams(getWidth(), getWidth()));
        tv_day.setLayoutParams(new LayoutParams(getWidth(), getWidth()));
        bottomPointView.setLayoutParams(new LayoutParams(getWidth(), getWidth()));
    }

    private void initView(){
        setClipChildren(false);
        chooseBgView = new CalendarChooseBgView(getContext());
        chooseBgView.setVisibility(View.INVISIBLE);
        bottomPointView = new CalendarBottomPointView(getContext());
        bottomPointView.setVisibility(View.INVISIBLE);
        tv_day = new TextView(getContext());
        tv_day.setTextSize(16);
        tv_day.setTextColor(getContext().getResources().getColor(R.color.colorFontGray4));
        tv_day.setGravity(Gravity.CENTER);
        tv_day.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDayView chooseCalendarDayView = monthView.getChooseCalendarDayView();
                if(chooseCalendarDayView != null)
                    chooseCalendarDayView.setUnChoose();
                AlphaAnimation alpha = new AlphaAnimation(0.5f, 1f);
                alpha.setDuration(300);
                chooseBgView.startAnimation(alpha);
                setChoose();
                monthView.choose_year = year;
                monthView.choose_month = month;
                monthView.choose_day = day;
                monthView.calendar.set(year, month, day);
                monthView.setTodayBottomPoint();
                if(monthView.mListener != null){
                    monthView.mListener.onDateChoose(monthView, year, month + 1, day, monthView.calendar.getTime());
                }
            }
        });
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if(!enabled){
            setUnChoose();
            tv_day.setEnabled(false);
            tv_day.setTextColor(0xffe4e4e4);
        }else{
            tv_day.setEnabled(true);
            if(isChoose){
                setChoose();
            }else{
                setUnChoose();
            }
        }
    }

    public void setText(String str){
        tv_day.setText(str);
    }

    private boolean isChoose = false;
    private boolean isShowBottomPoint = false;

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose() {
        isChoose = true;
        tv_day.setTextColor(Color.WHITE);
        chooseBgView.setVisibility(View.VISIBLE);
        bottomPointView.setVisibility(View.INVISIBLE);
    }

    public void setShowBottomPoint(boolean showBottomPoint) {
        isShowBottomPoint = showBottomPoint;
        if(isShowBottomPoint){
            if(isChoose){
                isShowBottomPoint = false;
                bottomPointView.setVisibility(View.INVISIBLE);
            }else{
                bottomPointView.setVisibility(View.VISIBLE);
            }
        }else{
            bottomPointView.setVisibility(View.INVISIBLE);
        }
    }

    public void setUnChoose() {
        isChoose = false;
        tv_day.setTextColor(getContext().getResources().getColor(R.color.colorFontGray4));
        chooseBgView.setVisibility(View.INVISIBLE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int yearCur = calendar.get(Calendar.YEAR);
        int monthCur = calendar.get(Calendar.MONTH);
        int dayCur = calendar.get(Calendar.DAY_OF_MONTH);
        if(year == yearCur && month == monthCur && day == dayCur){
            bottomPointView.setVisibility(View.VISIBLE);
        }
    }

    public void setMonthView(CalendarMonthView monthView) {
        this.monthView = monthView;
    }

    public void setTime(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH);
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    class CalendarChooseBgView extends View {

        public CalendarChooseBgView(Context context) {
            super(context);
            initView();
        }

        public CalendarChooseBgView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            initView();
        }

        public CalendarChooseBgView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            initView();
        }

        private void initView(){
            setLayerType(LAYER_TYPE_SOFTWARE, paint);
            paint = new Paint();
            paint.setAntiAlias(true);
            int pColor = getContext().getResources().getColor(R.color.colorPrimary);
            paint.setColor(pColor);
            int argb = Color.argb(85, Color.red(pColor), Color.green(pColor), Color.blue(pColor));
            paint.setShadowLayer(5, 2, 2, argb);
        }

        private Paint paint;

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawCircle(getWidth() / 2, getHeight() / 2, DisplayUtil.dip2px(getContext(), 17), paint);

        }
    }

    class CalendarBottomPointView extends View {

        public CalendarBottomPointView(Context context) {
            super(context);
            initView();
        }

        public CalendarBottomPointView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            initView();
        }

        public CalendarBottomPointView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            initView();
        }

        private void initView(){
            setLayerType(LAYER_TYPE_SOFTWARE, paint);
            paint = new Paint();
            paint.setAntiAlias(true);
            int pColor = getContext().getResources().getColor(R.color.colorPrimary);
            paint.setColor(pColor);
            int argb = Color.argb(85, Color.red(pColor), Color.green(pColor), Color.blue(pColor));
            paint.setShadowLayer(5, 2, 2, argb);
        }

        private Paint paint;

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawCircle(getWidth() / 2, getHeight() / 2 + tv_day.getTextSize(), DisplayUtil.dip2px(getContext(), 3), paint);

        }
    }

}
