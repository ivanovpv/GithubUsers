package ru.ivanovpv.githubusers.presenter;

import android.arch.paging.PagedList;

import ru.ivanovpv.githubusers.github.GithubDataSource;
import ru.ivanovpv.githubusers.view.IImageLoader;

public interface IFetchable<T> {
    void submitList(PagedList<T> list);
    void setFailureObserver(GithubDataSource dataSource);
    void onClickListItem(T t);
    IImageLoader getImageLoader();
}
