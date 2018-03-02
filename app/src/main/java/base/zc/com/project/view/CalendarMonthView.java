package base.zc.com.project.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import base.zc.com.project.R;
import base.zc.com.project.util.DisplayUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class CalendarMonthView extends RelativeLayout {

    public CalendarMonthView(Context context) {
        super(context);
        resetMonthData();
    }

    public CalendarMonthView(Context context, Date date, int choose_year, int choose_month, int choose_day) {
        super(context);
        if(calendar == null){
            calendar = Calendar.getInstance();
        }
        calendar.setTime(date);
        this.choose_year = choose_year;
        this.choose_month = choose_month;
        this.choose_day = choose_day;
        resetMonthData();
    }

    public CalendarMonthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        resetMonthData();
    }

    public CalendarMonthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        resetMonthData();
    }

    private int sideWidth;
    private int row = 4;
    private int column = 7;
    private LinearLayout ll_main;
    public Calendar calendar = Calendar.getInstance();
    public Calendar calendarToday = Calendar.getInstance();
    private String[] weekNames = new String[]{"日", "一", "二", "三", "四", "五", "六"};
    private boolean isUseWeekBar = false;
    public int choose_year;
    public int choose_month;
    public int choose_day;
    private CalendarDayView cdv_today;

    public void updateChooseDate(int choose_year, int choose_month, int choose_day){
        this.choose_year = choose_year;
        this.choose_month = choose_month;
        this.choose_day = choose_day;
        CalendarDayView chooseCalendarDayView = getChooseCalendarDayView();
        if(chooseCalendarDayView != null
                && (chooseCalendarDayView.year != choose_year
                || chooseCalendarDayView.month != choose_month
                || chooseCalendarDayView.day != choose_day)){
            chooseCalendarDayView.setUnChoose();
        }
    }

    public void resetMonthData(){

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        calendarToday.setTime(new Date());
        int yearToday = calendarToday.get(Calendar.YEAR);
        int monthToday = calendarToday.get(Calendar.MONTH);
        int dayToday = calendarToday.get(Calendar.DAY_OF_MONTH);

        int previousDayCount = 0;
        Calendar calFirstDayOfWeek = Calendar.getInstance();
        calFirstDayOfWeek.setTime(calendar.getTime());
        calFirstDayOfWeek.set(calFirstDayOfWeek.get(Calendar.YEAR), calFirstDayOfWeek.get(Calendar.MONTH), 1);
        int week = calFirstDayOfWeek.get(Calendar.DAY_OF_WEEK);
        switch (week){
            case Calendar.SUNDAY:
                previousDayCount = 0;
                break;
            case Calendar.MONDAY:
                previousDayCount = 1;
                break;
            case Calendar.TUESDAY:
                previousDayCount = 2;
                break;
            case Calendar.WEDNESDAY:
                previousDayCount = 3;
                break;
            case Calendar.THURSDAY:
                previousDayCount = 4;
                break;
            case Calendar.FRIDAY:
                previousDayCount = 5;
                break;
            case Calendar.SATURDAY:
                previousDayCount = 6;
                break;
        }

        int actualMaximum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        if((actualMaximum + previousDayCount) % column == 0){
            row = (actualMaximum + previousDayCount) / column;
        }else{
            row = (actualMaximum + previousDayCount) / column + 1;
        }

        int nextDayCount = row * column - actualMaximum - previousDayCount;
        Calendar calLastDayOfWeek = Calendar.getInstance();
        calLastDayOfWeek.setTime(calendar.getTime());
        calLastDayOfWeek.set(calLastDayOfWeek.get(Calendar.YEAR), calLastDayOfWeek.get(Calendar.MONTH), actualMaximum);

//        setBackgroundColor(Color.WHITE);
        sideWidth = (int) (getResources().getDisplayMetrics().widthPixels / column * 0.8f);
        if(ll_main == null){
            ll_main = new LinearLayout(getContext());
            ll_main.setOrientation(LinearLayout.VERTICAL);
            addView(ll_main, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        }else{
            ll_main.removeAllViews();
        }
        if(isUseWeekBar){
            addWeekBar();
        }

        if(previousDayCount > 0) {
            LinearLayout ll_row = new LinearLayout(getContext());
            ll_row.setOrientation(LinearLayout.HORIZONTAL);
            ll_main.addView(ll_row, new LayoutParams(LayoutParams.MATCH_PARENT, sideWidth));
            for (int j = 1; j <= previousDayCount; j++) {
                CalendarDayView cdv = new CalendarDayView(getContext());
                cdv.setMonthView(this);
                ll_row.addView(cdv, 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, sideWidth, 1));
                calFirstDayOfWeek.add(Calendar.DAY_OF_MONTH, -1);
                cdv.setText(calFirstDayOfWeek.get(Calendar.DAY_OF_MONTH) + "");
                cdv.setBackgroundColor(Color.WHITE);
                cdv.setEnabled(false);
            }
        }

        for (int i = 0; i < actualMaximum; i++) {
            LinearLayout ll_row = (LinearLayout) ll_main.getChildAt(ll_main.getChildCount() - 1);
            if (ll_row == null || ll_row.getChildCount() == column) {
                ll_row = new LinearLayout(getContext());
                ll_row.setOrientation(LinearLayout.HORIZONTAL);
                ll_main.addView(ll_row, new LayoutParams(LayoutParams.MATCH_PARENT, sideWidth));
            }
            int days = i + 1;
            CalendarDayView cdv = new CalendarDayView(getContext());
            cdv.setMonthView(this);
            ll_row.addView(cdv, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, sideWidth, 1));
            cdv.setText(days < 10 ? "0" + days : days + "");
            cdv.setBackgroundColor(Color.WHITE);
            if (days == choose_day && month == choose_month && year == choose_year) {
                cdv.setChoose();
            }
            if(days == dayToday && month == monthToday && year == yearToday){
                cdv_today = cdv;
                cdv.setShowBottomPoint(true);
            }
            Calendar eachCalendar = Calendar.getInstance();
            eachCalendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), days);
            cdv.setTime(eachCalendar.getTime());
        }

        if(nextDayCount > 0) {
            LinearLayout ll_row = (LinearLayout) ll_main.getChildAt(ll_main.getChildCount() - 1);
            for (int j = 0; j < nextDayCount; j++) {
                CalendarDayView cdv = new CalendarDayView(getContext());
                cdv.setMonthView(this);
                ll_row.addView(cdv, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, sideWidth, 1));
                calLastDayOfWeek.add(Calendar.DAY_OF_MONTH, 1);
                int days = calLastDayOfWeek.get(Calendar.DAY_OF_MONTH);
                cdv.setText(days < 10 ? "0" + days : days + "");
                cdv.setBackgroundColor(Color.WHITE);
                cdv.setEnabled(false);
            }
        }

        if(week == Calendar.SUNDAY){ //1号是周日 添加上一行
            LinearLayout ll_row = new LinearLayout(getContext());
            ll_row.setOrientation(LinearLayout.HORIZONTAL);
            ll_main.addView(ll_row, 0, new LayoutParams(LayoutParams.MATCH_PARENT, sideWidth));
            for (int j = 0; j < 7; j++) {
                CalendarDayView cdv = new CalendarDayView(getContext());
                cdv.setMonthView(this);
                ll_row.addView(cdv, 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, sideWidth, 1));
                calFirstDayOfWeek.add(Calendar.DAY_OF_MONTH, -1);
                cdv.setText(calFirstDayOfWeek.get(Calendar.DAY_OF_MONTH) + "");
                cdv.setBackgroundColor(Color.WHITE);
                cdv.setEnabled(false);
            }
        }

        if(ll_main.getChildCount() == 5){
            LinearLayout ll_row = new LinearLayout(getContext());
            ll_row.setOrientation(LinearLayout.HORIZONTAL);
            ll_main.addView(ll_row, new LayoutParams(LayoutParams.MATCH_PARENT, sideWidth));
            for (int j = 0; j < 7; j++) {
                CalendarDayView cdv = new CalendarDayView(getContext());
                cdv.setMonthView(this);
                ll_row.addView(cdv, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, sideWidth, 1));
                calLastDayOfWeek.add(Calendar.DAY_OF_MONTH, 1);
                int days = calLastDayOfWeek.get(Calendar.DAY_OF_MONTH);
                cdv.setText(days < 10 ? "0" + days : days + "");
                cdv.setBackgroundColor(Color.WHITE);
                cdv.setEnabled(false);
            }
        }


        //因为父控件vp, 底部间距通过多加一个view的方式
        LinearLayout ll_row = new LinearLayout(getContext());
        ll_row.setOrientation(LinearLayout.HORIZONTAL);
        ll_row.setBackgroundColor(Color.WHITE);
        ll_main.addView(ll_row, new LayoutParams(LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(getContext(), 20)));

    }

    public void setTodayBottomPoint(){
        if(cdv_today != null){
            cdv_today.setShowBottomPoint(true);
        }
    }

    private void addWeekBar(){
        LinearLayout ll_row = new LinearLayout(getContext());
        ll_row.setOrientation(LinearLayout.HORIZONTAL);
        ll_main.addView(ll_row, new LayoutParams(LayoutParams.MATCH_PARENT, sideWidth));
        for (int j = 0; j < column; j++) {
            TextView tv_week = new TextView(getContext());
            tv_week.setText(weekNames[j]);
            tv_week.setTextSize(16);
            tv_week.setGravity(Gravity.CENTER);
            tv_week.setBackgroundColor(Color.WHITE);
            if(j == 0 || j == weekNames.length - 1){
                tv_week.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
            ll_row.addView(tv_week, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, sideWidth, 1));
        }
    }

    public void showNextMonth(){
        calendar.add(Calendar.MONTH, 1);
        resetMonthData();
        if(mListener != null){
            mListener.onDatePreviousOrNext(this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.getTime());
        }
    }

    public void showPreviuosMonth(){
        calendar.add(Calendar.MONTH, -1);
        resetMonthData();
        if(mListener != null){
            mListener.onDatePreviousOrNext(this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.getTime());
        }
    }

    public String getCalendarDate(String format){
        return new SimpleDateFormat(format).format(calendar.getTime());
    }

    public CalendarDayView getChooseCalendarDayView(){
        int startRow = isUseWeekBar ? 1 : 0;
        for (int i = startRow; i < ll_main.getChildCount(); i++) {
            LinearLayout ll_row = (LinearLayout) ll_main.getChildAt(i);
            for (int j = 0; j < column; j++) {
                CalendarDayView child = (CalendarDayView) ll_row.getChildAt(j);
                if(child != null && child.isChoose()){
                    return child;
                }
            }
        }
        return null;
    }

    public void setmListener(onDateChooseListener mListener) {
        this. mListener = mListener;
    }

    public onDateChooseListener mListener;


    public interface onDateChooseListener{
        public void onDateChoose(CalendarMonthView cmv, int year, int month, int day, Date date);
        public void onDatePreviousOrNext(CalendarMonthView cmv, int year, int month, int day, Date date);
    }

}
