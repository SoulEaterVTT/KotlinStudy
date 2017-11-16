package com.vtotem.lzy.lzylibrary.base

import android.app.ProgressDialog
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.Window
import android.widget.FrameLayout
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.vtotem.lzy.lzylibrary.R
import com.vtotem.lzy.lzylibrary.custom.LzyObserver
import com.vtotem.lzy.lzylibrary.util.Network
import com.vtotem.lzy.lzylibrary.util.StatusBarCompat
import com.vtotem.lzy.lzylibrary.util.showLoadingDialog
import kotlinx.android.synthetic.main.activity_container.*

/**
 * Created by lizheyi on 2017/11/8 16:19.
 * Email: lizheyi@vtotem.com
 */

abstract class BaseActivity : AppCompatActivity(), LifecycleOwner, OnRefreshListener, OnLoadmoreListener {
    private val lifecycleRegistry = LifecycleRegistry(this@BaseActivity)
    private val mProgressDialog: ProgressDialog by lazy {
        showLoadingDialog(this)
    }
    private lateinit var container: FrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleRegistry.addObserver(LzyObserver())
        if (isSteepStatusBar()) {
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
            StatusBarCompat.compat(this, Color.BLACK)
        }
        val view = layoutInflater.inflate(R.layout.activity_container, null)
        setContentView(view)
        initDefaultView(bindLayout())
        if (isOpenRefreshAndLoadmore()) {
            refreshLayout.setEnableRefresh(true)
            refreshLayout.setEnableLoadmore(true)
            refreshLayout.setOnRefreshListener(this)
            refreshLayout.setOnLoadmoreListener(this)
        } else {
            refreshLayout.setEnableRefresh(false)
            refreshLayout.setEnableLoadmore(false)
        }
        doBusiness()
    }

    private fun initDefaultView(layoutResID: Int) {
        container = findViewById(R.id.container)
        val childView = LayoutInflater.from(this).inflate(layoutResID, null)
        container.addView(childView, 0)
    }

    //是否沉侵式状态栏
    abstract fun isSteepStatusBar(): Boolean

    //绑定视图
    abstract fun bindLayout(): Int

    //业务操作
    abstract fun doBusiness()

    abstract fun isOpenRefreshAndLoadmore(): Boolean

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    //显示加载动画
    fun showLoading() {
        hideLoading()
        mProgressDialog.show()
    }

    //隐藏加载动画
    fun hideLoading() {
        if (mProgressDialog.isShowing) {
            mProgressDialog.cancel()
        }
    }

    //获取当前网络状态
    fun isNetWorkConnected(): Boolean {
        return Network.isConnected
    }

}
