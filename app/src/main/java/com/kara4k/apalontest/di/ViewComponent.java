package com.kara4k.apalontest.di;


import com.kara4k.apalontest.di.modules.ViewModule;
import com.kara4k.apalontest.di.scopes.PerActivity;
import com.kara4k.apalontest.view.LockActivity;
import com.kara4k.apalontest.view.MainActivity;
import com.kara4k.apalontest.view.NoteFragment;
import com.kara4k.apalontest.view.NotesListFragment;

import dagger.Component;

@Component(modules = ViewModule.class, dependencies = AppComponent.class)
@PerActivity
public interface ViewComponent {

    void injectMainActivity(MainActivity activity);

    void injectLockActivity(LockActivity activity);

    void injectNotesListFragment(NotesListFragment fragment);

    void injectNoteFragment(NoteFragment fragment);

}
