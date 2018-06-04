package com.example.kanj.swiggyjunk

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.example.kanj.swiggyjunk.adapters.WeirdAdapter
import com.example.kanj.swiggyjunk.adapters.WeirdAdapterListener
import com.example.kanj.swiggyjunk.dagger.AppComponent
import com.example.kanj.swiggyjunk.dagger.DaggerAppComponent
import com.example.kanj.swiggyjunk.models.VariantSelectionModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), WeirdAdapterListener {
    private val component: AppComponent
    private var adapter: WeirdAdapter? = null

    private var variantSelectionModel: VariantSelectionModel? = null

    init {
        component = DaggerAppComponent.builder().build()
    }

    private var observeDataSubscription: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listRecyclerView.layoutManager = LinearLayoutManager(this)

        variantSelectionModel = ViewModelProviders.of(this).get(VariantSelectionModel::class.java)
        variantSelectionModel?.inject(component)

        variantSelectionModel?.let {
            observeDataSubscription = it.categories
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        adapter = WeirdAdapter(it, this@MainActivity)
                        listRecyclerView.adapter = adapter
                    }, {
                        it.printStackTrace()
                    })
        }
    }

    override fun onVariationClicked(pos: Int, selectState: Boolean, groupId: String, variationId: String) {
        variantSelectionModel?.let {model ->
            if (selectState) {
                // Need to uncheck previous selection in this group and toogle the enabled state of dependent items
                val previousData = model.getPreviousSelectionData(groupId)
                previousData?.let {
                    adapter?.setCheckedState(it.first, false)
                    adapter?.setEnabledStates(it.second, true)
                }
                model.selectedMap.put(groupId, variationId)
            } else {
                model.selectedMap.remove(groupId)
            }

            val toToggle = model.getDependentToggles(groupId, variationId)
            adapter?.setEnabledStates(toToggle, !selectState)
            adapter?.notifyItemChanged(pos) // This item also has an updated check state
        }
    }
}
