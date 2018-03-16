package com.hengfeng.arcface.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.SparseArray;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by feng on 2017/3/2.
 */
public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener {

    private SparseArray<View> mViews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //屏幕常亮
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mViews = new SparseArray<>();
        if(getLayoutResId() > 0) {
            setContentView(getLayoutResId());
        }
        initViews();
        initData();
        initListener();
    }

    /**
     * findViewById 简写
     * @param viewId
     * @param <E>
     * @return
     */
    public <E extends View> E F(int viewId) {

        E view = (E) mViews.get(viewId);
        if (null == view) {
            view = (E) findViewById(viewId);
            mViews.put(viewId, view);
        }
        return view;
    }

    public <E extends View> void C(E view) {

        view.setOnClickListener(this);
    }

    @Override
    public abstract void onClick(View v);

    public abstract int getLayoutResId();

    public abstract void initViews();

    public abstract void initData();

    public abstract void initListener();
}
