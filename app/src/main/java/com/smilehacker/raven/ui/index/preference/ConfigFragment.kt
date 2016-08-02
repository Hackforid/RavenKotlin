package com.smilehacker.raven.ui.index.preference

import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.smilehacker.raven.Megatron.KitPreferenceFragment
import com.smilehacker.raven.R
import com.smilehacker.raven.kit.ConfigManager
import com.smilehacker.raven.util.DLog
import org.jetbrains.anko.find

/**
 * Created by kleist on 16/7/26.
 */
class ConfigFragment: KitPreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    private val mSilencePref by lazy { findPreference(getString(R.string.pref_silence_time_key)) }

    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {
    }

    private lateinit var mToolbar : Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preference)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.frg_config, container, false)
        mToolbar = view.find(R.id.toolbar)
        view.find<LinearLayout>(R.id.container).addView(super.onCreateView(inflater, container, savedInstanceState))
        initToolbar()
        return view
    }

    override fun onCreatePreferences(p0: Bundle?, p1: String?) {
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initToolbar() {
        hostActivity.setSupportActionBar(mToolbar)
        mToolbar.title = "设置"
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        mToolbar.setNavigationOnClickListener {
            finish()
        }
        hostActivity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun initView() {
        mSilencePref.setOnPreferenceClickListener {
            showSilenceTimePicker()
            true
        }

        refreshSilenceTime()
    }

    override fun onVisible() {
        super.onVisible()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onInvisible() {
        super.onInvisible()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    private fun showSilenceTimePicker() {
        val time = ConfigManager.silenceTime
        var startHour : Int = time[0]
        var startMinute : Int = time[1]
        var endHour : Int = time[2]
        var endMinute : Int = time[3]
        fun showSilenceTimePickerEnd() {
            val dialog = TimePickerDialog(hostActivity,
                    TimePickerDialog.OnTimeSetListener {
                        view, hourOfDay, minute ->
                        DLog.i("hour=$hourOfDay min=$minute")
                        endHour = hourOfDay; endMinute = minute
                        saveSilenceTime(startHour, startMinute, endHour, endMinute)
                    },
                    endHour, endMinute, true)
            //dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", { dialogInterface, i -> dialogInterface.dismiss() })
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "下一步", {dialogInterface, i -> dialog.onClick(dialogInterface, DialogInterface.BUTTON_POSITIVE)})
            dialog.setTitle("请选择静音时段结束时间")
            dialog.show()
        }
        fun showSilenceTimePickerStart() {
            val dialog = TimePickerDialog(hostActivity,
                    TimePickerDialog.OnTimeSetListener {
                        view, hourOfDay, minute ->
                        DLog.i("hour=$hourOfDay min=$minute")
                        startHour = hourOfDay; startMinute = minute;
                        showSilenceTimePickerEnd()},
                    startHour, startMinute, true)
            //dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", { dialogInterface, i ->  dialog. dialogInterface.dismiss() })
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "下一步", {dialogInterface, i -> dialog.onClick(dialogInterface, DialogInterface.BUTTON_POSITIVE)})
            dialog.setTitle("请选择静音时段开始时间")
            dialog.show()
        }


        showSilenceTimePickerStart()
    }

    private fun saveSilenceTime(startHour : Int, startMinute : Int, endHour : Int, endMinute : Int) {
        DLog.i("select $startHour:$startMinute to $endHour:$endMinute")
        ConfigManager.silenceTime = intArrayOf(startHour, startMinute, endHour, endMinute)
        refreshSilenceTime()
    }

    private fun refreshSilenceTime() {
        val time = ConfigManager.silenceTime
        val summary = String.format("%02d:%02d - %02d:%02d", time[0], time[1], time[2], time[3])
        mSilencePref.summary = summary
    }
}
