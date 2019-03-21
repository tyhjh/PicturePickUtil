package com.yorhp.picturepickutil;

import com.squareup.leakcanary.LeakCanary;

/**
 * @author Tyhj
 * @date 2019/3/21
 */

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }
}
