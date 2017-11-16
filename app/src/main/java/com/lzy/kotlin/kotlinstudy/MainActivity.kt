package com.lzy.kotlin.kotlinstudy

import android.os.Bundle
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.vtotem.lzy.lzylibrary.base.BaseActivity
import com.vtotem.lzy.lzylibrary.util.Network
import com.vtotem.lzy.lzylibrary.util.View
import com.vtotem.lzy.lzylibrary.util.showToast

class MainActivity : BaseActivity() {

    override fun onLoadmore(refreshlayout: RefreshLayout?) {
    }

    override fun onRefresh(refreshlayout: RefreshLayout?) {
        showToast("" + Network.isConnected + "\n" + Network.networkState)
//        showLoading()
//        button1.setText("asdf")
    }

    override fun isOpenRefreshAndLoadmore(): Boolean {
        return true
    }


    override fun isSteepStatusBar(): Boolean {
        return true
    }

    override fun bindLayout(): Int {
        return R.layout.activity_main
    }

    var apiService: APIService? = null
    override fun doBusiness() {
        showToast("" + View.pxToDp(448F))
        showToast("" + View.pxToDp(256F))
//        apiService = ServiceFactory.createRetrofitService("", APIService::class.java, this)
//        showToast("" + Network.isConnected + "\n" + Network.networkState)
//        addFragment(BlankFragment(), R.id.main_container)
//        button1.setOnClickListener {
//            Network.showWifiDlg()
//        }
    }

    var index = 0
    var whenindex = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        setContentView(R.layout.activity_main)
//        showToast("求和：1+2=" + sum(1, 2))
//        showToast("求最大值：1,2" + max(1, 2))
//        for (item in itemsString) showToast("for循环：" + item)
//        while (index < itemsString.size) {
//            showToast("while循环：" + itemsString[index])
//            index++
//        }
//        when (whenindex) {
////            is String -> showToast("如果是String类型的")
//            0, 1 -> showToast("when语句：如果是0或者1")
//            2 -> showToast("when语句：" + 2)
//            3 -> showToast("when语句：" + 3)
//            in 4..10 -> showToast("在4-10之间")
//            !in 10..20 -> showToast("不在10-20之间")
//            else -> {
//                showToast("gg")
//            }
//        }
//        button1.setOnClickListener { newIntent<Main2Activity>() }
//        aaa@ for (abc in itemsInt) {
//            Log.d("1234", "" + abc)
//            hehe@ for (def in itemsInt) {
//                Log.d("1234", "" + def)
//                if (def == 2) break@aaa
//            }
//        }
    }


}
