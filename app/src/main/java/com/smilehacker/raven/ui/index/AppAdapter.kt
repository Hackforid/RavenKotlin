package com.smilehacker.raven.ui.index

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.bindView
import com.smilehacker.raven.R
import com.smilehacker.raven.model.AppInfo
import rx.lang.kotlin.toSingletonObservable
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
        app.packageName.toSingletonObservable()

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
