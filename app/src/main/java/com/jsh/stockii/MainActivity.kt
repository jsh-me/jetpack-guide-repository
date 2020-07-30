package com.jsh.stockii

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val storeAdapter = StoreAdapter()

        recycler_view.apply {
            adapter = storeAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        mainViewModel.itemLiveData.observe( this, Observer {
            storeAdapter.updateItems(it)
            supportActionBar?.title = "마스크 재고 있는 곳 : " +it.size
        })

        mainViewModel.fetchStoreInfo()
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
                mainViewModel.fetchStoreInfo()
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        }
    }
}

