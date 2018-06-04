package com.example.kanj.swiggyjunk.dagger;

import com.example.kanj.swiggyjunk.models.VariantSelectionModel;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(VariantSelectionModel variantSelectionModel);
}
