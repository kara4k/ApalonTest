package com.kara4k.apalontest.view;


import com.kara4k.apalontest.view.adapters.NoteViewObj;
import com.kara4k.apalontest.view.base.ViewIF;

import java.util.List;

public interface NotesListViewIF extends ViewIF {

    void showNotes(List<NoteViewObj> noteList);

    void showNoteCreator();

    void showNoteEdits(long id);

    void startActionMode();

    void finishActionMode();

    void setNoteSelection(int position, boolean isSelected);

    void showDeleteConfirm();

    void showSelectedCount(String count);

    void updateSortType(int sortType);

    void showAnimation();
}
