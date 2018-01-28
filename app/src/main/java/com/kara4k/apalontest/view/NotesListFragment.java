package com.kara4k.apalontest.view;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.kara4k.apalontest.R;
import com.kara4k.apalontest.di.DaggerViewComponent;
import com.kara4k.apalontest.di.modules.ViewModule;
import com.kara4k.apalontest.presenter.NotesListPresenter;
import com.kara4k.apalontest.view.adapters.NoteViewObj;
import com.kara4k.apalontest.view.adapters.NotesAdapter;
import com.kara4k.apalontest.view.base.BaseFragment;
import com.kara4k.apalontest.view.base.DrawerActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class NotesListFragment extends BaseFragment
        implements NotesListViewIF, SearchView.OnQueryTextListener {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.fab)
    FloatingActionButton mFab;

    @Inject
    NotesListPresenter mPresenter;
    private NotesAdapter mAdapter;
    private ActionMode mActionMode;
    private Menu mMenu;


    public static NotesListFragment newInstance() {
        return new NotesListFragment();
    }

    @Override
    protected int getLayout() {
        return R.layout.recycler_view;
    }

    @Override
    protected int getMenuRes() {
        return R.menu.fragment_notes_list;
    }

    @Override
    protected void injectDaggerDependencies() {
        DaggerViewComponent.builder()
                .appComponent(getAppComponent())
                .viewModule(new ViewModule(this))
                .build().injectNotesListFragment(this);
    }

    @Override
    protected void onViewReady() {
        super.onViewReady();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter = new NotesAdapter(mPresenter));
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.onStart();
    }

    @Override
    protected void onMenuInflated(Menu menu) {
        super.onMenuInflated(menu);
        mMenu = menu;

        SearchView searchView = (SearchView) menu.findItem(R.id.menu_item_search).getActionView();
        searchView.setOnQueryTextListener(this);

        mPresenter.onMenuCreate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_title:
                mPresenter.sortNotes(NotesListPresenter.BY_TITLE);
                return true;
            case R.id.sort_by_text:
                mPresenter.sortNotes(NotesListPresenter.BY_TEXT);
                return true;
            case R.id.sort_by_date:
                mPresenter.sortNotes(NotesListPresenter.BY_DATE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void updateSortType(int sortType) {
        SubMenu sortSubMenu = mMenu.findItem(R.id.menu_item_sort).getSubMenu();

        switch (sortType) {
            case NotesListPresenter.BY_TITLE:
                sortSubMenu.findItem(R.id.sort_by_title).setChecked(true);
                break;
            case NotesListPresenter.BY_TEXT:
                sortSubMenu.findItem(R.id.sort_by_text).setChecked(true);
                break;
            case NotesListPresenter.BY_DATE:
                sortSubMenu.findItem(R.id.sort_by_date).setChecked(true);
                break;
        }
    }

    @Override
    public void showNotes(List<NoteViewObj> noteList) {
        mAdapter.setNotes(noteList);
    }

    @Override
    public void showNoteCreator() {
        Intent intent = NoteActivity.newIntent(getContext(), NoteActivity.EMPTY);
        activityStart(intent);
    }

    @Override
    public void showNoteEdits(long id) {
        Intent intent = NoteActivity.newIntent(getContext(), id);
        activityStart(intent);
    }

    @OnClick(R.id.fab)
    void onFabClicked() {
        mPresenter.onCreateNewNote();
    }

    @Override
    public void startActionMode() {
        mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.actions_notes_list, menu);
                mFab.setVisibility(View.GONE);
                setDrawerMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_delete:
                        mPresenter.onDeleteNotes();
                        break;
                }
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                mPresenter.onActionModeDestroy();
                mAdapter.finishActionMode();
                mFab.setVisibility(View.VISIBLE);
                setDrawerMode(DrawerLayout.LOCK_MODE_UNDEFINED);
            }
        });
    }

    private void setDrawerMode(int lockMode) {
        ((DrawerActivity) getActivity()).setDrawerMode(lockMode);
    }

    @Override
    public void finishActionMode() {
        if (mActionMode != null) {
            mActionMode.finish();
            mActionMode = null;
        }
    }

    @Override
    public void setNoteSelection(int position, boolean isSelected) {
        mAdapter.setSelected(position, isSelected);
    }

    @Override
    public void showSelectedCount(String count) {
        if (mActionMode != null) {
            mActionMode.setTitle(count);
        }
    }

    @Override
    public void showDeleteConfirm() {
        String title = getString(R.string.delete_dialog_title);
        String message = getString(R.string.delete_dialog_message);
        DialogInterface.OnClickListener okListener = (dialogInterface, i)
                -> mPresenter.onDeleteConfirm();

        showConfirmDialog(title, message, okListener);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mPresenter.onSearch(newText);
        return true;
    }

    @Override
    public void showAnimation() {
        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_PARENT, 0.0f);

        animation.setDuration(1000);
        mFab.startAnimation(animation);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }
}
