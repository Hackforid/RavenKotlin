package com.smilehacker.raven.voice

import android.content.Context
import android.speech.tts.TextToSpeech
import com.smilehacker.raven.util.DLog
import java.util.*

/**
 * Created by kleist on 16/6/30.
 */
object TTSManager {

    private lateinit var mContext : Context
    private var mTTS : TextToSpeech? = null

    fun init(ctx: Context) {
        mContext = ctx.applicationContext
    }

    fun readText(text: String) {
        mTTS = TextToSpeech(mContext,
                TextToSpeech.OnInitListener {
                    DLog.d("tts it" + it)
                if (it == TextToSpeech.SUCCESS) {
                    val result = mTTS!!.setLanguage(Locale.CHINA)
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        DLog.d("tts not good " + result)
                    } else {
                        mTTS!!.speak(text, TextToSpeech.QUEUE_ADD, null)
                    }
                }
            })
    }
}
