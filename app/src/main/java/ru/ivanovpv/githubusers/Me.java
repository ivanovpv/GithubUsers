package ru.ivanovpv.githubusers;

import android.app.Activity;
import android.app.Application;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;

public class Me extends Application {

    private static MeComponent component;

    public static MeComponent getComponent() {
        return component;
    }

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        component=DaggerMeComponent.builder().meModule(new MeModule(this)).build();

        //component=buildComponent();
    }

}
