package ru.ivanovpv.githubusers;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { MeModule.class})
public interface MeComponent {
    void inject(MainActivity mainActivity);
}