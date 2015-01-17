package com.leilei.cropimg.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

/**
 * USER: liulei
 * DATA: 2015/1/17
 * TIME: 13:52
 */
public class BaseActivity extends ActionBarActivity {

    public ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getSupportActionBar();
    }
}
