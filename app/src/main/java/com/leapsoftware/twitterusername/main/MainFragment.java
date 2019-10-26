package com.leapsoftware.twitterusername.main;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.leapsoftware.twitterusername.R;
import com.leapsoftware.twitterusername.model.TwitterResponse;
import com.leapsoftware.twitterusername.networking.TwitterService;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainFragment extends Fragment {
    private static final String TAG = MainFragment.class.getSimpleName();

    private EditText mUsernameEditText;
    private TextView mStatusText;

    private MainViewModelFactory mMainViewModelFactory;
    private MainViewModel mViewModel;
    private CompositeDisposable mCompositeDisposable;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCompositeDisposable = new CompositeDisposable();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUsernameEditText = view.findViewById(R.id.userinput);
        mStatusText = view.findViewById(R.id.status);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TwitterService twitterService = new TwitterService();
        mMainViewModelFactory = new MainViewModelFactory(twitterService);
        mViewModel = ViewModelProviders.of(this, mMainViewModelFactory).get(MainViewModel.class);
    }

    @Override
    public void onStart() {
        super.onStart();
        mUsernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                /* no-op */
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /* no-op */
            }

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
                                    showSuccessMessage(getContext());
                                } else {
                                    showFailureMessage(getContext());
                                }
                            }
                        }));
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mCompositeDisposable.clear();
    }

    private void showSuccessMessage(Context context) {
        mStatusText.setText("Username is available");
        mStatusText.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_green_light));
    }

    private void showFailureMessage(Context context) {
        mStatusText.setText("Username is not available");
        mStatusText.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_red_light));
    }
}
