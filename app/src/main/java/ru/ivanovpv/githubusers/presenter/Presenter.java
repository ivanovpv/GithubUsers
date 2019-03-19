package ru.ivanovpv.githubusers.presenter;

import android.arch.paging.PagedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.ivanovpv.githubusers.github.GithubDataSource;
import ru.ivanovpv.githubusers.model.User;
import ru.ivanovpv.githubusers.tools.MainThreadExecutor;

@Singleton
public class Presenter implements IPresenter<User> {
    private static final Logger logger= LoggerFactory.getLogger(Presenter.class);

    @Inject
    GithubDataSource dataSource;
    @Inject
    MainThreadExecutor executor;

    IFetchable<User> fetchable;

    @Inject
    public Presenter() {
        //Me.getComponent().inject(this);
    }

    @Override
    public void fetchList(IFetchable<User> fetchable) {
        this.fetchable=fetchable;
        fetchable.getImageLoader().initLoader();
        // Configure paging
        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(10)
                .setInitialLoadSizeHint(20)
                .setEnablePlaceholders(true) // Show empty views until data is available
                .build();

        // Build PagedList
        PagedList<User> list =
                new PagedList.Builder<>(dataSource, config) // Can pass `pageSize` directly instead of `config`
                        // Do fetch operations on the main thread. We'll instead be using Retrofit's
                        // built-in enqueue() method for background api calls.
                        .setFetchExecutor(executor)
                        // Send updates on the main thread
                        .setNotifyExecutor(executor)
                        .build();

        // Required only once. Paging will handle fetching and updating the list.
        fetchable.submitList(list);
        fetchable.setFailureObserver(dataSource);
    }

    @Override
    public void onClickListItem(User user) {
        fetchable.onClickListItem(user);
    }

    @Override
    public IFetchable<User> getFetchable() {
        return fetchable;
    }

    @Override
    public void onBindUserRowViewAtPosition(int position, IUserRowView iUserRowView) {
        iUserRowView.setUserId();
        iUserRowView.setLogin();
        iUserRowView.setAvatar();
    }

}
