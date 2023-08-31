package com.mili.housead.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mili.housead.network.model.HouseProperty
import java.lang.IllegalArgumentException

class DetailViewModelFactory(private val houseProperty: HouseProperty,
                             private val app:Application):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
      if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(houseProperty, app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}