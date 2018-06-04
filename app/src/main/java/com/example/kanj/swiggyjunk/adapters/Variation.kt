package com.example.kanj.swiggyjunk.adapters

import android.support.v7.widget.RecyclerView
import android.util.Log

class Variation(val name:String, val price: Int, val inStock: Int, val varId: String, val groupId: String)
    : ListableInWeird {
    var selected = false
    private var blockerCount = 0

    override fun getType() = ListableInWeird.SUBSECTION_ITEM

    override fun bind(pos: Int, viewHolder: RecyclerView.ViewHolder, listener: WeirdAdapterListener) {
        if (viewHolder is WeirdAdapter.SubsectionItemViewHolder) {
            viewHolder.check.setOnCheckedChangeListener(null)
            viewHolder.check.isChecked = selected
            viewHolder.name.setText(name)
            viewHolder.price.setText("₹" + price)
            viewHolder.stock.setText("Stock count = " + inStock)
            viewHolder.itemView.isEnabled = (blockerCount == 0)
            viewHolder.check.isEnabled = (blockerCount == 0)
            viewHolder.check.setOnCheckedChangeListener({_, isChecked ->
                Log.v("Kanj", "got on check " + isChecked + " name = " + name)
                selected = isChecked
                listener.onVariationClicked(pos, isChecked, groupId, varId)
            })
        }
    }

    override fun setEnabledState(enable: Boolean) {
        synchronized(this) {
            if (enable) {
                blockerCount --
            } else {
                blockerCount ++
            }
        }
        if (!enable) { // I don't think this is possible
            selected = false
        }
    }

    override fun setCheckedState(checked: Boolean) {
        this.selected = checked
    }
}
