package com.kara4k.apalontest.presenter;


import android.util.SparseBooleanArray;

import com.kara4k.apalontest.model.DaoSession;
import com.kara4k.apalontest.model.Note;
import com.kara4k.apalontest.model.NoteDao;
import com.kara4k.apalontest.other.PreferenceManager;
import com.kara4k.apalontest.presenter.base.BasePresenter;
import com.kara4k.apalontest.view.NotesListViewIF;
import com.kara4k.apalontest.view.adapters.NoteViewObj;

import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.query.QueryBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NotesListPresenter extends BasePresenter<NotesListViewIF>
        implements SingleObserver<List<Note>> {

    public static final int BY_DATE = 1;
    public static final int BY_TITLE = 2;
    public static final int BY_TEXT = 3;

    private NoteDao mNoteDao;
    private List<Note> mNotes;
    private boolean isActionMode;
    private SparseBooleanArray mSelectedNotes;
    private int mSelectedCount;
    @Inject
    PreferenceManager mPrefs;

    @Inject
    public NotesListPresenter(DaoSession daoSession) {
        mNoteDao = daoSession.getNoteDao();
        resetSelections();
    }

    public void onStart() {
        getView().showAnimation();

        Callable<List<Note>> sortedCallable = getSortedCallable();

        Observable.fromCallable(sortedCallable)
                .flatMapIterable(notes -> notes)
                .toList()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
    }

    private Callable<List<Note>> getSortedCallable() {
        Property sortProperty = getSortProperty();

        return () -> getNotes(sortProperty);
    }

    private Property getSortProperty() {
        int sortType = mPrefs.getSortType();
        Property sortProperty;

        switch (sortType) {
            case BY_TITLE:
                sortProperty = NoteDao.Properties.Title;
                break;
            case BY_TEXT:
                sortProperty = NoteDao.Properties.Text;
                break;
            case BY_DATE:
                sortProperty = NoteDao.Properties.Date;
                break;
            default:
                sortProperty = NoteDao.Properties.Date;
                break;
        }

        return sortProperty;
    }

    private List<Note> getNotes(Property property) {
        return mNoteDao.queryBuilder().orderAsc(property).build().list();
    }

    @Override
    public void onSubscribe(Disposable d) {
        mCompositeDisposable.add(d);
    }

    @Override
    public void onSuccess(List<Note> notes) {
        mNotes = notes;
        updateUI();
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        getView().showError(e.getMessage());
    }

    public void onSearch(String newText) {
        Property sortProperty = getSortProperty();

        Observable.fromCallable(() -> getNotes(sortProperty, newText))
                .flatMapIterable(notes -> notes)
                .toList()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
    }

    private List<Note> getNotes(Property sortProperty, String text) {
        QueryBuilder<Note> noteQueryBuilder = mNoteDao.queryBuilder();
        noteQueryBuilder.whereOr(NoteDao.Properties.Title.like("%" + text + "%"),
                NoteDao.Properties.Text.like("%" + text + "%")).orderAsc(sortProperty);

        return noteQueryBuilder.list();
    }

    private void updateUI() {
        Observable.fromIterable(mNotes)
                .map(this::mapNote)
                .toList()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(noteViewObjs -> getView().showNotes(noteViewObjs), this::onError);
    }

    public void onMenuCreate(){
        getView().updateSortType(mPrefs.getSortType());
    }

    public void sortNotes(int type) {
        mPrefs.setSortType(type);
        getView().updateSortType(type);
        onStart();
    }

    public void startActionMode(int position) {
        isActionMode = true;
        getView().startActionMode();
        toggleSelection(position);
    }

    public void onNoteClicked(long id, int position) {
        if (isActionMode) {
            toggleSelection(position);
        } else {
            getView().showNoteEdits(id);
        }
    }

    public void onCreateNewNote() {
        getView().showNoteCreator();
    }

    private NoteViewObj mapNote(Note note) throws Exception {
        NoteViewObj noteViewObj = new NoteViewObj();
        Long date = note.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String formattedDate = dateFormat.format(new Date(date));

        noteViewObj.setId(note.getId());
        noteViewObj.setTitle(note.getTitle());
        noteViewObj.setText(note.getText());
        noteViewObj.setDate(formattedDate);
        return noteViewObj;
    }

    public void onDeleteNotes() {
        getView().showDeleteConfirm();
    }

    public void onDeleteConfirm() {
        Completable.fromAction(this::deleteSelectedNotes)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    onStart();
                    finishActionMode();
                }, this::onError);
    }

    private List<Note> getSelectedNotes() {
        List<Note> notes = new ArrayList<>();
        for (int i = 0; i < mSelectedNotes.size(); i++) {
            boolean isSelected = mSelectedNotes.valueAt(i);
            int key = mSelectedNotes.keyAt(i);

            if (isSelected) {
                notes.add(mNotes.get(key));
            }
        }
        return notes;
    }

    private void deleteSelectedNotes() {
        List<Note> selectedNotes = getSelectedNotes();
        mNoteDao.deleteInTx(selectedNotes);
    }

    public void toggleSelection(int position) {
        boolean isSelected = !mSelectedNotes.get(position, false);
        mSelectedCount = isSelected ? ++mSelectedCount : --mSelectedCount;

        if (mSelectedCount == 0) {
            finishActionMode();
            return;
        }

        mSelectedNotes.put(position, isSelected);
        getView().setNoteSelection(position, isSelected);
        getView().showSelectedCount(String.valueOf(mSelectedCount));
    }

    private void finishActionMode() {
        getView().finishActionMode();
    }

    private void resetSelections() {
        isActionMode = false;
        mSelectedNotes = new SparseBooleanArray();
        mSelectedCount = 0;
    }

    public void onActionModeDestroy() {
        resetSelections();
    }
}
