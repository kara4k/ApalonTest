package com.kara4k.apalontest.presenter.base;


import com.kara4k.apalontest.view.base.ViewIF;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class BasePresenter<V extends ViewIF> {

    @Inject
    V mView;

    protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public BasePresenter() {
    }

    protected V getView() {
        return mView;
    }

    public void onDestroy(){
        mCompositeDisposable.dispose();
    }
}
