package com.kara4k.apalontest.view.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.kara4k.apalontest.R;
import com.kara4k.apalontest.presenter.NotesListPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.Holder> {

    private List<NoteViewObj> mNotes;
    private NotesListPresenter mPresenter;

    public NotesAdapter(NotesListPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_note, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.onBind(mNotes.get(position), position);
        setSlideAnimation(holder.itemView);
    }

    private void setSlideAnimation(View view) {
        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,-1.0f, Animation.RELATIVE_TO_PARENT,0.0f,
                Animation.RELATIVE_TO_PARENT,0.0f, Animation.RELATIVE_TO_PARENT,0.0f);

        animation.setDuration(700);
        view.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return mNotes == null ? 0 : mNotes.size();
    }

    public void setNotes(List<NoteViewObj> notes) {
        mNotes = notes;
        notifyDataSetChanged();
    }

    public void finishActionMode() {
        for (int i = 0; i < mNotes.size(); i++) {
            mNotes.get(i).setSelected(false);
        }

        notifyDataSetChanged();
    }

    public void setSelected(int position, boolean isSelected) {
        mNotes.get(position).setSelected(isSelected);
        notifyItemChanged(position);
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.title_text_view)
        TextView mTitleTextView;
        @BindView(R.id.text_view)
        TextView mTextView;
        @BindView(R.id.num_text_view)
        TextView mNumTextView;
        @BindView(R.id.date_text_view)
        TextView mDateTextView;

        private NoteViewObj mNote;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        void onBind(NoteViewObj note, int position) {
            mNote = note;
            mTitleTextView.setText(note.getTitle());
            mTextView.setText(note.getText());
            mDateTextView.setText(note.getDate());
            mNumTextView.setText(String.valueOf(position + 1));

            itemView.setSelected(note.isSelected());
        }

        @Override
        public void onClick(View view) {
            mPresenter.onNoteClicked(mNote.getId(), getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            mPresenter.startActionMode(getAdapterPosition());
            return true;
        }
    }
}
