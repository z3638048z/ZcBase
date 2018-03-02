package base.zc.com.project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    private ImageView iv_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv_main = findViewById(R.id.iv_main);
        Glide.with(this).load("https://img.cuixianyuan.com/template/home/05723576862242212.jpg").into(iv_main);
    }
}
