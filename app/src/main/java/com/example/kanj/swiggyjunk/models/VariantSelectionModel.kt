package com.example.kanj.swiggyjunk.models

import android.arch.lifecycle.ViewModel
import com.example.kanj.swiggyjunk.adapters.ListableInWeird
import com.example.kanj.swiggyjunk.adapters.Variation
import com.example.kanj.swiggyjunk.adapters.VariationGroup
import com.example.kanj.swiggyjunk.api.MyJsonService
import com.example.kanj.swiggyjunk.api.model.response.Exclude
import com.example.kanj.swiggyjunk.api.model.response.VariantGroups
import com.example.kanj.swiggyjunk.dagger.AppComponent
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class VariantSelectionModel : ViewModel() {
    @Inject lateinit var jsonService: MyJsonService

    val selectedMap = HashMap<String, String>()

    private var excludeData: List<List<Exclude>>? = null
    private lateinit var variantGroups: List<VariantGroups>

    private var loadCategorySubscription: Disposable? = null

    val categories: BehaviorSubject<ArrayList<ListableInWeird>> by lazy {
        loadCategorySubscription = jsonService.myJson
                .subscribeOn(Schedulers.io())
                .map {
                    val items = ArrayList<ListableInWeird>()
                    it.variants?.variant_groups?.let {
                        it.forEach { group ->
                            items.add(VariationGroup(group.name))
                            group.variations?.forEach({
                                items.add(Variation(it.name, it.price, it.inStock, it.id, group.group_id))
                            })
                        }
                        variantGroups = it
                    }

                    excludeData = it.variants?.exclude_list
                    return@map items
                }
                .observeOn(Schedulers.io())
                .subscribe({
                    categories.onNext(it)
                }, {})
        BehaviorSubject.create<ArrayList<ListableInWeird>>()
    }

    fun inject(injector: AppComponent) {
        injector.inject(this)
    }

    override fun onCleared() {
        loadCategorySubscription?.dispose()
        super.onCleared()
    }

    fun getPreviousSelectionData(groupdId: String): Pair<Int, ArrayList<Int>>? {
        val previousSelection = selectedMap[groupdId]
        if (previousSelection != null) {
            val needToToggleEnableState = ArrayList<Int>()
            needToToggleEnableState.addAll(getDependentToggles(groupdId, previousSelection))
            return Pair(getIndexOfItemInList(groupdId, previousSelection), needToToggleEnableState)
        } else {
            return null
        }
    }

    fun getDependentToggles(groupdId: String, variantId: String): ArrayList<Int> {
        val needToToggle = ArrayList<Int>()
        excludeData?.forEach {
            var found = false
            val others: ArrayList<Pair<String, String>> = ArrayList()
            it.forEach({
                if (groupdId == it.group_id && variantId == it.variation_id) {
                    found = true
                } else {
                    others.add(Pair(it.group_id, it.variation_id))
                }
            })

            if (found) {
                others.forEach({
                    needToToggle.add(getIndexOfItemInList(it.first, it.second))
                })
            }
        }
        return needToToggle
    }

    private fun getIndexOfItemInList(groupId: String, variantId: String): Int {
        var count = 0
        variantGroups.forEach {
            count++
            var found = false
            if (groupId == it.group_id) {
                it.variations?.let {vars ->
                    var idx = 0
                    for (i in 0 until vars.size) {
                        if (variantId == vars[i].id) {
                            idx = i
                            found = true
                            break
                        }
                    }
                    count += idx
                }
            } else {
                it.variations?.let {
                    count += it.size
                }
            }

            if (found) {
                return count
            }
        }

        return count
    }
}