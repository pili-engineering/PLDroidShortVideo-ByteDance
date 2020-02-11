package com.qiniu.pili.droid.shortvideo.demo.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.qiniu.pili.droid.shortvideo.demo.R;
import com.qiniu.pili.droid.shortvideo.demo.utils.LoadResourcesTask;
import com.qiniu.pili.droid.shortvideo.demo.utils.SharedPreferencesUtils;
import com.qiniu.pili.droid.shortvideo.demo.utils.ToastUtils;


public class LoadResourcesActivity extends AppCompatActivity implements LoadResourcesTask.ILoadResourcesCallback {

    private static final String DST_FOLDER = "resource";
    private Button mBtStart;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_resources);
        initView();
        startTask();
    }

    private void initView() {
        mBtStart = findViewById(R.id.bt_start);
        mProgressBar = findViewById(R.id.progress);
    }

    public void startTask() {
        LoadResourcesTask mTask = new LoadResourcesTask(this);
        mTask.execute(DST_FOLDER);
    }

    @Override
    public void onStartTask() {
        mBtStart.setEnabled(false);
        mBtStart.setText("资源准备中");
        mProgressBar.setIndeterminate(true);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onEndTask(boolean result) {
        mProgressBar.setVisibility(View.GONE);
        if (result) {
            SharedPreferencesUtils.setResourceReady(this, result);
            ToastUtils.showShortMessage("资源准备就绪");
            mBtStart.setText("开始");
            mBtStart.setEnabled(true);
            finish();
        } else {
            ToastUtils.showShortMessage("资源处理失败");
        }
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

}
