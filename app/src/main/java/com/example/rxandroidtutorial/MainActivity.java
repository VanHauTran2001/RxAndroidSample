package com.example.rxandroidtutorial;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private Disposable disposable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Observable<User> observable = getObservableUser();
        Observer<User> observer = getObseverUser();
        observable.subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                   .subscribe(observer);
    }

    private Observable<User> getObservableUser(){
        List<User> list = getListUser();

        return Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<User> emitter) throws Throwable {
                    if (list==null||list.isEmpty()){
                        if (!emitter.isDisposed()){
                            emitter.onError(new Exception());
                        }
                    }
                    for (User user : list){
                        if (!emitter.isDisposed()){ //Kiem tra Obervable con ket noi voi Obsever
                            emitter.onNext(user);
                        }
                    }
                    if (!emitter.isDisposed()){
                        emitter.onComplete();
                    }
            }
        });
    }
    private Observer<User> getObseverUser(){
        return new Observer<User>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.e("Hau","OnSubscribe");
                disposable = d;
            }

            @Override
            public void onNext(@NonNull User user) {
                Log.e("Hau","OnNext" + user.toString());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("Hau","OnError");
            }

            @Override
            public void onComplete() {
                Log.e("Hau","OnComplete");
            }
        };
    }
    private List<User> getListUser(){
        List<User> userList = new ArrayList<>();
        userList.add(new User(1,"Hau"));
        userList.add(new User(2,"Hien"));
        userList.add(new User(3,"Trang"));
        return userList;
    }
    //ngat ket noi
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null){
            disposable.dispose();
        }
    }
}