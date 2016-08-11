package com.smilehacker.raven.voice

import android.app.Notification
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Message
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import com.smilehacker.raven.kit.AppData
import com.smilehacker.raven.kit.ConfigManager
import com.smilehacker.raven.kit.VoiceMaker
import com.smilehacker.raven.util.Callback
import java.util.*

/**
 * Created by kleist on 16/6/30.
 */
object TTSManager {

    private lateinit var mContext : Context
    private var mTTS : TextToSpeech? = null

    private val KEY_PARAM_UTTERANCE_ID = "10010"
    private val MSG_RELEASE_TTS = 1

    private lateinit var mAppData : AppData

    val mHandler : Handler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if (msg != null && msg.what == MSG_RELEASE_TTS) {
                if (mTTS == null) {
                    return
                }
                if (!mTTS!!.isSpeaking) {
                    releaseTTS()
                } else {
                    sendDelayReleaseTTSMsg()
                }
            }
        }
    }

    fun init(ctx: Context) {
        mContext = ctx.applicationContext
        mAppData = AppData(mContext)
    }

    fun readText(packageName: String, notification: Notification) {

        val app = mAppData.getAppByPackage(packageName)
        if (!ConfigManager.isEnable || app == null || !app.enable || "".equals(app.voiceFormat?.replace(" ", ""))) {
            return
        }
        val text = VoiceMaker.makeVoice(packageName, notification, app.voiceFormat ?: VoiceMaker.default_voice_format)


        if (mTTS == null) {
            mTTS = TextToSpeech(mContext,
                    TextToSpeech.OnInitListener {
                        if (it == TextToSpeech.SUCCESS) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                mTTS!!.speak(text, TextToSpeech.QUEUE_ADD, null, text)
                            } else {
                                val map = HashMap<String, String>()
                                map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, text)
                                mTTS!!.speak(text, TextToSpeech.QUEUE_ADD, map)
                            }
                        }
                    })
            mTTS!!.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onDone(p0: String?) {
                    sendDelayReleaseTTSMsg()
                }

                override fun onError(p0: String?) {
                    sendDelayReleaseTTSMsg()
                }

                override fun onStart(p0: String?) {
                }

            })
        } else {
            mTTS!!.speak(text, TextToSpeech.QUEUE_ADD, null, text)
        }

    }

    fun sendDelayReleaseTTSMsg() {
        mHandler.sendEmptyMessageDelayed(MSG_RELEASE_TTS, 5000)
    }

    fun releaseTTS() {
        if (mTTS != null) {
            mTTS!!.shutdown()
            mTTS = null
        }
    }

    fun checkTTS(callback: Callback<Void>) {
        var tts : TextToSpeech? = null
        tts = TextToSpeech(mContext,
                TextToSpeech.OnInitListener {
                    if (it == TextToSpeech.ERROR) {
                        callback.onError(Exception("未设置TTS"))
                    } else {
                        val result = tts!!.isLanguageAvailable(mContext.resources.configuration.locale)
                        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                            callback.onError(Exception("语言不支持"))
                        } else {
                            callback.onResult()
                        }
                    }
                    tts!!.shutdown()
                })
    }


}
