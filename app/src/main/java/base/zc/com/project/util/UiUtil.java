package base.zc.com.project.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import base.zc.com.project.R;
import base.zc.com.project.activity.BaseActivity;
import base.zc.com.project.receiver.NotificationReceiver;
import base.zc.com.project.view.AutoSplitTextView;
import base.zc.com.project.view.FixedSpeedScroller;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

public class UiUtil {

    public static Handler handler = new Handler();
    public final static int DEFAULT_DURATION = 600;
    private static WindowManager wm;
    private static View uiView;

    /**
     * 获得圆角图片
     *
     * @param context
     * @param sourceBm
     * @param ringWidthInPix
     * @param needRecycleOrigin
     * @return
     */
    public static Bitmap getIconCircleBitmap(Context context, Bitmap sourceBm, float ringWidthInPix, boolean needRecycleOrigin) {
        int width = sourceBm.getWidth();
        int height = sourceBm.getHeight();
        float radius = width > height ? height / 2 : width / 2;
        float centerX = width / 2;
        float centerY = height / 2;
        Bitmap bm_circle = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas_circle = new Canvas(bm_circle);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas_circle.drawCircle(centerX, centerY, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas_circle.drawBitmap(sourceBm, ringWidthInPix / 2, ringWidthInPix / 2, paint);
        if (needRecycleOrigin)
//            sourceBm.recycle();
        if (ringWidthInPix > 0) {
            paint.setXfermode(null);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(ringWidthInPix);
            paint.setColor(context.getResources().getColor(R.color.colorPrimary));
            canvas_circle.drawCircle(centerX, centerY, radius, paint);
        }
        Matrix matrix = new Matrix();
        float temp = context.getResources().getDisplayMetrics().density * 40;
        matrix.setScale(temp / (radius * 2), temp / (radius * 2));
        Bitmap returnBm = Bitmap.createBitmap(bm_circle, 0, 0, width, height, matrix, true);
//        bm_circle.recycle();
        return returnBm;
    }

    public static Bitmap getIconCircleBitmap(Context context, Bitmap sourceBm, float ringWidthInPix) {
        return getIconCircleBitmap(context, sourceBm, ringWidthInPix, false);
    }

    private static Toast toast;
    private static View layout;
    private static AutoSplitTextView tv_msg;

    /**
     * 在中间提示信息
     *
     * @param context
     * @param message
     */
    public static void showToastCenter(Context context, String message) {
        if (TextUtils.isEmpty(message) || context == null)
            return;
        if(toast != null){
            toast.cancel();
        }
//        if (toast == null) {
            toast = new Toast(context);
            layout = LayoutInflater.from(context).inflate(R.layout.toast, null);
            tv_msg = (AutoSplitTextView) layout.findViewById(R.id.tv_msg);
//        }
        tv_msg.setAutoSplitText(message);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    /**
     * 在底部显示提示信息
     *
     * @param context
     * @param message
     */
    public static void showToast(Context context, String message) {
//		if(TextUtils.isEmpty(message))
//			return;
//		if(toast == null){
//			toast = new Toast(context);
//			layout = LayoutInflater.from(context).inflate(R.layout.toast, null);
//			tv_msg = (AutoSplitTextView) layout.findViewById(R.id.tv_msg);
//		}
//		tv_msg.setAutoSplitText(message);
//		toast.setGravity(Gravity.BOTTOM, 0, (int) context.getResources().getDimension(R.dimen.toast_y_offset));
//		toast.setDuration(Toast.LENGTH_LONG);
//		toast.setView(layout);
//		toast.show();

        if (TextUtils.isEmpty(message) || context == null)
            return;
        if(toast != null){
            toast.cancel();
        }
//        if (toast == null) {
            toast = new Toast(context);
            layout = LayoutInflater.from(context).inflate(R.layout.toast, null);
            tv_msg = (AutoSplitTextView) layout.findViewById(R.id.tv_msg);
//        }
        tv_msg.setAutoSplitText(message);
        toast.setGravity(Gravity.BOTTOM, 0, (int) context.getResources().getDimension(R.dimen.toast_y_offset));
//		toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        if (message.length() <= 20) {
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        } else {
            long showTime = (long) (2000 + (((message.length() - 20) / 5) + 1) * 0.5 * 1000);
            showToast1(toast, showTime);
        }

    }

    /**
     * 在底部显示提示信息,显示时间短
     *
     * @param context
     * @param message
     */
    public static void showToastShort(Context context, String message) {
        if (TextUtils.isEmpty(message) || context == null)
            return;
        if (toast == null) {
            toast = new Toast(context);
            layout = LayoutInflater.from(context).inflate(R.layout.toast, null);
            tv_msg = (AutoSplitTextView) layout.findViewById(R.id.tv_msg);
        }
        tv_msg.setAutoSplitText(message);
        toast.setGravity(Gravity.BOTTOM, 0, (int) context.getResources().getDimension(R.dimen.toast_y_offset));
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public static void showToast(Context context, String message, long duration) {
        if (TextUtils.isEmpty(message) || context == null)
            return;
        if (toast == null) {
            toast = new Toast(context);
            layout = LayoutInflater.from(context).inflate(R.layout.toast, null);
            tv_msg = (AutoSplitTextView) layout.findViewById(R.id.tv_msg);
        }
        tv_msg.setAutoSplitText(message);
        toast.setGravity(Gravity.BOTTOM, 0, (int) context.getResources().getDimension(R.dimen.toast_y_offset));
//		toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        showToast1(toast, duration);
//		try {
//			Field field = toast.getClass().getDeclaredField("mTN");
//			field.setAccessible(true);
//			Object obj = field.get(toast);
//			Method showMethod = obj.getClass().getDeclaredMethod("show");
//			showMethod.invoke(obj);
//		} catch (Exception e) {
//		    e.printStackTrace();
//		}
//		tv_msg.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					Field field = toast.getClass().getDeclaredField("mTN");
//					field.setAccessible(true);
//					Object obj = field.get(toast);
//					Method hideMethod  = obj.getClass().getDeclaredMethod("hide");
//					hideMethod .invoke(obj);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}, duration);
    }

    public static void showToast1(final Toast toast, long duration) {
        final long SHORT = 2000;
        final long LONG = 3500;
        final long ONE_SECOND = 1000;
        final long d = duration <= SHORT ? SHORT : duration > LONG ? duration : LONG;
        new CountDownTimer(Math.max(d, duration), ONE_SECOND) {

            @Override
            public void onTick(long millisUntilFinished) {
                toast.show();
            }

            @Override
            public void onFinish() {
                toast.cancel();
            }
        }.start();
    }

//	private static LinearLayout ll_message;
//	private static TextView tv_title, tv_message;
//	private static LinearLayout ll_button;
//	private static FrameLayout fl_custom;
//	private static ScaleAnimation scaleShow, scaleHide;

    /**
     * 显示自定义view
     *
     * @param context
     * @param customView
     */
    public static void showButtonMessage(Context context, View customView) {
        showButtonMessage(context, null, null, customView, null, null, new View.OnClickListener[]{});
    }

    public static void showButtonMessage(Context context, String title, String message, View customView, String[] buttonNames, final View.OnClickListener... listeners) {
        showButtonMessage(context, title, message, customView, null, buttonNames, listeners);
    }

    /**
     * 显示自定义view框
     *
     * @param context
     * @param customView 自定义view
     */
    public static void showCustomMessage(Context context, View customView) {
        showCustomMessage(context, customView, null);
    }

    /**
     * 显示自定义view框
     *
     * @param context
     * @param customView 自定义view
     * @param animView   做动画的view
     */
    public static void showCustomMessage(Context context, View customView, View animView) {
        try {
            if (context instanceof Activity) {
                Activity mActivity = (Activity) context;
                final FrameLayout parent = (FrameLayout) mActivity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
                if (customView != null) {
                    customView.setId(R.id.custom_view);
                    View previousCustomView = parent.findViewById(R.id.custom_view);
                    if (previousCustomView != null)
                        parent.removeView(previousCustomView);
                    parent.addView(customView);
                    ScaleAnimation scaleShow = new ScaleAnimation(
                            0f, 1f, 0f, 1f,
                            Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, 0.5f);
                    scaleShow.setInterpolator(new OvershootInterpolator());
                    scaleShow.setDuration(400);

                    if (animView != null) {
                        animView.startAnimation(scaleShow);
                    } else {
                        customView.startAnimation(scaleShow);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭自定义布局
     *
     * @param context
     */
    public static boolean closeCustomMessage(Context context) {
        try {
            if (context instanceof Activity) {
                Activity mActivity = (Activity) context;
                final FrameLayout parent = (FrameLayout) mActivity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
                final View customView = parent.findViewById(R.id.custom_view);
                if (customView != null) {
                    ScaleAnimation scaleHide = new ScaleAnimation(
                            1f, 0f, 1f, 0f,
                            Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, 0.5f);
                    scaleHide.setInterpolator(new AnticipateInterpolator());
                    scaleHide.setDuration(300);
                    scaleHide.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            parent.removeView(customView);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    //				animView.startAnimation(scaleHide);
                    parent.removeView(customView);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;

    }

    public static void showButtonMessage(Context context, String title, String message,
                                         View customView, int[] colorIndexs, String[] buttonNames, final View.OnClickListener... listeners) {
        showButtonMessage(true, context, title, message, customView, colorIndexs, buttonNames, listeners);
    }

    /**
     * 显示对话框
     *
     * @param context     上下文
     * @param title       标题(空不显示)
     * @param message     提示信息(空不显示)
     * @param customView  自定义view(没有传null)
     * @param colorIndexs 文本颜色为选中颜色的位置数组
     * @param buttonNames 按钮显示文本
     * @param listeners   对应按钮点击监听
     */
    public static void showButtonMessage(final boolean needClickToClose, Context context, String title, String message,
                                         View customView, int[] colorIndexs, String[] buttonNames, final View.OnClickListener... listeners) {
        try {
            if (context instanceof Activity) {
                Activity mActivity = (Activity) context;
                final FrameLayout parent = (FrameLayout) mActivity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
                final ScaleAnimation scaleShow = new ScaleAnimation(
                        0f, 1f, 0f, 1f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                scaleShow.setInterpolator(new OvershootInterpolator());
                scaleShow.setDuration(300);
                View child = parent.findViewById(R.id.ll_message);
                if (child != null) {
//                    parent.removeView(child);
                    return;
                }
                final LinearLayout ll_message = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.message, null);
                TextView tv_title = (TextView) ll_message.findViewById(R.id.tv_title);
                TextView tv_message = (TextView) ll_message.findViewById(R.id.tv_msg);
                LinearLayout ll_button = (LinearLayout) ll_message.findViewById(R.id.ll_button);
                FrameLayout fl_custom = (FrameLayout) ll_message.findViewById(R.id.fl_custom);

                if (parent.getChildAt(parent.getChildCount() - 1).getId() == R.id.custom_view) {
                    parent.addView(ll_message, parent.getChildCount() - 1);
                } else {
                    parent.addView(ll_message);
                }
                ll_button.removeAllViews();
                fl_custom.removeAllViews();
                fl_custom.setVisibility(View.GONE);

                if (customView != null) {
                    fl_custom.addView(customView);
                    tv_message.setVisibility(View.GONE);
                    fl_custom.setVisibility(View.VISIBLE);
                }
                if (TextUtils.isEmpty(title)) {
                    tv_title.setVisibility(View.GONE);
                }
                if (TextUtils.isEmpty(message)) {
                    tv_message.setVisibility(View.GONE);
                }

                tv_title.setText(title);
                tv_message.setText(message);
                for (int i = 0; i < buttonNames.length; i++) {
                    Button btn = new Button(context);
                    btn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, DisplayUtil.dip2px(context, 40), 1));
                    btn.setBackgroundColor(Color.TRANSPARENT);
                    btn.setTextSize(15);
                    btn.setText(buttonNames[i]);
                    if (listeners.length > i) {
                        if (colorIndexs == null) {
                            if (i == 0) {
                                btn.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                            } else {
                                btn.setTextColor(context.getResources().getColor(R.color.colorFontGray));
                            }
                        } else {
                            btn.setTextColor(context.getResources().getColor(R.color.colorFontGray));
                            for (int j = 0; j < colorIndexs.length; j++) {
                                if (i == colorIndexs[j]) {
                                    btn.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                                }
                            }
                        }
                        final int index = i;
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                listeners[index].onClick(v);
                                ScaleAnimation scaleHide = new ScaleAnimation(
                                        1f, 0f, 1f, 0f,
                                        Animation.RELATIVE_TO_SELF, 0.5f,
                                        Animation.RELATIVE_TO_SELF, 0.5f);
                                scaleHide.setInterpolator(new AnticipateInterpolator());
                                scaleHide.setDuration(300);
                                scaleHide.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {
                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        parent.removeView(ll_message);
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {
                                    }
                                });
                                //							ll_message.getChildAt(0).startAnimation(scaleHide);
                                if(needClickToClose)
                                    parent.removeView(ll_message);
                            }
                        });
                    }
                    ll_button.addView(btn);
                    if (i < buttonNames.length - 1) {
                        View view = new View(context);
                        view.setBackgroundColor(context.getResources().getColor(R.color.colorDivider));
                        ll_button.addView(view, new LinearLayout.LayoutParams(DisplayUtil.dip2px(context, 1), LinearLayout.LayoutParams.MATCH_PARENT));
                    }
                }

                ll_message.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (ll_message.getWidth() > 0) {
                            ll_message.getChildAt(0).startAnimation(scaleShow);
                            if (VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                ll_message.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                        }
                    }
                });

            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean isButtonMessageShow(Context context){

        if (context instanceof Activity) {
            Activity mActivity = (Activity) context;
            final FrameLayout parent = (FrameLayout) mActivity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
            final ScaleAnimation scaleShow = new ScaleAnimation(
                    0f, 1f, 0f, 1f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            scaleShow.setInterpolator(new OvershootInterpolator());
            scaleShow.setDuration(300);
            View child = parent.findViewById(R.id.ll_message);
            if (child != null) {
                return true;
            }
        }

        return false;
    }

    /**
     * 显示底部框
     *
     * @param context
     * @param view          显示的布局
     * @param heightPercent 相对于屏幕高度的比例
     */
    public static void showBottomMessage(final Context context, View view, float heightPercent) {
        try {
            if (context instanceof Activity) {
                Activity mActivity = (Activity) context;
                UiUtil.hideSoftInput(context, mActivity.getCurrentFocus());
                final FrameLayout parent = (FrameLayout) mActivity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
                RelativeLayout rl_bottom = new RelativeLayout(context);
                rl_bottom.setLayoutParams(new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                rl_bottom.setId(R.id.bottom_message);
                rl_bottom.setGravity(Gravity.BOTTOM);
                view.setClickable(true);
                parent.addView(rl_bottom);

                View shadowView = new View(context);
                rl_bottom.addView(shadowView, 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                shadowView.setBackgroundColor(0xaa000000);
                shadowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeBottomMessage(context);
                    }
                });

                int height = context.getResources().getDisplayMetrics().heightPixels;

                RelativeLayout.LayoutParams layoutParams;
                if (heightPercent == 0) {
                    layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                } else {
                    layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) (height * heightPercent));
                }
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                rl_bottom.addView(view, layoutParams);
                rl_bottom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeBottomMessage(context);
                    }
                });
                TranslateAnimation bottomShow = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f);
                bottomShow.setDuration(200);
                view.startAnimation(bottomShow);

                AlphaAnimation alpha = new AlphaAnimation(0, 1);
                alpha.setDuration(300);
                shadowView.startAnimation(alpha);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示底部框
     *
     * @param context
     * @param view    显示的布局
     */
    public static void showBottomMessage(Context context, View view) {
        showBottomMessage(context, view, 0f);
    }

    /**
     * 关闭底部弹出框
     *
     * @param context
     */
    public static boolean closeBottomMessage(Context context) {
        try {
            if (context instanceof Activity) {
                Activity mActivity = (Activity) context;
                final FrameLayout parent = (FrameLayout) mActivity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
                final RelativeLayout rl_bottom = (RelativeLayout) parent.findViewById(R.id.bottom_message);
                if (rl_bottom != null) {
                    final View view = rl_bottom.getChildAt(1);
                    TranslateAnimation bottomHide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                            Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f);
                    bottomHide.setDuration(200);
                    bottomHide.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            rl_bottom.removeView(view);
                            parent.removeView(rl_bottom);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    //				view.startAnimation(bottomHide);
                    rl_bottom.removeView(view);
                    parent.removeView(rl_bottom);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean closeButtonMessage(Context context) {
        try {
            if (context instanceof Activity) {
                Activity mActivity = (Activity) context;
                final FrameLayout parent =  mActivity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
                final LinearLayout ll_message = parent.findViewById(R.id.ll_message);
                if (ll_message != null) {
                    final View view = ll_message.getChildAt(1);
                    TranslateAnimation bottomHide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                            Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f);
                    bottomHide.setDuration(200);
                    bottomHide.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            ll_message.removeView(view);
                            parent.removeView(ll_message);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    //				view.startAnimation(bottomHide);
                    ll_message.removeView(view);
                    parent.removeView(ll_message);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void showLoading(Object context) {
        String tip = "";
        if (context instanceof AppCompatActivity || context instanceof android.app.Fragment) {
            tip = ((AppCompatActivity) context).getString(R.string.loading_tip);
        } else if (context instanceof android.app.Fragment) {
            tip = ((AppCompatActivity) context).getString(R.string.loading_tip);
        }
        showLoading(context, tip, true);
    }

    public static void showLoading(Object context, String message) {
        showLoading(context, message, true);
    }

    public static void showLoadingNoMiss(Object context) {
        String tip = "";
        if (context instanceof AppCompatActivity || context instanceof android.app.Fragment) {
            tip = ((AppCompatActivity) context).getString(R.string.loading_tip);
        } else if (context instanceof android.app.Fragment) {
            tip = ((AppCompatActivity) context).getString(R.string.loading_tip);
        }
        showLoading(context, tip, false);
    }

    /**
     * 显示加载框
     *
     * @param context    只传Activity 或 Fragment
     * @param message    显示信息
     * @param canDismiss
     */
    public static void showLoading(Object context, String message, boolean canDismiss) {
        if (context == null) {
            return;
        }
        try {
            Activity mActivity;
            if (context instanceof Activity) {
                mActivity = (Activity) context;
            } else if (context instanceof Fragment) {
                mActivity = ((Fragment) context).getActivity();
            } else {
                return;
            }
            if (mActivity == null) {
                return;
            }
            final FrameLayout parent = (FrameLayout) mActivity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
            String tagName = context.getClass().getSimpleName();
            View child = parent.findViewById(R.id.loading);

            if (child == null) {
                LinearLayout ll_loading_parent = new LinearLayout(mActivity);
                ll_loading_parent.setGravity(Gravity.CENTER);
                ll_loading_parent.setId(R.id.loading);
                ll_loading_parent.setTag(canDismiss);
                ll_loading_parent.setTag(R.id.loading, tagName);
                RelativeLayout ll_loading_view = (RelativeLayout) LayoutInflater.from(mActivity).inflate(R.layout.loading_new_by_text, null);
                TextView tv_loading = (TextView) ll_loading_view.findViewById(R.id.tv_loading);
                ImageView iv_loading = (ImageView) ll_loading_view.findViewById(R.id.iv_loading);
                ll_loading_parent.addView(ll_loading_view, new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                if (parent.getChildAt(parent.getChildCount() - 1).getId() == R.id.custom_view) {
                    parent.addView(ll_loading_parent, parent.getChildCount() - 1, lp);
                } else {
                    parent.addView(ll_loading_parent, lp);
                }

//                int randomIndex = (int) (Math.random() * loadingAnimRess.length);
//                iv_loading.setImageResource(loadingAnimRes[randomIndex]);
//                ((AnimationDrawable)iv_loading.getDrawable()).start();

//				SceneAnimation scene = new SceneAnimation(iv_loading, loadingAnimRess[randomIndex], 30);

                rotateObject(iv_loading);

                if (!TextUtils.isEmpty(message)) {
                    tv_loading.setText(message);
                }

            } else {//正在加载中
                child.setTag(R.id.loading, tagName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean isNoMissLoadingShow(Object context) {
        try {
            Activity mActivity;
            if (context instanceof Activity) {
                mActivity = (Activity) context;
            } else if (context instanceof Fragment) {
                mActivity = ((Fragment) context).getActivity();
            } else {
                return false;
            }
            final FrameLayout parent = (FrameLayout) mActivity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
            View child = parent.findViewById(R.id.loading);
            if (child != null && (boolean) child.getTag() == false) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean hideLoading(Object context) {
        return hideLoading(context, false);
    }

    /**
     * 隐藏加载框
     *
     * @param context
     * @return 加载框存在状态下隐藏，返回true，不存在加载框 或者显示但禁止隐藏时调用此方法，返回false
     */
    public static boolean hideLoading(Object context, boolean isForceHide) {
        if (context == null) {
            return false;
        }
        try {
            Activity mActivity;
            if (context instanceof Activity) {
                mActivity = (Activity) context;
            } else if (context instanceof Fragment) {
                mActivity = ((Fragment) context).getActivity();
            } else {
                return false;
            }
            if (mActivity == null) {
                return false;
            }
            final FrameLayout parent = (FrameLayout) mActivity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
            final View child = parent.findViewById(R.id.loading);
            if (child != null) {
                if (!isForceHide) {
                    String tagName = (String) child.getTag(R.id.loading);
                    //当前隐藏发起者不是调用者
                    if (!tagName.equals(context.getClass().getSimpleName())) {
                        return false;
                    }
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        parent.removeView(child);
                    }
                });

                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 图片灰化
     *
     * @param bitmap
     * @return
     */
    public static final Bitmap gray(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap faceIconGreyBitmap = Bitmap
                .createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(faceIconGreyBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(
                colorMatrix);
        paint.setColorFilter(colorMatrixFilter);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return faceIconGreyBitmap;
    }

    /**
     * 显示键盘
     *
     * @param context
     * @param target
     */
    public static void showSoftInput(Context context, View target) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(target, 0);
    }

    /**
     * 隐藏键盘
     *
     * @param context
     */
    public static void hideSoftInput(Context context) {
        hideSoftInput(context, context instanceof Activity ? ((Activity) context).getCurrentFocus() : null);
    }

    /**
     * 隐藏键盘
     *
     * @param context
     * @param target
     */
    public static void hideSoftInput(Context context, View target) {
        if(context != null && target != null){
            InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(target.getWindowToken(), 0);
        }
    }

    /**
     * 显示对话框2
     *
     * @param context     上下文
     * @param title       标题(空不显示)
     * @param message     提示信息(空不显示)
     * @param customView  自定义view(没有传null)
     * @param buttonNames 按钮显示文本
     * @param listeners   对应按钮点击监听
     */
    public static void showButtonMessageSetting(Context context, String title, String message,
                                                View customView, String[] buttonNames, final View.OnClickListener... listeners) {
        if (context instanceof Activity) {
            Activity mActivity = (Activity) context;
            final FrameLayout parent = (FrameLayout) mActivity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
            View child = parent.findViewById(R.id.ll_message);
            if (child != null) {
                parent.removeView(child);
            }
            final LinearLayout ll_message = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.message, null);
            TextView tv_title = (TextView) ll_message.findViewById(R.id.tv_title);
            TextView tv_message = (TextView) ll_message.findViewById(R.id.tv_msg);
            LinearLayout ll_button = (LinearLayout) ll_message.findViewById(R.id.ll_button);
            FrameLayout fl_custom = (FrameLayout) ll_message.findViewById(R.id.fl_custom);
            parent.addView(ll_message);
            ll_button.removeAllViews();
            fl_custom.removeAllViews();
            fl_custom.setVisibility(View.GONE);
            if (customView != null) {
                fl_custom.addView(customView);
                tv_message.setVisibility(View.GONE);
                fl_custom.setVisibility(View.VISIBLE);
            }
            if (TextUtils.isEmpty(title)) {
                tv_title.setVisibility(View.GONE);
            }
            if (TextUtils.isEmpty(message)) {
                tv_message.setVisibility(View.GONE);
            }
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tv_title.getLayoutParams();
            layoutParams.height = DisplayUtil.dip2px(context, 63);
            layoutParams.width = DisplayUtil.dip2px(context, 120);
            tv_title.setGravity(Gravity.CENTER);
            tv_title.setLayoutParams(layoutParams);
            tv_title.setText(title);
//            tv_title.setTextSize(DisplayUtil.sp2px(context, 16));
            tv_message.setText(message);
            for (int i = 0; i < buttonNames.length; i++) {
                Button btn = new Button(context);
                btn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, DisplayUtil.dip2px(context, 40), 1));
                btn.setBackgroundColor(Color.TRANSPARENT);
                btn.setTextSize(15);
                btn.setText(buttonNames[i]);
                if (listeners.length > i) {
                    if (i == 0) {
                        btn.setTextColor(context.getResources().getColor(R.color.colorFontGray));
                    } else {
                        btn.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    }
                    final int index = i;
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listeners[index].onClick(v);
                            ScaleAnimation scaleHide = new ScaleAnimation(
                                    1f, 0f, 1f, 0f,
                                    Animation.RELATIVE_TO_SELF, 0.5f,
                                    Animation.RELATIVE_TO_SELF, 0.5f);
                            scaleHide.setInterpolator(new AnticipateInterpolator());
                            scaleHide.setDuration(300);
                            scaleHide.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    parent.removeView(ll_message);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {
                                }
                            });
//							ll_message.getChildAt(0).startAnimation(scaleHide);
                            parent.removeView(ll_message);
                        }
                    });
                }
                ll_button.addView(btn);
                if (i < buttonNames.length - 1) {
                    View view = new View(context);
                    view.setLayoutParams(new LinearLayout.LayoutParams(DisplayUtil.dip2px(context, 1), DisplayUtil.dip2px(context, 40)));
                    view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorDivider));
                    ll_button.addView(view);
                }
            }

            final ScaleAnimation scaleShow = new ScaleAnimation(
                    0f, 1f, 0f, 1f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            scaleShow.setInterpolator(new OvershootInterpolator());
            scaleShow.setDuration(300);

            ll_message.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (ll_message.getWidth() > 0) {
                        ll_message.getChildAt(0).startAnimation(scaleShow);
                        if (VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            ll_message.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    }
                }
            });
        }
    }

    /**
     * 显示底部框
     *
     * @param context
     * @param view          显示的布局
     * @param isAdapt       是否自适应高度
     * @param heightPercent 相对于屏幕高度的比例
     */
    public static void showBottomMessage(final Context context, View view, boolean isAdapt, float heightPercent) {
        if (context instanceof Activity) {
            Activity mActivity = (Activity) context;
            final FrameLayout parent = (FrameLayout) mActivity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
            LinearLayout ll_bottom = new LinearLayout(context);
            ll_bottom.setLayoutParams(new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            ll_bottom.setId(R.id.bottom_message);
            ll_bottom.setBackgroundColor(0xaa000000);
            ll_bottom.setGravity(Gravity.BOTTOM);
            ll_bottom.setClickable(true);
            ll_bottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeBottomMessage(context);
                }
            });
            parent.addView(ll_bottom);
            int height = context.getResources().getDisplayMetrics().heightPixels;
            LinearLayout.LayoutParams layoutParams = null;
            if (isAdapt) {
                layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            } else {
                layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (height * heightPercent));
            }
            ll_bottom.addView(view, layoutParams);
            TranslateAnimation bottomShow = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f);
            bottomShow.setDuration(200);
            view.startAnimation(bottomShow);
        }
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusTop(Context context) {
        //状态栏以及标题栏总高度
        int contentTop = ((Activity) context).getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        if (contentTop == 0) {
            Rect rect = new Rect();
            ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);//状态栏高度
            contentTop = rect.top;
        }
        return contentTop;
    }

    /**
     * 获取当前页面高度(不包括状态栏)
     *
     * @param context
     * @param view
     * @return
     */
    public static int getTopInWindow(Context context, View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        int statusTop = getStatusTop(context);
        return location[1] == 0 ? 0 : location[1] - statusTop;
    }

    /**
     * 自定义样式布局id
     */
    private static int[] customStyleIds =
            {
                    0,
                    0,
                    R.layout.custom_push,
                    0,
                    0

            };

    public static void showCustomNotification(Context context, String title, String message) {
        showCustomNotification(context, 0, new Intent(), title, message);
    }

    public static void showCustomNotification(Context context, int customStyleId, String title, String message) {
        showCustomNotification(context, customStyleId, new Intent(), title, message);
    }

    public static int getRandomId(){
        return (int) (Math.random() * 10000000);
    }

    /**
     * 显示通知
     *
     * @param context
     * @param customStyleId 自定义样式编号(自己定义, 布局中显示icon的imageview ID统一设置为iv_icon)
     * @param intent        点击后跳转页面
     */
    public static void showCustomNotification(Context context, int customStyleId, Intent intent, String title, String message) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        int notifyId = 0;
        if (VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarNotification[] actives = mNotificationManager.getActiveNotifications();
            notifyId = actives.length;
        }

        notifyId = getRandomId();
        while(NotificationReceiver.messageIdLst.contains(notifyId)){
            notifyId = getRandomId();
        }
        NotificationReceiver.messageIdLst.add(notifyId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notifyId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews mRemoteView = new RemoteViews(context.getPackageName(), customStyleIds[customStyleId]);
        if (customStyleId > 0) {
            mRemoteView.setImageViewResource(R.id.iv_icon, R.mipmap.app_icon);
            mRemoteView.setTextViewText(R.id.tv_title, title);
            mRemoteView.setTextViewText(R.id.tv_message, message);
            mRemoteView.setTextViewText(R.id.tv_time, new SimpleDateFormat("HH:mm").format(new Date()));
            mBuilder.setCustomContentView(mRemoteView);
        } else {
            mBuilder.setContentTitle(title);
            mBuilder.setContentText(message);
        }
        mBuilder.setContentIntent(pendingIntent) //设置通知栏点击意图
                //  .setNumber(number) //设置通知集合的数量
//				.setTicker("测试通知来啦") //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                //  .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_SOUND
                        | Notification.DEFAULT_VIBRATE
                        | Notification.DEFAULT_LIGHTS)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
//				.setDefaults(Notification.DEFAULT_ALL)  //Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(R.mipmap.app_icon);//设置通知小ICON

        Notification notification = mBuilder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL
                | Notification.FLAG_SHOW_LIGHTS;
        ; //点击消失 呼吸灯闪烁
//		notify.flags = Notification.FLAG_ONGOING_EVENT;  //正在进行的，点击不消失

        mNotificationManager.notify(notifyId, notification);

    }

    /**
     * 设置ViewPager的滑动时间
     *
     * @param context
     * @param viewpager      ViewPager控件
     * @param DurationSwitch 滑动延时
     */
    public static void controlViewPagerSpeed(Context context, ViewPager viewpager, int DurationSwitch) {
        try {
            Field mField;

            mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);

            FixedSpeedScroller mScroller = new FixedSpeedScroller(context,
                    new DecelerateInterpolator());
            mScroller.setmDuration(DurationSwitch);
            mField.set(viewpager, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 程序是否在后台
     *
     * @param context
     * @return
     */
    public static boolean isApplicationInBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName topActivity = taskList.get(0).topActivity;
            if (topActivity != null && !topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取最顶层activity名称
     *
     * @return
     */
    public static String getTopActivitySimpleName(Context context) {
        if(context == null){
            return "";
        }
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String className = mActivityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        if (className.indexOf(".") != -1) {
            className = className.substring(className.lastIndexOf(".") + 1);
        }
        return className;
    }

    public static void rotateObject(View view) {
        rotateObject(view, 800);
    }

    public static void rotateObject(View view, long duration) {
        if (view != null) {
            view.clearAnimation();
            RotateAnimation rotateAnimation = new RotateAnimation(
                    0f, 360f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(duration);
            rotateAnimation.setInterpolator(new LinearInterpolator());
            rotateAnimation.setRepeatCount(Animation.INFINITE);
            view.startAnimation(rotateAnimation);
        }
    }

    public static Activity getTopActivity(Application application) {

        try {
            String actName = topAct(application);// com.example.calledjar.MainActivity
            Class clz = application.getClass().forName(
                    "android.app.ActivityThread");
            Method meth = clz.getMethod("currentActivityThread");
            Object currentActivityThread = meth.invoke(null);
            Field f = clz.getDeclaredField("mActivities");
            f.setAccessible(true);
            ArrayMap obj = (ArrayMap) f.get(currentActivityThread);
            for (Object key : obj.keySet()) {
                Object activityRecord = obj.get(key);
                Field actField = activityRecord.getClass().getDeclaredField("activity");
                actField.setAccessible(true);
                Object activity = actField.get(activityRecord);
                System.out.println(activity);
                Activity act1 = (Activity) activity;
                if (act1 != null &&actName.indexOf(act1.getClass().getSimpleName()) > 0) {
                    return (Activity) activity;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String topAct(Application application) {

        ActivityManager am = (ActivityManager) application.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(30).get(0).topActivity;
        return cn.getClassName();

    }

    /**
     *  设置DrawerLayout滑动范围
     * @param activity
     * @param drawerLayout
     * @param displayWidthPercentage
     */
    public static void setDrawerEdgeSize(Activity activity,
                                         DrawerLayout drawerLayout, float displayWidthPercentage) {
        if (activity == null || drawerLayout == null)
            return;
        try {
            // find ViewDragHelper and set it accessible
            Field leftDraggerField = drawerLayout.getClass().getDeclaredField(
                    "mLeftDragger");
            leftDraggerField.setAccessible(true);
            ViewDragHelper leftDragger = (ViewDragHelper) leftDraggerField
                    .get(drawerLayout);
            // find edgesize and set is accessible
            Field edgeSizeField = leftDragger.getClass().getDeclaredField(
                    "mEdgeSize");
            edgeSizeField.setAccessible(true);
            int edgeSize = edgeSizeField.getInt(leftDragger);
            // set new edgesize
            // Point displaySize = new Point();
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            edgeSizeField.setInt(leftDragger, Math.max(edgeSize,
                    (int) (dm.widthPixels * displayWidthPercentage)));
        } catch (NoSuchFieldException e) {
            // ignore
        } catch (IllegalArgumentException e) {
            // ignore
        } catch (IllegalAccessException e) {
            // ignore
        }
    }

    /**
     * 跳转相机
     * @param context
     * @param file 指定保存文件位置
     */
    public static void goToCamera(Context context, File file){
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        if(file != null){
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        }
        ((BaseActivity) context).startActivityForResult(intent, 1000);
    }

    public static Uri goToCamera(BaseActivity context){
//		if(PermissionCheckerUtil.isLackPermsissionOperation(context, new String[]{Manifest.permission.CAMERA}, new String[]{"拍照"})){
//			return null;
//		}

//		//跳转系统相册
//		Intent intent = new Intent(Intent.ACTION_PICK,
//				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//		((BaseActivity)context).startActivityForResult(intent, 1000);

//		Intent getImageByCamera = new Intent("Android.media.action.IMAGE_CAPTURE");
        long mImageTime = System.currentTimeMillis();
        long dateSeconds = mImageTime / 1000;
        Intent getImageByCamera = new Intent();
        getImageByCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, System.currentTimeMillis() + ".jpg");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
//		values.put(MediaStore.Images.ImageColumns.TITLE, "测试图片1");
//		values.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, "测试图片111");
        values.put(MediaStore.Images.ImageColumns.DATE_TAKEN, mImageTime);
        values.put(MediaStore.Images.ImageColumns.DATE_ADDED, dateSeconds);
        values.put(MediaStore.Images.ImageColumns.DATE_MODIFIED, dateSeconds);
        values.put(MediaStore.Images.ImageColumns.MIME_TYPE, "image/png");
        Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//		getImageByCamera.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);

//		File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/floruit/cxy/picture");
//		if(!file.exists()){
//			file.mkdirs();
//		}

//		boolean sdCardExist = Environment.getExternalStorageState()
//				.equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
//		UiUtil.showToast(context, "是否可用~~：" + sdCardExist);

//		File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/dcim/Camera");
//		File file = new File("/stystm/dcim/Camera");
//		goToCamera(context, new File(file, "aaabbb.jpg"));
//		goToCamera(context, null);
        ((BaseActivity) context).startActivityForResult(getImageByCamera, 1000);
        return uri;
    }

    /**
     * 获取指定URI的绝对路径
     * @param context
     * @param contentUri
     * @return
     */
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor != null){
            if(cursor.moveToFirst()){;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                res = cursor.getString(column_index);
            }
            cursor.close();
        }else{
            res = contentUri.getPath();
        }
        return res;
    }


    /**
     * 删除上一张图片(按时间排)
     * @param context
     */
    public static void deleteLastPic(Context context){
        String columns[] = new String[] { MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.PICASA_ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.TITLE,
                MediaStore.Images.Media.SIZE, MediaStore.Images.Media.BUCKET_DISPLAY_NAME };
        Cursor cur = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null,
                MediaStore.Images.Media.DATE_MODIFIED + " desc");
        if (cur.moveToFirst()) {
            int idIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int displayNameIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            String id = cur.getString(idIndex);
            String displayName = cur.getString(displayNameIndex);
            LogUtil.e("操作", "删除的名字为：" + id + "," + displayName);
            int row = context.getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media._ID + "=?", new String[]{id});
            LogUtil.e("操作", "删除的行数为：" + row);
        }
        cur.close();
    }

    public static void showLayoutInRoot(Activity activity, View view){
        FrameLayout parent = (FrameLayout) activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        RelativeLayout rl_show = new RelativeLayout(activity);
        rl_show.setTag("floruit_show");
        rl_show.addView(view, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        parent.addView(rl_show, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public static void removeLayoutInRoot(Activity activity){
        FrameLayout parent = (FrameLayout) activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        RelativeLayout rl_show = (RelativeLayout) parent.findViewWithTag("floruit_show");
        if(rl_show != null){
            rl_show.removeAllViews();
            parent.removeView(rl_show);
        }
    }

    public static void setSoftKeyboardListener(Context context, final OnSoftKeboardListener onSoftKeboardListener){
        if(context instanceof Activity){
            setSoftKeyboardListener(((Activity) context), onSoftKeboardListener);
        }
    }

    /**
     * 设置软键盘监听
     * @param activity
     * @param onSoftKeboardListener
     */
    public static void setSoftKeyboardListener(Activity activity, final OnSoftKeboardListener onSoftKeboardListener){
        if(activity != null && onSoftKeboardListener != null){
            View rootView = ((FrameLayout) activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT)).getChildAt(0);
            rootView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    if(oldBottom != bottom){
                        onSoftKeboardListener.before();
                        if(oldBottom > bottom){ //弹出
                            onSoftKeboardListener.open(oldBottom - bottom);
                        }else{ //关闭
                            onSoftKeboardListener.close(bottom - oldBottom);
                        }
                        onSoftKeboardListener.after();
                    }
                }
            });
        }
    }

    public interface OnSoftKeboardListener{
        void open(int softKeyboardHeight);
        void close(int softKeyboardHeightBeforeClose);
        void before();
        void after();
    }

}
