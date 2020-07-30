package com.jsh.stockii

data class Store(
    var addr: String,
    var code: String,
    var created_at: String,
    var lat: Double,
    var lng: Double,
    var name: String,
    var remain_stat: String,
    var stock_at: String,
    var type: String,
    var distance: Double
) : Comparable<Store> {
    override fun compareTo(other: Store): Int{
        return distance.compareTo(other.distance)
    }
}