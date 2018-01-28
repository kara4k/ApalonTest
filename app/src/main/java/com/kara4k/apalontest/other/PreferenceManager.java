package com.kara4k.apalontest.other;


import android.content.SharedPreferences;

import com.kara4k.apalontest.presenter.NotesListPresenter;

import javax.inject.Inject;

public class PreferenceManager {

    private static final String IS_LOCKED_KEY = "is_locked";
    private static final String PASSWORD_KEY = "this_is_not_password";
    private static final String SORT_KEY = "sort";
    private static final String DEFAULT_PASS = "pass";

    private SharedPreferences mSharedPrefs;

    @Inject
    public PreferenceManager(SharedPreferences sharedPrefs) {
        mSharedPrefs = sharedPrefs;
    }

    public boolean isLocked() {
        return mSharedPrefs.getBoolean(IS_LOCKED_KEY, false);
    }

    public String getPassword(){
        return mSharedPrefs.getString(PASSWORD_KEY, DEFAULT_PASS);
    }

    public int getSortType(){
        return mSharedPrefs.getInt(SORT_KEY, NotesListPresenter.BY_DATE);
    }

    public void setSortType(int type){
        mSharedPrefs.edit().putInt(SORT_KEY, type).apply();
    }
}
