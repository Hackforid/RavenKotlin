package com.smilehacker.raven.ui.index

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
class AppAdapter(val ctx : Context) : RecyclerView.Adapter<AppAdapter.ViewHolder>() {

    private val mApps : MutableList<AppInfo> = ArrayList()

    fun setApps(apps: MutableList<AppInfo>) {
        mApps.clear()
        mApps.addAll(apps)
        notifyDataSetChanged()
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

        constructor(itemView: View?) : super(itemView) {

        }


    }
}
