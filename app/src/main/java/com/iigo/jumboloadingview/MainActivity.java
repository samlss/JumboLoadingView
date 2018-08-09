package com.iigo.jumboloadingview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startCircleSample(View view) {
        startActivity(new Intent(this, CircleSampleActivity.class));
    }

    public void startTriangleSample(View view) {
        startActivity(new Intent(this, TriangleSampleActivity.class));
    }

    public void startSquareSample(View view) {
        startActivity(new Intent(this, SquareSampleActivity.class));
    }

    public void startStarSample(View view) {
        startActivity(new Intent(this, StarSampleActivity.class));
    }
}
