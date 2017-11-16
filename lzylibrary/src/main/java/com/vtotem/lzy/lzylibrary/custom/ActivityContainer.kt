package com.vtotem.lzy.lzylibrary.custom

import android.app.Activity

import java.util.ArrayList

/**
 * Created by lizheyi on 2017/11/13 17:28.
 * Email: lizheyi@vtotem.com
 */

class ActivityContainer private constructor() {

    fun addActivity(activity: Activity) {
        activityStack.add(activity)
    }

    fun removeActivity(activity: Activity) {
        activityStack.remove(activity)
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        var i = 0
        val size = activityStack.size
        while (i < size) {
            if (null != activityStack[i]) {
                activityStack[i].finish()
            }
            i++
        }
        activityStack.clear()
    }

    companion object {

        val instance = ActivityContainer()
        private val activityStack = ArrayList<Activity>()
    }

}
