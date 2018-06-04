package com.example.kanj.swiggyjunk.adapters

interface WeirdAdapterListener {
    fun onVariationClicked(pos: Int, selectState: Boolean, groupId: String, variationId: String)
}