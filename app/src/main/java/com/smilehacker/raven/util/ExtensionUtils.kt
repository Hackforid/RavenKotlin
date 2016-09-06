package com.smilehacker.raven.util

/**
 * Created by kleist on 16/8/3.
 */

fun String.isChinese() : Boolean {
    val c = this.first()
    return isChineseByBlock(c)
}

fun isChineseByBlock(c: Char): Boolean {
    val ub = Character.UnicodeBlock.of(c)
    if (ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
            || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
            || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
            || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C
            || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D
            || ub === Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
            || ub === Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT) {
        return true
    } else {
        return false
    }
}

inline fun <T> Array<out T>?.isNullOrEmpty() : Boolean = this == null || this.isEmpty()

inline fun <T> T?.nullOr(value : T) : T = if (this != null) this else value

