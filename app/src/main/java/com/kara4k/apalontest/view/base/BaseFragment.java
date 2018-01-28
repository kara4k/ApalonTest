package com.kara4k.apalontest.view.base;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kara4k.apalontest.R;
import com.kara4k.apalontest.di.AppComponent;
import com.kara4k.apalontest.other.App;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment implements ViewIF {

    protected abstract int getLayout();

    protected abstract int getMenuRes();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        injectDaggerDependencies();
        onFragmentCreate();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        ButterKnife.bind(this, view);
        onViewReady();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(getMenuRes(), menu);
        onMenuInflated(menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void showError(String message) {
        showToast(message);
    }

    protected void onFragmentCreate() {}

    protected void injectDaggerDependencies() {}

    protected void onViewReady() {}

    protected void onMenuInflated(Menu menu) {}

    protected void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    protected void activityStart(Intent intent){
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.from_in, R.anim.to_in);
    }

    protected void showConfirmDialog(String title, String text, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(text)
                .setPositiveButton(android.R.string.ok, okListener)
                .setNegativeButton(android.R.string.cancel, null)
                .create().show();
    }

    protected AppComponent getAppComponent() {
        return ((App) getActivity().getApplication()).getAppComponent();
    }

}

