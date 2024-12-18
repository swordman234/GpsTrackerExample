package com.example.gpstrackerexample

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gpstrackerexample.adapter.TrackingItemAdapter
import com.example.gpstrackerexample.data.TrackingItem
import com.example.gpstrackerexample.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    private var adapter = TrackingItemAdapter()

    private var permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }else{
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.main)
        ActivityCompat.requestPermissions(this@MainActivity,permissions, PERMISSION_REQUEST)
        setView()
    }

    private fun setView() {
//        binding.rvTracking.apply {
//            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
//            adapter = this@MainActivity.adapter.apply {
//                onClickItem = object : TrackingItemAdapter.OnItemClickCallback{
//                    override fun onClickItem(item: TrackingItem) {
//                        Toast.makeText(this@MainActivity, "latitude : ${item.latitude}", Toast.LENGTH_SHORT).show()
//                        Toast.makeText(this@MainActivity, "longitude : ${item.longitude}", Toast.LENGTH_SHORT).show()
//
//                    }
//                }
//            }
//        }
        binding.btnStartLiveTracking.setOnClickListener {
            if (checkPermission(permissions)){
                val locationServiceIntent = Intent(this@MainActivity, LocationServices::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    startForegroundService(locationServiceIntent)
                }else{
                    startService(locationServiceIntent)
                }
            }else{
                ActivityCompat.requestPermissions(this@MainActivity,permissions, PERMISSION_REQUEST)
            }
        }
        binding.btnEndLiveTracking.setOnClickListener {
            val locationServiceIntent = Intent(this@MainActivity, LocationServices::class.java)
            stopService(locationServiceIntent)
        }
    }

    private fun checkPermission(permissionArray: Array<String>): Boolean {
        var allSuccess = true
        for (i in permissionArray.indices) {
            if (ActivityCompat.checkSelfPermission(this@MainActivity, permissionArray[i]) != PackageManager.PERMISSION_GRANTED){
                allSuccess = false
            }
        }
        return allSuccess
    }
}