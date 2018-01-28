package com.kara4k.apalontest.view;


import com.kara4k.apalontest.view.base.ViewIF;

public interface NoteViewIF extends ViewIF {

    void setTitle(String title);

    void setText(String text);

    void closeView();

    void showAnimation();
}
