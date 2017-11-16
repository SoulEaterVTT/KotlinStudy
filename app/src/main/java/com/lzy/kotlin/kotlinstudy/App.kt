package com.lzy.kotlin.kotlinstudy

import android.app.Application
import com.iflytek.cloud.SpeechConstant
import com.iflytek.cloud.SpeechUtility
import com.vtotem.lzy.lzylibrary.util.Network

/**
 * Created by lizheyi on 2017/11/10 17:46.
 * Email: lizheyi@vtotem.com
 */
class App : Application() {
    override fun onCreate() {
        SpeechUtility.createUtility(applicationContext, SpeechConstant.APPID + "=5a0bfb83")
        super.onCreate()
        Network.initNetUtil(applicationContext)
    }
}