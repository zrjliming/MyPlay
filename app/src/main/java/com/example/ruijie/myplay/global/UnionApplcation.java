package com.example.ruijie.myplay.global;

import android.app.Application;
import android.content.Context;

/**
 * Created by ruijie on 2016/8/16.
 * 自己可以在全局定义一个观察者，对用户的登录 注册等进行观察
 *
 * 观察者
 *
 */
public class UnionApplcation  extends Application {
    public static Context applicationContext;
    private static UnionApplcation instance;
    @Override
    public void onCreate() {
        super.onCreate();
//        初始化 全局错误的处理UncaughtException
        CrashHandler.getInstance().init(getApplicationContext());
        applicationContext = this;
        instance = this;
/*        // 初始化okhttpclient
        OkHttpClient client = OkHttpUtils.getInstance().getOkHttpClient();
        client.setConnectTimeout(10000, TimeUnit.MILLISECONDS);*/
    }


    public static UnionApplcation getInstance() {
        return instance;
    }



}
