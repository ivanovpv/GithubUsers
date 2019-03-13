package ru.ivanovpv.githubusers;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

@Component(modules = { AndroidInjectionModule.class, MeModule.class})
public interface MeComponent extends AndroidInjector<Me> {
}