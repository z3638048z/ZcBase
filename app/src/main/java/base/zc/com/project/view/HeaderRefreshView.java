package base.zc.com.project.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.callback.IHeaderCallBack;
import base.zc.com.project.R;
import base.zc.com.project.util.DisplayUtil;
import base.zc.com.project.util.LogUtil;
import base.zc.com.project.util.UiUtil;

public class HeaderRefreshView extends FrameLayout implements IHeaderCallBack {

    private ImageView iv_loading;
    private TextView tv_loading;
//    private AnimationDrawable animation;

    public HeaderRefreshView(Context context) {
        super(context);
        initView(context);
    }

    public HeaderRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public HeaderRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        LayoutInflater.from(context).inflate(R.layout.header_refresh_view, this);
        iv_loading = (ImageView) findViewById(R.id.iv_loading);
        tv_loading = (TextView) findViewById(R.id.tv_loading);
//        animation = (AnimationDrawable) iv_loading.getDrawable();
    }

    @Override
    public void onStateNormal() {
        LogUtil.e("状态******", "onStateNormal");
        tv_loading.setText("下拉刷新");
    }

    @Override
    public void onStateReady() {
        LogUtil.e("状态******", "onStateReady");
        tv_loading.setText("松开刷新");
    }

    @Override
    public void onStateRefreshing() {
//        if(!animation.isRunning())
//            animation.start();
        UiUtil.rotateObject(iv_loading);
        tv_loading.setText("正在刷新");
    }

    @Override
    public void onStateFinish(boolean success) {
        if(success){
            tv_loading.setText("刷新成功");
        }else{
            tv_loading.setText("刷新失败");
        }
        if(onHomeHideTopListener != null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onHomeHideTopListener.onShow();
                }
            }, 1000);
        }
    }

    @Override
    public void onHeaderMove(double headerMovePercent, int offsetY, int deltaY) {
//        LogUtil.e("移动信息----" , headerMovePercent + "," + offsetY + "," + deltaY);
        if(headerMovePercent == 0){
//            animation.stop();
            iv_loading.clearAnimation();
        }

//        if(!isRefresh){
//            if(offsetY > getHeight() - (tv_loading.getTop() + ((ViewGroup)tv_loading.getParent()).getTop())){
//                tv_loading.setText("松开刷新");
//                needRefresh = true;
//            }else{
//                tv_loading.setText("下拉刷新");
//            }
//        }

        if(onHomeHideTopListener != null){
            if(headerMovePercent == 0){
                isStartHideAnim = false;
            }
            if(headerMovePercent > 0 && headerMovePercent <= 0.3f && deltaY < 0){
                onHomeHideTopListener.onShow();
            }
            if(headerMovePercent > 0.3f){
                if(!isStartHideAnim){
                    isStartHideAnim = true;
                    onHomeHideTopListener.onHide();
                }
            }
        }

    }

    @Override
    public void setRefreshTime(long lastRefreshTime) {

    }

    @Override
    public void hide() {
    }

    @Override
    public void show() {
    }

    @Override
    public int getHeaderHeight() {
//        return getHeight();
        return DisplayUtil.dip2px(getContext(), 70);
    }

    public interface OnHomeHideTopListener{
        public void onShow();
        public void onHide();
    }

    private OnHomeHideTopListener onHomeHideTopListener;
    private boolean isStartHideAnim = false;

    public void setOnHomeHideTopListener(OnHomeHideTopListener onHomeHideTopListener) {
        this.onHomeHideTopListener = onHomeHideTopListener;
    }
}
