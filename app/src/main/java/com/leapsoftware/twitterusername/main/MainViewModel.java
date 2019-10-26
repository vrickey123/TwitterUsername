package com.leapsoftware.twitterusername.main;

import androidx.lifecycle.ViewModel;

import com.leapsoftware.twitterusername.model.TwitterResponse;
import com.leapsoftware.twitterusername.networking.TwitterService;

import io.reactivex.Observable;

public class MainViewModel extends ViewModel {
    private TwitterService mTwitterService;

    MainViewModel(TwitterService twitterService){
        mTwitterService = twitterService;
    }

    Observable<TwitterResponse> checkUsername(String fullName, boolean suggest, String userName) {
        return mTwitterService.checkUsername(fullName, suggest, userName);
    }
}
