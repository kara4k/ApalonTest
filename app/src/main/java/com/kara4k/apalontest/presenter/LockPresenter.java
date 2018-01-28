package com.kara4k.apalontest.presenter;


import android.text.Editable;

import com.kara4k.apalontest.other.PreferenceManager;
import com.kara4k.apalontest.presenter.base.BasePresenter;
import com.kara4k.apalontest.view.LockViewIF;

import javax.inject.Inject;

public class LockPresenter extends BasePresenter<LockViewIF> {

    private PreferenceManager mPrefs;

    @Inject
    public LockPresenter(PreferenceManager preferenceManager) {
        mPrefs = preferenceManager;
    }

    public void onSubmit(Editable text) {
            String pass = text.toString();
            String realPassword = mPrefs.getPassword();

            if (pass.equals(realPassword)) {
                getView().grantAccess();
            } else {
                getView().showError("Incorrect password");
            }
        }
}
