package com.mili.housead.overview.filter.model

import com.mili.housead.network.model.RangeFilterProperty

object FilterItem{
    val features:MutableMap<Int,String> = mutableMapOf()
    val categories:MutableMap<Int,String> = mutableMapOf()
    val rooms:MutableMap<Int,String> = mutableMapOf()
    val neighborhoods:MutableMap<Int,String> = mutableMapOf()
    val sell:RangeFilterProperty = RangeFilterProperty()
    val prepayment:RangeFilterProperty = RangeFilterProperty()
    val rent:RangeFilterProperty = RangeFilterProperty()
}