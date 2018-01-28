package com.kara4k.apalontest.presenter;


import com.kara4k.apalontest.other.PreferenceManager;
import com.kara4k.apalontest.presenter.base.BasePresenter;
import com.kara4k.apalontest.view.MainViewIF;

import javax.inject.Inject;

public class MainPresenter extends BasePresenter<MainViewIF> {

    private PreferenceManager mPrefs;

    @Inject
    public MainPresenter(PreferenceManager prefs) {
        mPrefs = prefs;
    }

    public void onCreate() {
        if (mPrefs.isLocked()) {
            getView().showLock();
        } else {
            getView().showNotes();
        }
    }

    public void onGrantAccess() {
        getView().showNotes();
    }

    public void onShowNotes() {
        getView().showNotes();
    }

    public void onShowSettings() {
        getView().showSettings();
    }
}
