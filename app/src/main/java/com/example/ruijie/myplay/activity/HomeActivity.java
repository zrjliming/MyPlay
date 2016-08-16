package com.example.ruijie.myplay.activity;

import android.os.Bundle;
import android.os.PersistableBundle;

import com.example.ruijie.myplay.R;
import com.example.ruijie.myplay.activity.base.BaseActivity;

/**
 * 先加载   super.onCreate(savedInstanceState, persistentState);
 * 和加载tobar
 * 然后就去loadLaout()
 */
public class HomeActivity extends BaseActivity {


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        initView();
    }

    private void initView() {

    }

    @Override
    public int loadLaout() {

        return R.layout.activity_home;
    }
}
