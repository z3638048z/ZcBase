package base.zc.com.project.fragment;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import org.greenrobot.eventbus.EventBus;

import java.security.MessageDigest;

import base.zc.com.project.R;
import base.zc.com.project.glide4.GlideApp;
import base.zc.com.project.util.DisplayUtil;
import base.zc.com.project.util.UiUtil;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MyGoodsFragment extends Fragment implements View.OnClickListener {

    private ImageView iv_main;
    private Button btn_click;
    private View view;
    private ImageView iv_back;


    //    private ImageView iv_main;
//    private Button btn_click;
    public MyGoodsFragment() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_goods, container, false);

        initView(view);
        return view;
    }

    private void initView(View view) {
        iv_main = (ImageView) view.findViewById(R.id.iv_main);
        btn_click = (Button) view.findViewById(R.id.btn_click);
        btn_click.setOnClickListener(this);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
//        iv_back.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                if(iv_back.getWidth() > 0){
//                    iv_back.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                    Bitmap bm = Bitmap.createBitmap(iv_back.getWidth(), iv_back.getHeight(), Bitmap.Config.ARGB_8888);
//                    Canvas canvas = new Canvas(bm);
//                    Paint paint = new Paint();
//                    paint.setAntiAlias(true);
//                    paint.setColor(Color.BLACK);
//                    int radius = DisplayUtil.dip2px(getActivity(), 20);
//                    canvas.drawRoundRect(new RectF(0, 0, bm.getWidth(), bm.getHeight()), radius, radius, paint);
//                    iv_back.setBackground(new BitmapDrawable(getResources(), bm));
//                }
//            }
//        });

        GlideApp.with(getActivity()).load(R.mipmap.pic_none)
                .roundBound(100, 20, getResources().getColor(R.color.colorPrimary))
                .dontAnimate()
                .into(iv_back);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_click:
                GlideApp.with(this).load("https://img.cuixianyuan.com/template/home/05746840375990271.jpg")
                        .roundBound(100, 20, Color.RED)
                        .into(iv_main);
                break;
        }
    }
}
