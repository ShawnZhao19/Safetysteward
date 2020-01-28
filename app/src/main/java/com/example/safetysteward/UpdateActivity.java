package com.example.safetysteward;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


public class UpdateActivity extends AppCompatActivity {


    private UpdateManager mUpdateManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        //这里来检测版本是否需要更新
        mUpdateManager = new UpdateManager(this);
        mUpdateManager.checkUpdateInfo();
    }
}