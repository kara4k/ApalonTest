package com.kara4k.apalontest.presenter;


import com.kara4k.apalontest.model.DaoSession;
import com.kara4k.apalontest.model.Note;
import com.kara4k.apalontest.model.NoteDao;
import com.kara4k.apalontest.presenter.base.BasePresenter;
import com.kara4k.apalontest.view.NoteActivity;
import com.kara4k.apalontest.view.NoteViewIF;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NotePresenter extends BasePresenter<NoteViewIF> implements SingleObserver<Note> {

    private NoteDao mNoteDao;
    private Note mNote;

    @Inject
    public NotePresenter(DaoSession daoSession) {
        mNoteDao = daoSession.getNoteDao();
    }

    public void onCreate(long id) {

        getView().showAnimation();

        if (mNote != null) {
            updateUI(mNote);
            return;
        }

        if (id == NoteActivity.EMPTY) {
            mNote = new Note();
        } else {
            Single.fromCallable(() -> getNote(id))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this);
        }
    }

    private Note getNote(long id) {
        Note note = mNoteDao.queryBuilder()
                .where(NoteDao.Properties.Id.eq(id))
                .build().unique();

        if (note == null) return new Note();

        return note;
    }

    @Override
    public void onSubscribe(Disposable d) {
        mCompositeDisposable.add(d);
    }

    @Override
    public void onSuccess(Note note) {
        mNote = note;
        updateUI(note);
    }

    private void updateUI(Note note) {
        getView().showAnimation();
        getView().setTitle(note.getTitle());
        getView().setText(note.getText());
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        getView().showError(e.getMessage());
    }

    public void onSaveClicked() {
        Completable.fromAction(() -> saveNote(mNote))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> getView().closeView(), this::onError);
    }

    private void saveNote(Note note) {
        mNote.setDate(System.currentTimeMillis());
        mNoteDao.insertOrReplace(note);
    }

    public void onTitleChanged(CharSequence charSequence) {
        mNote.setTitle(charSequence.toString());
    }

    public void onTextChanged(CharSequence charSequence) {
        mNote.setText(charSequence.toString());
    }
}
