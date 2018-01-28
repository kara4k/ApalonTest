package com.kara4k.apalontest.view;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;

import com.kara4k.apalontest.R;
import com.kara4k.apalontest.di.DaggerViewComponent;
import com.kara4k.apalontest.di.modules.ViewModule;
import com.kara4k.apalontest.presenter.MainPresenter;
import com.kara4k.apalontest.view.base.DrawerActivity;
import com.kara4k.apalontest.view.settings.SettingsActivity;

import javax.inject.Inject;

public class MainActivity extends DrawerActivity implements MainViewIF {

    private static final int REQUEST_PASSWORD = 1;

    @Inject
    MainPresenter mPresenter;

    @Override
    protected void onViewReady() {
        super.onViewReady();
        mPresenter.onCreate();
    }

    @Override
    protected void injectDaggerDependencies() {
        DaggerViewComponent.builder()
                .appComponent(getAppComponent())
                .viewModule(new ViewModule(this))
                .build().injectMainActivity(this);
    }

    @Override
    protected void onNavItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_item_note:
                mPresenter.onShowNotes();
                break;
            case R.id.navigation_item_settings:
                mPresenter.onShowSettings();
                break;
        }
    }

    @Override
    public void showNotes() {
        setFragment(NotesListFragment.newInstance());
    }

    @Override
    public void showLock() {
        Intent intent = LockActivity.newIntent(this);

        startActivityForResult(intent, REQUEST_PASSWORD);
        overridePendingTransition(R.anim.from_in, R.anim.to_in);
    }

    @Override
    public void showSettings() {
        activityStart(SettingsActivity.newIntent(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PASSWORD && resultCode == Activity.RESULT_OK) {
            mPresenter.onGrantAccess();
        }
    }
}
