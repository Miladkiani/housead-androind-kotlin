package com.mili.housead.gallery

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mili.housead.network.model.HouseImageProperty

class GalleryViewModel(gallery: List<HouseImageProperty>, application: Application) : AndroidViewModel(application){

    private val _gallery = MutableLiveData<List<HouseImageProperty>>()

        val gallery:LiveData<List<HouseImageProperty>>
        get() = _gallery


    init {
        _gallery.value = gallery
    }


    override fun onCleared() {
        super.onCleared()
    }
}