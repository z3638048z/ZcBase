package base.zc.com.project.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import base.zc.com.project.R;
import base.zc.com.project.fragment.MyGoodsFragment;
import base.zc.com.project.fragment.TestFragment;
import base.zc.com.project.util.CartUtil;
import base.zc.com.project.util.NetStringCallback;
import base.zc.com.project.util.NetUtil;
import base.zc.com.project.util.SystemUtil;
import base.zc.com.project.util.UiUtil;

import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends BaseActivity {

    private TextView tv_title;
    private View v_title_line;
    private LinearLayout ll_bottom, ll_right;
    private FragmentManager fragmentManager;
    private MyGoodsFragment myGoodsFragment;
    private Fragment currentFragment;
    private int curIndex;
    private ScaleAnimation scale_one;
    private View animView;
    public TextView tv_menu;
    private View iv_add_goods;
    private MyGoodsFragment pickingDetailFragment;
    private TestFragment statementsDetailFragment;
    private MyGoodsFragment personFragment;
    private MyGoodsFragment refundFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_title = findViewById(R.id.tv_title);
        v_title_line = findViewById(R.id.v_title_line);
        ll_bottom = findViewById(R.id.ll_bottom);
        ll_right = findViewById(R.id.ll_right);
        tv_menu = findViewById(R.id.tv_menu);
        iv_add_goods = findViewById(R.id.iv_add_goods);

        isMainPage = true;
        tv_title.setText("我的商品");
        v_title_line.setVisibility(View.INVISIBLE);
        ll_right.setVisibility(View.INVISIBLE);

//        if(!CartUtil.hasPermission(mContext, NetUtil.ADD_SAVE_GOODS)){
//            tv_menu.setVisibility(View.INVISIBLE);
//        }

        tv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UiUtil.showLoading(mContext);
                NetUtil.get(mContext, NetUtil.GOODS_ADD).execute(new NetStringCallback(mContext) {

                    @Override
                    public void onSidInvalid() {
                        UiUtil.hideLoading(mContext);
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response, boolean isSessionOk) {
                        UiUtil.hideLoading(mContext);
                        try {
                            JSONObject json = new JSONObject(s);
                            int code = json.optInt("code");
                            if (code == 200) {
//                                Intent intent = new Intent(mContext, AddGoodsActivity.class);
//                                intent.putExtra("data", s);
//                                startActivity(intent);
                            }else{
                                UiUtil.showToast(mContext, json.optString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            UiUtil.showToast(mContext, getResources().getString(R.string.net_error));
                        }
                    }

                    @Override
                    public void onErrorWithSafe(Call call, Response response, Exception e) {
                        UiUtil.hideLoading(mContext);
                        UiUtil.showToast(mContext, getResources().getString(R.string.net_error));
                    }
                });
            }
        });

        initFragment();


    }


    //初始化Fragment集合
    private void initFragment() {
        fragmentManager = getSupportFragmentManager();
        myGoodsFragment = new MyGoodsFragment();
        pickingDetailFragment = new MyGoodsFragment();
        statementsDetailFragment = new TestFragment();
        personFragment = new MyGoodsFragment();
        refundFragment = new MyGoodsFragment();
        fragmentManager.beginTransaction().add(R.id.container_fragment_fl, personFragment).hide(personFragment).commitAllowingStateLoss();
        fragmentManager.beginTransaction().add(R.id.container_fragment_fl, statementsDetailFragment).hide(statementsDetailFragment).commitAllowingStateLoss();
        fragmentManager.beginTransaction().add(R.id.container_fragment_fl, refundFragment).hide(refundFragment).commitAllowingStateLoss();
        fragmentManager.beginTransaction().add(R.id.container_fragment_fl, pickingDetailFragment).hide(pickingDetailFragment).commitAllowingStateLoss();
        fragmentManager.beginTransaction().add(R.id.container_fragment_fl, myGoodsFragment).commitAllowingStateLoss();

        iv_add_goods.setVisibility(View.VISIBLE);

        for (int i = 0; i < 5; i++) {
            View view = ll_bottom.findViewWithTag((i + 1) + "");

            switch (i){
                case 0: //我的商品
                    if(!CartUtil.hasPermission(mContext, NetUtil.STORE_GOODS)){
                        view.setVisibility(View.GONE);
                    }
                    break;
                case 1: //备货单
                    if(!CartUtil.hasPermission(mContext, NetUtil.REFUND_INFO)){
                        view.setVisibility(View.GONE);
                    }
                    break;
                case 2: //退款退货
                    if(!CartUtil.hasPermission(mContext, NetUtil.STORE_GOODS)){
                        view.setVisibility(View.GONE);
                    }
                    break;
                case 3: //结算单
                    if(!CartUtil.hasPermission(mContext, NetUtil.BILL_DETAIL)){
                        view.setVisibility(View.GONE);
                    }
                    break;
                case 4: //个人中心
                    if(!CartUtil.hasPermission(mContext, NetUtil.MEMBER_CENTER_INFO)){
                        view.setVisibility(View.GONE);
                    }
                    break;
            }

            final int index = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    curIndex = index;

                    //清空选项
                    for (int j = 0; j < 5; j++) {
                        View view = ll_bottom.findViewWithTag((j + 1) + "");
                        ImageView iv = (ImageView) view.findViewWithTag("iv");
                        TextView tv = (TextView) view.findViewWithTag("tv");
                        tv.setTextColor(getResources().getColor(R.color.colorFontGray3));
                        switch (j) {
                            case 0:
                                iv.setImageResource(R.mipmap.update_rocket);
                                break;
                            case 1:
                                iv.setImageResource(R.mipmap.update_rocket);
                                break;
                            case 2:
                                iv.setImageResource(R.mipmap.update_rocket);
                                break;
                            case 3:
                                iv.setImageResource(R.mipmap.update_rocket);
                                break;
                            case 4:
                                iv.setImageResource(R.mipmap.update_rocket);
                                break;
                        }
                    }

                    tv_menu.setVisibility(View.INVISIBLE);
                    iv_add_goods.setVisibility(View.INVISIBLE);

                    View view = ll_bottom.findViewWithTag((index + 1) + "");
                    ImageView iv = view.findViewWithTag("iv");
                    TextView tv = view.findViewWithTag("tv");
                    tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    switch (index) {
                        case 0:
                            tv_title.setText("我的商品");
                            iv.setImageResource(R.mipmap.update_rocket);
                            fragmentTransaction.hide(currentFragment).show(myGoodsFragment).commitAllowingStateLoss();
                            currentFragment = myGoodsFragment;
                            if(CartUtil.hasPermission(mContext, NetUtil.ADD_SAVE_GOODS)){
                                ll_right.setVisibility(View.VISIBLE);
                                tv_menu.setVisibility(View.VISIBLE);
                            }
                            iv_add_goods.setVisibility(View.VISIBLE);
                            break;
                        case 1:
                            tv_title.setText("备货单");
                            tv_menu.setCompoundDrawables(null, null, null, null);
                            iv.setImageResource(R.mipmap.update_rocket);
                            fragmentTransaction.hide(currentFragment).show(pickingDetailFragment).commitAllowingStateLoss();
                            currentFragment = pickingDetailFragment;
                            break;
                        case 2:
                            tv_title.setText("退款退货");
                            tv_menu.setCompoundDrawables(null, null, null, null);
                            iv.setImageResource(R.mipmap.update_rocket);
                            fragmentTransaction.hide(currentFragment).show(refundFragment).commitAllowingStateLoss();
                            currentFragment = refundFragment;
                            break;
                        case 3:
                            tv_title.setText("结算单");
                            tv_menu.setCompoundDrawables(null, null, null, null);
                            iv.setImageResource(R.mipmap.update_rocket);
                            fragmentTransaction.hide(currentFragment).show(statementsDetailFragment).commitAllowingStateLoss();
                            currentFragment = statementsDetailFragment;
                            break;
                        case 4:
                            tv_title.setText("个人中心");
                            tv_menu.setCompoundDrawables(null, null, null, null);
                            iv.setImageResource(R.mipmap.update_rocket);
                            fragmentTransaction.hide(currentFragment).show(personFragment).commitAllowingStateLoss();
                            currentFragment = personFragment;
                            break;
                    }

                    //标签抖动动画
                    if (scale_one == null) {
                        scale_one = new ScaleAnimation(
                                0.8f, 1.2f, 0.8f, 1.2f,
                                Animation.RELATIVE_TO_SELF, 0.5f,
                                Animation.RELATIVE_TO_SELF, 0.5f);
                        scale_one.setDuration(200);

                        scale_one.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                ScaleAnimation scale_two = new ScaleAnimation(
                                        1.2f, 1f, 1.2f, 1f,
                                        Animation.RELATIVE_TO_SELF, 0.5f,
                                        Animation.RELATIVE_TO_SELF, 0.5f);
                                scale_two.setDuration(200);
                                animView.startAnimation(scale_two);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }
                        });
                    }

                    if (index == 0) {
                        animView = iv;
                    } else {
                        animView = iv;
                    }
                    animView.startAnimation(scale_one);

                    curIndex = index;

                }
            });
        }

        currentFragment = myGoodsFragment;
        if(CartUtil.hasPermission(mContext, NetUtil.ADD_SAVE_GOODS)){
            ll_right.setVisibility(View.VISIBLE);
            tv_menu.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        int tab = getIntent().getIntExtra("tab", -1);
        if(tab > -1){
            curIndex = tab;
            ll_bottom.findViewWithTag((curIndex + 1) + "").performClick();
        }

        getIntent().putExtra("tab", -1);

//        if(!SystemUtil.isNotificationEnabled(mContext)){
//            UiUtil.showButtonMessage(mContext, "提示",
//                    "您的系统通知权限已关闭,是否需要打开系统通知权限以便您接收到来自"+getString(R.string.app_name)+"的信息?",
//                    null,new int[]{1,1}, new String[]{"取消", "确定"},
//                    new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                        }
//                    },
//                    new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent();
//                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                            Uri uri = Uri.fromParts("package", getPackageName(), null);
//                            intent.setData(uri);
//                            startActivity(intent);
//                        }
//                    }
//            );
//        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
}
