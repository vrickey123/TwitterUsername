# TwitterUsername
MVVM and RxJava sample that checks if a username is available on Twitter.

<img src="https://github.com/vrickey123/TwitterUsername/blob/master/docs/twitter_username.gif" width="360">

## TwitterApi Network Request
Using an RxJava Adapter, we can make an `Observable<TwitterResponse>` to check for available usernames.
```
public interface TwitterApi {
    @GET("i/users/username_available.json")
    Observable<TwitterResponse> checkUsername(@Query("full_name") String fullName,
                                              @Query("suggest") boolean suggest,
                                              @Query("username") String userName);
}
```

## ViewModel
Our ViewModel will house this network request so that it is idependent from configuration changes and other Activity/Fragment lifecycle events.
```
public class MainViewModel extends ViewModel {
    private TwitterService mTwitterService;

    MainViewModel(TwitterService twitterService){
        mTwitterService = twitterService;
    }

    Observable<TwitterResponse> checkUsername(String fullName, boolean suggest, String userName) {
        return mTwitterService.checkUsername(fullName, suggest, userName);
    }
}
```

## Fragment and TextWatcher
Using a [TextWatcher](https://developer.android.com/reference/android/text/TextWatcher), we will observe changes made to the [TextInputEditText](https://material.io/develop/android/components/text-input-layout/) on `AndroidSchedulers.mainThread()` to show either a success or error message.
```
    @Override
    public void onStart() {
        super.onStart();
        mUsernameEditText.addTextChangedListener(new TextWatcher() {
            ...

            @Override
            public void afterTextChanged(Editable s) {
                mCompositeDisposable.add(mViewModel.checkUsername("Droid", true, mUsernameEditText.getText().toString())
                        .debounce(300, TimeUnit.MILLISECONDS)
                        .distinctUntilChanged()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<TwitterResponse>() {
                            @Override
                            public void accept(TwitterResponse twitterResponse) throws Exception {
                                if (twitterResponse.valid) {
                                    showSuccessMessage();
                                } else {
                                    showErrorMessage();
                                }
                            }
                        }));
            }
        });
    }
```

## Debounce
The `debounce` and `distinctUntilChanged` operators are used so that we avoid spamming the backend with a new network request everytime a character is entered. We will wait until 300ms after a character is entered before firing a `checkUsername` request.

```
.debounce(300, TimeUnit.MILLISECONDS)
.distinctUntilChanged()
```

## Composite Disposables
In our Fragment's `onStop()`, we will clear all of our Rx subscriptions to prevent a memory leak using a `CompositeDisposable`. A `CompositeDisposable` allows us to keep track and clear multiple Rx subscriptions with a single call.

```
@Override
    public void onStop() {
        super.onStop();
        mCompositeDisposable.clear();
    }
```
