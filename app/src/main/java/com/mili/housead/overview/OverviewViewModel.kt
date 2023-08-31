package com.mili.housead.overview

import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mili.housead.network.*
import com.mili.housead.network.model.*
import com.mili.housead.overview.filter.model.FilterItem
import kotlinx.coroutines.*

enum class HouseApiStatus {
    LOADING, ERROR, EMPTY, DONE
}

class OverviewViewModel : ViewModel() {

    private val _status = MutableLiveData<HouseApiStatus>()

    private var _word: String = ""

    private val word: String
        get() = _word


    private val _isSearch = MutableLiveData<Boolean>()

    val isSearch: LiveData<Boolean>
        get() = _isSearch

    private var _meta: Meta =
        Meta(0, 0, 0, 0)

    val meta: Meta
        get() = _meta

    val status: LiveData<HouseApiStatus>
        get() = _status

    private val _properties = MutableLiveData<MutableList<HouseProperty>>()

    val properties: LiveData<MutableList<HouseProperty>>
        get() = _properties

    private val _navigateToSelectedProperty = MutableLiveData<Pair<HouseProperty, ImageView>>()

    val navigateToSelectedProperty
        get() = _navigateToSelectedProperty


    private val _filterProperty = MutableLiveData<FilterProperty>()
    val filterProperty: LiveData<FilterProperty>
        get() = _filterProperty


    init {
        getHouseProperties()
    }

    private fun <T> MutableLiveData<MutableList<T>>.notifyObserver(values: List<T>) {
        val value = this.value ?: mutableListOf()
        value.addAll(values)
        this.value = value
    }

    fun getHouseProperties() {
        viewModelScope.launch {
            nextPage()
            val getPropertiesDeferred: Deferred<HouseItems>
            if (_filterProperty.value != null) {
                _filterProperty.value!!.word = word
                getPropertiesDeferred =
                    HouseApi.retrofitService.getFilterPropertiesAsync(
                        filterProperty.value!!,
                        meta.currentPage
                    )
            } else {
                getPropertiesDeferred =
                    HouseApi.retrofitService.getPropertiesAsync(meta.currentPage, word)
            }
            try {
                _status.value = HouseApiStatus.LOADING
                val listResult = getPropertiesDeferred.await()
                _status.value = HouseApiStatus.DONE
                _properties.notifyObserver(listResult.items)
                _meta = listResult.meta
            } catch (t: Throwable) {
                _status.value = HouseApiStatus.ERROR
                _properties.value = ArrayList()
            }
        }
    }

    fun setEmptyStatus(){
        this._status.value = HouseApiStatus.EMPTY
    }

    fun setFilterHouseProperties() {
        setFilter()
        clearWord()
        refreshProperties()
    }


    fun displayPropertyDetails(houseProperty: HouseProperty, houseBg: ImageView) {
        _navigateToSelectedProperty.value = Pair(houseProperty, houseBg)
    }

    fun displayPropertyDetailsComplete() {
        _navigateToSelectedProperty.value = null
    }

    private fun nextPage() {
        _meta.currentPage++
    }

    private fun refreshPage() {
        _meta = Meta(0, 0, 0, 0)
    }

    fun refreshProperties() {
        _properties.value?.clear()
        refreshPage()
        getHouseProperties()
    }

    fun setWordSearch(word: String) {
        _word = word
    }

    fun searchHasCompleted() {
        _isSearch.value = false
    }

    fun waitingSearch() {
        _isSearch.value = true
    }

    private fun clearWord() {
        _word = ""
    }


    fun deleteFilter(filterType: FilterType) {
        when (filterType) {
            FilterType.CATEGORY -> FilterItem.categories.clear()
            FilterType.FEATURE -> FilterItem.features.clear()
            FilterType.ROOM -> FilterItem.rooms.clear()
            FilterType.NEIGHBORHOOD -> FilterItem.neighborhoods.clear()
            FilterType.SELL -> {
                FilterItem.sell.min = null
                FilterItem.sell.max = null
            }
            FilterType.PREPAYMENT -> {
                FilterItem.prepayment.min = null
                FilterItem.prepayment.max = null
            }
            FilterType.RENT -> {
                FilterItem.rent.min = null
                FilterItem.rent.max = null
            }
        }
        setFilter()
        refreshProperties()
    }

    private fun setFilter() {
        _filterProperty.value = FilterProperty()
        FilterItem.features.mapKeys {
            filterProperty.value!!.feature.add(it.key)
        }

        FilterItem.categories.mapKeys {
            filterProperty.value!!.category.add(it.key)
        }

        FilterItem.neighborhoods.mapKeys {
            filterProperty.value!!.neighborhood.add(it.key)
        }

        FilterItem.rooms.mapKeys {
            filterProperty.value!!.room.add(it.key)
        }

        _filterProperty.value!!.sell = FilterItem.sell
        _filterProperty.value!!.prepayment = FilterItem.prepayment
        _filterProperty.value!!.rent = FilterItem.rent

    }
}