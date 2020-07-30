package com.jsh.stockii

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_store.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.stream.Collectors

class MainActivity : AppCompatActivity() {
    val TAG = MainActivity::class.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val storeAdapter = StoreAdapter()

        recycler_view.apply {
            adapter = storeAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

         Retrofit.Builder()
                .baseUrl(Consts.baseUrl)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(MaskService::class.java)
                .fetchStoreInfo()
                .enqueue(object: Callback<StoreInfo>{
                    override fun onFailure(call: Call<StoreInfo>, t: Throwable) {
                        Log.e(TAG, t.localizedMessage!!)
                    }

                    override fun onResponse(call: Call<StoreInfo>, response: Response<StoreInfo>) {
                        val items = response.body()?.stores

                        items?.let {
                            //null인 요소를 filtering
                            storeAdapter.updateItems(it.stream()
                                .filter{it.remain_stat != null}
                                .collect(Collectors.toList()))
                            supportActionBar?.title = "마스크 재고 있는 곳: "+ it.size
                        }
                    }
                })
        }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_refresh -> {
                //refresh()
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        }
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
        var colorStat = Color.GREEN

        holder.nameTextView.text = store.name
        holder.addrTextView.text = store.addr
        holder.distanceTextView.text = "1.0km"

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