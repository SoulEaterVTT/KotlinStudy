package com.lzy.kotlin.kotlinstudy

/**
 * Created by lizheyi on 2017/8/2 10:46.
 * 1.var 关键字声明可变属性, val 关键字声明只读属性。
 * 2.?表示可空
 * 3.sealed关键字声明密封类（密封类的好处在于when表达式）
 */

/**
 *函数体
 */
//求和
fun sum(num1: Int, num2: Int): Int = num1 + num2

//求最大值
fun max(num1: Int, num2: Int): Int = if (num1 > num2) num1 else num2

val itemsString = listOf("1", "2", "3")
val itemsInt = listOf(1, 2, 3)


