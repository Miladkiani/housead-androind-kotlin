package com.mili.housead.gallery

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mili.housead.network.model.HouseImageProperty
import java.lang.IllegalArgumentException

class GalleryViewModelFactory(private val gallery: List<HouseImageProperty>,
                              private val app:Application):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GalleryViewModel::class.java)) {
            return GalleryViewModel(gallery, app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}