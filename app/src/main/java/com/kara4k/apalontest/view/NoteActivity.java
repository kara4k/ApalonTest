package com.kara4k.apalontest.view;


import android.content.Context;
import android.content.Intent;

import com.kara4k.apalontest.view.base.BaseActivity;

public class NoteActivity extends BaseActivity {

    public static final String NOTE_ID = "id";
    public static final Long EMPTY = -1L;

    @Override
    protected void onViewReady() {
        super.onViewReady();
        if (getIntent() == null) return;

        long id = getIntent().getLongExtra(NOTE_ID, EMPTY);

        setFragment(NoteFragment.newInstance(id));
    }

    public static Intent newIntent(Context context, long id) {
        Intent intent = new Intent(context, NoteActivity.class);
        intent.putExtra(NOTE_ID, id);
        return intent;
    }
}
