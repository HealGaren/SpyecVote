package kr.spyec.spyecvote;

import android.app.Application;

//이동진, 싱글톤설정 애플리케이션클래스, 6월7일
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DataManager.init(this);
    }
}
