package ru.ivanovpv.githubusers;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MeModule {
    @ContributesAndroidInjector
    abstract MainActivity contributeActivityInjector();
}