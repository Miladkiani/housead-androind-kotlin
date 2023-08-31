package com.mili.housead.overview.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mili.housead.network.*
import com.mili.housead.network.model.*
import com.mili.housead.overview.HouseApiStatus
import com.mili.housead.overview.filter.model.FilterItem
import com.mili.housead.overview.filter.model.OptionProperty

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FilterViewModel : ViewModel(){

    private val _status = MutableLiveData<HouseApiStatus>()

    val status :LiveData<HouseApiStatus>
        get() = _status

    private val _categories = MutableLiveData<MutableList<CategoryProperty>>()

    val categories : LiveData<MutableList<CategoryProperty>>
       get()= _categories

    private val _cities = MutableLiveData<List<CityProperty>>()

    val cities:LiveData<List<CityProperty>>
        get() = _cities

    private val _neighborhoods = MutableLiveData<List<NeighborhoodProperty>>()

    val neighborhoods:LiveData<List<NeighborhoodProperty>>
        get() = _neighborhoods

    private val _neighborhoodsFilter = MutableLiveData<MutableList<NeighborhoodProperty>>()

    val neighborhoodsFilter:LiveData<MutableList<NeighborhoodProperty>>
        get() = _neighborhoodsFilter

    private val _features = MutableLiveData<MutableList<FeatureProperty>>()

    val features : LiveData<MutableList<FeatureProperty>>
        get()= _features

    private val _range = MutableLiveData<RangeProperty>()

    val range : LiveData<RangeProperty>
        get()= _range

    private val _prepayment = MutableLiveData<RangeFilterProperty>()

    val prepayment : LiveData<RangeFilterProperty>
        get() = _prepayment

    private val _rent = MutableLiveData<RangeFilterProperty>()

    val rent : LiveData<RangeFilterProperty>
        get() = _rent

    private val _sell = MutableLiveData<RangeFilterProperty>()

    val sell : LiveData<RangeFilterProperty>
        get() = _sell


    fun setSellFilter(min:Long,max:Long){
        if (max > min) {
            FilterItem.sell.min = min * 10
            FilterItem.sell.max = max * 10
        }
    }

    fun setPrepaymentFilter(min:Long,max:Long){
        if (max>min) {
            FilterItem.prepayment.min = min * 10
            FilterItem.prepayment.max = max * 10
        }
    }

    fun setRentFilter(min:Long,max:Long){
        if(max > min) {
            FilterItem.rent.min = min * 10
            FilterItem.rent.max = max * 10
        }
    }

    private fun setRange(range:RangeProperty){
        range.apply {

            minRangeSell = if(minRangeSell != null){
                minRangeSell!!.div(10)
            }else{
                0
            }

            maxRangeSell = if(maxRangeSell != null){
                maxRangeSell!!.div(10)
            }else{
                0
            }

            minRangePrepayment = if(minRangePrepayment != null){
                minRangePrepayment!!.div(10)
            }else{
                0
            }

            maxRangePrepayment = if(maxRangePrepayment != null){
                maxRangePrepayment!!.div(10)
            }else{
                0
            }

            minRangeRent = if(minRangeRent != null){
                minRangeRent!!.div(10)
            }else{
                0
            }

            maxRangeRent = if(maxRangeRent != null){
                maxRangeRent!!.div(10)
            }else{
                0
            }
        }
        _range.value = range
    }

    private fun setSellProgress(){
        _sell.value = RangeFilterProperty()
        if (FilterItem.sell.min != null  ){
            _sell.value?.min = FilterItem.sell.min!!/10
        }

        if (FilterItem.sell.max != null ){
            _sell.value?.max = FilterItem.sell.max!!/10
        }
    }

    private fun setPrepaymentProgress(){
       _prepayment.value = RangeFilterProperty()
        if (FilterItem.prepayment.min != null  ){
            _prepayment.value?.min = FilterItem.prepayment.min!!/10
        }

        if (FilterItem.prepayment.max != null ){
            _prepayment.value?.max = FilterItem.prepayment.max!!/10
        }
    }

    private fun setRentProgress(){
        _rent.value = RangeFilterProperty()
        if (FilterItem.rent.min != null  ){
            _rent.value?.min = FilterItem.rent.min!!/10
        }
        if (FilterItem.rent.max != null ){
            _rent.value?.max = FilterItem.rent.max!!/10
        }
    }

    private val _rooms = MutableLiveData<MutableList<RoomProperty>>()

    val rooms : LiveData<MutableList<RoomProperty>>
        get()= _rooms


    private fun setNeighborhoodFilters(){
        _neighborhoodsFilter.value!!.clear()
        FilterItem.neighborhoods.map {
            _neighborhoodsFilter.value!!.add(NeighborhoodProperty
                (id = it.key,title = it.value))
        }
        _neighborhoodsFilter.notifyObserver()
    }



    private val _navigateToApplyFilter = MutableLiveData<Boolean>()
    val navigateToApplyFilter:LiveData<Boolean>
        get()= _navigateToApplyFilter


    init {
        _neighborhoodsFilter.value = mutableListOf()
        _features.value = mutableListOf()
        _categories.value = mutableListOf()
        getProperties()
    }


    private fun setRooms(){
        _rooms.value = mutableListOf(
            RoomProperty(1,"1"),
            RoomProperty(2,"2"),
            RoomProperty(3,"3"),
            RoomProperty(4,"4"),
            RoomProperty(5,"5"),
            RoomProperty(6,"6")
            )
    }

    private fun getProperties(){
       viewModelScope.launch {
            val getCategoriesDeferred = HouseApi.retrofitService.getCategoriesAsync()
            val getFeaturesDeferred = HouseApi.retrofitService.getFeaturesAsync()
            val getRangeDeferred = HouseApi.retrofitService.getRangeAsync()
            val getCitiesDeferred = HouseApi.retrofitService.getCitiesAsync()
            try {
                _status.value = HouseApiStatus.LOADING
                val listResultCategories = getCategoriesDeferred.await()
                val listResultFeatures = getFeaturesDeferred.await()
                val resultRange = getRangeDeferred.await()
                val listResultCities = getCitiesDeferred.await()
                _status.value = HouseApiStatus.DONE
                _cities.value = listResultCities
                _features.value?.addAll(listResultFeatures)
                _categories.value?.addAll(listResultCategories)
                setRange(resultRange)
                setRentProgress()
                setSellProgress()
                setPrepaymentProgress()
                setRooms()
                setNeighborhoodFilters()
            }catch (t:Throwable){
                _status.value = HouseApiStatus.ERROR
                _features.value = ArrayList()
                _categories.value = ArrayList()
                _cities.value = ArrayList()
                _range.value = RangeProperty()
            }
        }
    }

    fun navigateFilter(){
        _navigateToApplyFilter.value = true
    }

    fun navigateFilterComplete(){
        _navigateToApplyFilter.value = null
    }

    fun setNeighborhoodByCity(cityId:Int){
        viewModelScope.launch {
            val getPropertiesDeferred =
                HouseApi.retrofitService.getNeighborhoodByCityAsync(cityId)
            try {
                val listResult = getPropertiesDeferred.await()
                _neighborhoods.value = listResult
            }catch (t:Throwable){
                _neighborhoods.value = ArrayList()
            }
        }
    }

    fun addNeighborhoodFilter(neighborhood: NeighborhoodProperty){
        if (!FilterItem.neighborhoods.contains(neighborhood.id)) {
            FilterItem.neighborhoods[neighborhood.id] = neighborhood.title
            _neighborhoodsFilter.value?.add(neighborhood)
            _neighborhoodsFilter.notifyObserver()
        }
    }

    fun deleteNeighborhoodFilter(option: OptionProperty){
        FilterItem.neighborhoods.remove(option.id)
        _neighborhoodsFilter.value?.remove(option)
        _neighborhoodsFilter.notifyObserver()
    }

    fun deleteAllNeighborhoodsFilter() {
        FilterItem.neighborhoods.clear()
        _neighborhoodsFilter.value?.clear()
        _neighborhoodsFilter.notifyObserver()
    }



   private fun <T>  MutableLiveData<T>.notifyObserver(){
        this.value = this.value
    }

}