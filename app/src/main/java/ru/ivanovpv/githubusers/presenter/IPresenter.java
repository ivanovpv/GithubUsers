package ru.ivanovpv.githubusers.presenter;

import dagger.Module;

@Module
public interface IPresenter<T> {
    void fetchList(IFetchable<T> fetchable);
    void onBindUserRowViewAtPosition(int position, IUserRowView iUserRowView);
    void onClickListItem(T t);
    IFetchable<T> getFetchable();
}
