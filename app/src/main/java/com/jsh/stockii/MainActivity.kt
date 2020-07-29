package com.jsh.stockii

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_store.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler_view.adapter = StoreAdapter()
        recycler_view.layoutManager = LinearLayoutManager(this)

        testRecyclerView()
    }

    private fun testRecyclerView(){
        val testItems = ArrayList<Store>()
        testItems.add(Store("서울 영등포구 장양동 201-222", "","",0.0,0.0,"우리약국","","",""))
        testItems.add(Store("서울 영등포구 장양동 201-222", "","",0.0,0.0,"우리약국","","",""))
        testItems.add(Store("서울 영등포구 장양동 201-222", "","",0.0,0.0,"우리약국","","",""))
        testItems.add(Store("서울 영등포구 장양동 201-222", "","",0.0,0.0,"우리약국","","",""))

        StoreAdapter().updateItems(testItems)
    }
}

class StoreAdapter: RecyclerView.Adapter<StoreAdapter.StoreViewHolder>() {
    private var _items = listOf<Store>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_store, parent, false)
        return StoreViewHolder(view)
    }

    override fun getItemCount(): Int  = _items.size

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        val store = _items[position]

        holder.nameTextView.text = store.name
        holder.addrTextView.text = store.addr
        holder.distanceTextView.text = "1.0km"
        holder.remainTextView.text = store.remain_stat
        holder.countTextView.text = "100개 이상"
    }

    inner class StoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameTextView: TextView = itemView.name_textView
        var addrTextView: TextView = itemView.addr_textView
        var distanceTextView: TextView = itemView.distance_textView
        var remainTextView: TextView = itemView.remain_textView
        var countTextView: TextView = itemView.count_textView
    }

    fun updateItems(items: List<Store>){
        _items = items
        notifyDataSetChanged()
    }
}