package com.example.kanj.swiggyjunk.adapters

import android.support.v7.widget.RecyclerView

interface ListableInWeird {
    fun getType(): Int

    fun bind(pos: Int, viewHolder: RecyclerView.ViewHolder, listener: WeirdAdapterListener)

    fun setEnabledState(enable: Boolean)

    fun setCheckedState(checked: Boolean)

    companion object {
        val SUBSECTION_TITLE = 0
        val SUBSECTION_ITEM = 1
    }
}
