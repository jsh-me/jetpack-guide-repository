package com.jsh.stockii

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_store.view.*

class StoreAdapter: RecyclerView.Adapter<StoreAdapter.StoreViewHolder>() {
    private var _items = listOf<Store>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_store, parent, false)
        return StoreViewHolder(view)
    }

    override fun getItemCount(): Int  = _items.size

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        val store = _items[position]
        var colorStat = Color.GREEN

        holder.nameTextView.text = store.name
        holder.addrTextView.text = store.addr
        holder.distanceTextView.text = String.format("%.2fkm",store.distance)

        when(store.remain_stat){
            "plenty" -> {
                holder.countTextView.text = "100개 이상"
                holder.remainTextView.text = "충분"
                colorStat = Color.GREEN
            }
            "some" -> {
                holder.countTextView.text = "30개 이상"
                holder.remainTextView.text = "여유"
                colorStat = Color.YELLOW
            }
            "few" -> {
                holder.countTextView.text = "2개 이상"
                holder.remainTextView.text = "매진 임박"
                colorStat = Color.RED
            }
            "empty" -> {
                holder.countTextView.text = "1개 이하"
                holder.remainTextView.text = "재고 없음"
                colorStat = Color.GRAY
            }
            else -> {
                holder.countTextView.text =""
            }
        }
        holder.remainTextView.setTextColor(colorStat)
        holder.countTextView.setTextColor(colorStat)
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