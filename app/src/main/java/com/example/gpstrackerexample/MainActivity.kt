package com.example.gpstrackerexample

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
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

class MainActivity : AppCompatActivity(), LocationListener {
    private lateinit var binding : ActivityMainBinding

    private var adapter = TrackingItemAdapter()

    private var client: FusedLocationProviderClient? = null

    private var permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    val mainHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.main)

        client = LocationServices.getFusedLocationProviderClient(this)

        setView()
    }

    private fun setView() {
        binding.rvTracking.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = this@MainActivity.adapter.apply {
                onClickItem = object : TrackingItemAdapter.OnItemClickCallback{
                    override fun onClickItem(item: TrackingItem) {
                        Toast.makeText(this@MainActivity, "latitude : ${item.latitude}", Toast.LENGTH_SHORT).show()
                        Toast.makeText(this@MainActivity, "longitude : ${item.longitude}", Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }
        binding.btnStartLiveTracking.setOnClickListener {
            mainHandler.post(object : Runnable {
                override fun run() {
                    getCurrentLocation()
                    mainHandler.postDelayed(this, 5000)
                }
            })
        }
        binding.btnEndLiveTracking.setOnClickListener {
            mainHandler.removeCallbacksAndMessages(null);
        }
    }

    override fun onLocationChanged(p0: Location) {
        //TODO("Not yet implemented")
    }

    private fun getCurrentLocation() {
        if (!checkPermission(permissions)) {
            ActivityCompat.requestPermissions(this, permissions, 1)
            return
        }
        // Initialize Location manager
        val locationManager = getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f,this)

        // Check condition
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        ) {
            // When location service is enabled
            // Get last location
            client!!.lastLocation.addOnCompleteListener { task ->
                // Initialize location
                if (task.result != null){
                    val location = task.result
                    adapter.trackingList.add(
                        TrackingItem(
                            adapter.trackingList.size.toString(),
                            latitude = location.latitude.toString(),
                            longitude = location.longitude.toString()
                        )
                    )
                    adapter.notifyDataSetChanged()
                }
            }
        }
        else {
            Toast.makeText(this, "GPS Tidak Aktif", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkPermission(permissionArray: Array<String>): Boolean {
        var allSuccess = true
        for (i in permissionArray.indices) {
            if (ActivityCompat.checkSelfPermission(this, permissionArray[i]) != PackageManager.PERMISSION_GRANTED){
                allSuccess = false
            }
        }
        return allSuccess
    }
}