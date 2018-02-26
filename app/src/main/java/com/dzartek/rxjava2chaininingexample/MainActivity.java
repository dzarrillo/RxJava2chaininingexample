package com.dzartek.rxjava2chaininingexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getName();
    private DataAdapter mDataAdapter;
    private List<String> mDataList = new ArrayList<>();
    @BindView(R.id.buttonGetData)
    TextView mButtonGetData;
    @BindView(R.id.recyclerViewData)
    RecyclerView mRecyclerViewData;

    private CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initialiseWidgets();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }

    private void initialiseWidgets() {
        mDataAdapter = new DataAdapter(mDataList);
        mRecyclerViewData.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mRecyclerViewData.setHasFixedSize(true);
        mRecyclerViewData.setAdapter(mDataAdapter);

        mButtonGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  This can be written
//                getObservable()
//                        // Run on background thread
//                        .subscribeOn(Schedulers.io())
//                        // Be notified on mainthread
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .map(new Function<String, List<String>>() {
//                                 @Override
//                                 public List<String> apply(String s) throws Exception {
//                                     return convertDataToList(s);
//                                 }
//                             }
//                        )
//                        .subscribe(getObserver()
//                        );

                Observable.fromCallable(() -> {
                    return getData();
                })
                        .subscribeOn(Schedulers.io())
                        /* I had to move the .observeOn after the .map operator because I do not
                           want the .map operator to run on main thread.
                         */

//                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<String, List<String>>() {
                            @Override
                            public List<String> apply(String s) throws Exception {
                                Log.d(TAG, " Map - " + " current thread is: "
                                        + Thread.currentThread().getName());
                                return convertDataToList(s);
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<String>>() {
                            @Override
                            public void accept(List<String> strings) throws Exception {
                                Log.d(TAG, " Subscribe Size: " + strings.size()
                                        + " current thread is: "
                                        + Thread.currentThread().getName());
                                // update recyclerview
                            }
                        });
            }
        });
    }

    // This can be an api call or sqlitedb
    private String getData() {
        Log.d(TAG, "Getdata - " + " current thread is: "
                + Thread.currentThread().getName());
        return getString(R.string.skillset);
    }

    private List convertDataToList(String data) {
        Log.d(TAG, "convertDataToList - " + " current thread is: "
                + Thread.currentThread().getName());
        return new ArrayList<>(Arrays.asList(data.split(",")));
    }


    //  This can be written
//    private Observable<String> getObservable() {
//        return Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> e) throws Exception {
//                if (!e.isDisposed()) {
//                    e.onNext(getData());
//                    e.onComplete();
//                }
//            }
//        });
//    }
//
//    private Observer<List<String>> getObserver() {
//        return new Observer<List<String>>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//                Log.d(TAG, " onSubscribe : " + d.isDisposed());
//            }
//
//            @Override
//            public void onNext(List<String> strings) {
//                Log.d(TAG, " onNext Size: " + strings.size());
//                //update recyclerview here
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.d(TAG, " onError : " + e.getMessage());
//            }
//
//            @Override
//            public void onComplete() {
//                Log.d(TAG, " onComplete");
//            }
//        };
//
//    }


}
