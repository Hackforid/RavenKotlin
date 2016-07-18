package com.smilehacker.raven.kit

import android.support.annotation.DrawableRes
import com.smilehacker.raven.R

/**
 * Created by zhouquan on 16/7/17.
 */
object VoiceMaker {
    data class VoiceSymbol(val id: Int, val symbol: String, val text: String, @DrawableRes val icon: Int)

    val voiceSymbols = arrayOf(
            VoiceSymbol(1, "[title]", "title", R.drawable.input_title),
            VoiceSymbol(2, "[message]", "message", R.drawable.input_title),
            VoiceSymbol(3, "[ticker]", "ticker", R.drawable.input_title),
            VoiceSymbol(4, "[appname]", "appname", R.drawable.input_title)
    )
}
