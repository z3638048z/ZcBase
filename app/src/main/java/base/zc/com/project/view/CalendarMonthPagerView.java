package base.zc.com.project.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.Date;

public class CalendarMonthPagerView extends ViewPager {

    public CalendarMonthPagerView(Context context) {
        super(context);
        initView();
    }

    public CalendarMonthPagerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private int choosePosition;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = 0;
        //下面遍历所有child的高度
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height) //采用最大的view的高度。
                height = h;
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
                MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int index = getCurrentItem();
//        int height = 0;
//        View v = (View) getAdapter().instantiateItem(this, index);
//        if (v != null) {
//            v.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//            height = v.getMeasuredHeight();
//        }
//        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    }

    private void initView(){

        mAdapter = new MonthPagerAdapter(getContext());
        setAdapter(mAdapter);
        setCurrentItem(choosePosition = mAdapter.getCount() / 2);

        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                CalendarMonthView cmv = (CalendarMonthView) findViewById(position);

                if(mListener != null){

                    mListener.onDatePreviousOrNext(
                            cmv,
                            cmv.calendar.get(Calendar.YEAR),
                            cmv.calendar.get(Calendar.MONTH) + 1,
                            cmv.calendar.get(Calendar.DAY_OF_MONTH),
                            cmv.calendar.getTime());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private MonthPagerAdapter mAdapter;
    public int choose_year;
    public int choose_month;
    public int choose_day;

    class MonthPagerAdapter extends PagerAdapter {

        Context context;

        public MonthPagerAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            final Calendar calendar = Calendar.getInstance();
            if(choose_year == 0){
                calendar.setTime(new Date());
                choose_year = calendar.get(Calendar.YEAR);
                choose_month = calendar.get(Calendar.MONTH);
                choose_day = calendar.get(Calendar.DAY_OF_MONTH);
            }else{
                calendar.set(choose_year, choose_month, choose_day);
            }

            calendar.add(Calendar.MONTH, position - choosePosition);

            CalendarMonthView calendarMonthView = new CalendarMonthView(getContext(), calendar.getTime(), choose_year, choose_month, choose_day);
            calendarMonthView.setId(position);
            calendarMonthView.setmListener(new CalendarMonthView.onDateChooseListener() {
                @Override
                public void onDateChoose(CalendarMonthView cmv, int year, int month, int day, Date date) {
                    choose_year = year;
                    choose_month = month - 1;
                    choose_day = day;
                    choosePosition = position;
                    if(mListener != null){
                        mListener.onDateChoose(cmv, year, month, day, date);
                    }

                    for (int i = 0; i < getChildCount(); i++) {
                        CalendarMonthView cmv_each = (CalendarMonthView) getChildAt(i);
                        cmv_each.updateChooseDate(choose_year, choose_month, choose_day);
                    }

                }

                @Override
                public void onDatePreviousOrNext(CalendarMonthView cmv, int year, int month, int day, Date date) {
                }
            });

            container.addView(calendarMonthView);
            return calendarMonthView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    private CalendarMonthView.onDateChooseListener mListener;

    public void setmListener(CalendarMonthView.onDateChooseListener mListener) {
        this.mListener = mListener;
    }
}
