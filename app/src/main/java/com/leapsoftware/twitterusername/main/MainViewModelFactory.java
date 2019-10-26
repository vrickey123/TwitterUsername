package com.leapsoftware.twitterusername.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.leapsoftware.twitterusername.networking.TwitterService;

public class MainViewModelFactory implements ViewModelProvider.Factory {
    private TwitterService mTwitterService;

    public MainViewModelFactory(TwitterService twitterService) {
        mTwitterService = twitterService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(mTwitterService);
        }
        throw new IllegalArgumentException("Unknown ViewModel");
    }
}
