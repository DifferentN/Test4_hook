package com.example.a17916.test4_hook.util;

//import rx.Observable;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;

/**
 * Created by vector on 16/11/3.
 */

public class RxUtil {

    //参考http://www.jianshu.com/p/a8b5278cdbcd，我们 app 有非常多的网络请求，而且除了网络请求，其他的数据库操作 或者 文件读写操作 都需要一样的线程切换。因此，为了代码复用，我们利用 RxJava 提供的 Transformer 来进行封装。
//    public static <T>Observable.Transformer<T,T> normalSchedulers(){
//        return new Observable.Transformer<T,T>(){
//            @Override
//            public Observable<T> call(Observable<T> source) {
//                return source.subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread());
//            }
//        };
//    }
}
