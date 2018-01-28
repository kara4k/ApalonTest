package com.kara4k.apalontest.view;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.kara4k.apalontest.R;
import com.kara4k.apalontest.di.DaggerViewComponent;
import com.kara4k.apalontest.di.modules.ViewModule;
import com.kara4k.apalontest.other.TextChangeListener;
import com.kara4k.apalontest.presenter.NotePresenter;
import com.kara4k.apalontest.view.base.BaseActivity;
import com.kara4k.apalontest.view.base.BaseFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class NoteFragment extends BaseFragment implements NoteViewIF {

    public static final String NOTE_ID = "id";

    @BindView(R.id.title_edit_text)
    EditText mTitleEditText;
    @BindView(R.id.edit_text)
    EditText mEditText;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.title_layout)
    LinearLayout mTitleLayout;
    @BindView(R.id.text_layout)
    LinearLayout mTextLayout;
    @Inject
    NotePresenter mPresenter;

    public static NoteFragment newInstance(long id) {
        Bundle args = new Bundle();
        args.putLong(NOTE_ID, id);
        NoteFragment fragment = new NoteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_note;
    }

    @Override
    protected int getMenuRes() {
        return R.menu.fragment_note_details;
    }

    @Override
    protected void injectDaggerDependencies() {
        DaggerViewComponent.builder()
                .appComponent(getAppComponent())
                .viewModule(new ViewModule(this))
                .build().injectNoteFragment(this);
    }

    @Override
    protected void onViewReady() {
        super.onViewReady();
        long id = getArguments().getLong(NOTE_ID, NoteActivity.EMPTY);

        setListeners();

        mPresenter.onCreate(id);
    }

    private void setListeners() {
        mTitleEditText.addTextChangedListener(new TextChangeListener() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mPresenter.onTitleChanged(charSequence);
            }
        });

        mEditText.addTextChangedListener(new TextChangeListener() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mPresenter.onTextChanged(charSequence);
            }
        });
    }

    @Override
    public void setTitle(String title) {
        mTitleEditText.setText(title);
    }

    @Override
    public void setText(String text) {
        mEditText.setText(text);
    }

    @Override
    public void closeView() {
        ((BaseActivity)getActivity()).onBackPressed();
    }

    @OnClick(R.id.fab)
    void onFabClicked() {
        mPresenter.onSaveClicked();
    }

    @Override
    public void showAnimation() {
        TranslateAnimation titleAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);

        titleAnimation.setDuration(1000);
        mTitleLayout.startAnimation(titleAnimation);

        TranslateAnimation textAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);

        textAnimation.setDuration(1000);
        mTextLayout.startAnimation(textAnimation);

        TranslateAnimation fabAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_PARENT, 0.0f);

        fabAnimation.setDuration(1000);
        mFab.startAnimation(fabAnimation);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }
}
