package com.kara4k.apalontest.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.kara4k.apalontest.di.modules.AppModule;
import com.kara4k.apalontest.model.DaoSession;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    Context shareContext();

    DaoSession shareDaoSession();

    SharedPreferences sharePreferences();
}