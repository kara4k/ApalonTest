package com.kara4k.apalontest.view;


import android.content.Context;
import android.content.Intent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import com.kara4k.apalontest.R;
import com.kara4k.apalontest.di.DaggerViewComponent;
import com.kara4k.apalontest.di.modules.ViewModule;
import com.kara4k.apalontest.presenter.LockPresenter;
import com.kara4k.apalontest.view.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class LockActivity extends BaseActivity implements LockViewIF {

    @BindView(R.id.pass_edit_text)
    EditText mPassEditText;
    @BindView(R.id.submit_btn)
    Button mSubmitBtn;

    @Inject
    LockPresenter mPresenter;

    @Override
    protected int getContentView() {
        return R.layout.activity_lock;
    }

    @Override
    protected void injectDaggerDependencies() {
        DaggerViewComponent.builder()
                .appComponent(getAppComponent())
                .viewModule(new ViewModule(this))
                .build().injectLockActivity(this);
    }

    @Override
    protected void onViewReady() {
        super.onViewReady();
        mPassEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mPresenter.onSubmit(mPassEditText.getText());
            }
            return false;
        });
    }

    @OnClick(R.id.submit_btn)
    void onSubmit(){
        mPresenter.onSubmit(mPassEditText.getText());
    }

    @Override
    public void grantAccess() {
        setResult(RESULT_OK);
        finish();
        overridePendingTransition(R.anim.from_out, R.anim.to_out);
    }

    @Override
    public void onBackPressed() {}

    public static Intent newIntent(Context context){
        return new Intent(context, LockActivity.class);
    }
}
