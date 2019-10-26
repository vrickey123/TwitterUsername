package com.leapsoftware.twitterusername.networking;

import com.leapsoftware.twitterusername.model.TwitterResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TwitterApi {
    @GET("i/users/username_available.json")
    Observable<TwitterResponse> checkUsername(@Query("full_name") String fullName,
                                              @Query("suggest") boolean suggest,
                                              @Query("username") String userName);
}
