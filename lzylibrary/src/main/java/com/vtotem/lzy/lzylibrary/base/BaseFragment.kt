package com.vtotem.lzy.lzylibrary.base

import android.app.ProgressDialog
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.vtotem.lzy.lzylibrary.R
import com.vtotem.lzy.lzylibrary.custom.LzyObserver
import com.vtotem.lzy.lzylibrary.util.Network
import com.vtotem.lzy.lzylibrary.util.showLoadingDialog
import kotlinx.android.synthetic.main.activity_container.view.*

/**
 * Created by lizheyi on 2017/11/9 17:27.
 * Email: lizheyi@vtotem.com
 */

abstract class BaseFragment : Fragment(), LifecycleOwner, OnRefreshListener, OnLoadmoreListener {

    private val lifecycleRegistry = LifecycleRegistry(this)
    private val mProgressDialog: ProgressDialog by lazy {
        showLoadingDialog(context)
    }
    private lateinit var container: FrameLayout

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        lifecycleRegistry.addObserver(LzyObserver())
        val view = layoutInflater.inflate(R.layout.activity_container, null)
        initDefaultView(bindLayout(), view)
        if (isOpenRefreshAndLoadmore()) {
            view.refreshLayout.setEnableRefresh(true)
            view.refreshLayout.setEnableLoadmore(true)
            view.refreshLayout.setOnRefreshListener(this)
            view.refreshLayout.setOnLoadmoreListener(this)
        } else {
            view.refreshLayout.setEnableRefresh(false)
            view.refreshLayout.setEnableLoadmore(false)
        }
        doBusiness()
        return view
    }

    private fun initDefaultView(layoutResID: Int, view: View) {
        container = view.findViewById(R.id.container)
        val childView = LayoutInflater.from(context).inflate(layoutResID, null)
        container.addView(childView, 0)
    }

    //绑定视图
    abstract fun bindLayout(): Int

    //业务操作
    abstract fun doBusiness()

    abstract fun isOpenRefreshAndLoadmore(): Boolean

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
