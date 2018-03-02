package base.zc.com.project.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;

import base.zc.com.project.R;
import base.zc.com.project.util.CartUtil;
import base.zc.com.project.util.NetUtil;
import base.zc.com.project.util.ShareUtil;

public class SplashActivity extends BaseActivity {

    //闪屏
    private LinearLayout ll_pass;
    private TextView tv_time;
    private Handler handler = new Handler();
    private Runnable countDownRunnable;
    private int leftCount = 5;
    private ImageView iv_splash;
    private VideoView vv_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ll_pass = (LinearLayout) findViewById(R.id.ll_pass);
        tv_time = (TextView) findViewById(R.id.tv_time);
        iv_splash = (ImageView) findViewById(R.id.iv_splash);
        vv_splash = (VideoView) findViewById(R.id.vv_splash);
        playSplash();

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); //状态栏字体深色
//            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN); //状态栏字体浅色
        }

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

//        //透明状态栏
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window window = getWindow();
//            window.setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }
//        //透明状态栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        //透明导航栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//
//
//        // 部分机型的statusbar会有半透明的黑色背景
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setStatusBarColor(Color.TRANSPARENT);// SDK21
//        }

//        initNet();
//        initPush();
        doSplash();

//        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.splash);
//        NetUtil.uploadBitmap(this, bm, new NetStringCallback(mContext) {
//
//            @Override
//            public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
//                super.upProgress(currentSize, totalSize, progress, networkSpeed);
//
//                LogUtil.e("上传进度", progress * 100 + "%");
//
//            }
//
//            @Override
//            public void onSuccess(String s, Call call, Response response, boolean isSessionOk) {
//                LogUtil.e("上传", s);
//                try {
//                    JSONObject json = new JSONObject(s);
//                    int code = json.optInt("code");
//                    if(code == 200){
//
//                    }else{
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

    }

    private void goMain(){

        Intent intent = getIntent();
        String action = intent.getAction();
        Intent goIntent = new Intent();
        if(TextUtils.isEmpty(CartUtil.getSessionId(mContext))){
            goIntent.setClass(mContext,LoginActivity.class);
        }else{
            goIntent.setClass(mContext,MainActivity.class);
        }


        if(Intent.ACTION_VIEW.equals(action)){
            Uri uri = intent.getData();
            if(uri != null){
//                String name = uri.getQueryParameter("name");
//                String age= uri.getQueryParameter("age");
                goIntent.putExtra("uriData", uri.toString());
                goIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                goIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            }
        }

        startActivityForResult(goIntent, 1000);
        overridePendingTransition(R.anim.fade_in_anim, R.anim.fade_out_anim);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finishWithDefault();
            }
        }, 1000);
    }

    private void doSplash(){
        canBackToFinish = false;
        tv_time.setText(leftCount + "");
        countDownTime();
        ll_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canBackToFinish = true;
                handler.removeCallbacks(countDownRunnable);
                goMain();
            }
        });
    }

    private void countDownTime(){
        handler.postDelayed(countDownRunnable = new Runnable() {
            @Override
            public void run() {

                leftCount--;
                if(leftCount > 0){
                    tv_time.setText(leftCount + "");
                    handler.postDelayed(this, 1000);
                }else{
                    canBackToFinish = true;
                    goMain();
                }
            }
        }, 1000);
    }

    private void playSplash(){
        Bitmap bitmap = BitmapFactory.decodeFile(NetUtil.FILE_DOWNLOAD_PATH + "/splash.png");
        String splash_pic = (String) ShareUtil.get(mContext, "splash_pic");
        if(bitmap != null && !TextUtils.isEmpty(splash_pic)){
            ll_pass.setVisibility(View.VISIBLE);
            iv_splash.setImageBitmap(bitmap);
        }

        if(true){// 以下为测试启动页短视频代码
            return;
        }

        if(Math.random() > 0.5f){
            return;
        }

//        if (mediaPlayer != null) {
//            mediaPlayer.release();
//            mediaPlayer = null;
//        }

//        MediaController mMediaController = new MediaController(this);
//        vv_splash.setMediaController(mMediaController);

        File videoFile = new File(NetUtil.FILE_DOWNLOAD_PATH, "splash.mp4");

        if(videoFile.exists()){
            iv_splash.setVisibility(View.INVISIBLE);
            Uri uri = Uri.parse(videoFile.getAbsolutePath());
            vv_splash.setVideoURI(uri);
            vv_splash.start();

            vv_splash.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(final MediaPlayer mp) {
                    //         mp.setLooping(true);
                    mp.start();// 播放
//                    Toast.makeText(mContext, "本地加载播放", Toast.LENGTH_LONG).show();
                }
            });

            vv_splash.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
//                    Toast.makeText(mContext, "播放完毕", Toast.LENGTH_SHORT).show();

                }
            });
        }else{
            String url1 = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
            String url2 = "http://flashmedia.eastday.com/newdate/news/2016-11/shznews1125-19.mp4";
            NetUtil.downLoadFile(url2, NetUtil.FILE_DOWNLOAD_PATH , "splash.mp4",
                    new NetUtil.DownLoadCallBack(){
                        @Override
                        public void callBack(int current, int total, float percent, File downLoadFile) {
                            if(percent == 1){
//                                UiUtil.showToast(mContext, "下载完毕");
                                Uri uri = Uri.parse(downLoadFile.getAbsolutePath());
                                vv_splash.setVideoURI(uri);
                                vv_splash.start();

                                vv_splash.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    @Override
                                    public void onPrepared(MediaPlayer mp) {
                                        //         mp.setLooping(true);
                                        mp.start();// 播放
//                                    Toast.makeText(mContext, "网络加载播放", Toast.LENGTH_LONG).show();
                                    }
                                });

                                vv_splash.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
//                                        Toast.makeText(mContext, "播放完毕", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        }

                        @Override
                        public void error(String message) {

                        }
                    });
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        finish();
        overridePendingTransition(0, 0);

    }
}
