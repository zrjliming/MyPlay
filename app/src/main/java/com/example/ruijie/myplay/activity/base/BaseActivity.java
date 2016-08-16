package com.example.ruijie.myplay.activity.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ruijie.myplay.R;

/**
 *把 系统的actionbar进行取消调
 *  BaseActivity  对activit进行控制
 * 提供方法  可以对 左边的图标进修改
 * 对右边的文字也可以进设置
 *
 * 因为你对toobar 进初始化和封装  你不能再tv.setonClickListener
 * 所以你要 暴露接口 让子类也能对toobar的控件进操作
 *
 * 所以这边只是调用 在父类  子类你随便进行操作
 * 通过回调接口 设置监听
 *  网络请求用okhtpp
 *
 */
public abstract class BaseActivity extends AppCompatActivity {

    private TextView tv_toolbar;
    private TextView tv_toolbar_regiht;
    private ImageView img_toolbar_left1;
    private Toolbar tb;
    private OnLeftImageClickListener onLeftImageClickListener;
    private OnRegihtTextClickListener onRegihtTextClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 竖屏
        setContentView(loadLaout());
        initToobar();



    }

    private void initToobar() {
        tb = (Toolbar) findViewById(R.id.base_toolbar);
//        这个是为了让toolbar 去取消当前工程名字
        tb.setTitle("");
        setSupportActionBar(tb);

        tv_toolbar= (TextView) findViewById(R.id.tv_toolbar_mid);
        tv_toolbar_regiht = (TextView) findViewById(R.id.tv_toolbar_regiht);
        img_toolbar_left1 = (ImageView) findViewById(R.id.img_toolbar_left);

//        点击图片的监听
        img_toolbar_left1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onLeftImageClickListener!=null){
                    onLeftImageClickListener.onLeftTitleClick();
                }
            }
        });

//        点击文字的监听
        tv_toolbar_regiht.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRegihtTextClickListener != null) {
                    onRegihtTextClickListener.onRegihtTitleClick();
                }
            }
        });



    }

    public abstract int loadLaout();


    // 显示左边图标
    protected void showToolBarLeft1Img() {
        img_toolbar_left1.setVisibility(View.VISIBLE);
    }

    // 设置右边的图标
    protected void setToolBarLeftIcon(int iconRes) {
        img_toolbar_left1.setImageResource(iconRes);
    }



    // 设置右边文字的设置的显示
    protected void setToolBarRight(String str) {
        tv_toolbar_regiht.setText(str);
    }

    protected void showToolBarRight() {
        tv_toolbar_regiht.setVisibility(View.VISIBLE);
    }



    //  隐藏标题栏
    protected void hideToolBar() {
        tb.setVisibility(View.GONE);
    }

    // 显示标题栏
    protected void showToolBar() {
        tb.setVisibility(View.VISIBLE);
    }


  /*  // 隐藏返回按钮
    protected void hideToolBarBack() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    // 显示返回按钮
    protected void showToolBarBack() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }*/

    // 标题栏左端标题监听
    public interface OnLeftImageClickListener {
        public void onLeftTitleClick();
    }

    public void setOnOnLeftImageClickListener(OnLeftImageClickListener onLeftImageClickListener){
        this.onLeftImageClickListener=onLeftImageClickListener;
    }

    // 标题栏右端标题监听
    public interface OnRegihtTextClickListener {
        public void onRegihtTitleClick();
    }

    public void setOnRegihtTextClickListener(OnRegihtTextClickListener onRegihtTextClickListener){
       this.onRegihtTextClickListener=onRegihtTextClickListener;
    }




}
