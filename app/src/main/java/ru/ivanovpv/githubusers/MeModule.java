package ru.ivanovpv.githubusers;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.ivanovpv.githubusers.presenter.IPresenter;
import ru.ivanovpv.githubusers.presenter.Presenter;
import ru.ivanovpv.githubusers.view.UserListAdapter;

@Module
public class MeModule {

    Application me;
    private Context context;


    public MeModule(Application application) {
        me = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return me;
    }

    public MeModule(@NonNull Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    @NonNull
    public Context provideContext(){
        return context;
    }

    @Provides
    @Singleton
    IPresenter providesIPresenter(Presenter presenter) {
        return presenter;
    }
}