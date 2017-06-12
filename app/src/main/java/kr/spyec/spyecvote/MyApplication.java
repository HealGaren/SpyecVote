package kr.spyec.spyecvote;

import android.app.Application;

/**
 * Created by 최예찬 on 2016-08-22.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DataManager.init(this);
    }
}
