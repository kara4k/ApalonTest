package com.kara4k.apalontest.other;


import android.app.Application;

import com.kara4k.apalontest.di.AppComponent;
import com.kara4k.apalontest.di.DaggerAppComponent;
import com.kara4k.apalontest.di.modules.AppModule;


public class App extends Application {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initComponents();
    }

    private void initComponents() {
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(getApplicationContext()))
                .build();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
