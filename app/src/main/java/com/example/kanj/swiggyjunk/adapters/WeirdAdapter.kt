package com.example.kanj.swiggyjunk.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.example.kanj.swiggyjunk.R

class WeirdAdapter(val list: List<ListableInWeird>, val listener: WeirdAdapterListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount() = list.size

    override fun getItemViewType(position: Int) = list[position].getType()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val infalter = LayoutInflater.from(parent.context)
        val v = infalter.inflate(
                when (viewType) {
                    ListableInWeird.SUBSECTION_TITLE -> R.layout.item_category
                    else -> R.layout.item_variation // Only 2 possibilities
                },
                parent,
                false
        )

        return when (viewType) {
            ListableInWeird.SUBSECTION_TITLE -> SubsectionTitleViewHolder(v)
            else -> SubsectionItemViewHolder(v)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        list[position].bind(position, holder, listener)
    }

    private fun handleSubsectionItemClick(pos: Int, selectState: Boolean) {
        val item = list[pos]

        if (item.getType() == ListableInWeird.SUBSECTION_ITEM) {
            val variation = item as Variation
            variation.selected = selectState
            listener.onVariationClicked(pos, selectState, variation.groupId, variation.varId)
        }
    }

    fun setEnabledStates(affected: ArrayList<Int>, enabled: Boolean) {
        affected.forEach({
            list[it].setEnabledState(enabled)
            notifyItemChanged(it)
        })
    }

    fun setCheckedState(pos: Int, checked: Boolean) {
        list[pos].setCheckedState(checked)
        notifyItemChanged(pos)
    }

    inner class SubsectionTitleViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView

        init {
            title = v.findViewById(R.id.tv_title)
        }
    }

    inner class SubsectionItemViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val name: TextView
        val stock: TextView
        val price: TextView
        val check: CheckBox

        init {
            name = v.findViewById(R.id.tv_name)
            stock = v.findViewById(R.id.tv_stock)
            price = v.findViewById(R.id.tv_price)
            check = v.findViewById(R.id.cb_check)

            v.setOnClickListener({
                handleSubsectionItemClick(adapterPosition, !check.isChecked)
            })
        }
    }
}