package com.smilehacker.raven.ui.index.index

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import butterknife.bindView
import com.smilehacker.raven.R
import com.smilehacker.raven.base.App
import com.smilehacker.raven.model.AppInfo
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.toSingletonObservable
import rx.schedulers.Schedulers
import java.util.*

/**
 * Created by kleist on 16/6/28.
 */
class AppAdapter(val ctx : Context, val mAppCallback : AppCallback) : RecyclerView.Adapter<AppAdapter.ViewHolder>() {

    private val mApps : MutableList<AppInfo> = ArrayList()

    private val mOnAppCheckboxChangeListener = CompoundButton.OnCheckedChangeListener {
        button, checked ->
            val packageName = button.getTag(R.string.tag_key_packagename) as String?
            if (packageName != null) {
                setAppUIChecked(packageName, checked)
                mAppCallback.onAppEnableChange(packageName, checked)
            }
    }

    private val mOnAppCBClickListener = View.OnClickListener {
        view ->
        val checkBox = view as CheckBox
        val packageName = view.getTag(R.string.tag_key_packagename) as String?
        if (packageName != null) {
            setAppUIChecked(packageName, checkBox.isChecked)
            mAppCallback.onAppEnableChange(packageName, checkBox.isChecked)
        }
    }

    private val mOnAppClickListener = View.OnClickListener {
        view ->
        val app = view.getTag(R.string.tag_key_app)
        if (app != null) {
            mAppCallback.onOpenAppConfig(app as AppInfo)
        }
    }

    fun setApps(apps: MutableList<AppInfo>) {
        mApps.clear()
        mApps.addAll(apps)
        notifyDataSetChanged()
    }

    private fun setAppUIChecked(packageName: String, checked: Boolean) {
        val app = mApps.find { it.packageName.equals(packageName) }
        if (app != null) {
            if (app.enable != checked) {
                app.enable = checked
                Handler(Looper.getMainLooper()).post {
                    notifyItemChanged(mApps.indexOf(app))
                }
            }
        }
    }


    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val app = mApps[position]
        holder?.name?.text = app.appName

        holder?.icon?.setTag(R.string.tag_key_icon, app.packageName)
        app.packageName.toSingletonObservable()
            .observeOn(Schedulers.io())
            .map {
                getIcon(App.inst, it)
            }
            .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val tag = holder?.icon?.getTag(R.string.tag_key_icon) as String?
                    if (tag != null && tag.equals(app.packageName)) {
                        if (it == null) {

                        } else {
                            holder?.icon?.setImageDrawable(it)
                        }
                    }
                })

        holder?.checkBox?.isChecked = app.enable
        holder?.checkBox?.setTag(R.string.tag_key_packagename, app.packageName)
        holder?.checkBox?.setOnClickListener(mOnAppCBClickListener)
        holder?.itemView?.setTag(R.string.tag_key_app, app)
        holder?.itemView?.setOnClickListener(mOnAppClickListener)
    }


    fun getIcon(ctx: Context, packageName: String) : Drawable? {
        val pm = ctx.packageManager
        try {
            return pm.getApplicationIcon(packageName)
        } catch (e : PackageManager.NameNotFoundException) {
            return null
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        return ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_app, parent, false))
    }

    override fun getItemCount(): Int {
        return mApps.size
    }


    class ViewHolder : RecyclerView.ViewHolder {

        val icon by bindView<ImageView>(R.id.iv_icon)
        val name by bindView<TextView>(R.id.tv_name)
        val checkBox by bindView<CheckBox>(R.id.cb_enable)

        constructor(itemView: View?) : super(itemView) {

        }


    }

    interface AppCallback {
        fun onAppEnableChange(packageName: String, enable: Boolean)
        fun onOpenAppConfig(app: AppInfo)
    }
}
