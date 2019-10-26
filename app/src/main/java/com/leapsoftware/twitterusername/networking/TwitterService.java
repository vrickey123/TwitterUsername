package com.leapsoftware.twitterusername.networking;

import com.leapsoftware.twitterusername.model.TwitterResponse;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class TwitterService {

    private TwitterApi mTwitterApi;

    public TwitterService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.twitter.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        mTwitterApi = retrofit.create(TwitterApi.class);
    }


    public Observable<TwitterResponse> checkUsername(String fullName, boolean suggest, String userName) {
        return mTwitterApi.checkUsername(fullName, suggest, userName);
    }
}
