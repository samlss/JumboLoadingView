package com.iigo.jumboloadingview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

import com.iigo.library.JumboLoadingView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author SamLeung
 * @e-mail 729717222@qq.com
 * @github https://github.com/samlss
 * @csdn https://blog.csdn.net/Samlss
 * @description The star type sample.
 */
public class StarSampleActivity extends AppCompatActivity {

    private JumboLoadingView jumboLoadingView1;
    private JumboLoadingView jumboLoadingView2;

    private Disposable updateProgressDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.star_sample_activity);

        jumboLoadingView1 = findViewById(R.id.jlv1);
        jumboLoadingView2 = findViewById(R.id.jlv2);

        updateProgress();
    }

    private void updateProgress(){
        updateProgressDisposable = Observable.interval(0, 1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        int progress = (jumboLoadingView2.getProgress() + 1) % 100;
                        jumboLoadingView2.setProgress(progress);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (updateProgressDisposable != null){
            updateProgressDisposable.dispose();
        }

        jumboLoadingView1.release();
        jumboLoadingView2.release();
    }

    public void onStart(View view) {
        jumboLoadingView1.start();
        jumboLoadingView2.start();
    }

    public void onStop(View view) {
        jumboLoadingView1.stop();
        jumboLoadingView2.stop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        jumboLoadingView1.setCircleColor(Color.RED);
//        jumboLoadingView1.setShapeColor(Color.RED);

        jumboLoadingView2.setProgressTextColor(Color.RED);
        jumboLoadingView2.setProgressTextSize(12);
        return super.onKeyDown(keyCode, event);
    }
}
