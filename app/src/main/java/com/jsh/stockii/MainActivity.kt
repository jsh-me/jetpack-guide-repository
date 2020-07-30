package com.jsh.stockii

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val permissionlistener = object : PermissionListener{
            override fun onPermissionGranted() {
                Toast.makeText(this@MainActivity, "Permission Granted", Toast.LENGTH_SHORT).show();
                performAction()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(this@MainActivity, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        }

        TedPermission.with(this)
            .setPermissionListener(permissionlistener)
            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
            .check()


    }

    @SuppressLint("MissingPermission")
    private fun performAction() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation
            .addOnFailureListener {
                Log.e("MainActivity", it.cause.toString())
            }
            .addOnSuccessListener { location: Location? ->
                location?.let{
                    mainViewModel.location = it
                    mainViewModel.fetchStoreInfo()
                    Toast.makeText(this@MainActivity,
                    "lat: ${it.latitude} and long: ${it.longitude}", Toast.LENGTH_LONG).show()
                }
            }

        val storeAdapter = StoreAdapter()

        recycler_view.apply {
            adapter = storeAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        mainViewModel.itemLiveData.observe(this, Observer {
            storeAdapter.updateItems(it)
            supportActionBar?.title = "마스크 재고 있는 곳 : " + it.size
        })
        //mainViewModel.fetchStoreInfo()

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

