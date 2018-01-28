package com.kara4k.apalontest.di.modules;


import com.kara4k.apalontest.di.scopes.PerActivity;
import com.kara4k.apalontest.view.LockViewIF;
import com.kara4k.apalontest.view.MainViewIF;
import com.kara4k.apalontest.view.NoteViewIF;
import com.kara4k.apalontest.view.NotesListViewIF;
import com.kara4k.apalontest.view.base.ViewIF;

import dagger.Module;
import dagger.Provides;

@Module
public class ViewModule {

    private ViewIF mViewIF;

    public ViewModule(ViewIF viewIF) {
        mViewIF = viewIF;
    }

    @Provides
    @PerActivity
    ViewIF provideUsersViewIF() {
        return mViewIF;
    }

    @Provides
    @PerActivity
    NotesListViewIF provideNotesListViewIF() {
        return (NotesListViewIF) mViewIF;
    }

    @Provides
    @PerActivity
    NoteViewIF provideNoteViewIF() {
        return (NoteViewIF) mViewIF;
    }

    @Provides
    @PerActivity
    MainViewIF provideMainViewIF() {
        return (MainViewIF) mViewIF;
    }

    @Provides
    @PerActivity
    LockViewIF provideLockViewIF() {
        return (LockViewIF) mViewIF;
    }
}
