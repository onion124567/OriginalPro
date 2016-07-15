package com.open.teachermanager.factory;

import com.open.teachermanager.presenter.Presenter;

public interface PresenterFactory<P extends Presenter> {
    P createPresenter();
}
