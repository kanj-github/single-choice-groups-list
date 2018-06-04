package com.example.kanj.swiggyjunk.adapters

import android.support.v7.widget.RecyclerView

class VariationGroup(val name: String) : ListableInWeird {
    override fun getType() = ListableInWeird.SUBSECTION_TITLE

    override fun bind(pos: Int, viewHolder: RecyclerView.ViewHolder, listener: WeirdAdapterListener) {
        if (viewHolder is WeirdAdapter.SubsectionTitleViewHolder) {
            viewHolder.title.setText(name)
        }
    }

    override fun setEnabledState(enable: Boolean) {
        // Not required
    }

    override fun setCheckedState(checked: Boolean) {
        // Not required
    }
}
