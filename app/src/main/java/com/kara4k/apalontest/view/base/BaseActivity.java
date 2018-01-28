package com.kara4k.apalontest.view.base;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.kara4k.apalontest.R;
import com.kara4k.apalontest.di.AppComponent;
import com.kara4k.apalontest.other.App;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements ViewIF{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        injectDaggerDependencies();
        onViewReady();
    }

    protected int getContentView() {
        return R.layout.fragment_container;
    }

    protected void setFragment(Fragment fragment) {
        try {
            FragmentManager sfm = getSupportFragmentManager();
            int container = R.id.fragment_container;

            Fragment currentFrag = sfm.findFragmentById(container);
            if (currentFrag == null) {
                sfm.beginTransaction().add(container, fragment).commit();
            } else {
                sfm.beginTransaction().replace(container, fragment).commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showError(String message) {
        showToast(message);
    }

    protected void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    protected void activityStart(Intent intent){
        startActivity(intent);
        overridePendingTransition(R.anim.from_in, R.anim.to_in);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.from_out, R.anim.to_out);
    }

    protected AppComponent getAppComponent() {
        return ((App) getApplication()).getAppComponent();
    }

    protected void injectDaggerDependencies() {};

    protected void onViewReady(){};
}
