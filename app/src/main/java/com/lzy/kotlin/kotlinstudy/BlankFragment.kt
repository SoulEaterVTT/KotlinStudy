package com.lzy.kotlin.kotlinstudy

import android.support.v4.app.Fragment
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.vtotem.lzy.lzylibrary.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_blank.*


/**
 * A simple [Fragment] subclass.
 */
class BlankFragment : BaseFragment() {

    override fun onRefresh(refreshlayout: RefreshLayout?) {
        fragmentBtntest.setText("呵呵")
    }

    override fun onLoadmore(refreshlayout: RefreshLayout?) {
    }

    override fun bindLayout(): Int {
        return R.layout.fragment_blank
    }

    override fun doBusiness() {
    }

    override fun isOpenRefreshAndLoadmore(): Boolean {
        return true
    }
}
