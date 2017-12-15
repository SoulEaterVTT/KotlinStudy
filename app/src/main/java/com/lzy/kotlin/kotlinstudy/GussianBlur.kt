package com.lzy.kotlin.kotlinstudy

import android.graphics.Bitmap
import android.graphics.Color

/**
 * Created by Administrator on 2017/11/21.
 * 高斯模糊
 */

class GussianBlur {
    var sigma = 15   //通过调节该参数设置高斯模糊的模糊程度，越大越模糊
    private var radius = 3 * sigma
    var kernel = DoubleArray(radius + 1)  //kernel[i]表示离中心点距离为i的权重

    /**
     * 初始化卷积核
     * 0.39894 = 1 / (Math.sqrt(2 * PI))
     */
    private fun initKernel() {
        var sum = 0.0
        for (i in kernel.indices) {
            kernel[i] = 0.39894 * Math.exp(-(i.toDouble() * i.toDouble() * 1.0) / (2.0 * sigma.toDouble() * sigma.toDouble())) / sigma
            if (i > 0) {
                sum = sum + kernel[i] * 2.0
            } else {
                sum = sum + kernel[i]
            }
        }
        for (i in kernel.indices) {
            kernel[i] = kernel[i] / sum
        }
    }

    /**
     * 高斯模糊
     */
    fun blur(bitmap: Bitmap): Bitmap {
        val w = bitmap.width
        val h = bitmap.height
        val pix = IntArray(w * h)
        bitmap.getPixels(pix, 0, w, 0, 0, w, h)
        val tmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val result = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        //横向
        for (i in 0 until w * h) {
            val x = i % w
            val y = i / w
            val sum = DoubleArray(4)
            for (j in -radius..radius) {
                val currentX = Math.min(Math.max(x + j, 0), w - 1)
                val index = y * w + currentX
                val a = Color.alpha(pix[index])
                val r = Color.red(pix[index])
                val g = Color.green(pix[index])
                val b = Color.blue(pix[index])
                sum[0] = sum[0] + a * kernel[Math.abs(j)]
                sum[1] = sum[1] + r * kernel[Math.abs(j)]
                sum[2] = sum[2] + g * kernel[Math.abs(j)]
                sum[3] = sum[3] + b * kernel[Math.abs(j)]
            }
            val rc = Color.argb(sum[0].toInt(), sum[1].toInt(), sum[2].toInt(), sum[3].toInt())
            tmp.setPixel(x, y, rc)
        }
        tmp.getPixels(pix, 0, w, 0, 0, w, h)
        //纵向
        for (i in 0 until w * h) {
            val x = i % w
            val y = i / w
            val sum = DoubleArray(4)
            for (j in -radius..radius) {
                val currentY = Math.min(Math.max(y + j, 0), h - 1)
                val index = currentY * w + x
                val r = Color.red(pix[index])
                val g = Color.green(pix[index])
                val b = Color.blue(pix[index])
                val a = Color.alpha(pix[index])
                sum[0] = sum[0] + a * kernel[Math.abs(j)]
                sum[1] = sum[1] + r * kernel[Math.abs(j)]
                sum[2] = sum[2] + g * kernel[Math.abs(j)]
                sum[3] = sum[3] + b * kernel[Math.abs(j)]
            }
            val rc = Color.argb(sum[0].toInt(), sum[1].toInt(), sum[2].toInt(), sum[3].toInt())
            result.setPixel(x, y, rc)
        }
        return result
    }

    private fun setRadius(radius: Int) {
        this.sigma = radius
        this.radius = 3 * sigma
        this.kernel = DoubleArray(this.radius + 1)
        initKernel()
    }

    companion object {
        val INSTANCE = GussianBlur()

        fun getInstance(radius: Int): GussianBlur {
            INSTANCE.setRadius(radius)
            return INSTANCE
        }

        val instance: GussianBlur
            get() {
                INSTANCE.setRadius(15)
                return INSTANCE
            }
    }
}
