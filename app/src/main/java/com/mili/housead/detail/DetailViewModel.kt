package com.mili.housead.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mili.housead.gallery.GalleryFragment
import com.mili.housead.network.model.HouseImageProperty
import com.mili.housead.network.model.HouseProperty

class DetailViewModel(houseProperty: HouseProperty, app:Application) : AndroidViewModel(app){
    private val _selectedProperty = MutableLiveData<HouseProperty>()

    val selectedProperty:LiveData<HouseProperty>
      get() = _selectedProperty

    private val _navigateToDial = MutableLiveData<String>()
    val navigateToDial:LiveData<String>
        get() = _navigateToDial

    private val _navigateToSelectedProperty = MutableLiveData<List<HouseImageProperty>>()

    val navigateToSelectedProperty : LiveData<List<HouseImageProperty>>
    get()= _navigateToSelectedProperty

    fun displayGallery(gallery: List<HouseImageProperty>){
        _navigateToSelectedProperty.value = gallery
    }

    fun displayGalleryComplete(){
        _navigateToSelectedProperty.value = null
    }

    fun dial(phoneNumber:String){
        _navigateToDial.value = phoneNumber
    }

    fun navigateToDialComplete(){
        _navigateToDial.value = null
    }

    init {
        _selectedProperty.value = houseProperty
    }
}