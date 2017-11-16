package com.vtotem.lzy.lzylibrary.util

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.Application
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.os.Build
import android.provider.Settings
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import com.readystatesoftware.chuck.ChuckInterceptor
import com.vtotem.lzy.lzylibrary.R
import com.vtotem.lzy.lzylibrary.base.BaseFragment
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by lizheyi on 2017/11/8 16:24.
 * Email: lizheyi@vtotem.com
 */


/**
 * Toast
 */
fun Context.showToast(message: String): Toast {
    var toast: Toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.show()
    return toast
}

/**
 * Activity添加Fragment
 */
inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
    val fragmentTransaction = beginTransaction()
    fragmentTransaction.func()
    fragmentTransaction.commit()
}

fun AppCompatActivity.addFragment(fragment: BaseFragment, frameId: Int) {
    supportFragmentManager.inTransaction { add(frameId, fragment) }
}

/**
 *  Intent
 */
inline fun <reified T : Activity> Activity.startNewIntent() = startActivity(Intent(this, T::class.java))

/**
 *  Loadding
 */
fun showLoadingDialog(context: Context): ProgressDialog {
    val progressDialog = ProgressDialog(context)
    progressDialog.show()
    if (progressDialog.window != null) {
        progressDialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
    progressDialog.setContentView(R.layout.progress_dialog)
    progressDialog.isIndeterminate = true
    progressDialog.setCancelable(false)
    progressDialog.setCanceledOnTouchOutside(false)
    return progressDialog
}

/**
 *  获取设备ID
 */
@SuppressLint("all")
fun getDeviceId(context: Context): String {
    return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
}

/**
 *  是否是手机号
 */
fun isPhoneNum(phoneNum: String): Boolean {
    val telRegex = "13\\d{9}|14[5789]\\d{8}|15[^4]\\d{8}|17[013567]\\d{8}|18\\d{9}"
    return phoneNum.matches(telRegex.toRegex())
}

/**
 *  是否是邮箱
 */
fun isEmailValid(email: String): Boolean {
    val pattern: Pattern
    val matcher: Matcher
    val EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    pattern = Pattern.compile(EMAIL_PATTERN)
    matcher = pattern.matcher(email)
    return matcher.matches()
}

/**
 *  创建Retrofit初始化
 */
object ServiceFactory {

    private fun getLogInterceptor(): Interceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    fun <T> createRetrofitService(BASE_URL: String, clazz: Class<T>, context: Context): T {
        val builder = OkHttpClient.Builder().
                addInterceptor(getLogInterceptor()).
                addInterceptor(CacheInterceptor).
                addInterceptor(ChuckInterceptor(context)).
                readTimeout(15, TimeUnit.SECONDS).
                writeTimeout(15, TimeUnit.SECONDS).
                connectTimeout(15, TimeUnit.SECONDS)

        val retro = Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(builder.build()).build()
        return retro.create(clazz)
    }
}

/**
 * Px、Dp转换 改变图片灰色
 */

object View {
    fun pxToDp(px: Float): Int {
        val densityDpi = Resources.getSystem().getDisplayMetrics().density
        return ((px * densityDpi + 0.5).toInt())
    }

    fun dpToPx(dp: Float): Int {
        val density = Resources.getSystem().getDisplayMetrics().density
        return Math.round(dp * density)
    }

    fun changeIconDrawableToGray(context: Context, drawable: Drawable?) {
        if (drawable != null) {
            drawable.mutate()
            drawable.setColorFilter(ContextCompat
                    .getColor(context, R.color.dark_gray), PorterDuff.Mode.SRC_ATOP)
        }
    }
}


/**
 *  关于网络
 */
@SuppressLint("StaticFieldLeak")
object Network {
    private var connManager: ConnectivityManager? = null
    val NETWORN_NONE = 0
    val NETWORN_WIFI = 1
    val NETWORN_MOBILE = 2
    private var mContext: Context? = null
    //获取网络状态
    val networkState: Int
        get() {
            if (isConnected) {
                when (connManager!!.activeNetworkInfo.type) {
                    ConnectivityManager.TYPE_WIFI -> return NETWORN_WIFI
                    ConnectivityManager.TYPE_MOBILE -> return NETWORN_MOBILE
                }
            } else return NETWORN_NONE
            return NETWORN_NONE
        }

    //检查网络是否连接
    val isConnected: Boolean
        get() {
            return connManager!!.activeNetworkInfo.isConnected
        }

    fun initNetUtil(context: Context) {
        mContext = context
        connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    //设置显示系统设置网络
    fun showWifiDlg() {
        var intent: Intent? = null
        if (android.os.Build.VERSION.SDK_INT > 10) {
            intent = Intent(Settings
                    .ACTION_WIFI_SETTINGS)
        } else {
            intent = Intent()
            intent.setClassName("com.android.settings",
                    Settings.ACTION_WIFI_SETTINGS)
        }
        if (mContext is Application) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        mContext!!.startActivity(intent)
    }
}


object CacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain?): Response {
        var request = chain!!.request()
        val netAvailable = Network.isConnected

        if (netAvailable) {
            request = request.newBuilder()
                    //网络可用 强制从网络获取数据
                    .cacheControl(CacheControl.FORCE_NETWORK)
                    .build()
        } else {
            request = request.newBuilder()
                    //网络不可用 从缓存获取
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build()
        }
        var response = chain.proceed(request)
        if (netAvailable) {
            response = response.newBuilder()
                    .removeHeader("Pragma")
                    // 有网络时 设置缓存超时时间1个小时
                    .header("Cache-Control", "public, max-age=" + 60 * 60)
                    .build()
        } else {
            response = response.newBuilder()
                    .removeHeader("Pragma")
                    // 无网络时，设置超时为1周
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + 7 * 24 * 60 * 60)
                    .build()
        }
        return response
    }

}

object NetWorkErrorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain?): Response {
        val request = chain!!.request()
        val proceed = chain.proceed(request)
        return proceed
    }

}

/**
 *  获取屏幕宽高
 */
object Screen {

    fun getScreenWidth(context: Context): Int {
        val windowManager = context
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        return dm.widthPixels
    }

    fun getScreenHeight(context: Context): Int {
        val windowManager = context
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        return dm.heightPixels
    }

    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources
                .getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
}

/**
 *  键入
 */
object Keyboard {

    fun hideSoftInput(activity: Activity) {
        var view = activity.currentFocus
        if (view == null) view = View(activity)
        val imm = activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showSoftInput(edit: EditText, context: Context) {
        edit.isFocusable = true
        edit.isFocusableInTouchMode = true
        edit.requestFocus()
        val imm = context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(edit, 0)
    }

    fun toggleSoftInput(context: Context) {
        val imm = context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }
}

/**
 *  沉侵式状态栏
 */
object StatusBarCompat {
    private val INVALID_VAL = -1
    private val COLOR_DEFAULT = Color.parseColor("#20000000")

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun compat(activity: Activity, statusColor: Int) {

        //当前手机版本为5.0及以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (statusColor != INVALID_VAL) {
                activity.window.statusBarColor = statusColor
            }
            return
        }

        //当前手机版本为4.4
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            var color = COLOR_DEFAULT
            val contentView = activity.findViewById<ViewGroup>(android.R.id.content)
            if (statusColor != INVALID_VAL) {
                color = statusColor
            }
            val statusBarView = View(activity)
            val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(activity))
            statusBarView.setBackgroundColor(color)
            contentView.addView(statusBarView, lp)
        }

    }

    fun compat(activity: Activity) {
        compat(activity, INVALID_VAL)
    }


    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

}